package csgomasters.steam2telegram.domain

import csgomasters.steam2telegram.infrastructure.ConfigLoader
import csgomasters.steam2telegram.infrastructure.Tools.cache
import csgomasters.steam2telegram.infrastructure.scala_logging.ScalaLoggingLogger
import csgomasters.steam2telegram.infrastructure.{HttpRequest, Tools}
import play.api.libs.json.{JsValue, Json}


object SteamApi {

  private val baseUrl = "http://api.steampowered.com"
  private val userApi = "ISteamUser"
  private val playerApi = "IPlayerService"
  private val apiKey = ConfigLoader.steam("apiKey")
  private val logger = new ScalaLoggingLogger()


  private def friendsList(steamId: String): JsValue = {
    val endpoint = s"$baseUrl/$userApi/GetFriendList/v0001/?key=$apiKey&steamid=$steamId&relationship=friend"
    HttpRequest.get(endpoint)
  }

  private def playersInfo(steamIds: Seq[String]): JsValue = {
    val stringIds = steamIds.mkString(",")
    val endpoint = s"$baseUrl/$userApi/GetPlayerSummaries/v0002/?key=$apiKey&steamids=$stringIds&format=json"
    HttpRequest.get(endpoint)
  }

  def recentlyPlayedGames(steamId: String): JsValue = {
    val endpoint = s"$baseUrl/$playerApi/GetRecentlyPlayedGames/v0001/?key=$apiKey&steamid=$steamId&format=json"
    HttpRequest.get(endpoint)
  }

  def getMyFriendsInfo: JsValue = {
    val mySteamId = ConfigLoader.steam("myId")

    if (cache.getIfPresent("friendsList").isEmpty) {
      logger.info("[SteamApi][getMyFriendsInfo] > NO cache available, calling SteamApi.friendsList")
      val friendsList: JsValue = SteamApi.friendsList(mySteamId)
      logger.info("[SteamApi][getMyFriendsInfo] > Setting friendsList in cache")
      cache.put("friendsList", Json.stringify(friendsList))
      cache.put("buildPlayers", "build")
    }
    val cachedFriendsList = Tools.getOptionValue(cache.getIfPresent("friendsList"))
    val friendsListResponse = Json.parse(cachedFriendsList)

    val friendsList: JsValue = (friendsListResponse \ "friendslist" \ "friends").get
    val steamFriendsIds: Seq[String] = (friendsList \\ "steamid").map(_.as[String])

    val playersIds: Seq[String] = steamFriendsIds :+ mySteamId

    val friendsInfoJson: JsValue = SteamApi.playersInfo(playersIds)

    (friendsInfoJson \ "response" \ "players").get
  }



}
