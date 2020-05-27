package Actors

import akka.actor.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import pureconfig._
import pureconfig.generic.auto._

import scala.collection.immutable.HashMap

object GibMainActor {

  final case class ProcessGibData()

  final case class GibServiceConf(url: String, args: Map[String, String])

  def apply(): Behavior[ProcessGibData] = Behaviors.setup { context =>
    val caller = context.spawn(GibCallerActor(), "Caller")

    Behaviors.receiveMessage { message =>
      val configResult = ConfigSource.default.load[GibServiceConf]

      configResult match {
        case Right(conf) =>
          caller ! GibCallerActor.GibRequestSettings(conf.url, conf.args)
          Behaviors.same
        case Left(err) =>
          context.log.error(err.prettyPrint())
          Behaviors.stopped
      }
    }
  }
}

