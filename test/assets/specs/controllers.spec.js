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
    describe('menuCtrl', function() {
        it('should list 3 items', function() {
            var scope = {}, ctrl = new menuCtrl(scope);
            expect(scope.menuitems.length).toBe(3);
        });
    });
    describe('newGameCtrl', function() {
        it('should list 5 steps', function() {
            var scope = {}, ctrl = new newGameCtrl(scope);
            expect(scope.steps.length).toBe(5);
        });
    });
});