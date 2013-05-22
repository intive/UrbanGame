/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 * 		 http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		 http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import com.github.nscala_time.time.Imports._
import models.mutils._

class UnitTestSpec extends Specification {
	"Model utils" should {
		"parse time string given in 'hh:mm' format" in {
			val time: String = "12:30"
			val actual = parseTime(time)
			val expected = List(12,30,0,0)
			actual should equalTo(expected)
		}

		"parse time string given in 'hh:mm:ss' format" in {
			val time: String = "12:30:05"
			val actual = parseTime(time)
			val expected = List(12,30,5,0)
			actual should equalTo(expected)
		}

		"parse time string given in 'hh:mm:ss.nn' format" in {
			val time: String = "12:30:10.20"
			val actual = parseTime(time)
			val expected = List(12,30,10,20)
			actual should equalTo(expected)
		}

		"join date and time string into a date" in {
			val time = "10:20"
			val date = "2013-05-20"
			val actual = combineDate(date, time)
			actual should equalTo(new DateTime("2013-05-20T10:20:00.00"))
		}
	}
}