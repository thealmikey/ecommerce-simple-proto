package com.neo4scala.model

import java.util.UUID

import Common._
import com.wix.accord._
import com.wix.accord.dsl._
import gremlin.scala.label
import gremlin.scala._

case class ShopUUID(value: UUID) extends AnyVal

sealed trait ShopType
case object Butchery extends ShopType
case object Hardware extends ShopType
case object Greengrocer extends ShopType

@label("shop")
trait ShopTrait {
  def name: String
  def shopId: ShopUUID
  def location: Location
  def logo: Image
  def shopType: ShopType
  // def productCatalog: List[Product]
}

@label("shop")
case class Shop(name: String,
                shopId: ShopUUID,
                location: Location,
                logo: Image,
                shopType: ShopType)
    extends ShopTrait

object ShopTrait {

  implicit val shopValidator = validator[ShopTrait] { shop =>
    shop.name is notEmpty
    shop.shopId.value.isInstanceOf[UUID]
  }
}
