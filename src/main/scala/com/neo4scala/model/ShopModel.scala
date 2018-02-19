package com.neo4scala.model

import java.util.UUID
import Common._

case class ShopUUID(value:UUID) extends AnyVal

trait Shop {
  def name: String
  def shopId: ShopUUID
  def location: Location
  def logo: Image
  def productCatalog: List[Product]
}