package com.neo4scala.model

import java.util.UUID

import Common._
import com.wix.accord._
import com.wix.accord.dsl._
import gremlin.scala.label
import gremlin.scala._

sealed trait Size
case object Small extends Size
case object Medium extends Size
case object Large extends Size

sealed trait InStock
case object Available extends InStock
case object Unavailable extends InStock

sealed trait ProductType
case object PlainProduct extends ProductType
case object ServiceProduct extends ProductType

@label("product")
sealed trait ProductTrait {
  def productName: String
  def productId: UUID
  def sizes: Option[Size]
  def price: Double
  def productStatus: Status
  def quatityInStock: Option[Int]
  def stock: InStock
  def images: Option[List[Image]]
  def categories: Option[List[Category]]
  def productType: ProductType
}

@label("product")
case class Product(productName: String,
                   productId: UUID,
                   sizes: Option[Size],
                   price: Double,
                   productStatus: Status,
                   quatityInStock: Option[Int],
                   stock: InStock,
                   images: Option[List[Image]],
                   categories: Option[List[Category]] = None,
                   productType: ProductType)
    extends ProductTrait

object Product {
  implicit val productValidator = validator[Product] { product =>
    product.price must be > 0.0
    product.categories.get.size must be > 0
    product.images.get.size must be > 1
    product.productId.isInstanceOf[UUID]
    product.productName.size must be > 3
    product.quatityInStock.get must be > 0
  }
}
