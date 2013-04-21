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

app.controller 'newGameCtrl', ['$scope', '$location', ($scope, $location) ->

    $scope.steps = [
        {no: 1, name: 'Data'},
        {no: 2, name: 'Tasks'},
        {no: 3, name: 'Skin'},
        {no: 4, name: 'Publish'}
    ]
    
    $scope.selection = $scope.steps[0]

    $scope.getCurrentStepIndex = ->
        _.indexOf($scope.steps, $scope.selection)

    $scope.isDisabled = (index) ->
        true if ((index > $scope.getCurrentStepIndex()+1) || ($scope.form.$invalid && index > $scope.getCurrentStepIndex()))

    $scope.isLast = ->
        !$scope.hasNextStep()
        
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

    $scope.saveit = ->
        alert "Here this project will be saved"

    $scope.publishit = ->
        $scope.save()
        alert "and published too"

    $scope.master = {}

    $scope.update = (user) ->
        $scope.master = angular.copy(user)

    $scope.reset = ->
        $scope.user = angular.copy($scope.master)

    $scope.isUnchanged = (user) ->
        angular.equals(user, $scope.master)
        
    $scope.reset()
]