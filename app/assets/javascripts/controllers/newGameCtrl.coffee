newGameCtrl = app.controller 'newGameCtrl', ['$scope', '$location', '$route', '$rootScope', 'Games', ($scope, $location, $route, $rootScope, Games) ->

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
                nextStep = stepIndex + 1
                $scope.selection = $scope.steps[nextStep]
        ) if ( $scope.hasNextStep() )

    $scope.decrementStep = ->
        (
            stepIndex = $scope.getCurrentStepIndex()
            previousStep = stepIndex - 1
            $scope.selection = $scope.steps[previousStep]
        ) if ( $scope.hasPreviousStep() )

    $scope.savegame = ->
        $scope.game.playersNum = 1000000 if $scope.game.playersNum == null

        if ($scope.gameid == null || _.isUndefined($scope.gameid))
            Games.save(
                {game: $scope.game},
                (data) ->
                    $scope.gameid = data.id
                    $scope.selection = $scope.steps[$scope.getCurrentStepIndex() + 1]
                (error) ->
                    alert("Error occured while saving a game " + error)
            )
        else
            Games.update(
                {gid: $scope.gameid, game: $scope.game},
                (data) ->
                    $scope.gameid = data.id
                    $scope.selection = $scope.steps[$scope.getCurrentStepIndex() + 1]
                (error) ->
                    alert("Error occured while updating a game " + error)
            )

    $scope.publishgame = ->
        alert "published"

    $scope.isValidName = ->
        Games.checkName(
            {name: $scope.game.name},
            (result) ->
                alert(result.valid)
            (error) ->
                alert("Error occured")
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
]
$ ->
    $("#locationInput").geocomplete types: ['(cities)']
    $("#locationInput").bind 'propertychange blur input paste', (event)->
        $sco = angular.element($('#outer')).scope()
        setTimeout (->
            $sco.game.loc = $("#locationInput").val()
        ), 500

    $("#startTime, #startDate, #endTime, #endDate").bind 'input blur', (event)->
        scp = angular.element($('#outer')).scope()
        if scp.game.startDate instanceof Date && scp.game.endDate instanceof Date && scp.form.gStartTime.$valid && scp.form.gEndTime.$valid
            from = new Date(scp.game.startDate.getTime())
            to = new Date(scp.game.endDate.getTime())
            from.setHours(parseInt(scp.game.startTime.split(":")[0]),parseInt(scp.game.startTime.split(":")[1]))
            to.setHours(parseInt(scp.game.endTime.split(":")[0]),parseInt(scp.game.endTime.split(":")[1]))
            if from.getTime()>to.getTime()
                scp.form.$setValidity "dates", false
            else
                scp.form.$setValidity "dates", true
        else
            scp.form.$setValidity "dates", true

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
                'undefined'

            

