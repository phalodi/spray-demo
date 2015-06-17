package com.knoldus.sprayservices

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import spray.json._
import DefaultJsonProtocol._

/**
 * @author knoldus
 */

case class Person(name: String)

object JsonImplicits extends DefaultJsonProtocol {
  implicit val personFormats = jsonFormat1(Person)
}

object Service {
  def add(num1: Int, num2: Int): Future[Int] = {
    Future(num1 + num2)
  }

  def postService(person: String) = {
    Future(person)
  }
}