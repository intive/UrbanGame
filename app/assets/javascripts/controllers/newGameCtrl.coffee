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

newGameCtrl = app.controller 'newGameCtrl', ['$scope', '$location', '$route', '$rootScope', 'Games', '$dialog', ($scope, $location, $route, $rootScope, Games, $dialog) ->
    $scope.steps = [
        {no: 1, name: 'Data'},
        {no: 2, name: 'Tasks'},
        {no: 3, name: 'Skin'},
        {no: 4, name: 'Publish'}
    ]

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
        awards: ""
    }

    $scope.tasks = []

    loadModelData = (data) ->
        $scope.game = {
            name: data.name,
            description: data.description,
            location: data.location,
            startTime: new Date(data.startTime).toLocaleTimeString(),
            startDate: new Date(data.startTime),
            endTime: new Date(data.endTime).toLocaleTimeString(),
            endDate: new Date(data.endTime),
            winning: data.winning,
            winningNum: data.nWins,
            diff: data.difficulty,
            playersNum: if (data.maxPlayers == 1000000) then null else data.maxPlayers,
            awards: data.awards
        }

    isEdit = (gameid) ->
        if (/\/my\/games\/\d+\/edit/gi.test(window.location.pathname))
            $scope.gameid = parseInt(/(\d+)/.exec(window.location.pathname)[1])
            true
        else
            false

    fillGameModel = ->
        if isEdit($scope.gameid)
            Games.get(
                {gid: $scope.gameid},
                (data) ->
                    loadModelData(data)
                (error) ->
                    alert "Error occured while querying the data."
            )

    fillGameModel()
    
    $scope.selection = $scope.steps[0]

    $scope.getCurrentStepIndex = ->
        _.indexOf($scope.steps, $scope.selection)

    $scope.isDisabled = (index) ->
        true if ((index > $scope.getCurrentStepIndex()+1) || ($scope.form.$invalid && index > $scope.getCurrentStepIndex()))

    $scope.isLast = ->
        !$scope.hasNextStep()
        
    $scope.goToStep = (index) ->
            $scope.selection = $scope.steps[index] if ( !_.isUndefined($scope.steps[index]) )

    $scope.hasNextStep = ->
        stepIndex = $scope.getCurrentStepIndex()
        nextStep = stepIndex + 1
        !_.isUndefined($scope.steps[nextStep])

    $scope.hasPreviousStep = ->
        stepIndex = $scope.getCurrentStepIndex()
        previousStep = stepIndex - 1
        !_.isUndefined($scope.steps[previousStep])

    $scope.incrementStepIfValid = ->
        $scope.incrementStep() if (!$scope.form.$invalid)
    
    $scope.incrementStep = ->
        (
            stepIndex = $scope.getCurrentStepIndex()
            if stepIndex == 0
                $scope.savegame()
            else
                if stepIndex == 1
                    !$scope.geolocationBound=false
                nextStep = stepIndex + 1
                $scope.selection = $scope.steps[nextStep]
        ) if ( $scope.hasNextStep() )
        
    $scope.decrementStep = ->
        (
            stepIndex = $scope.getCurrentStepIndex()
            previousStep = stepIndex - 1
            $scope.selection = $scope.steps[previousStep]
            if ($scope.getCurrentStepIndex()==0)
                $scope.onTab1Switch() 
        ) if ( $scope.hasPreviousStep() )

    $scope.savegame = ->
        if $scope.game.playersNum == null
            $scope.game.playersNum = 1000000
            notSetByUser = true 

        if ($scope.gameid == null || _.isUndefined($scope.gameid))
            Games.save(
                {data: $scope.game},
                (data) ->
                    $scope.gameid = data.val

                    if $scope.getCurrentStepIndex() == 3
                        window.location = "/my/games"
                    
                    $scope.selection = $scope.steps[$scope.getCurrentStepIndex() + 1]
                (error) ->
                    alert "Error occured while saving a game."
            )
        else
            Games.update(
                {id: $scope.gameid, data: $scope.game},
                (data) ->
                    if $scope.getCurrentStepIndex() == 3
                        window.location = "/my/games"

                    $scope.selection = $scope.steps[$scope.getCurrentStepIndex() + 1]
                (error) ->
                    alert "Error occured while updating a game."
            )
        if notSetByUser
            $scope.game.playersNum = null

    $scope.publishgame = ->
        if ($scope.gameid != null && !_.isUndefined($scope.gameid))
            title = $scope.game.name
            msg = 'Are you sure you want to publish this game?'
            btns = [{result:'no', label: 'No'}, {result:'ok', label: 'Yes, publish this game', cssClass: 'btn-primary'}]

            $dialog.messageBox(title, msg, btns)
                .open()
                .then (result) ->
                    if(result == "ok")
                        Games.publish(
                            {data: $scope.gameid},
                            (data) ->
                                window.location = "/my/games"
                            (error) ->
                                alert "Error occured when publishing the game."
                        )
    
     $scope.isValidName = ->
        if ($scope.gameid == null || _.isUndefined($scope.gameid))
            Games.checkName(
                {data: $scope.game.name},
                (result) ->
                    if result.val
                        $scope.form.$setValidity "nameunique", true
                    else
                        $scope.form.$setValidity "nameunique", false
                (error) ->
                    alert("Error occured while checking if game name is unique " + error)
            )

    $scope.incrementPlayersNum = ->
        if  $scope.game.playersNum==null
            $scope.game.playersNum=2
        else
            $scope.game.playersNum+=1
        
    $scope.decrementPlayersNum = ->
        if $scope.game.playersNum>2
            $scope.game.playersNum-=1  
        else 
            $scope.game.playersNum=null
        
    $scope.incrementWinningNum = ->
        $scope.game.winningNum+=1
   
        
    $scope.decrementWinningNum = ->
        $scope.game.winningNum-=1 if $scope.game.winningNum>1
        
    $scope.timezone = "GMT" + ((if new Date().getTimezoneOffset() > 0 then "" else "+")) + (new Date().getTimezoneOffset() / (-60))

    $scope.gameduration = ->
        $scope.game.endDate - $scope.game.startDate
    
    $scope.$watch '[game.winningNum, game.playersNum]', ->
        if $scope.game.winningNum==undefined
            $scope.form.$setValidity "morewinnersthanplayers", false
        else
            if ($scope.game.playersNum!=null)
                if ($scope.game.playersNum>$scope.game.winningNum)
                    $scope.form.$setValidity "morewinnersthanplayers", true
                else
                    $scope.form.$setValidity "morewinnersthanplayers", false
            else
                $scope.form.$setValidity "morewinnersthanplayers", true
    , true
    
    $scope.onTab1Switch = ->
        setTimeout (->
            $("#locationInput").on 'click', -> 
                if (!$scope.geolocationBound) 
                    $("#locationInput").geocomplete types: ['(cities)']
                    $scope.geolocationBound=true
            $("#nameInput").on 'keyup', ->
                if $scope.game.name!=undefined
                    $scope.isValidName()
            $("#locationInput").on 'click', -> $("#locationInput").bind 'propertychange blur input paste', (event)->
                setTimeout (->
                    $scope.game.loc = $("#locationInput").val()
                ), 500
            $("#startTime, #startDate, #endTime, #endDate").bind 'input blur', (event)->
                if $scope.game.startDate instanceof Date && $scope.game.endDate instanceof Date && $scope.form.gStartTime.$valid && $scope.form.gEndTime.$valid
                    from = new Date($scope.game.startDate.getTime())
                    to = new Date($scope.game.endDate.getTime())
                    from.setHours(parseInt($scope.game.startTime.split(":")[0]),parseInt($scope.game.startTime.split(":")[1]))
                    to.setHours(parseInt($scope.game.endTime.split(":")[0]),parseInt($scope.game.endTime.split(":")[1]))
                    if from.getTime()>to.getTime()
                        $scope.form.$setValidity "dates", false
                    else
                        $scope.form.$setValidity "dates", true
                else
                    $scope.form.$setValidity "dates", true
        ), 200
        
    $ ->
        $scope.onTab1Switch()
]


time_regexp = /^(20|21|22|23|[01]\d|\d)(:[0-5]\d)$/
app.directive 'time', ->
    require: "ngModel"
    link: (scope, elm, attr, ctrl) ->
        ctrl.$parsers.unshift (viewValue) ->
            if (time_regexp.test(viewValue))
                ctrl.$setValidity "time", true
                viewValue
            else
                ctrl.$setValidity "time", false
                undefined

            

