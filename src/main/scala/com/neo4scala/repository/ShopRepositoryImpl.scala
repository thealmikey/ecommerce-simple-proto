package com.neo4scala.repository

import java.util.UUID

import cats.Id
import com.neo4scala.model.Common.{Image, Location}
import com.neo4scala.model._
import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.record.impl.ODocument
import gremlin.scala.Vertex
import gremlin.scala._
import shapeless._

import scala.util.{Success, Try}

object ShopRepositoryImpl extends ShopRepository[Id] {
  var graph = Util.TheGraph

  override def findByUUID(shopId: UUID, label: String): Option[Shop] = {
    val shopUUID = Key[UUID]("uuid")
    var theShop = graph.V.hasLabel(label).has(shopUUID, shopId).head
    var theLabel = theShop.label()
    Some(theShop.toCC[Shop])
  }

  def findByName(name: String, label: String): Option[Shop] = {
    var shopName = Key[String]("name")
    var theShop = graph.V.hasLabel(label).has(shopName, name).head
    var theLabel = theShop.label()
    Some(theShop.toCC[Shop])
  }

  val marshaller = new Marshallable[Shop] {
    def fromCC(cc: Shop) =
      FromCC(
        None,
        "regular_shop",
        Map(
          "name" -> cc.name,
          "shopId" -> cc.shopId.value,
          "logo" -> cc.logo.imageUrl,
          "location" -> new ODocument("Location")
            .field("longitude", cc.location.longitude)
            .field("latitude", cc.location.latitude),
          "shopType" -> cc.shopType
        )
      )

    def toCC(id: AnyRef, valueMap: Map[String, Any]): Shop =
      Shop(
        name = valueMap("name").asInstanceOf[String],
        shopId = ShopUUID(valueMap("shopId").asInstanceOf[UUID]),
        logo = Image(valueMap("logo").asInstanceOf[String]),
        location = Location(
          valueMap("location")
            .asInstanceOf[ODocument]
            .toMap
            .get("latitude")
            .asInstanceOf[Long],
          valueMap("location")
            .asInstanceOf[ODocument]
            .toMap
            .get("longitude")
            .asInstanceOf[Long]
        ),
        shopType = valueMap("shopType").asInstanceOf[String]
      )
  }

  override def add(shop: Shop): Id[Try[Shop]] = {
    shop match {
      case x: Shop =>
        var theShop = graph.+(x.asInstanceOf[Shop])(marshaller)
        Success(theShop.toCC[Shop](marshaller))
    }
  }

  override def update(shop: Shop): Id[Try[Shop]] = {
    val shopUUID = Key[UUID]("shopId")
    var rawQuery = graph.V
      .has(shopUUID, shop.shopId.value)
      .head
    var theShop = shop match {
      case z: Shop  => rawQuery.updateWith(z.asInstanceOf[Shop])(marshaller)
    }
    var theLabel = theShop.label()
    Success(theShop.toCC[Shop])
  }

  override def remove(shop: Shop): Id[Try[Shop]] = {
    val shopUUID = Key[UUID]("shopId")
    graph.V.hasLabel("regular_shop").has(shopUUID, shop.shopId.value).drop()
    Success(shop)
  }

}
