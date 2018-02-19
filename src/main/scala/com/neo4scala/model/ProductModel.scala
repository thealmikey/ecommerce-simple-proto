package com.neo4scala.model

import java.util.UUID

sealed trait Size
case object Small extends Size
case object Medium extends Size
case object Large extends Size

sealed trait InStock
case object Available extends InStock
case object Unavailable extends InStock


sealed trait Product {
  def productName: String
  def productId: UUID
  def sizes: Size
  def price: Double
  def productStatus: Status
  def quatityInStock: Int
  def stock: InStock
  def images: List[Image]
}