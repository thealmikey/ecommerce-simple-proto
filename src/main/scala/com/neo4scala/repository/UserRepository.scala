package com.neo4scala.repository

import java.util.UUID

import cats.Id
import com.neo4scala.model.{Customer, User}

import scala.util.{Success, Try}

trait UserRepository[F[_]] {

  def findByUUID(property:UUID, label:String):Option[User]
  def findByPhone(property:Long, label:String):Option[User]
  def add(user:User):F[Try[User]]
  def update(user:Customer):F[Try[User]]
  def remove(user:User):F[Try[User]]
}


