package com.neo4scala

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.neo4scala.model.Common.{Image, Location}
import com.neo4scala.model.{Shop, ShopUUID}
import com.neo4scala.repository.{ShopRepositoryImpl, Util}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

object MainOne extends App {

  implicit val actorSystem: ActorSystem = ActorSystem("mikesys")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

  var newShop = Shop("tinie tech shop",
                     ShopUUID(Util.createUUID),
                     Location(1000, 1000),
                     Image("Love doctor"),
    shopType = "power tech shop"
  )
  println(ShopRepositoryImpl.add(newShop))

  val publicRoutes = pathPrefix("customers") {
    pathEnd {
      complete("customers port")
    } ~ path(IntNumber) { myInt =>
      complete(s"nop $myInt")
    } ~ path(Segment) { boo =>
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
  } ~ path("testing") {
    complete("testing space")
  }

  val bindFuture = Http().bindAndHandle(publicRoutes, "localhost", 8989)

  StdIn.readLine()

  bindFuture.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
}
