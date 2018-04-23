package com.neo4scala

import java.io.File
import java.util.Date

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.neo4scala.model.Common.{Image, Location}
import com.neo4scala.model.{Shop, ShopUUID}
import com.neo4scala.repository.{ShopRepositoryImpl, Util}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn
import io.circe.syntax._

object MainOne extends App with FailFastCirceSupport {

  implicit val actorSystem: ActorSystem = ActorSystem("mikesys")
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

  var newShop = Shop("tinie tech shop",
                     "stringID",
                     Location("1000","1000"),
                     Image("Gravity Matters"),
    "Butchery",
    Some("fekki"),Some("lekki"))

  val myInput = """{ "name":"tinie tech shop","shopId":"stringID","location":"1000","logo":"Gravity Matters","shopType":"Butchey","openTime" :"1524504630560","closeTime":"1524504630568"}"""

//  )
//  println(ShopRepositoryImpl.add(newShop))
import com.neo4scala.model.ShopCodec._
  import io.circe.parser.decode
  println(encodeShop(newShop))
  println(decode[Shop](myInput))
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
  } ~ pathPrefix("shop") {
   pathEnd{
     get {
       complete(
     ShopRepositoryImpl.fetchAll().get.asJson
    )
   } ~ post{
     entity(as[Shop]){
       shop =>
         complete{
           ShopRepositoryImpl.add(shop)
         "i put up a shop for u"
         }
     }
   }
  }
  }

  val bindFuture = Http().bindAndHandle(publicRoutes, "localhost", 8989)

  StdIn.readLine()

  bindFuture.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
}
