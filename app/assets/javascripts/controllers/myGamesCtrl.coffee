app.controller 'myGamesCtrl', ['$scope', '$timeout', ($scope, $timeout) ->
	
	$scope.safeApply =  ->
		phase = this.$root.$$phase;
		if(phase == '$apply' || phase == '$digest')
			this.$eval();
		else
			this.$apply();

	$scope.games = [
		{
			gid: 1, 
			name: "Game1", 
			version: "1.0", 
			location: "Wroclaw, Poland", 
			timestart: "04/04/2013 12:00 GMT+0100",
			timeend: "05/04/2013 12:00 GMT+0100", 
			tasksno: 10, 
			status: "online", 
			icon: "games/gameicon.png"
		}
		{
			gid: 2, 
			name: "Game2", 
			version: "1.0", 
			location: "Warszawa, Poland", 
			timestart: "05/20/2013 10:00 GMT+0100", 
			timeend: "06/28/2013 12:00 GMT+0100",
			tasksno: 50, 
			status: "project", 
			icon: "games/gameicon.png"
		}
		{
			gid: 3, 
			name: "Game3", 
			version: "2.0", 
			location: "Aberdeen City, United Kingdom", 
			timestart: "04/30/2013 12:00 GMT",
			timeend: "07/21/2013 12:00 GMT",
			tasksno: 28, 
			status: "published", 
			icon: "games/gameicon.png"
		}
	]

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
		$scope.mtimeLeft[_i] = $scope.timeLeft i.timestart, i.timeend for i in $scope.games
		$scope.$apply()
	, 1000)

]