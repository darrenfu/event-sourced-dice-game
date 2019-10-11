# Event sourced dice game
A simple dice game implemented using CQRS and Event-Sourcing.  

| Tech stack | Frontend or Backend | Adopted Version | Latest version | Comments |
| ---------- | ------------------- | --------------- | -------------- | -------- |
| Akka       | Backend             | 2.4             | 2.5            | Breaking Change in 2.5: `ActorSubscriber/ActorPublisher` were deprecated. Use `GraphStage` instead |
| Play       | Backend             | 2.5             | 2.7            | Play 2.6+ depends on Akka 2.5+ |
| Spray      | Backend             | 1.3             | 10.1           | Replaced by Akka HTTP |
| sbt        | Backend             | 0.13            | 1.x            | sbt 1.x only supports Play [sbt-plugin](https://github.com/sbt/sbt/wiki/sbt-1.x-plugin-migration#completed) 2.6.6+ |
| Scala      | Backend             | 2.11            | 2.13           |          |
| WebJars    | Frontend            |                 |                |          |
| AngularJS  | Frontend            | 1.3             | 8.2            |          |
| Bootstrap  | Frontend            | 3.3             | 4.3            |          |
| JQuery     | Frontend            | 2.1             | 3.4            |          |

## Architecture overview
![Architecture overview](https://raw.githubusercontent.com/LukasGasior1/event-sourced-dice-game/master/doc/diagram.png)

## Running

1. Run RabbitMQ (Docker is enough for quick start): `docker run -d -p 5672:5672 -p 15672:15672 rabbitmq`
2. Run game server: `sbt "project game" run`
3. Run statistics app (optional): `sbt "project statistics" run`
4. Run web application: `sbt "project webapp" run`

Once webapp is running, navigate to `http://127.0.0.1:9000/` to play.

If statistics app is running, navigate to `http://127.0.0.1:8083/stats` to get dice rolls stats.

Reference: [ScalaC Team Blog](https://scalac.io/event-sourced-game-implementation-example-part-1-3-getting-started/).
