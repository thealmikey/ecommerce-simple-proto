package com.neo4scala.repository

import java.util.{Date, UUID}

import cats.Id
import cats.syntax.monoid._
import cats.implicits._
import cats.kernel.Monoid
import com.neo4scala.model.{Customer, Owner, User}
import gremlin.scala.Key
import gremlin.scala._
import gremlin.scala.dsl._

import scala.util.{Success, Try}

object UserRepositoryImpl extends UserRepository[Id] {

  var graph = Util.TheGraph

  def removeSpecific(phoneNo: Long) = {
    var phoneNumber = Key[Long]("phone")
    var theUser =
      graph.V.hasLabel("customer").has(phoneNumber, phoneNo).drop().iterate()
  }

  override def findByUUID(userId: UUID, label: String): Option[User] = {
    val userUUID = Key[UUID]("uuid")
    var theUser = graph.V.hasLabel(label).has(userUUID, userId).head
    var theLabel = theUser.label()
    Some(theUser.toCC[User])
  }

  def findByPhone(phone: Long, label: String): Option[User] = {
    var phoneNumber = Key[Long]("phone")
    var theUser = graph.V.hasLabel(label).has(phoneNumber, phone).head
    var theLabel = theUser.label()
    Some(theUser.toCC[User])
  }

  override def add(user: User): Id[Try[User]] = {
    var theUser = graph.+(user)
    Success(theUser.toCC[User])
  }

  override def update(user: User): Id[Try[User]] = {
    val userUUID = Key[UUID]("uuid")
    var theClassName = user.getClass.getSimpleName.toLowerCase
    var theUser = graph.V
      .hasLabel(theClassName)
      .has(userUUID, user.userId.get.value)
      .head
      .updateWith(user)
    var theLabel = theUser.label()
    Success(theUser.toCC[User])
  }

  override def remove(user: User): Id[Try[User]] = {
    val userUUID = Key[UUID]("uuid")
    var removedUser = graph.V
      .hasLabel("user")
      .has(userUUID, user.userId.get.value)
      .drop()
      .headOption()
    println(removedUser.get.toCC[User])
    Success(removedUser.get.toCC[User])
  }
}
