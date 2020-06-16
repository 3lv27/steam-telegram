package csgomasters.steam2telegram

import play.api.libs.json._
import java.util.TimerTask
import java.util.Timer

import csgomasters.steam2telegram.infrastructure.Tools.{cache, mapResponse}
import csgomasters.steam2telegram.domain.{Player, SteamApi, SteamFriends, TelegramBot}
import csgomasters.steam2telegram.infrastructure.scala_logging.ScalaLoggingLogger


object Steam2telegram  {

  private var playersList:Seq[Player] = Seq[Player]()
  private val logger = new ScalaLoggingLogger()

  private def notifyOnlinePlayers(player: Player): Unit = {
    val csgoId = Some("730")
    logger.info(s"[Steam2telegram][notifyOnlinePlayers] > $player")
    if (player.gameId == csgoId && !player.currentlyOnline) {
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

    Steam2telegram.playersList.foreach(player => {
      list.foreach(toUpdate => {
        if (player.steamId == toUpdate.steamId) {
          player.game = toUpdate.game
          player.gameId = toUpdate.gameId
        }
      })
    })

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