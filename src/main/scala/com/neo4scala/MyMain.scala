package com.neo4scala

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.neo4scala.model.Customer
import com.neo4scala.repository.{UserRepositoryImpl, Util}
import com.neo4scala.service.UserServiceImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn
import gremlin.scala._

object MainOne extends App {

  implicit val actorSystem: ActorSystem = ActorSystem("mikesys")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()


// var newGuy = UserServiceImpl.createNewCustomer("Wonderwoman","Gal",44,8453499L,Util.createUUID)
//  var savedNewGuy = UserRepositoryImpl.add(newGuy)
//  val newGuy = UserRepositoryImpl.findByPhone(8453499,"customer").get.asInstanceOf[Customer]

  println(UserRepositoryImpl.removeSpecific(8453499L))
  //UserRepositoryImpl.removeSpecific

  val publicRoutes = pathPrefix("customers") {
    pathEnd {
      complete("customers port")
    } ~ path(IntNumber) { int =>
      complete("nop")
    } ~ path(Segment){ boo=>
      complete(s"i see you $boo")
    }
  } ~ path("users") {
    get {
      complete("users page")
    }
  } ~ path("products") {
    get {
      complete("products page")
    }
  } ~ path("testing"){
    complete("testing space")
  }

  val bindFuture = Http().bindAndHandle(publicRoutes, "localhost", 8989)

  StdIn.readLine()

  bindFuture.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
}
