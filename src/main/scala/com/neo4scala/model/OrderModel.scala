package com.neo4scala.model

import java.util
import java.util.{Calendar, Date, UUID}

import User._
import com.neo4scala.model.Product._

import scala.util.{Failure, Success, Try}
import com.wix.accord._
import com.wix.accord.dsl._
import Product.productValidator
import gremlin.scala.label

sealed trait Status
case object Enabled extends Status
case object Disabled extends Status

trait OrderState
case object Submitted extends OrderState
case object Pending extends OrderState
case object Received extends OrderState
case object Processing extends OrderState
case object Shipped extends OrderState

case class OrderUUID(value: UUID) extends AnyVal
object OrderUUID {
  implicit val orderUUIDValidator = validator[OrderUUID] { o =>
    o.value.isInstanceOf[UUID]
  }
}

@label("order")
case class Order private (customer: OwnerType,
                          shop: Shop,
                          products: List[Product],
                          orderId: OrderUUID,
                          orderState: OrderState,
                          openDate: Date,
                          closeDate: Option[Date])

object Order {

  implicit val orderValidator = validator[Order] { order =>
    order.customer is valid
    order.shop is valid
    order.orderId is valid
    order.products.each is valid
  }

  def createOrder(customer: OwnerType,
                  shop: Shop,
                  products: List[Product],
                  orderId: OrderUUID,
                  orderState: OrderState,
                  openDate: Option[Date],
                  closeDate: Option[Date]) = {
    closeDateCheck(openDate, closeDate).map { d =>
      Order(customer, shop, products, orderId, orderState, d._1, d._2)
    }
  }

  def closeDateCheck(openDate: Option[Date],
                     closeDate: Option[Date]): Try[(Date, Option[Date])] = {
    val od = openDate.getOrElse(Calendar.getInstance().getTime)

    closeDate
      .map { cd =>
        if (cd before (od))
          Failure(
            new Exception(
              s"Close date $cd cannot be earlier than open date $od"))
        else
          Success((od, Some(cd)))
      }
      .getOrElse {
        Success((od, closeDate))
      }
  }
}
