/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
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
*/
var ngctrl, time_regexp;
ngctrl = app.controller('newGameCtrl', [
  '$scope', '$location', '$route', '$rootScope', function($scope, $location, $route, $rootScope) {
    $scope.steps = [
      {
        no: 1,
        name: 'Data'
      }, {
        no: 2,
        name: 'Tasks'
      }, {
        no: 3,
        name: 'Skin'
      }, {
        no: 4,
        name: 'Publish'
      }
    ];
    $scope.game = {
      name: "",
      description: "",
      awards: "",
      type: "gameType1",
      loc: "",
      startTime: "",
      startDate: null,
      endTime: "",
      endDate: null,
      playersNum: 1,
      diff: ""
    };
    $scope.selection = $scope.steps[0];
    $scope.getCurrentStepIndex = function() {
      return _.indexOf($scope.steps, $scope.selection);
    };
    $scope.isDisabled = function(index) {
      if ((index > $scope.getCurrentStepIndex() + 1) || ($scope.form.$invalid && index > $scope.getCurrentStepIndex())) {
        return true;
      }
    };
    $scope.isLast = function() {
      return !$scope.hasNextStep();
    };
    $scope.goToStep = function(index) {
      if (!_.isUndefined($scope.steps[index])) {
        return $scope.selection = $scope.steps[index];
      }
    };
    $scope.hasNextStep = function() {
      var nextStep, stepIndex;
      stepIndex = $scope.getCurrentStepIndex();
      nextStep = stepIndex + 1;
      return !_.isUndefined($scope.steps[nextStep]);
    };
    $scope.hasPreviousStep = function() {
      var previousStep, stepIndex;
      stepIndex = $scope.getCurrentStepIndex();
      previousStep = stepIndex - 1;
      return !_.isUndefined($scope.steps[previousStep]);
    };
    $scope.incrementStep = function() {
      var nextStep, stepIndex;
      if ($scope.hasNextStep()) {
        stepIndex = $scope.getCurrentStepIndex();
        nextStep = stepIndex + 1;
        return $scope.selection = $scope.steps[nextStep];
      }
    };
    $scope.decrementStep = function() {
      var previousStep, stepIndex;
      if ($scope.hasPreviousStep()) {
        stepIndex = $scope.getCurrentStepIndex();
        previousStep = stepIndex - 1;
        return $scope.selection = $scope.steps[previousStep];
      }
    };
    $scope.saveit = function() {
      return alert("Here this project will be saved");
    };
    $scope.publishit = function() {
      $scope.saveit();
      return alert("and published too");
    };
    $scope.incrementPlayersNum = function() {
      return $scope.game.playersNum += 1;
    };
    $scope.decrementPlayersNum = function() {
      if ($scope.game.playersNum > 0) {
        return $scope.game.playersNum -= 1;
      }
    };
    return $scope.timezone = "GMT" + (new Date().getTimezoneOffset() > 0 ? "" : "+") + (new Date().getTimezoneOffset() / (-60));
  }
]);
$(function() {
  $("#locationInput").geocomplete({
    types: ['(cities)']
  });
  $("#locationInput").bind('propertychange blur input paste', function(event) {
    var $sco;
    $sco = angular.element($('#outer')).scope();
    return setTimeout((function() {
      return $sco.game.loc = $("#locationInput").val();
    }), 500);
  });
  return $("#startTime, #startDate, #endTime, #endDate").bind('input blur', function(event) {
    var from, scp, to;
    console.log('blur');
    scp = angular.element($('#outer')).scope();
    if (scp.game.startDate instanceof Date && scp.game.endDate instanceof Date && scp.form.gStartTime.$valid && scp.form.gEndTime.$valid) {
      from = new Date(scp.game.startDate.getTime());
      to = new Date(scp.game.endDate.getTime());
      from.setHours(parseInt(scp.game.startTime.split(":")[0]), parseInt(scp.game.startTime.split(":")[1]));
      to.setHours(parseInt(scp.game.endTime.split(":")[0]), parseInt(scp.game.endTime.split(":")[1]));
      if (from.getTime() > to.getTime()) {
        return scp.dateNotOrdered = true;
      } else {
        return scp.dateNotOrdered = false;
      }
    } else {
      return scp.dateNotOrdered = false;
    }
  });
});
time_regexp = /^(20|21|22|23|[01]\d|\d)(:[0-5]\d)$/;
app.directive('time', function() {
  return {
    require: "ngModel",
    link: function(scope, elm, attr, ctrl) {
      return ctrl.$parsers.unshift(function(viewValue) {
        if (time_regexp.test(viewValue)) {
          ctrl.$setValidity("time", true);
          return viewValue;
        } else {
          ctrl.$setValidity("time", false);
          return 'undefined';
        }
      });
    }
  };
});