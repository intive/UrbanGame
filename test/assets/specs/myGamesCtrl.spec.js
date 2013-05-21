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
    describe('myGamesCtrl', function() {

		beforeEach(module('web'));

		var ctrl, scope, $httpBackend;

		beforeEach(inject(function(_$httpBackend_, $controller, $rootScope) {
			$httpBackend = _$httpBackend_;
			scope = $rootScope.$new();
			ctrl = $controller('myGamesCtrl', {
				$scope: scope
			});
		}));

        it('should calculate dates interval', function() {
            var interval = scope.timeLeft("2013-05-20", "2013-05-21")
            expect(interval).toContain("d:");
            expect(interval).toContain("h:");
            expect(interval).toContain("min:");
			expect(interval).toContain("sec");
        });
    });
});
