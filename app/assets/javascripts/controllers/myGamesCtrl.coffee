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
app.controller 'myGamesCtrl', ['$scope', '$location', 'Games', 'Utilities', '$filter', ($scope, $location, Games, Utilities, $filter) ->

    # ------------------- INITIAL DATA LOADING
    $scope.games = []
    $scope.sortOrder = ['startDate']
    $scope.reverse = false
    $scope.filteredItems = []
    $scope.groupedItems = null
    $scope.itemsPerPage = 5
    $scope.pagedItems = []
    $scope.currentPage = 0
    $scope.isArchive = false
    $scope.error = null

    resource = {
        "querygames": ->
            Games.query(
                {},
                (data) ->
                    $scope.games = data
                    $scope.search()
                (error) ->
                    $scope.error = Messages("js.errors.gameslist")
            )
        ,
        "queryarchives": ->
            Games.archive(
                {},
                (data) ->
                    $scope.games = data
                    $scope.search()
                (error) ->
                    $scope.error = Messages("js.errors.gameslist")
            )
        ,
        "delete": (idx) ->
            game = $scope.pagedItems[$scope.currentPage][idx]
            Games.delete(
                {gid: game.id},
                (data) ->
                    i = _.indexOf($scope.games, game)
                    curP = $scope.currentPage
                    $scope.games.splice(i, 1)
                    $scope.search()
                    $scope.currentPage = curP
                (error) ->
                    $scope.error = Messages("js.errors.ajax", "deleting")
            )
        ,
        "cancel": (idx) ->
            game = $scope.pagedItems[$scope.currentPage][idx]
            Games.cancel(
                {data: game.id},
                (data) ->
                    i = _.indexOf($scope.games, game)
                    curP = $scope.currentPage
                    $scope.games[i].status = "project"
                    $scope.search()
                    $scope.currentPage = curP
                (error) ->
                    $scope.error = Messages("js.errors.ajax", "canceling")
            )
    }
       

    if (/\/my\/games\/archive/gi.test(window.location.pathname))
        $scope.isArchive = true
        resource["queryarchives"]()
    else
        resource["querygames"]()

    # ------------------- GAME OPTIONS ACTIONS
    $scope.showDetails = (idx) ->
        games = $scope.pagedItems[$scope.currentPage][idx]
        window.location.pathname = "/my/games/" + games.id if games.status == 'online' || games.status == 'finished'
        window.location.pathname = "/my/games/" + games.id + "/edit" if (games.status == 'published' || games.status == 'project')

    $scope.delete = (idx) ->
        game = $scope.pagedItems[$scope.currentPage][idx]
        action = "delete"

        if (game.status == 'project')
            Utilities.showDialog(game.name, action)
                .then (result) ->
                    if(result == "ok")
                        resource[action](idx)
                        
    $scope.cancel = (idx) ->
        game = $scope.pagedItems[$scope.currentPage][idx]
        action = "cancel"

        if (game.status == 'published')
            Utilities.showDialog(game.name, action)
                .then (result) ->
                    if(result == "ok")
                        resource[action](idx)

    # ------------------------------ UTILITY METHODS - TIME COUNTER
    $scope.mtimeLeft = [];

    intervalID = window.setInterval( ->
        $scope.mtimeLeft[_i] = (
            Utilities.difference i.startTime, i.endTime, true
        ) for i in $scope.pagedItems[$scope.currentPage] if !_.isUndefined($scope.pagedItems[$scope.currentPage])
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
                if attr != "endTime" && attr != "image"
                    if attr == "startTime"
                        res = $filter('date')(game[attr], 'dd MMM yyyy HH:mm')
                    else
                        res = game[attr]

                    if searchMatch(res, $scope.query)
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
        if !end
            end = start
            start = 0
        for i in [start..end-1] by 1
            ret.push(i)
        ret
    
    $scope.prevPage = ->
        $scope.currentPage-- if $scope.currentPage > 0
            
    
    $scope.nextPage = ->
        $scope.currentPage++ if $scope.currentPage < $scope.pagedItems.length - 1
            
    
    $scope.setPage = ->
        $scope.currentPage = this.n
]