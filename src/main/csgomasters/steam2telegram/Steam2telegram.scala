package csgomasters.steam2telegram

import play.api.libs.json._
import java.util.TimerTask
import java.util.Timer

import csgomasters.steam2telegram.infrastructure.Tools.{cache, mapResponse}
import csgomasters.steam2telegram.domain.{Player, SteamApi, SteamFriends, TelegramBot}
import csgomasters.steam2telegram.infrastructure.scala_logging.ScalaLoggingLogger
import csgomasters.steam2telegram.infrastructure.ConfigLoader


object Steam2telegram  {

  private var playersList:Seq[Player] = Seq[Player]()
  private val logger = new ScalaLoggingLogger()
  private val gameId = Some(ConfigLoader.steam("gameId"))

  private def notifyOnlinePlayers(player: Player): Unit = {
    logger.info(s"[Steam2telegram][notifyOnlinePlayers] > $player")
    if (player.gameId == gameId && !player.currentlyOnline) {
      logger.info(s"[Steam2telegram][notifyOnlinePlayers][ONLINE] > $player")
      player.currentlyOnline = true
      TelegramBot.friendIsOnlineMessage(player)
    }
    if (player.gameId.isEmpty && player.currentlyOnline) {
      logger.info(s"[Steam2telegram][notifyOnlinePlayers][OFFLINE] > $player")
      player.currentlyOnline = false
      TelegramBot.friendIsOfflineMessage(player)
    }
  }


  def updatePlayersInfo(friendsInfo: JsValue):Unit = {
    val list = mapResponse(friendsInfo)

    for {
      playerCurrentStatus <- list
      player <- Steam2telegram.playersList
      if (player.steamId == playerCurrentStatus.steamId)
    } yield {
      player.game = playerCurrentStatus.game
      player.gameId = playerCurrentStatus.gameId
    }

  }

  def checkForOnlinePlayersTask(): Unit = {
    logger.info("[Steam2telegram][checkForOnlinePlayersTask] > Calling task")
    val friendsInfo: JsValue = SteamApi.getMyFriendsInfo

    if (cache.getIfPresent("buildPlayers").isDefined) {
      val players = new SteamFriends(friendsInfo)
      Steam2telegram.playersList = players.get
    }
    updatePlayersInfo(friendsInfo)
    Steam2telegram.playersList.foreach(notifyOnlinePlayers)
  }


  def main(args: Array[String]): Unit = {

    val timer = new Timer()
    val task = new TimerTask() {
      override def run(): Unit = {
        logger.info("[Steam2telegram][main] > Executing scheduled task")
        checkForOnlinePlayersTask()
      }
    }

    val min = 1
    val delayMs = 500
    val periodMin = min * 1000 * 60
    timer.scheduleAtFixedRate(task, delayMs, periodMin)

  }


}
