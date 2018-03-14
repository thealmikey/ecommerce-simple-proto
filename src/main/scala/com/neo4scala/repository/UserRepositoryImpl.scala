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

  def removeSpecific(phoneNo:Long)={
    var phoneNumber = Key[Long]("phone")
    var theUser = graph.V.hasLabel("customer").has(phoneNumber, phoneNo).drop().iterate()
  }

  def matchLabelToCC(theLabel: String, theUser: Vertex): Option[User] = {
    theLabel match {
      case "customer" => Option(theUser.toCC[Customer].asInstanceOf[User])
      case "owner"    => Option(theUser.toCC[Owner].asInstanceOf[User])
    }
  }

  override def findByUUID(userId: UUID, label: String): Option[User] = {
    val userUUID = Key[UUID]("uuid")
    var theUser = graph.V.hasLabel(label).has(userUUID, userId).head
    var theLabel = theUser.label()
    matchLabelToCC(theLabel, theUser)
  }

  def findByPhone(phone: Long, label: String): Option[User] = {
    var phoneNumber = Key[Long]("phone")
    var theUser = graph.V.hasLabel(label).has(phoneNumber, phone).head
    var theLabel = theUser.label()
    matchLabelToCC(theLabel, theUser)
  }

  override def add(user: User): Id[Try[User]] = {
    user match {
      case x: Customer =>
        var theUser = graph.+(x.asInstanceOf[Customer])
        Success(user)
      case x: Owner =>
        var theUser = graph.+(x.asInstanceOf[Owner])
        Success(user)
    }
  }

  override def update(user: Customer): Id[Try[User]] = {
    val userUUID = Key[UUID]("uuid")
    var theUser = graph.V
      .hasLabel[Customer]
      .has(userUUID, user.userId.get.value)
      .head
      .updateWith(user)
    var theLabel = theUser.label()
    Success(matchLabelToCC(theLabel, theUser).get)
  }

  override def remove(user: User): Id[Try[User]] = {
    val userUUID = Key[UUID]("uuid")
    user match {
      case x: Customer =>
       var testRemove = graph.V.hasLabel("customer").has(userUUID, x.userId.get.value).drop().headOption()
        println(testRemove)
        Success(user)
      case x: Owner =>
        graph.V.hasLabel("owner").has(userUUID, x.userId.get.value).drop()
        Success(user)
    }
  }
}
