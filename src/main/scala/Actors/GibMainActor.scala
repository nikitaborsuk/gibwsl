package Actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object GibMainActor {

  final case class ProcessGibData()

  def apply(): Behavior[ProcessGibData] = Behaviors.setup { context =>
    val caller = context.spawn(GibCallerActor(), "Caller")

    Behaviors.receiveMessage { message =>

    }
  }

}
