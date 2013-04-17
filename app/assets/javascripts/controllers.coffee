###*Copyright 2013 BLStream, BLStream's Patronage Program Contributors
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
###
app = angular.module('web', ['ui'])

NewGameCtrl = ($scope, $location) ->

    $scope.steps = [
        'Start',
        '1',
        '2',
        '3',
        'Finish'
    ]
    
    $scope.selection = $scope.steps[0]

    $scope.getCurrentStepIndex = ->
        _.indexOf($scope.steps, $scope.selection)
        
    $scope.goToStep = (index) ->
        $scope.selection = $scope.steps[index] if ( !_.isUndefined($scope.steps[index]) )

    $scope.hasNextStep = ->
        stepIndex = $scope.getCurrentStepIndex()
        nextStep = stepIndex + 1
        !_.isUndefined($scope.steps[nextStep])

    $scope.hasPreviousStep = ->
        stepIndex = $scope.getCurrentStepIndex()
        previousStep = stepIndex - 1
        !_.isUndefined($scope.steps[previousStep])

    $scope.incrementStep = ->
        (
            stepIndex = $scope.getCurrentStepIndex()
            nextStep = stepIndex + 1
            $scope.selection = $scope.steps[nextStep]
        ) if ( $scope.hasNextStep() )

    $scope.decrementStep = ->
        (
            stepIndex = $scope.getCurrentStepIndex()
            previousStep = stepIndex - 1
            $scope.selection = $scope.steps[previousStep]
        ) if ( $scope.hasPreviousStep() )

MenuCtrl = ($scope) ->

    $scope.menuitems = [
        {name: "Page1", href: "/"}
        {name: "Page2", href: "/"}
        {name: "Page3", href: "/"}
    ]