package com.knoldus.sprayservices

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import com.knoldus.sprayservices.JsonImplicits._
import akka.actor.Actor
import spray.http._
import spray.http.HttpResponse
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing._
import spray.routing.HttpService
import spray.json._

/**
 * @author knoldus
 */

trait RoutesHandler extends HttpService {

  import com.knoldus.sprayservices.JsonImplicits._

  val myRoute =
    path("index") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h1>Hello spray</h1>
              </body>
            </html>

          }
        }
      }
    } ~
      path("add") {
        get {
          parameter('num1.as[Int], 'num2.as[Int]) { (num1, num2) =>
            respondWithMediaType(`text/html`) { ctx =>
              val result = Service.add(num1, num2)
              result.onComplete {
                case Success(x)     => ctx.complete(HttpResponse(OK, x.toString()))

                case Failure(error) => ctx.complete(HttpResponse(StatusCodes.FailedDependency, error.toString()))
              }
            }
          }
        }
      } ~
      path("person") {
        get {
          respondWithMediaType(`text/html`) {
            complete(OK, "get request on person")
          }
        } ~
          post {
            entity(as[String]) { person =>
              respondWithMediaType(`text/html`) { ctx =>
                val result = Service.postService(person)
                result.onComplete {
                  case Success(x)     => ctx.complete(HttpResponse(OK, x.toString()))
                  case Failure(error) => ctx.complete(HttpResponse(StatusCodes.FailedDependency, error.toString()))
                }
              }
            }
          }
      }
}

class RoutesHandlerActor extends Actor with RoutesHandler {

  def actorRefFactory = context

  def receive = runRoute(myRoute)
}
  
