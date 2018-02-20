package com.neo4scala.model

import java.util.UUID
import Common._

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
  def sizes: Option[Size]
  def price: Double
  def productStatus: Status
  def quatityInStock: Option[Int]
  def stock: InStock
  def images: Option[List[Image]]
  def categories: Option[List[Category]]
}

case class PlainProduct(productName: String,
                        productId: UUID,
                        sizes: Option[Size],
                        price: Double,
                        productStatus: Status,
                        quatityInStock: Option[Int],
                        stock: InStock,
                        images: Option[List[Image]],
                        categories: Option[List[Category]] = None)
    extends Product
