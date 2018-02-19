package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.Category

import scala.util.Try

trait CategoryRepository[F[_]]{
  def query(categoryId:UUID):F[Option[Category]]
  def query(categoryIds:UUID*):F[Option[Seq[Map[UUID,Category]]]]
  def update(category:Category):F[Option[Category]]
  def store(category:Category):F[Try[Category]]
  def removeCategory(categoryId:UUID):F[Try[Category]]
}