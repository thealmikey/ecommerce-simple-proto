package com.neo4scala

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.neo4scala.model.{Customer, User}
import com.neo4scala.service.UserServiceImplementation
import gremlin.scala._
import org.apache.commons.configuration.BaseConfiguration
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn
import scala.util.{Success, Try}
import scala.collection.JavaConverters._
object MainOne extends App {

  implicit val actorSystem: ActorSystem = ActorSystem("mikesys")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

  import org.apache.tinkerpop.gremlin.orientdb.OrientGraph

  var config = new BaseConfiguration()
  config.setProperty("orient-url","remote:localhost/Mike")
  config.setProperty("orient-user","root")
  config.setProperty("orient-pass","root")

  val Price = Key[Double]("price")
  val ProductName = Key[String]("product_name")
  val ProductId = Key[Int]("product_id")

  var txGraph = OrientGraph.open(config).asScala()

//  Future{
//    txGraph + ("Product",Price->100, ProductName->"Ovacado",ProductId ->29019)
//  }.onComplete(_=>println("The holy fruit has been saved"))
//
//  txGraph + ("Product",Price->100, ProductName->"KDF",ProductId ->99921)

  val customer = Customer("Michael","Gikaru",56,8358392458L)
  val v = txGraph + customer
  v.toCC[Customer]
  println(txGraph.V.hasLabel[Customer].toList())

  val publicRoutes = pathPrefix("customers") {
    pathEnd {
      complete("customers port")
    } ~ path(IntNumber) { int =>
      complete(if (int % 5 == 0) "even ball" else "odd ball")
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
//  driver.close()

}
