package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import com.github.nscala_time.time.Imports._

object mutils extends mutils {
  case class GamesDetails(id: Option[Int], name: String, version: Int = 1, description: String, location: String, lat: Float = 0, lon: Float = 0, 
    operatorId: Int, created: DateTime = DateTime.now, startTime: DateTime = DateTime.now, endTime: DateTime, started: Option[DateTime] = None, 
    ended: Option[DateTime] = None, winning: String = "max_points", nWins: Int = 1, difficulty: String = "easy", maxPlayers: Int = 1000000, awards: String, 
    status: String = "project", image: String="games/gameicon.png")

  case class GamesList(id: Option[Int], name: String, version: Int, location: String, startTime: DateTime, endTime: DateTime, status: String, 
    image: String)

  case class TasksList(id: Option[Int], gameId: Int, version: Int, name: String, description: String, deadline: DateTime, maxpoints: Int, 
    maxattempts: Int)

  case class OperatorsData(id: Option[Int], login: String, pass_hash: String)

  case class SmallGame(name: String, description: String, location: String, startTime: String, startDate: String, endTime: String, endDate: String, 
    winning: String, winningNum: Int, diff: String, playersNum: Int, awards: String)


  implicit val gamesListWrites = Json.writes[GamesList]
  implicit val tasksListReads = Json.reads[TasksList]
  implicit val gamesDetailsReads = Json.reads[GamesDetails]

  implicit val gamesReads: Reads[SmallGame] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "description").read[String] and
    (JsPath \ "location").read[String] and
    (JsPath \ "startTime").read[String] and
    (JsPath \ "startDate").read[String] and
    (JsPath \ "endTime").read[String] and
    (JsPath \ "endDate").read[String] and
    (JsPath \ "winning").read[String] and
    (JsPath \ "winningNum").read[Int] and
    (JsPath \ "diff").read[String] and
    (JsPath \ "playersNum").read[Int] and
    (JsPath \ "awards").read[String]
  )(SmallGame)

  implicit val operatorsDataReads = Json.reads[OperatorsData]
  implicit val operatorsDataWrites = Json.writes[OperatorsData]

}

trait mutils {
  def combineDate(date: String, time: String): DateTime = {
    val timeList: List[Int] = parseTime(time)
    new DateTime(date).withTime(timeList(0), timeList(1), timeList(2), timeList(3))
  }

  def parseTime(time: String): List[Int] = {
    val timeparsed = time.length match {
      case 5 => val a = time.split(":").map(_.toInt).toList
                a :+ 0 :+ 0
      case 8 => val a = time.split(":").map(_.toInt).toList
                a :+ 0
      case 11 => val a = time.substring(0,8).split(":").map(_.toInt).toList
                a :+ time.substring(9,11).toInt
    }
    timeparsed
  }
}