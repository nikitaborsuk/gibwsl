package Actors

import java.net.URLEncoder

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.Materializer
import akka.util.ByteString

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


object GibCallerActor {

  case class GibRequestSettings(url: String, args: Map[String, String])

  def apply(): Behavior[GibRequestSettings] = Behaviors.receive { (context, message) =>

    implicit val materializer: Materializer = Materializer(context.system)

    val req = Http(context.system)
    val query = message.url + "?" + message.args.foldLeft("")((s, data) =>
      s + raw"${URLEncoder.encode(data._1, "UTF-8")}=${URLEncoder.encode(data._2, "UTF-8")}&").stripSuffix("&")
    context.log.debug(query)
    req.singleRequest(HttpRequest(uri = query)).onComplete {
      case Success(req) => req match {
        case HttpResponse(StatusCodes.OK, headers, entity, _) =>
          entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
            context.log.info("Got response, body: " + body.utf8String)
          }
        case resp@HttpResponse(code, _, _, _) =>
          context.log.info("Request failed, response code: " + code)
          resp.discardEntityBytes()
      }
      case Failure(exception) => context.log.debug(s"Error getting data $exception")
    }

      Behaviors.same
    }
  }


