package Actors

import java.net.URLEncoder

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.Materializer
import akka.util.ByteString

import scala.concurrent._
import io.circe.syntax._
import io.circe._
import io.circe.parser._
import io.circe.generic.auto._

import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


object GibCallerActor {

  case class GibRequestSettings(url: String, args: Map[String, String])

  case class GibResponseDevice(platform: String, browser: String, canvas_fingerprint: String, webgl_fingerprint: String)

  case class GibResponseGeo(country: String, city: String)

  case class GibResponseScreen(width: Int, height: Int, color_depth: Int)

  case class GibResponseData(event_id: String,
                             type_id: Int,
                             type_title: String,
                             risk_level: String,
                             event_score: Int,
                             device_score: Int,
                             created: String,
                             ip: String,
                             geo: GibResponseGeo,
                             isp: String,
                             session_id: String,
                             agent_id: String,
                             channel: String,
                             login: Option[String],
                             login_extra: Option[String],
                             user_agent: String,
                             referer: String,
                             csid: Option[String],
                             screen: GibResponseScreen,
                             device: GibResponseDevice)

  case class GibResponse(status: String, error: Option[String], data: Option[List[GibResponseData]])


  val SUCCESS = "success";

  //    val INVALIDARG = "invalid_arg";
  //    val MISSINGARG = "missing_arg";
  //    val UNKNOWN = "unknown";
  //    val PERMISSION_DENIED = "permission_denied";
  //    val UNAUTHORIZED = "unauthorized";


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
            val response = decode[GibResponse](body.utf8String)
            response match {
              case Right(data) =>
                data.status match {
                  case SUCCESS =>
                    data.data.foreach(_.foreach(res => {
                      val caller = context.spawn(GibParserActor(), res.event_id)
                      caller ! res
                    }))
                  case _ => context.log.error(data.error.getOrElse("Undefined error from"))
                }
              case Left(err) => context.log.error(err.getMessage)
            }
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


