'use strict';

angular.module('whiteboardApp')
	.controller('WhiteboardCtrl', function ($scope, $location, $http, $window, $routeParams, whiteboard, websocket) {
		$scope.titleShowIt = 0;
		$scope.informationShowIt = 0;
		$scope.colorShowIt = 0;
		$scope.selectedColor = 'yellow';
		$scope.whiteboardName = $routeParams.whiteboardName;

		var ws = websocket.startWebsocket();

		$scope.$watch('stateChange', function () {
			websocket.getWhiteboard($scope.whiteboardName);
		});

		ws.onopen = function (e) {
			console.log(e);
			$scope.stateChange = 0;
			$scope.$apply();
		};

		ws.onerror = function (error) {
			console.log('WebSocket Error ' + error);
		};

		ws.onclose = function (event) {
			console.log('Remote host closed or refused WebSocket connection');
			console.log(event);
		};

		ws.onmessage = function (message) {
			if (message.data === 'refresh') {
				websocket.getWhiteboard($scope.whiteboardName);
			} else {
				$scope.posts = JSON.parse(message.data).db;
				$scope.$apply();
			}
		};

		$scope.backToWhiteboards = function () {
			$location.path('/');
			$scope.stateChange = 1;
		};

		$scope.titleClicked = function (note) {
			$scope.colorShowIt = 0;
			$scope.informationShowIt = 0;
			$scope.specificNote = note;
			$scope.titleTextUpdate = $scope.specificNote.title;
			$scope.titleShowIt = note.id;
		};

		$scope.informationClicked = function (note) {
			$scope.colorShowIt = 0;
			$scope.titleShowIt = 0;
			$scope.specificNote = note;
			$scope.informationTextUpdate = $scope.specificNote.information;
			$scope.informationShowIt = note.id;
		};

		$scope.colorClicked = function (note) {
			var getCheckedRadioButton = '';
			$scope.titleShowIt = 0;
			$scope.informationShowIt = 0;
			$scope.colorShowIt = note.id;
			$scope.specificNote = note;

			if (note.color.yellow) {
				getCheckedRadioButton = 'yellow' + note.id;
				$scope.selectedChangeColor = 'yellow';
			} else if (note.color.green) {
				getCheckedRadioButton = 'green' + note.id;
				$scope.selectedChangeColor = 'green';
			} else if (note.color.blue) {
				getCheckedRadioButton = 'blue' + note.id;
				$scope.selectedChangeColor = 'blue';
			} else if (note.color.red) {
				getCheckedRadioButton = 'red' + note.id;
				$scope.selectedChangeColor = 'red';
			}
			document.getElementById(getCheckedRadioButton).checked = true;
		};

		$scope.updateTitle = function (textUpdate) {
			$scope.titleShowIt = 0;
			$scope.specificNote.title = textUpdate;

			whiteboard.updateItem($scope.specificNote);
			$scope.stateChange = 2;
		};

		$scope.updateInformation = function (textUpdate) {
			$scope.informationShowIt = 0;
			$scope.specificNote.information = textUpdate;

			whiteboard.updateItem($scope.specificNote);
			$scope.stateChange = 3;
		};

		$scope.updateColor = function () {
			var arrColorTemp = {
				'yellow': false,
				'green': false,
				'blue': false,
				'red': false
			};

			if ($scope.selectedChangeColor === 'yellow') {
				arrColorTemp.yellow = true;
			} else if ($scope.selectedChangeColor === 'green') {
				arrColorTemp.green = true;
			} else if ($scope.selectedChangeColor === 'blue') {
				arrColorTemp.blue = true;
			} else if ($scope.selectedChangeColor === 'red') {
				arrColorTemp.red = true;
			}

			$scope.specificNote.color = arrColorTemp;
			$scope.titleShowIt = 0;
			$scope.informationShowIt = 0;
			$scope.colorShowIt = 0;

			whiteboard.updateItem($scope.specificNote);
			$scope.stateChange = 4;
		};

		$scope.deleteNote = function (note) {
			var deleteNotePrivate = $window.confirm('Are you absolutely sure you want to delete this note?');

			if (deleteNotePrivate) {
				whiteboard.deleteItem(note);
				$scope.stateChange = 5;
			}
		};

		$scope.getColor = function (color) {
			$scope.selectedColor = color;
		};

		$scope.getColorUpdate = function (color) {
			$scope.selectedChangeColor = color;
		};

		$scope.insertNewNote = function () {
			var tempNote,
				arrTempColors = {
					'yellow': false,
					'green': false,
					'blue': false,
					'red': false
				};

			if ($scope.selectedColor === 'yellow') {
				arrTempColors.yellow = true;
			} else if ($scope.selectedColor === 'green') {
				arrTempColors.green = true;
			} else if ($scope.selectedColor === 'blue') {
				arrTempColors.blue = true;
			} else if ($scope.selectedColor === 'red') {
				arrTempColors.red = true;
			}

			tempNote = {
				'title': $scope.titleText,
				'information': $scope.informationText,
				'color': arrTempColors
			};

			whiteboard.addItem(tempNote, $routeParams.whiteboardName);
			$scope.titleText = '';
			$scope.informationText = '';
			$scope.selectedColor = 'yellow';
			document.getElementById('yellow').checked = true;
			$scope.signupform.$setPristine();
			$scope.stateChange = 6;
		};
	});