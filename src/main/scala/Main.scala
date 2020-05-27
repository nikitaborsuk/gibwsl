import Actors.GibCallerActor.GibRequestSettings
import Actors.GibMainActor
import akka.actor.typed.ActorSystem
import Actors.GibMainActor.ProcessGibData
import akka.stream.StreamRefMessages.ActorRef


object Main extends App {
  val gibMain: ActorSystem[ProcessGibData] = ActorSystem(GibMainActor(), "MainActorStart")
//  while(true){
//    gibMain ! ProcessGibData()
//    Thread.sleep(30000)
//  }

}