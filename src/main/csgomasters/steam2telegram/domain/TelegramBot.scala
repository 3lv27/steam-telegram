package csgomasters.steam2telegram.domain

import csgomasters.steam2telegram.infrastructure.ConfigLoader
import csgomasters.steam2telegram.infrastructure.{HttpRequest, Tools}
import play.api.libs.json.JsValue

object TelegramBot {

  private val baseUrl = "https://api.telegram.org"
  private val botKey = ConfigLoader.telegram("botKey")
  private val chatId =  ConfigLoader.telegram("chatId")


  def sendMessage(message: String): JsValue = {
    val endpoint = s"$baseUrl/$botKey/sendMessage?chat_id=$chatId&text=$message"
    HttpRequest.get(endpoint)
  }

  def friendIsOnlineMessage(player: Player): Unit = {
    val gameName: String =
      //Just because XO4N's prefer this way...
      if (player.game.contains("Counter-Strike: Global Offensive")) "CS:GO" else Tools.getOptionValue(player.game)
    sendMessage("%E2%80%BC "+player.name + " is now playing  %F0%9F%8E%AE "+ gameName)
  }

  def friendIsOfflineMessage(player: Player): Unit = {
    sendMessage("%E2%80%BC "+player.name + " is Offline %F0%9F%92%80")
  }

}
