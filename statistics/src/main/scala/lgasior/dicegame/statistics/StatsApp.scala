package lgasior.dicegame.statistics

import akka.actor.ActorSystem
import akka.io.IO
import akka.stream.ActorMaterializer
import akka.stream.actor.ActorSubscriber
import akka.stream.scaladsl.{Sink, Source}
import io.scalac.amqp.{Connection, Message, Queue}
import lgasior.dicegame.statistics.actor.{StatsActor, SubscriberActor}
import lgasior.dicegame.statistics.api.StatsApiServiceActor
import lgasior.dicegame.statistics.config.Config
import org.slf4j.LoggerFactory
import spray.can.Http

import scala.util.{Failure, Success}

object StatsApp {

  implicit val system = ActorSystem("StatisticsSystem")
  import system.dispatcher

  val log = LoggerFactory.getLogger(StatsApp.getClass)

  val statsActor = system.actorOf(StatsActor.props, "stats")

  def main(args: Array[String]) = {
    setupRestApi()
    setupEventStreamConsumption()
  }

  private def setupRestApi() = {
    import Config.Api._
    val statsApiServiceActor = system.actorOf(StatsApiServiceActor.props(statsActor), "stats-api-service")
    IO(Http).tell(Http.Bind(statsApiServiceActor, bindHost, bindPort), statsApiServiceActor)
  }

  private def setupEventStreamConsumption() = {
    import Config.Events._

    implicit val connection = Connection()
    val queue = Queue(queueName, durable = false, autoDelete = true)
    val bindArgs = Map("x-match" -> "all", "type" -> "DiceRolled")

    val resultFuture = for {
      _ <- connection.queueDeclare(queue)
      _ <- connection.queueBind(queueName, exchangeName, "", bindArgs)
    } yield ()

    resultFuture onComplete {
      case Success(_) =>
        val subscriber = ActorSubscriber[Message](system.actorOf(SubscriberActor.props(statsActor)))
        Source.fromPublisher(connection.consume(queueName))
          .map(_.message)
          .to(Sink.fromSubscriber[Message](subscriber))
          .run()(ActorMaterializer())
      case Failure(ex) =>
        log.error("Cannot setup queue", ex)
        sys.exit(1)
    }

  }

}
