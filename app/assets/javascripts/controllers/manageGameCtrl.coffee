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

# '------------------------------------- MANAGE GAME CTRL
app.controller 'manageGameCtrl', ['$scope', '$location', '$route', '$rootScope', 'Games', 'Utilities', '$dialog', ($scope, $location, $route, $rootScope, Games, Utilities, $dialog) ->
    
    # ------------------ INITIAL DATA FOR MODEL
    $scope.gameid = null
    $scope.game = {
        name: "",
        description: "",
        location: "",
        startTime: "",
        startDate: null,
        endTime: "",
        endDate: null,
        winning: "max_points",
        winningNum: 1,
        diff: 'easy',
        playersNum: null,
        awards: "",
        tasksNo: 0,
        status: "project",
        version: 1
    }
    $scope.skin = {
        image: "games/gameicon.png"
    }
    $scope.error = null

        # ------------------ MODEL DATA LOADER WHEN IN EDIT MODE
    loadModelData = (data) ->
        $scope.game = {
            name: data.name,
            description: data.description,
            location: data.location,
            startTime: new Date(data.startTime).toLocaleTimeString().substring(0,5),
            startDate: new Date(data.startTime),
            endTime: new Date(data.endTime).toLocaleTimeString().substring(0,5),
            endDate: new Date(data.endTime),
            winning: data.winning,
            winningNum: data.nWins,
            diff: data.difficulty,
            playersNum: if (data.maxPlayers == 1000000) then null else data.maxPlayers,
            awards: data.awards,
            tasksNo: data.tasksNo,
            status: data.status,
            version: data.version
        }

    fillGameModel = ->
        $scope.gameid = parseInt(/(\d+)/.exec(window.location.pathname)[1])
        Games.get(
            {gid: $scope.gameid},
            (data) ->
                if data.status == 'online' ||  data.status == 'finished'
                    loadModelData(data)
                    $scope.error = null
                else
                    window.location = "/my/games/" + $scope.gameid + "/edit"
            (error) ->
                $scope.error = Messages("js.errors.ajax", "querying")
        )

    fillGameModel()
]
 