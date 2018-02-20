package com.neo4scala.service

import com.neo4scala.model.{Customer, User}
import UserValidater._
import cats.data.ValidatedNel
import cats.syntax.validated._
import cats.Apply
import cats.implicits._

trait UserService {
  def createNewCustomer(firstName:String,lastName:String,age:Int,phone:Long): ValidationResult[User]
}

object UserServiceImplementation extends UserService{
  def createNewCustomer(firstName:String,lastName:String,age:Int,phone:Long): ValidationResult[User] =
( validateFirstName(firstName),validateLastName(lastName), validateAge(age),validatePhone(phone)).mapN((a,b,c,d) =>User.createCustomer(a, b,c,d))
}
