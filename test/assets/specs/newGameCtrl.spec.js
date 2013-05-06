/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 * 		 http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		 http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
describe('Controllers', function() {
    describe('newGameCtrl', function() {

		beforeEach(module('web'));

		var ctrl, scope, $httpBackend;

		beforeEach(inject(function(_$httpBackend_, $controller, $rootScope) {
			$httpBackend = _$httpBackend_;
			scope = $rootScope.$new();
			ctrl = $controller('newGameCtrl', {
				$scope: scope
			});
		}));

        it('should list 4 steps', function() {
			expect(scope.steps.length).toBe(4);
        });

        it('should set $scope.selection on the 1st step at start', function() {
			expect(scope.selection.no).toBe(1);
        });

        it('should increment steps', function() {
			scope.incrementStep();
			expect(scope.selection.no).toBe(2);
        });

        it('should increment players number', function() {
			scope.game.playersNum=5;
            scope.game.incrementPlayersNum();
			expect(scope.game.playersNum).toBe(6);
        });
        
        it('should decrement players number', function() {
			scope.game.playersNsum=5;
            scope.decrementPlayersNum();
			expect(scope.game.playersNum).toBe(4);
        });
        
        it('should contains game defined', function() {
            expect(scope.game).not.toBe(null);
        });
        
        it('should be set at nolimit(null) players at start', function() {
            expect(scope.game.playersNum).toBe(null);
        });
    });
});
