package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.User

import scala.util.Try

trait UserRepository {
  def findUser(id: UUID): Try[Option[User]]
  def updateUser(user: User): Try[Option[User]]
}
