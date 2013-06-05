###*Copyright 2013 BLStream, BLStream's Patronage Program Contributors
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
###

# '------------------------------------------ GAMES RESOURCE MODEL
app.factory 'Games', ($resource) ->
    $resource '/my/games/json/:ctrl:gid',
        {
            gid: '@id',
            ctrl: '@ctrl'
        },
        {
            get: {method: 'GET', headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            query: {method: 'GET', params: {ctrl: "list"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}], isArray: true},
            archive: {method: 'GET', params: {ctrl: "archive"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}], isArray: true},
            save: {method: 'POST', headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            update: {method: 'PUT', headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            delete: {method: 'DELETE', headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            cancel: {method: 'POST', params: {ctrl: "cancel"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            publish: {method: 'POST', params: {ctrl: "publish"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            checkName: {method: 'POST', params: {ctrl: "checkName"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]}
        }