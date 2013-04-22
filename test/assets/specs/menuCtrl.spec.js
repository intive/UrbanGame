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
'use strict';

describe('Controllers', function() {
    describe('menuCtrl', function() {
	beforeEach(module('web'));
	var ctrl, scope;
	beforeEach(inject(function($controller, $rootScope) {
		scope = $rootScope.$new();
		ctrl = $controller('menuCtrl', {
			$scope: scope
		});
	}));
        it('should list 3 items', function() {
            expect(scope.menuitems.length).toBe(3);
        });
    });
});
