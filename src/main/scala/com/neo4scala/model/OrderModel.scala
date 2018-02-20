package com.neo4scala.model

import java.util.{Calendar, Date, UUID}

import scala.util.{Failure, Success, Try}

sealed trait Status
case object Enabled extends Status
case object Disabled extends Status

trait withOrderCreateAndCloseDate {
  def openDate: Date
  def closeDate: Option[Date]
}

trait OrderState
case object Submitted extends OrderState
case object Pending extends OrderState
case object Received extends OrderState
case object Processing extends OrderState
case object Shipped extends OrderState

case class OrderUUID(value:UUID) extends AnyVal

case class Order private(customer: Customer, shop: Shop, orderId:OrderUUID, orderState: OrderState,openDate:Date,closeDate:Option[Date]) extends withOrderCreateAndCloseDate

object Order {

  def createOrder(customer: Customer, shop: Shop, orderId: OrderUUID, orderState: OrderState, openDate: Option[Date], closeDate: Option[Date]) = {
    closeDateCheck(openDate, closeDate).map { d =>
      Order(customer, shop, orderId, orderState, d._1, d._2)
    }
  }


  def closeDateCheck(
                      openDate: Option[Date],
                      closeDate: Option[Date]): Try[(Date, Option[Date])]

  = {
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