'use strict';

angular.module('whiteboardApp')
  .factory('whiteboard', function ($http, $timeout, websocket) {
    // Service logic
    var ws = websocket.startWebsocket();

    // Public API here
    return {
      addWhiteboard: function (whiteboardName, arrTemp) {
        var checkIfAlreadyUsed = true,
          i;

        for (i = 0; i < arrTemp.length; i = i + 1) {
          if (whiteboardName.toLowerCase() === arrTemp[i].whiteboard.toLowerCase()) {
            checkIfAlreadyUsed = false;
          }
        }

        if (checkIfAlreadyUsed) {
          var send = JSON.stringify({
            'type': 'post',
            'operate': whiteboardName
          });

          ws.send(send);
          websocket.getAllRecords();
        }
      },
      deleteWhiteboard: function (whiteboardName) {
        var send = JSON.stringify({
          'type': 'delete',
          'operate': whiteboardName
        });

        ws.send(send);
        websocket.getAllRecords();
      },
      updateWhiteboard: function (whiteboardName, whiteboardObject, arrTemp) {
        var i,
          duplicateCheck = true,
          tempNotes = whiteboardObject;

        for (i = 0; i < arrTemp.length; i = i + 1) {
          if (whiteboardName.toLowerCase() === arrTemp[i].whiteboard.toLowerCase() &&
            tempNotes.id !== arrTemp[i].id) {
            duplicateCheck = false;
          }
        }

        tempNotes.whiteboard = whiteboardName;
        tempNotes = JSON.stringify(tempNotes);

        if (duplicateCheck) {
          var send = JSON.stringify({
            'type': 'put',
            'operate': tempNotes
          });

          ws.send(send);
          websocket.getAllRecords();
        }
      },
      addItem: function (note, whiteboardName) {
        var send = {
          'type': 'postnote',
          'operate': whiteboardName,
          'note': JSON.stringify(note)
        };

        ws.send(JSON.stringify(send));
      },
      deleteItem: function (note) {
        var send = {
          'type': 'deletenote',
          'operate': JSON.stringify(note)
        };

        ws.send(JSON.stringify(send));
      },
      updateItem: function (note) {
        var send = {
          'type': 'putnote',
          'operate': JSON.stringify(note),
        };

        ws.send(JSON.stringify(send));
        websocket.getAllRecords();
      }
    };
  });