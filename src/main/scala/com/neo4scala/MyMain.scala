package com.neo4scala

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.neo4scala.model.Common.{Image, Location}
import com.neo4scala.model._
import com.neo4scala.repository.{ShopRepositoryImpl, Util}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn
import gremlin.scala._

import collection.JavaConverters._


object MainOne extends App {

  implicit val actorSystem: ActorSystem = ActorSystem("mikesys")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

 // var newProd = PlainProduct("one",Util.createUUID,None,23.0,Enabled,Some(12),Available,None,None)
  //var theLocation = Util.TheGraph.+(Location(12L,13L)).toCC[Location]
 // var newShop = Shop("wakanda",ShopUUID(Util.createUUID),Location(144L,169L),Image("theImage"))
  var newShop = ShopRepositoryImpl.findByName("updated","regular_shop").get.asInstanceOf[Shop]
  var updatedShop = newShop.copy(name="black panther")
  //  var savedNewGuy = UserRepositoryImpl.add(newGuy)
//  val newGuy = UserRepositoryImpl.findByPhone(8453499,"customer").get.asInstanceOf[Customer]
println(ShopRepositoryImpl.update(updatedShop))
//  println(UserRepositoryImpl.removeSpecific(8453499L))
  //UserRepositoryImpl.removeSpecific

  implicit def marshaller = new Marshallable[Location] {
    def fromCC(cc: Location) =
      FromCC(None,
        "Location",
        Map("latitude" -> cc.latitude, "longitude" -> cc.longitude))

    def toCC(id: AnyRef, valueMap: Map[String, Any]): Location =
      Location(latitude = valueMap("latitude").asInstanceOf[Long],
        longitude = valueMap("longitude").asInstanceOf[Long])
  }

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
