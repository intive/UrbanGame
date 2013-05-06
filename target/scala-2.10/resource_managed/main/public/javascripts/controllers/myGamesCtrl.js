app.controller('myGamesCtrl', [
  '$scope', '$timeout', 'Games', function($scope, $timeout, Games) {
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
    Games.query({}, function(data) {
      return $scope.games = data;
    }, function(error) {
      return console.log("Error occurred while getting list of games");
    });
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