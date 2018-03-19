package com.neo4scala.model

import java.util.UUID

import Common._
import com.wix.accord._
import com.wix.accord.dsl._
import gremlin.scala.label
import gremlin.scala._

case class ShopUUID(value: UUID) extends AnyVal

@label("shop")
trait ShopTrait {
  def name: String
  def shopId: ShopUUID
  def location: Location
  def logo: Image
 // def productCatalog: List[Product]
}

@label("regular_shop")
case class Shop(name: String,
                      shopId: ShopUUID,
                      location: Location,
                      logo: Image)
    extends ShopTrait

@label("butchery")
case class Butchery(name: String,
                    shopId: ShopUUID,
                    location: Location,
                    logo: Image,
                    productCatalog: List[Product])
    extends ShopTrait

@label("hardware")
case class Hardware(name: String,
                    shopId: ShopUUID,
                    location: Location,
                    logo: Image,
                    productCatalog: List[Product])
    extends ShopTrait

@label("greengrocer")
case class Greengrocer(name: String,
                    shopId: ShopUUID,
                    location: Location,
                    logo: Image,
                    productCatalog: List[Product])
  extends ShopTrait

object ShopTrait {

  implicit val shopValidator = validator[ShopTrait] { shop =>
    shop.name is notEmpty
    shop.shopId.value.isInstanceOf[UUID]
  }
}
