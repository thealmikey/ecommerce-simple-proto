package com.neo4scala.repository

import java.util.UUID

import cats.Id
import com.neo4scala.model._
import gremlin.scala.Vertex
import gremlin.scala._

import scala.util.{Success, Try}

object ShopRepositoryImpl extends ShopRepository[Id] {
  var graph = Util.TheGraph

  def matchLabelToCC(theLabel: String, theShop: Vertex): Option[ShopTrait] = {
    theLabel match {
      case "regular_shop" => Option(theShop.toCC[Shop].asInstanceOf[ShopTrait])
      case "butchery"     => Option(theShop.toCC[Butchery].asInstanceOf[ShopTrait])
      case "hardware"     => Option(theShop.toCC[Hardware].asInstanceOf[ShopTrait])
      case "greengrocer" =>
        Option(theShop.toCC[Greengrocer].asInstanceOf[ShopTrait])
    }
  }

  override def findByUUID(shopId: UUID, label: String): Option[ShopTrait] = {
    val shopUUID = Key[UUID]("uuid")
    var theShop = graph.V.hasLabel(label).has(shopUUID, shopId).head
    var theLabel = theShop.label()
    matchLabelToCC(theLabel, theShop)
  }

  def findByName(name: String, label: String): Option[ShopTrait] = {
    var shopName = Key[String]("name")
    var theShop = graph.V.hasLabel(label).has(shopName, name).head
    var theLabel = theShop.label()
    matchLabelToCC(theLabel, theShop)
  }

  override def add(shop: ShopTrait): Id[Try[ShopTrait]] = {
    shop match {
      case x: Shop =>
        var theShop = graph.+(x.asInstanceOf[Shop])
        Success(theShop.toCC[Shop])
      case x: Greengrocer =>
        var theShop = graph.+(x.asInstanceOf[Greengrocer])
        Success(theShop.toCC[Greengrocer])
      case x: Butchery =>
        var theShop = graph.+(x.asInstanceOf[Butchery])
        Success(theShop.toCC[Butchery])
      case x: Hardware =>
        var theShop = graph.+(x.asInstanceOf[Hardware])
        Success(theShop.toCC[Hardware])
    }
  }

  override def update(shop: ShopTrait): Id[Try[ShopTrait]] = {
    val shopUUID = Key[UUID]("uuid")
    var theShop = graph.V
      .hasLabel[Customer]
      .has(shopUUID, shop.shopId.value)
      .head
      .updateWith(shop)
    var theLabel = theShop.label()
    Success(matchLabelToCC(theLabel, theShop).get)
  }

  override def remove(shop: ShopTrait): Id[Try[ShopTrait]] = {
    val shopUUID = Key[UUID]("uuid")
    var theClassName = shop.getClass.getSimpleName.toLowerCase
    graph.V.hasLabel(theClassName).has(shopUUID, shop.shopId.value).drop()
    Success(shop)
  }
}
