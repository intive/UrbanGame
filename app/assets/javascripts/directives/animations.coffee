app.directive 'fadey', -> 
    restrict: 'A',
    link: (scope, elm, attrs) ->
        duration = parseInt(attrs.fadey)
        duration = 500 if (isNaN(duration))
            
        elm = jQuery(elm)
        elm.hide()
        elm.fadeIn(duration)

        scope.delete = (complete) ->
            elm.fadeOut duration, ->
                complete.apply(scope) if (complete)
    