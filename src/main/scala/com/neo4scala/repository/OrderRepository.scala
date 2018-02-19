package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.{Order, OrderState}

import scala.util.Try

trait OrdertRepository[F[_]]{
  def query(orderId:UUID):F[Option[Order]]
  def query(orderIds:UUID*):F[Option[Seq[Map[UUID,Order]]]]
  def query(orderStatus:OrderState):F[Option[Seq[Order]]]
  def update(order:Order):F[Option[Order]]
  def store(order:Order):F[Try[Order]]
  def removeOrder(order:Order):F[Try[Order]]
  def removeOrder(orderId:UUID):F[Try[Order]]
}