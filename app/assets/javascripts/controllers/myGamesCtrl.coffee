app.controller 'myGamesCtrl', ['$scope', '$location', '$timeout', 'Games', ($scope, $location, $timeout, Games) ->
    
    $scope.safeApply =  ->
        phase = this.$root.$$phase;
        if(phase == '$apply' || phase == '$digest')
            this.$eval();
        else
            this.$apply();

    $scope.games = []
    $scope.tasksNo = 0

    Games.query(
        {},
        (data) ->
            $scope.games = data
        (error) ->
            console.log("Error occurred while getting list of games")
    )

    $scope.showDetails = (idx) ->
        window.location.pathname = "/my/games/" + $scope.games[idx].id if $scope.games[idx].status == 'online'
        window.location.pathname = "/my/games/edit" + $scope.games[idx].id if $scope.games[idx].status == 'published' || $scope.games[idx].status == 'project'
    $scope.delete = (idx) ->
        if ($scope.games[idx].status == 'published' || $scope.games[idx].status == 'project')
            Games.delete(
                {gid: $scope.games[idx].id},
                (data) ->
                    alert("msg: " + data.msg)
                    $scope.games.splice(idx, 1)
                (error) ->
                    alert("error: " + error)
            ) 

    $scope.cancel = (idx) ->
        alert "Here the game with id: " + $scope.games[idx].id + " will be canceled"

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