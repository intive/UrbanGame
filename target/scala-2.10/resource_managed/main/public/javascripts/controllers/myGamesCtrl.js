app.controller('myGamesCtrl', [
  '$scope', '$location', '$timeout', 'Games', function($scope, $location, $timeout, Games) {
    var intervalID;
    $scope.safeApply = function() {
      var phase;
      phase = this.$root.$$phase;
      if (phase === '$apply' || phase === '$digest') {
        return this.$eval();
      } else {
        return this.$apply();
      }
    };
    $scope.games = [];
    $scope.tasksNo = 0;
    Games.query({}, function(data) {
      return $scope.games = data;
    }, function(error) {
      return console.log("Error occurred while getting list of games");
    });
    $scope.showDetails = function(idx) {
      if ($scope.games[idx].status === 'online') {
        window.location.pathname = "/my/games/" + $scope.games[idx].id;
      }
      if ($scope.games[idx].status === 'published' || $scope.games[idx].status === 'project') {
        return window.location.pathname = "/my/games/edit" + $scope.games[idx].id;
      }
    };
    $scope["delete"] = function(idx) {
      if ($scope.games[idx].status === 'published' || $scope.games[idx].status === 'project') {
        return Games["delete"]({
          gid: $scope.games[idx].id
        }, function(data) {
          alert("msg: " + data.msg);
          return $scope.games.splice(idx, 1);
        }, function(error) {
          return alert("error: " + error);
        });
      }
    };
    $scope.cancel = function(idx) {
      return alert("Here the game with id: " + $scope.games[idx].id + " will be canceled");
    };
    $scope.timeLeft = function(sdate, edate) {
      var dateEnd, dateStart, days, diff, hours, minutes, now, seconds, zeros;
      zeros = function(x) {
        return (x < 10 ? "0" : "") + x;
      };
      dateStart = new Date(sdate);
      dateEnd = new Date(edate);
      now = new Date();
      diff = now > dateStart ? dateEnd - now : dateStart - now;
      days = parseInt(diff / (1000 * 60 * 60 * 24));
      hours = parseInt((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      minutes = parseInt(((diff % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) / (1000 * 60));
      seconds = parseInt((((diff % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) % (1000 * 60)) / 1000);
      return zeros(days) + "d:" + zeros(hours) + "h:" + zeros(minutes) + "min:" + zeros(seconds) + "sec";
    };
    $scope.mtimeLeft = [];
    return intervalID = window.setInterval(function() {
      var i, _i, _len, _ref;
      _ref = $scope.games;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        i = _ref[_i];
        $scope.mtimeLeft[_i] = $scope.timeLeft(i.startTime, i.endTime);
      }
      return $scope.$apply();
    }, 1000);
  }
]);