time_regexp = /^(20|21|22|23|[01]\d|\d)(:[0-5]\d)$/

app.directive 'time', ->
    require: "ngModel",
    link: (scope, elm, attr, ctrl) ->
        ctrl.$parsers.unshift (viewValue) ->
            if (time_regexp.test(viewValue))
                ctrl.$setValidity "time", true
                viewValue
            else
                ctrl.$setValidity "time", false
                undefined

app.directive 'taskListMap', ->
    restrict: 'A',
    link: (scope, elm, attr) ->
        scope.setMap()
        undefined
        
app.directive 'gameName', ->
    restrict: 'A',
    link: (scope, elm, attr) ->
        elm.on 'keyup', ->
            if scope.game.name!=undefined
                scope.isValidName()
            else
                $("#processing").attr "class", ""
                $scope.form.$setValidity "nameunique", true
        
app.directive 'geoComplete', ->
    restrict: 'A',
    link: (scope, elm, attr) ->
            elm.geocomplete types: ['(cities)']
            
app.directive 'timeCheck', ->
    restrict: 'A',
    link: (scope, elm, attr) ->
        $("#startTime, #startDate, #endTime, #endDate").bind 'input blur', (event)->
            if scope.game.startDate instanceof Date && scope.game.endDate instanceof Date && scope.form.gStartTime.$valid && scope.form.gEndTime.$valid
                from = new Date(scope.game.startDate.getTime())
                to = new Date(scope.game.endDate.getTime())
                from.setHours(parseInt(scope.game.startTime.split(":")[0]),parseInt(scope.game.startTime.split(":")[1]))
                to.setHours(parseInt(scope.game.endTime.split(":")[0]),parseInt(scope.game.endTime.split(":")[1]))
                if from.getTime()>to.getTime()
                    scope.form.$setValidity "dates", false
                else
                    scope.form.$setValidity "dates", true
            else
                scope.form.$setValidity "dates", true