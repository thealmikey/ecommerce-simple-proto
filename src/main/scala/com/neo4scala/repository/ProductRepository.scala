package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.{Category, Product}

import scala.util.Try

trait ProductRepository[F[_]]{
  def query(productId:UUID):F[Option[Product]]
  def query(productIds:UUID*):F[Option[Seq[Map[UUID,Product]]]]
  def query(category:Category):F[Option[Seq[Product]]]
  def update(product:Product):F[Option[Product]]
  def store(product:Product):F[Try[Product]]
  def removeProduct(productId:UUID):F[Try[Product]]
}