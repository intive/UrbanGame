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

app.controller 'myGamesCtrl', ['$scope', '$location', '$timeout', 'Games', '$dialog', ($scope, $location, $timeout, Games, $dialog) ->

    $scope.games = []

    querygame = ->
        Games.query(
            {},
            (data) ->
                $scope.games = data
            (error) ->
                console.log("Error occurred while getting list of games")
        )

    querygame()

    $scope.showDetails = (idx) ->
        games = $scope.games[idx]
        window.location.pathname = "/my/games/" + games.id if games.status == 'online'
        window.location.pathname = "/my/games/" + games.id + "/edit" if games.status == 'published' || games.status == 'project'

    $scope.delete = (idx) ->
        games = $scope.games[idx]
        if (games.status == 'project')
            title = games.name
            msg = 'Are you sure you want to delete this game?'
            btns = [{result:'no', label: 'No'}, {result:'ok', label: 'Yes, delete this game', cssClass: 'btn-primary'}]

            $dialog.messageBox(title, msg, btns)
                .open()
                .then (result) ->
                    if(result == "ok")
                        Games.delete(
                            {gid: games.id},
                            (data) ->
                                $scope.games.splice(idx, 1)
                            (error) ->
                                alert("Error occured when deleting the game.")
                        )

    $scope.cancel = (idx) ->
        games = $scope.games[idx]
        if (games.status == 'published')
            title = games.name
            msg = 'Are you sure you want to cancel this game?'
            btns = [{result:'no', label: 'No'}, {result:'ok', label: 'Yes, cancel this game', cssClass: 'btn-primary'}]

            $dialog.messageBox(title, msg, btns)
                .open()
                .then (result) ->
                    if(result == "ok")
                        Games.cancel(
                            {data: games.id},
                            (data) ->
                                $scope.games[idx].status = "project"
                            (error) ->
                                alert("Error occured when canceling the game.")
                        )

    $scope.timeLeft = (sdate, edate) ->
        zeros = (x) -> 
            ( if x < 10 then "0" else "") + x

        dateStart = new Date(sdate)
        dateEnd = new Date(edate)
        now = new Date()
        
        diff = if now > dateStart then (dateEnd - now) else (dateStart - now)
        days = parseInt(diff / (1000*60*60*24))
        hours = parseInt((diff % (1000*60*60*24)) / (1000*60*60))
        minutes = parseInt(((diff % (1000*60*60*24)) % (1000*60*60)) / (1000*60))
        seconds = parseInt((((diff % (1000*60*60*24)) % (1000*60*60)) % (1000*60)) / 1000)

        zeros(days) + "d:" + zeros(hours) + "h:" + zeros(minutes) + "min:" + zeros(seconds) + "sec"

    $scope.mtimeLeft = [];

    intervalID = window.setInterval( ->
        $scope.mtimeLeft[_i] = $scope.timeLeft i.startTime, i.endTime for i in $scope.games
        $scope.$apply()
    , 1000)

]