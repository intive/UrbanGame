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

# '------------------------------------------ GAMES LIST CTRL
app.controller 'myGamesCtrl', ['$scope', '$location', '$timeout', 'Games', 'Utilities', '$dialog', '$filter', ($scope, $location, $timeout, Games, Utilities, $dialog, $filter) ->

    # ------------------- INITIAL DATA LOADING
    $scope.games = []
    $scope.sortOrder = ['startDate']
    $scope.order = ['startDate']
    $scope.reverse = false
    $scope.filteredItems = []
    $scope.groupedItems = null
    $scope.itemsPerPage = 5
    $scope.pagedItems = []
    $scope.currentPage = 0

    querygames = ->
        Games.query(
            {},
            (data) ->
                $scope.games = data
                $scope.search()
            (error) ->
                alert Messages("js.errors.gameslist")
        )

    queryarchives = ->
        Games.archive(
            {},
            (data) ->
                $scope.games = data
                $scope.search()
            (error) ->
                alert Messages("js.errors.gameslist")
        )

    if (/\/my\/games\/archive/gi.test(window.location.pathname))
        queryarchives()
    else
        querygames()

    # ------------------- GAME OPTIONS ACTIONS
    $scope.showDetails = (idx) ->
        games = $scope.pagedItems[$scope.currentPage][idx]
        window.location.pathname = "/my/games/" + games.id if games.status == 'online' || games.status == 'finished'
        window.location.pathname = "/my/games/" + games.id + "/edit" if (games.status == 'published' || games.status == 'project')

    $scope.delete = (idx) ->
        game = $scope.pagedItems[$scope.currentPage][idx]
        if (game.status == 'project')
            title = game.name
            msg = Messages("js.errors.sure", "delete")
            btns = [{result:'no', label: Messages("js.no")}, {result:'ok', label: Messages("js.yes", "delete"), cssClass: 'btn-primary'}]

            $dialog.messageBox(title, msg, btns)
                .open()
                .then (result) ->
                    if(result == "ok")
                        Games.delete(
                            {gid: game.id},
                            (data) ->
                                i = _.indexOf($scope.games, $scope.pagedItems[$scope.currentPage][idx])
                                curP = $scope.currentPage
                                $scope.games.splice(i, 1)
                                $scope.search()
                                $scope.currentPage = curP
                            (error) ->
                                alert Messages("js.errors.ajax", "deleting")
                        )

    $scope.cancel = (idx) ->
        game = $scope.pagedItems[$scope.currentPage][idx]
        if (game.status == 'published')
            title = game.name
            msg = Messages("js.errors.sure", "cancel")
            btns = [{result:'no', label: Messages("js.no")}, {result:'ok', label: Messages("js.yes", "cancel"), cssClass: 'btn-primary'}]

            $dialog.messageBox(title, msg, btns)
                .open()
                .then (result) ->
                    if(result == "ok")
                        Games.cancel(
                            {data: game.id},
                            (data) ->
                                i = _.indexOf($scope.games, $scope.pagedItems[$scope.currentPage][idx])
                                curP = $scope.currentPage
                                $scope.games[i].status = "project"
                                $scope.search()
                                $scope.currentPage = curP
                            (error) ->
                                alert Messages("js.errors.ajax", "canceling")
                        )

    # ------------------------------ UTILITY METHODS - TIME COUNTER
    $scope.mtimeLeft = [];

    intervalID = window.setInterval( ->
        $scope.mtimeLeft[_i] = Utilities.difference i.startTime, i.endTime, true for i in $scope.pagedItems[$scope.currentPage]
        $scope.$apply()
    , 1000)

    # ------------------------------ SORTING
    $scope.sortBy = (newOrder) ->
        $scope.reverse = !$scope.reverse if $scope.sortOrder == newOrder

        $scope.sortOrder = newOrder

        $('th i').each ->
            $(this).removeClass().addClass('icon-sort')

        if $scope.reverse
            $('th.'+newOrder+' i').removeClass().addClass('icon-chevron-up')
        else
            $('th.'+newOrder+' i').removeClass().addClass('icon-chevron-down')
        $scope.search()

    # ------------------------------ SEARCHING
    searchMatch = (haystack, needle) ->
        if !needle
            true 
        else
            haystack.toString().toLowerCase().indexOf(needle.toLowerCase()) != -1

    $scope.search = ->
        $scope.filteredItems = $filter('filter')($scope.games, (game) ->
            for attr of game
                if searchMatch(game[attr], $scope.query)
                    return true 
            return false
        )

        $scope.filteredItems = $filter('orderBy')($scope.filteredItems, $scope.sortOrder, $scope.reverse) if $scope.sortOrder != ''
        $scope.currentPage = 0
        $scope.groupToPages()

    # ------------------------------ PAGINATING
    $scope.groupToPages = ->
        $scope.pagedItems = []
        
        for i in [0..$scope.filteredItems.length-1] by 1
            if i % $scope.itemsPerPage == 0
                $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)] = [ $scope.filteredItems[i] ]
            else
                $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)].push($scope.filteredItems[i])


    $scope.range = (start, end) ->
        ret = []
        if (!end)
            end = start
            start = 0
        for i in [start..end-1] by 1
            ret.push(i)
        ret
    
    $scope.prevPage = ->
        if ($scope.currentPage > 0) 
            $scope.currentPage--
    
    $scope.nextPage = ->
        if ($scope.currentPage < $scope.pagedItems.length - 1)
            $scope.currentPage++
    
    $scope.setPage = ->
        $scope.currentPage = this.n
]