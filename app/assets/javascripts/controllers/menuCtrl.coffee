angular.module('web', ['ui.bootstrap']).controller 'menuCtrl', ['$scope', ($scope) ->

	$scope.menuitems = [
        {name: "Page1", href: "/"}
        {name: "Page2", href: "/"}
        {name: "Page3", href: "/"}
    ]
]