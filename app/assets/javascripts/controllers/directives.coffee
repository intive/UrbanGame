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
            if scope.game.name != undefined
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

app.directive 'passwordConfirm', ->
    require: "ngModel",
    link: (scope, elem, attrs, ctrl) ->
        otherInput = elem.inheritedData("$formController")[attrs.passwordConfirm]

        ctrl.$parsers.push((value) ->
            if(value == otherInput.$viewValue)
                ctrl.$setValidity "repeat", true
                return value
            ctrl.$setValidity "repeat", false
        )

        otherInput.$parsers.push((value) ->
            ctrl.$setValidity "repeat", value == ctrl.$viewValue
            return value
        )

app.directive 'userEmail', ->
    require: "ngModel",
    link: (scope, elem, attrs, ctrl) ->
        ctrl.$parsers.push((value) ->
            if value != undefined
                scope.isValidEmail(value).
                    success((data, status) ->
                        if data.val
                            ctrl.$setValidity "emailunique", true
                        else
                            ctrl.$setValidity "emailunique", false
                        scope.error = null
                    ).
                    error((data, status) ->
                        ctrl.$setValidity "emailunique", true
                        scope.error = Messages("js.errors.email") + data
                    )
            $("#processing").removeClass("img-processing")
            value
        )