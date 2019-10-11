package controllers

import actors.WebsocketEventPublisher
import akka.actor.ActorSystem
import akka.stream.Materializer
import javax.inject.Inject
import models.GameEvent
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._
import play.api.libs.streams.ActorFlow

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * index template refers to example: https://github.com/webjars/webjars-play/tree/master/test-project
   */
class MainController @Inject()(indexTemplate: views.html.index)
                              (implicit system: ActorSystem, mat: Materializer, ws: WSClient)
  extends InjectedController {

  import config.Config.Game._

  implicit val gameEventFormat = Json.format[GameEvent]

  implicit val gameEventFrameFormatter = MessageFlowTransformer.jsonMessageFlowTransformer[String, GameEvent]

  def index = Action {
    Ok(indexTemplate())
  }

  def createGame() = Action.async { request =>
    ws.url(s"$apiUrl/game")
      .post("")
      .map {
        case res if res.status == CREATED => Created(res.body)
        case _ => InternalServerError
      }
      .recover { case _ => InternalServerError }
  }

  def startGame(gameId: String, playersCount: Int) = Action.async { request =>
    val players = 1 to playersCount map { n => s"Player$n"}
    postCommand(
      url = s"/game/$gameId/start",
      data = Json.obj("players" -> players))
  }

  def roll(gameId: String, playerId: String) = Action.async { request =>
    postCommand(
      url = s"/game/$gameId/roll/$playerId",
      data = Json.obj())
  }

  private def postCommand(url: String, data: JsValue) = {
    ws.url(s"$apiUrl$url")
      .post(data)
      .map {
        case res if res.status == ACCEPTED => Accepted
        case res if res.status == BAD_REQUEST => BadRequest(res.body)
        case res => InternalServerError
      }
      .recover { case _ => InternalServerError }
  }

  def gameEvents(gameId: String) = WebSocket.accept[String, GameEvent] { request =>
    ActorFlow.actorRef { out =>
      WebsocketEventPublisher.props(gameId, out)
    }
  }

}
