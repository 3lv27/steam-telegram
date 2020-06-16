package csgomasters.steam2telegram.domain

case class Player( steamId: String, name: String, var game: Option[String], var gameId: Option[String]) {
  var currentlyOnline = false
}
