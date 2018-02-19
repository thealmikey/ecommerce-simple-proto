package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.Shop

import scala.util.Try

trait ShopRepository[F[_]]{
  def query(shopId:UUID):F[Option[Shop]]
  def query(shops:UUID*):F[Option[Seq[Map[UUID,Shop]]]]
  def update(shop:Shop):F[Option[Shop]]
  def store(shop:Shop):F[Try[Product]]
  def removeShop(shopId:UUID):F[Try[Shop]]
}