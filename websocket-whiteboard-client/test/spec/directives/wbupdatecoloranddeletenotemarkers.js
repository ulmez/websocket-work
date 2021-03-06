'use strict';

describe('Directive: wbUpdateColorAndDeleteNoteMarkers', function () {

  // load the directive's module
  beforeEach(module('whiteboardApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<wb-update-color-and-delete-note-markers></wb-update-color-and-delete-note-markers>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('');
  }));
});