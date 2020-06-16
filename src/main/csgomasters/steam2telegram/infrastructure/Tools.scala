package csgomasters.steam2telegram.infrastructure

import com.github.blemale.scaffeine.{Cache, Scaffeine}
import play.api.libs.json.{JsPath, JsValue, Reads}
import play.api.libs.functional.syntax._
import scala.concurrent.duration._

import csgomasters.steam2telegram.domain.Player

object Tools {

  val cache: Cache[String, String] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(24.hour)
      .maximumSize(500)
      .build[String, String]()

  def getOptionValue(option: Option[String]): String = {
    option match {
      case Some(value) => value
    }
  }

  def mapResponse(friendsList: JsValue): Seq[Player] = {
    implicit val playersReads: Reads[Player] = (
      (JsPath \ "steamid").read[String] and
        (JsPath \ "personaname").read[String] and
        (JsPath \ "gameextrainfo").readNullable[String] and
        (JsPath \ "gameid").readNullable[String]
      )(Player.apply _)
    friendsList.as[Seq[Player]]
  }

}
