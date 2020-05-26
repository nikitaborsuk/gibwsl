package Actors

import java.util.Date

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors


object GibCallerActor  {

  case class GibRequestSettings(url: String, key: String, fromDate: Option[Date], login: String, csid: Option[String], limit: Int)

  def apply(): Behavior[GibRequestSettings] = Behaviors.receive { (context, message) =>

  }

}

