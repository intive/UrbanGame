/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 *       http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

describe('Controllers', function() {
    describe('menuCtrl', function() {
        beforeEach(module('web'));
        var ctrl, scope, rootScope, location;
        beforeEach(inject(function($controller, $rootScope, $location) {
            rootScope = $rootScope;
            scope = $rootScope.$new();
            location = $location;
            ctrl = $controller('menuCtrl', {
                $scope: scope;
            });
        }));

        it('should have correct location path', function() {
            location.path('/');
            rootScope.$apply();
            expect(location.path()).toBe('/');

            location.path('/my/games/new');
            rootScope.$apply();
            expect(location.path()).toBe('/my/games/new');

            location.path('/my/games');
            rootScope.$apply();
            expect(location.path()).toBe('/my/games');

            location.path('/my/games/archive');
            rootScope.$apply();
            expect(location.path()).toBe('/my/games/archive');
        });
    });
});
