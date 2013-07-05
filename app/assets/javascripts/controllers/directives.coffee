time_regexp = /^(20|21|22|23|[01]\d|\d)(:[0-5]\d)$/
number_regexp = /^\d+$/
number_pn_regexp = /^[\+\-]?(\d+)?$/

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

app.directive 'radius', ->
    restrict: 'A',
    link: (scope, elm, attr, ctrl) ->
        scope.radiusIsNumber = true
        elm.on 'keyup', ->
            if number_regexp.test(elm.val())
                radius = Number(elm.val())
                scope.changeRadius(radius)
                scope.radiusIsNumber = true
            else
                scope.radiusIsNumber = false
            scope.$apply()
            
            
app.directive 'minpoints', ->
    restrict: 'A',
    require: "ngModel",
    link: (scope, elm, attr, ctrl) ->
        scope.minPointsValid = true
        elm.on 'keyup', ->
            if (ctrl.$viewValue == "")
                ctrl.$setValidity "minpoints", true
            else
                if !number_regexp.test(ctrl.$viewValue)
                    scope.$apply ->
                        ctrl.$setValidity "minpoints", false
                else
                    minpoints = Number(ctrl.$viewValue)
                    sum = 0
                    pointsCorrect = true
                    scope.task.answers.forEach (answer) ->
                        if (pointsCorrect)
                            if number_pn_regexp.test(answer.points)
                                num = Number(answer.points)
                                sum += num if num>0
                            else
                                pointsCorrect = false
                    if !pointsCorrect
                        scope.$apply ->
                            ctrl.$setValidity "minpoints", false
                    else
                        scope.$apply ->
                            ctrl.$setValidity "minpoints", (sum>=minpoints)
                    
app.directive 'points', ->
    restrict: 'A',
    link: (scope, elm, attr, ctrl) ->
        elm.on 'keyup', ->
            if (this.value != this.value.replace(/[^0-9\-\+]/g, ''))
                this.value = this.value.replace(/[^0-9\-\+]/g, '')
                scope.$apply(attr.ngModel + '=' + elm.val())
            if !number_pn_regexp.test(elm.val())
                scope.$apply(attr.ngModel + '=' + "0")
                
app.directive 'positivePoints', ->
    restrict: 'A',
    link: (scope, elm, attr, ctrl) ->
        elm.on 'keyup', ->
            if (this.value != this.value.replace(/[^0-9]/g, ''))
                this.value = this.value.replace(/[^0-9]/g, '')
                scope.$apply(attr.ngModel + '=' + elm.val())
            if !number_pn_regexp.test(elm.val())
                scope.$apply(attr.ngModel + '=' + "0")
                
app.directive 'attempts', ->
    restrict: 'A',
    require: "ngModel",
    link: (scope, elm, attr, ctrl) ->
        elm.on 'keyup', ->
            scope.$apply ->
                ctrl.$setValidity "attempts", ((number_regexp.test(ctrl.$viewValue) && Number(ctrl.$viewValue)>=2) || ctrl.$viewValue == "")
            
            
app.directive 'taskListMap', ->
    restrict: 'A',
    link: (scope, elm, attr) ->
        scope.setMap()
        undefined
        
app.directive 'taskGpsMap', ->
    restrict: 'A',
    link: (scope, elm, attr) ->
        scope.setTMap()
        undefined
        
app.directive 'gameName', ->
    restrict: 'A',
    link: (scope, elm, attr) ->
        elm.on 'keyup', ->
            if scope.game.name != undefined
                scope.isValidName()
            else
                $("#processing").attr "class", ""
                scope.form.$setValidity "nameunique", true
        
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
