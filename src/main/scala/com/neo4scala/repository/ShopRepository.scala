package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.Shop

import scala.util.Try

trait ShopRepository[F[_]] {

  def findByUUID(property:UUID, label:String):Option[Shop]
  def findByName(property:String, label:String):Option[Shop]
  def add(shop:Shop):F[Try[Shop]]
  def update(shop:Shop):F[Try[Shop]]
  def remove(shop:Shop):F[Try[Shop]]

}