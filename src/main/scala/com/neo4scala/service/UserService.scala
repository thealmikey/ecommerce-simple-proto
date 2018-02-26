package com.neo4scala.service

import com.neo4scala.model.{Customer, User}
import UserValidater._
import cats.data.ValidatedNel
import cats.syntax.validated._
import cats.Apply
import cats.implicits._

import scala.util.Try

trait UserService {
  def createNewCustomer(firstName: String,
                        lastName: String,
                        age: Int,
                        phone: Long): User
}

object UserServiceImplementation extends UserService {
  def createNewCustomer(firstName: String,
                        lastName: String,
                        age: Int,
                        phone: Long):User=
    User.createCustomer(firstName, lastName, age, phone)
}
