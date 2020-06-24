package csgomasters.steam2telegram.infrastructure

import com.typesafe.config.{Config, ConfigFactory}

object ConfigLoader {

  private val steamConfig: Config = ConfigFactory.load("steam").getConfig("config")
  private val telegramConfig: Config = ConfigFactory.load("telegram").getConfig("config")
  private val localEnvironment: Boolean = Option(System.getenv("APP_ENV")).getOrElse("").nonEmpty
  private val chatId: String = if (localEnvironment) "dev" else "prod"


  val steam = Map(
    "apiKey" ->  steamConfig.getString("apiKey"),
    "myId" -> steamConfig.getString("myId"),
    "gameId" ->  steamConfig.getString("gameId")
  )
  val telegram =  Map(
    "botKey" ->  telegramConfig.getString("botKey"),
    "chatId" -> telegramConfig.getString(s"chatId.$chatId")
  )

}
