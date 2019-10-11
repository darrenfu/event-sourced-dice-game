package actors

import akka.actor._
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.stream.actor.ActorSubscriber
import akka.stream.scaladsl.{Sink, Source}
import config.Config
import io.scalac.amqp.{Connection, Message, Queue}
import models.GameEvent

object WebsocketEventPublisher {
  def props(gameId: String, out: ActorRef) =
    Props(new WebsocketEventPublisher(gameId, out))
}

class WebsocketEventPublisher(gameId: String, out: ActorRef)
  extends Actor
  with ActorLogging {

  // To replace ImplicitFlowMaterializer
  final implicit val materializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  lazy val connection = Connection()

  import context.dispatcher

  override def preStart() = {
    import Config.Events._

    val queue = Queue(name = gameId, durable = false, autoDelete = true)

    val bindFuture = for {
      _ <- connection.queueDeclare(queue)
      _ <- connection.queueBind(queue.name, exchangeName, "", Map("gameId" -> gameId))
    } yield ()

    bindFuture.map { _ =>
      val actor = context.system.actorOf(EventSubscriber.props(self))
      val sink = Sink.fromSubscriber[Message](ActorSubscriber[Message](actor))
      Source.fromPublisher(connection.consume(queue.name))
        .map(_.message)
        .to(sink)
        .run()
    }.failed.map { ex =>
      log.error(ex, "Cannot bind queue to events from game {}", gameId)
      context stop self
    }
  }

  override def receive = {
    case ev: GameEvent if ev.gameId == gameId =>
      out ! ev
  }

}
