package Actors

import akka.actor.Actor
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object GibParserActor {
  def apply(): Behavior[Actors.GibCallerActor.GibResponseData] = Behaviors.receive { (context, message) =>
    if(message.)
    Behaviors.stopped
  }
}
