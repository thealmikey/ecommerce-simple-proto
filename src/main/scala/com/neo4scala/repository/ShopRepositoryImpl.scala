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

  def matchLabelToCC(theLabel: String, theShop: Vertex): Option[ShopTrait] = {
    theLabel match {
      case "regular_shop" => Option(theShop.toCC[Shop](marshaller).asInstanceOf[ShopTrait])
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

  val marshaller = new Marshallable[Shop] {
    def fromCC(cc: Shop) =
      FromCC(
        None,
        "regular_shop",
        Map(
          "name" -> cc.name,
          "shopId" -> cc.shopId.value,
          "logo" -> cc.logo.imageUrl,
          "location" ->new ODocument("Location").field("longitude",cc.location.longitude).field("latitude",cc.location.latitude)
        )
      )

    def toCC(id: AnyRef, valueMap: Map[String, Any]): Shop =
      Shop(
        name = valueMap("name").asInstanceOf[String],
        shopId = ShopUUID(valueMap("shopId").asInstanceOf[UUID]),
        logo = Image(valueMap("name").asInstanceOf[String]),
        location = Location(valueMap("location")
                              .asInstanceOf[ODocument].toMap.get("latitude").asInstanceOf[Long],
                            valueMap("location")
                              .asInstanceOf[ODocument].toMap.get("longitude").asInstanceOf[Long])
      )
  }

  override def add(shop: ShopTrait): Id[Try[ShopTrait]] = {
    shop match {
      case x: Shop =>
        var theShop = graph.+(x.asInstanceOf[Shop])(marshaller)
        Success(theShop.toCC[Shop](marshaller))
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
    val shopUUID = Key[UUID]("shopId")
    var theClassName = shop.getClass.getSimpleName.toLowerCase

    var ButcheryType = TypeCase[Butchery]
    var HardwareType = TypeCase[Hardware]
    var GreengrocerType = TypeCase[Greengrocer]
    var ShopType = TypeCase[Shop]
    var rawQuery = graph.V
      .has(shopUUID, shop.shopId.value)
      .head
    var theShop = shop match {
      case x: Butchery    => rawQuery.updateWith(x.asInstanceOf[Butchery])
      case q: Greengrocer => rawQuery.updateWith(q.asInstanceOf[Greengrocer])
      case y: Hardware    => rawQuery.updateWith(y.asInstanceOf[Hardware])
      case z: Shop        => rawQuery.updateWith(z.asInstanceOf[Shop])(marshaller)
    }
    var theLabel = theShop.label()
    Success(matchLabelToCC(theLabel, theShop).get)
  }

  override def remove(shop: ShopTrait): Id[Try[ShopTrait]] = {
    val shopUUID = Key[UUID]("shopId")
    var theClassName = shop.getClass.getSimpleName.toLowerCase
    graph.V.hasLabel(theClassName).has(shopUUID, shop.shopId.value).drop()
    Success(shop)
  }

  object ButcheryType {
    def unapply(shopTrait: ShopTrait): Option[Any] = ???
  }

}
