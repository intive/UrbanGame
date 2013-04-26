app.controller 'myGamesCtrl', ['$scope', ($scope) ->
	$scope.games = [
		{
			gid: 1, 
			name: "Game1", 
			version: "1.0", 
			location: "Wroclaw, Poland", 
			timestart: "12:00 GMT+1 23/04/2013", 
			tostart: "", 
			toend: "2d:10h:20min:15sec", 
			tasksno: 10, 
			status: "online", 
			icon: "games/gameicon.png"
		}
		{
			gid: 2, 
			name: "Game2", 
			version: "1.0", 
			location: "Warszawa, Poland", 
			timestart: "10:00 GMT+1 20/05/2013", 
			tostart: "", 
			toend: "", 
			tasksno: 50, 
			status: "project", 
			icon: "games/gameicon.png"
		}
		{
			gid: 3, 
			name: "Game3", 
			version: "2.0", 
			location: "Aberdeen City, United Kingdom", 
			timestart: "12:00 GMT 30/04/2013", 
			tostart: "10h:23min:00sec", 
			toend: "", 
			tasksno: 28, 
			status: "published", 
			icon: "games/gameicon.png"
		}
	]
]