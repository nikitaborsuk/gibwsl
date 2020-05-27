package Actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.immutable.HashMap


object GibMainActor {

  final case class ProcessGibData()

  def apply(): Behavior[ProcessGibData] = Behaviors.setup { context =>
    val caller = context.spawn(GibCallerActor(), "Caller")

    Behaviors.receiveMessage { message =>
      val args = HashMap(
        "key" -> "asdasdasdasdasdsada",
        "login" -> "asdasdasd")

      caller ! GibCallerActor.GibRequestSettings("https://b17b5988-604c-4a2f-9877-ce69a8688fe4.mock.pstmn.io/test", args)
      Behaviors.same
    }
  }

}
