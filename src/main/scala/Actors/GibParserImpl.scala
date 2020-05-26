package Actors

import Parsers.Parser
import akka.actor.Actor

object GibParserImpl extends Actor {
  override def receive: Receive = ???

  override def parse[T](data: T): Unit = ???
}
