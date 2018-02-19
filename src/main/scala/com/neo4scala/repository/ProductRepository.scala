package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.{Category, Product}

import scala.util.Try

trait ProductRepository{
  def query(productId:UUID):Try[Option[Product]]
  def query(productIds:UUID*):Try[Option[Seq[Map[UUID,Product]]]]
  def query(category:Category):Try[Option[Seq[Product]]]
  def store(product:Product):Try[Product]
  def removeProduct(productId:UUID):Try[Product]
}