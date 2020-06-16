package csgomasters.steam2telegram.infrastructure

import play.api.libs.json.{JsValue, Json}

object HttpRequest {


  def get(url: String): JsValue = {
    val response = scala.io.Source.fromURL(url)
    val stringResponse = try response.mkString finally response.close()
    Json.parse(stringResponse)
  }


}
