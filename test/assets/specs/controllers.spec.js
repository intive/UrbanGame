describe('Controllers', function() {
    describe('MenuCtrl', function() {
        it('should list 3 items', function() {
            var scope = {}, ctrl = new MenuCtrl(scope);
            expect(scope.menuitems.length).toBe(3);
        });
    });
});