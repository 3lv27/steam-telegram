package csgomasters.steam2telegram.domain

import csgomasters.steam2telegram.infrastructure.Tools.{cache, mapResponse}
import csgomasters.steam2telegram.infrastructure.scala_logging.ScalaLoggingLogger
import play.api.libs.json.JsValue


class SteamFriends(friendsList: JsValue) {
  var list: Seq[Player] = Seq[Player]()
  private val logger = new ScalaLoggingLogger

  private def buildPlayers(friendsList: JsValue):Unit = {
    cache.invalidate("buildPlayers")
    logger.info(" [SteamFriends][buildPlayers] > Mapping players")
    list = mapResponse(friendsList)
  }

  def get :Seq[Player] = {
    if (list.isEmpty) buildPlayers(friendsList)
    list
  }

}




