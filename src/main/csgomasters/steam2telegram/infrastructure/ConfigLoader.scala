package csgomasters.steam2telegram.infrastructure

import com.typesafe.config.{Config, ConfigFactory}

object ConfigLoader {

  private val steamConfig: Config = ConfigFactory.load("steam").getConfig("creds")
  private val telegramConfig: Config = ConfigFactory.load("telegram").getConfig("creds")

  val steam = Map("apiKey" ->  steamConfig.getString("apiKey"), "myId" ->  steamConfig.getString("myId"))
  val telegram =  Map("botKey" ->  telegramConfig.getString("botKey"), "chatId" -> telegramConfig.getString("chatId.prod"))

}
