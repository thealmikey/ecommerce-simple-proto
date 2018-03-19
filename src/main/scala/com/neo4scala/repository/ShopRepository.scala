package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.ShopTrait

import scala.util.Try

trait ShopRepository[F[_]] {

  def findByUUID(property:UUID, label:String):Option[ShopTrait]
  def findByName(property:String, label:String):Option[ShopTrait]
  def add(shop:ShopTrait):F[Try[ShopTrait]]
  def update(shop:ShopTrait):F[Try[ShopTrait]]
  def remove(shop:ShopTrait):F[Try[ShopTrait]]
}