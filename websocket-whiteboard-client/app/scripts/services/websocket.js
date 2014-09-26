'use strict';

angular.module('whiteboardApp')
  .factory('websocket', function () {
    // Service logic
    var url = 'ws://localhost:8080/ws-ulme-websocket-server/serverendpoint',
      ws = new WebSocket(url);

    // Public API here
    return {
      startWebsocket: function () {
        return ws;
      },
      getAllRecords: function () {
        var send = JSON.stringify({
          'type': 'get',
          'operate': ''
        });
        ws.send(send);
      },
      getWhiteboard: function (whiteboardName) {
        var send = JSON.stringify({
          'type': 'get',
          'operate': whiteboardName
        });
        ws.send(send);
      }
    };
  });