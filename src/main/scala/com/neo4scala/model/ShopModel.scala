package com.neo4scala.model

import java.util.{Date, UUID}

import Common._
import com.wix.accord._
import com.wix.accord.dsl._
import gremlin.scala.label
import gremlin.scala._
import io.circe.{HCursor, Json}
import io.circe.generic.JsonCodec
import io.circe.syntax._

case class ShopUUID(value: UUID) extends AnyVal

trait ShopTrait {
  def name: String
  def shopId: String
  def location: Location
  def logo: Image
  def shopType: String
  def openTime:Option[String]
  def closeTime:Option[String]
  // def productCatalog: List[Product]
}

import io.circe.{Decoder, Encoder}
import ShopCodec._
object ShopCodec {
  implicit val encodeShop: Encoder[Shop] = new Encoder[Shop]{
    def apply(a: Shop): Json = Json.obj(
      ("name", Json.fromString(a.name)),
      ("shopId", Json.fromString(a.shopId)),
      ("location",
        Json.fromString(a.location.latitude.toString ++ a.location.toString)),
      ("imageUrl", Json.fromString(a.logo.imageUrl)),
      ("shopType", Json.fromString(a.shopType)),
      ("openTime", Json.fromString(a.openTime.get)),
      ("closeTime", Json.fromString(a.closeTime.get))
    )
  }

  implicit val decodeShop: Decoder[Shop] = new Decoder[Shop]{
    def apply(c: HCursor): Decoder.Result[Shop] =
      for {
        name <- c.downField("name").as[String]
        shopId <- c.downField("shopId").as[String]
        location <- c.downField("location").as[String]
        imageUrl <- c.downField("logo").as[String]
        shopType <- c.downField("shopType").as[String]
        openTime <- c.downField("openTime").as[String]
        closeTime <- c.downField("closeTime").as[String]
      } yield {
        new Shop(name,
          shopId,
          Location(location, location),
          Image(imageUrl),
          shopType,
          Some(openTime),
          Some(closeTime))
      }
  }
}

@label("shop")
case class Shop(name: String,
                shopId: String,
                location: Location,
                logo: Image,
                shopType: String,
                openTime:Option[String],
                closeTime:Option[String]
               )
    extends ShopTrait

object ShopTrait {

  implicit val shopValidator = validator[ShopTrait] { shop =>
    shop.name is notEmpty
    //shop.shopId.value.isInstanceOf[UUID]
  }
}
