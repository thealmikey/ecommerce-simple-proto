package com.neo4scala.model

import java.util.UUID
import Common._

trait Shop {
  def name: String
  def shopId: UUID
  def location: Location
  def logo: Image
  def productCatalog: List[Product]
}