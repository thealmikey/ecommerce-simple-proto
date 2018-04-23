package com.neo4scala.repository

import java.util.{Date, UUID}

import cats.Id
import com.neo4scala.model.Common.{Image, Location}
import com.neo4scala.model._
import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.record.impl.ODocument
import gremlin.scala.Vertex
import gremlin.scala._
import io.circe.{HCursor, Json}
import shapeless._

import scala.util.{Success, Try}

object ShopRepositoryImpl extends ShopRepository[Id] {
  var graph = Util.TheGraph

  override def findByUUID(shopId: UUID): Option[Shop] = {
    val shopUUID = Key[UUID]("uuid")
    var theShop = graph.V.hasLabel("shop").has(shopUUID, shopId).head
    var theLabel = theShop.label()
    Some(theShop.toCC[Shop])
  }

  def findByName(name: String): Option[Shop] = {
    var shopName = Key[String]("name")
    var theShop = graph.V.hasLabel("shop").has(shopName, name).head
    var theLabel = theShop.label()
    Some(theShop.toCC[Shop])
  }

  val marshaller = new Marshallable[Shop] {
    def fromCC(cc: Shop) =
      FromCC(
        None,
        "shop",
        Map(
          "name" -> cc.name,
          "shopId" -> cc.shopId,
          "logo" -> cc.logo.imageUrl,
          "location" -> new ODocument("Location")
            .field("longitude", cc.location.longitude)
            .field("latitude", cc.location.latitude),
          "shopType" -> cc.shopType,
          "openTime" -> cc.openTime.get,
          "closeTime" -> cc.closeTime.get
        )
      )

    def toCC(id: AnyRef, valueMap: Map[String, Any]): Shop =
      Shop(
        name = valueMap("name").asInstanceOf[String],
        shopId = valueMap("shopId").asInstanceOf[String],
        logo = Image(valueMap("logo").asInstanceOf[String]),
        location = Location(
          valueMap("location")
            .asInstanceOf[ODocument]
            .toMap
            .get("latitude")
            .asInstanceOf[String],
          valueMap("location")
            .asInstanceOf[ODocument]
            .toMap
            .get("longitude")
            .asInstanceOf[String]
        ),
        shopType = valueMap("shopType").asInstanceOf[String],
        openTime = Some(valueMap("openTime").asInstanceOf[String]),
        closeTime = Some(valueMap("closeTime").asInstanceOf[String])
      )
  }

  override def add(shop: Shop): Id[Try[Shop]] = {
    var theShop = graph.+(shop.asInstanceOf[Shop])(marshaller)
    Success(theShop.toCC[Shop](marshaller))
  }

  override def update(shop: Shop): Id[Try[Shop]] = {
    val shopUUID = Key[String]("shopId")
    var rawQuery = graph.V
      .has(shopUUID, shop.shopId)
      .head
    var theShop = shop match {
      case z: Shop => rawQuery.updateWith(z.asInstanceOf[Shop])(marshaller)
    }
    var theLabel = theShop.label()
    Success(theShop.toCC[Shop])
  }

  override def remove(shop: Shop): Id[Try[Shop]] = {
    val shopUUID = Key[String]("shopId")
    graph.V.hasLabel("shop").has(shopUUID, shop.shopId).drop()
    Success(shop)
  }

  val name = Key[String]("name")
  override def fetchAll(): Try[List[String]] = {
    var theName = Key[String]("name")
    var theRawRes = graph.V.hasLabel("shop").value(theName).toList()
    //println(theRawRes.toMap[String,Any])
    Success(theRawRes)
  }

}
