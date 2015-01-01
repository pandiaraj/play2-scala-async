package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.{WS, WSRequestHolder, WSResponse}
import play.api.mvc._
import play.api.Play.current

import scala.concurrent.Future

object Application extends Controller {

  def index = Action {
    Redirect(routes.Application.joke())
  }

  def joke = Action.async {
    val holder: WSRequestHolder = WS.url("http://api.icndb.com/jokes/random")
    val complexHolder: WSRequestHolder = holder.withHeaders("Accept" -> "application/json").withRequestTimeout(10000)
    val futureResponse: Future[WSResponse] = complexHolder.get()
    val joke: Future[String] = futureResponse.map(response => (response.json \ "value" \ "joke").as[String])
    joke.map(s => Ok(views.html.index(s)))
  }

}