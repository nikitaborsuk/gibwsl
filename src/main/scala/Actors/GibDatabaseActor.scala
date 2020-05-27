package Actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

case class GetDatabaseData()

object GibDatabaseActor {
  def apply(): Behavior[GetDatabaseData] = Behaviors.receive((context, message) => {

  }

  )
}
