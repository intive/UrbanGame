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

# '------------------------------------- OPTIONS CTRL
app.controller 'optionsCtrl', ['$scope', '$http', 'User', ($scope, $http, User) ->
    $scope.user = {
        name: "",
        email: "",
        about: "",
        passwordOld: "",
        passwordNew: "",
        passwordConfirm: "",
        logo: "users/logo.png"
    }

    $scope.error = null
    $scope.info = null

    clearPasswords = ->
        $scope.user.passwordOld = ""
        $scope.user.passwordNew = ""
        $scope.user.passwordConfirm = ""

    resource = {
        "get": ->
            User.get(
                {},
                (data) ->
                    $scope.user = {
                        name: data.name,
                        email: data.email,
                        about: data.description,
                        logo: data.logo
                    }
                (error) ->
                    $scope.error = Messages("js.errors.options.profile.load")
            )
        "update": (values) ->
            User.update(
                {
                    data: values
                },
                (data) ->
                    $scope.info = Messages("notify.options.profile.saved")
                    clearPasswords()
                (error) ->
                    $scope.error = Messages("js.errors.options.profile.save")
                    clearPasswords()
            )
    }

    $scope.loadProfile = ->
        resource["get"]()

    $scope.editorEnabled = false

    $scope.edit = ->
        $scope.editorEnabled = true
        $scope.uname = $scope.user.name

    $scope.cancel = ->
        $scope.editorEnabled = false

    $scope.ok = ->
        if($scope.uname == "")
            return false

        $scope.user.name = $scope.uname
        $scope.cancel()

    $scope.saveProfile = ->
        user = $scope.user
        if(user.passwordOld != "" && user.passwordOld != undefined)
            alert user.passwordOld
            $http({method: 'GET', url: '/my/options/json/checkPassword/' + user.passwordOld}).
                success((data, status) ->
                    matchPasswords = data.val
                    alert matchPasswords
                    if(!matchPasswords || user.passwordNew != user.passwordConfirm)
                        $scope.error = Messages("js.errors.options.profile.passwords.match")
                        clearPasswords()
                        return
                    values = {
                        name: user.name,
                        description: user.about,
                        password: user.passwordNew,
                        logo: user.logo
                    }

                    resource["update"](values)
                ).
                error((data, status) ->
                    $scope.error = Messages("js.errors.options.profile.passwords.match.undefined")
                    clearPasswords()
                )
        else
            values = {
                name: user.name,
                description: user.about,
                logo: user.logo
            }

            resource["update"](values)
        

    $scope.loadProfile()

    $ ->
        $('#fileupload').fileupload({
            dataType: false,
            add: (e,data) ->
                $('#progress-bar').css('width', '0%')
                $('#progress').show()
                data.submit()
                .success((result, textStatus, jqXHR) -> 
                    $.each(result.files, (index, file) ->
                        $scope.user.logo = file.url
                        $scope.$apply()
                    )
                )
                .error((jqXHR, textStatus, errorThrown) -> 
                    $scope.error = Messages("js.errors.options.profile.file.upload") + errorThrown
                )
            progressall: (e, data) ->
                progress = parseInt(data.loaded / data.total * 100, 10) + '%'
                $('#progress-bar').css('width', progress)
            done: (e, data) ->
                $('#progress').fadeOut()
            maxFileSize: 5000000, 
            maxNumberOfFiles: 1,
            acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i
        })
]

app.factory 'User', ($resource) ->
    $resource '/my/options/json/:ctrl',
        {
            ctrl: '@ctrl'
        },
        {
            get: {method: 'GET', params: {ctrl: "profile"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            update: {method: 'PUT', params: {ctrl: "profile"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]}
        }