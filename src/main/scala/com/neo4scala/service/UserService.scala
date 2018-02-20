package com.neo4scala.service

import com.neo4scala.model.{Customer, User}
import UserValidater._
import cats.data.ValidatedNel
import cats.syntax.validated._
import cats.Apply
import cats.implicits._

trait UserService {
  def createNewCustomer(user: Customer): ValidationResult[User] = {
    (validateFirstName(user.firstName),
     validateLastName(user.lastName),
     validateAge(user.age),
     validatePhone(user.phone)).mapN((a,b,c,d)=>Customer(a,b,c,d))
  }
}
