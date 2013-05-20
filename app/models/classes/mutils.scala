package models

import com.github.nscala_time.time.Imports._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object mutils extends mutils {
  case class GamesDetails(id: Option[Int], name: String, version: Int = 1, description: String, location: String, operatorId: Int, 
    created: DateTime = DateTime.now, startTime: DateTime = DateTime.now, endTime: DateTime, started: DateTime = DateTime.now, 
    ended: DateTime, winning: String = "max_points", nWins: Int = 1, difficulty: String = "easy", maxPlayers: Int = 1000000, 
    awards: String, status: String = "project", image: String="games/gameicon.png")

  case class GamesList(id: Option[Int], name: String, version: Int, location: String, startTime: DateTime, endTime: DateTime, status: String, image: String)

  case class TasksList(id: Option[Int], gameId: Int, version: Int, name: String, description: String, deadline: DateTime, 
    maxpoints: Int, maxattempts: Int)

  case class OperatorsData(id: Option[Int], login: String, pass_hash: String)


  implicit val gamesListWrites = Json.writes[GamesList]
  implicit val tasksListReads = Json.reads[TasksList]
  implicit val gamesDetailsReads = Json.reads[GamesDetails]
  implicit val operatorsDataReads = Json.reads[OperatorsData]
  implicit val operatorsDataWrites = Json.writes[OperatorsData]

}

trait mutils {

}