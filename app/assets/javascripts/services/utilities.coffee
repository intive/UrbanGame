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

# '------------------------------------------ UTILITY FUNCTIONS
app.factory 'Utilities', ['$dialog', ($dialog) ->
    {
        difference: (sdate, edate, now) ->
            zeros = (x) -> 
                ( if x < 10 then "0" else "") + x

            if now
                dateStart = new Date(sdate)
                dateEnd = new Date(edate)
                now = new Date()
                diff = if now > dateStart then (dateEnd - now) else (dateStart - now)
            else
                diff = edate - sdate

            days = parseInt(diff / (1000*60*60*24))
            hours = parseInt((diff % (1000*60*60*24)) / (1000*60*60))
            minutes = parseInt(((diff % (1000*60*60*24)) % (1000*60*60)) / (1000*60))
            seconds = parseInt((((diff % (1000*60*60*24)) % (1000*60*60)) % (1000*60)) / 1000)

            zeros(days) + "d:" + zeros(hours) + "h:" + zeros(minutes) + "min:" + zeros(seconds) + "sec"

        showDialog: (title, action) ->
            msg = Messages("js.errors.sure", action)
            btns = [{result:'no', label: Messages("js.no")}, {result:'ok', label: Messages("js.yes", action), cssClass: 'btn-primary'}]

            $dialog.messageBox(title, msg, btns)
                .open()
    }
]