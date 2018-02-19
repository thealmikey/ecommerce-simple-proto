package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.User

import scala.util.Try

trait UserRepository[F[_]] {
  def query(userId:UUID):F[Option[User]]
  def query(users:UUID*):F[Option[Seq[Map[UUID,User]]]]
  def update(user:User):F[Option[User]]
  def store(user:User):F[Try[User]]
  def removeUser(user:User):F[Try[User]]
  def removeUser(userId:UUID):F[Try[User]]
}
