package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.{Category, Product}

import scala.util.Try

trait ProductRepository[F[_]] {
  def findByUUID(productId: UUID, label: String): F[Option[Product]]
  def findByName(productName: String, label: String): F[Option[Product]]
  //def findManyByUUID(productIds:UUID*):F[Option[Seq[Map[UUID,Product]]]]
  def findByCategory(category: Category): F[Option[Seq[Product]]]
  def update(product: Product): F[Try[Product]]
  def add(product: Product): F[Try[Product]]
  def remove(product: Product): F[Try[Product]]
}
