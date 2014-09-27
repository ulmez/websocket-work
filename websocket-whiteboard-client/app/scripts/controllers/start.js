'use strict';

angular.module('whiteboardApp')
	.controller('StartCtrl', function ($scope, $location, $route, $timeout, whiteboard, websocket) {
		var i;
		$scope.errorWhiteboardNameMessage = '';
		$scope.errorWhiteboardNameUpdateMessage = '';
		$scope.whiteboardName = '';
		$scope.checker = 0;
		$scope.showUpdateWhiteboard = 0;
		$scope.showErrorMessage = true;

		var ws = websocket.startWebsocket();

		$scope.$watch('stateChange', function () {
			websocket.getAllRecords();
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
				websocket.getAllRecords();
			} else {
				$scope.posts = JSON.parse(message.data);
				$scope.$apply();
			}
		};

		$scope.mouseOverOperation = function (val) {
			$scope.checker = val;
		};

		$scope.whiteboardNameClicked = function (whiteboardName) {
			$location.path('/whiteboards/' + whiteboardName);
			$route.reload();
		};

		$scope.updateClicked = function (val, whiteboardName) {
			$scope.showUpdateWhiteboard = val;
			$scope.wUpdate = whiteboardName;
			$scope.showErrorMessage = false;
		};

		$scope.insertNewWhiteboard = function (whiteboardName) {
			$scope.errorWhiteboardNameMessage = '';
			$scope.showUpdateWhiteboard = 0;

			for (i = 0; i < $scope.posts.length; i = i + 1) {
				if ($scope.posts[i].whiteboard.toLowerCase() === whiteboardName.toLowerCase()) {
					$scope.errorWhiteboardNameMessage = 'Blackboard already used';
				}
			}

			if (whiteboardName === '') {
				$scope.errorWhiteboardNameMessage = 'No name inserted yet';
			}

			if ($scope.errorWhiteboardNameMessage === '') {
				$scope.whiteboardName = '';
				whiteboard.addWhiteboard(whiteboardName, $scope.posts);
			}
		};

		$scope.whiteboardDelete = function (obj) {
			$scope.showUpdateWhiteboard = 0;
			var conf = window.confirm('Are you absolutely sure you would like to delete this blackboard?');

			if (conf) {
				whiteboard.deleteWhiteboard(obj.whiteboard);
			}
		};

		$scope.whiteboardUpdate = function (name, obj) {
			$scope.showErrorMessage = true;
			$scope.errorWhiteboardNameUpdateMessage = '';
			$scope.idOfPressedButton = obj.id;

			for (i = 0; i < $scope.posts.length; i = i + 1) {
				if (name.toLowerCase() === $scope.posts[i].whiteboard.toLowerCase() &&
					obj.id !== $scope.posts[i].id) {
					$scope.errorWhiteboardNameUpdateMessage = 'Blackboard name taken';
				}
			}

			if ($scope.errorWhiteboardNameUpdateMessage === '') {
				$scope.showUpdateWhiteboard = 0;
				$scope.showErrorMessage = false;
				whiteboard.updateWhiteboard(name, obj, $scope.posts);
			}
		};
	});