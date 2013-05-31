app.factory 'Games', ($resource) ->
    $resource '/my/games/json/:ctrl:gid',
        {
            gid: '@id',
            ctrl: '@ctrl'
        },
        {
            get: {method: 'GET', headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            query: {method: 'GET', params: {ctrl: "list"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}], isArray: true},
            save: {method: 'POST', headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            update: {method: 'PUT', headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            delete: {method: 'DELETE', headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            cancel: {method: 'POST', params: {ctrl: "cancel"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            publish: {method: 'POST', params: {ctrl: "publish"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]},
            checkName: {method: 'POST', params: {ctrl: "checkName"}, headers: [{'Content-Type': 'application/json'}, {'Accept'  : 'application/json'}]}
        }