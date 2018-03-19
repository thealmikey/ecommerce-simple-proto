package com.neo4scala.repository

import java.util.UUID

import cats.Id
import com.neo4scala.model.{Category, PlainProduct, Product}
import gremlin.scala._

import scala.util.{Success, Try}

object ProductRepositoryImpl extends ProductRepository[Id] {
  var graph = Util.TheGraph

  def matchLabelToCC(theLabel: String, theProduct: Vertex): Option[Product] = {
    theLabel match {
      case "plain_product" =>
        Option(theProduct.toCC[PlainProduct].asInstanceOf[Product])
    }
  }

  override def findByUUID(productId: UUID, label: String): Option[Product] = {
    val productUUID = Key[UUID]("uuid")
    var theProduct = graph.V.hasLabel(label).has(productUUID, productId).head
    var theLabel = theProduct.label()
    matchLabelToCC(theLabel, theProduct)
  }

  override def findByName(productName: String,
                          label: String): Option[Product] = {
    var Name = Key[String]("name")
    var theProduct = graph.V.hasLabel(label).has(Name, productName).head
    var theLabel = theProduct.label()
    matchLabelToCC(theLabel, theProduct)
  }

  override def findByCategory(category: Category): Id[Option[Seq[Product]]] = {
    var categoryName = Key[String]("name")
    var theClassName = category.getClass.getSimpleName.toLowerCase
    var theProducts = graph.V
      .hasLabel(theClassName)
      .has(categoryName, category.categoryName)
      .out()
      .dedup()
      .toList()
    Some(theProducts.foldRight(List.empty[Product])((a, b) =>
      b :+ a.toCC[PlainProduct].asInstanceOf[Product]))
  }

  override def add(product: Product): Id[Try[Product]] = {
    product match {
      case x: PlainProduct =>
        var theProduct = graph + x
        Success(theProduct.toCC[PlainProduct].asInstanceOf[Product])
    }
  }

  override def update(product:Product): Id[Try[Product]] = {
    val productUUID = Key[UUID]("uuid")
    var theClassName = product.getClass.getSimpleName.toLowerCase

    var updateProduct = product match {
      case x:PlainProduct => x.asInstanceOf[PlainProduct]
    }

    //todo update to generic product
    var theProduct = graph.V
      .hasLabel(theClassName)
      .has(productUUID, product.productId)
      .head
      .updateWith(updateProduct)

//    def typeBend[T<:Product](product: T):T = product match {
//      case x:PlainProduct => x.asInstanceOf[T]
//    }
def manOf[T: Manifest](t: T): Manifest[T] = manifest[T]
    var theLabel = theProduct.label()
    Success(matchLabelToCC(theLabel, theProduct).get)
  }

  override def remove(product: Product): Id[Try[Product]] = {
    val productUUID = Key[UUID]("uuid")
    var theClassName = product.getClass.getSimpleName.toLowerCase
    graph.V.hasLabel(theClassName).has(productUUID, product.productId).drop()
    Success(product)
  }
}
