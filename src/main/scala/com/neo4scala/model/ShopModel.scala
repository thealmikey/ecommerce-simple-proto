package com.neo4scala.model

import java.util.UUID
import Common._
import com.wix.accord._
import com.wix.accord.dsl._

case class ShopUUID(value: UUID) extends AnyVal

trait Shop {
  def name: String
  def shopId: ShopUUID
  def location: Location
  def logo: Image
  def productCatalog: List[Product]
}

object Shop {

  implicit val shopValidator = validator[Shop] { shop =>
    shop.name is notEmpty
    shop.location is notEmpty
    shop.shopId is notEmpty
    shop.logo is notEmpty
  }
}
