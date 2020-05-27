import Actors.GibMainActor
import akka.actor.typed.ActorSystem
import Actors.GibMainActor.ProcessGibData

object Main extends App {
  val gibMain: ActorSystem[ProcessGibData] = ActorSystem(GibMainActor(), "MainActorStart")
  gibMain ! ProcessGibData()
}