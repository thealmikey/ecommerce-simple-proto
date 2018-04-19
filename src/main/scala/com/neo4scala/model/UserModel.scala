package com.neo4scala.model
import gremlin.scala._

import java.util.{Calendar, Date, UUID}

import scala.util.{Failure, Success, Try}
import com.wix.accord._
import com.wix.accord.dsl._
import Common._

sealed trait UserType
case class Customer(value:String="customer") extends UserType
case class Owner(value:String="owner") extends UserType

case class UserUUID(value: UUID) extends AnyVal

@label("user")
trait UserTrait {
  def firstName: String
  def lastName: String
  def age: Int
  def userId: Option[UserUUID]
  def phone: Long
  def profilePicture: Option[Image]
  def openDate: Option[Date]
  def closeDate: Option[Date]
  def userType: UserType
}

@label("user")
case class User private (firstName: String,
                         lastName: String,
                         age: Int,
                         phone: Long,
                         userId: Option[UserUUID] = None,
                         profilePicture: Option[Image] = None,
                         openDate: Option[Date] = None,
                         closeDate: Option[Date] = None,
                         userType: UserType)
    extends UserTrait

object User {

  implicit val userValidator = validator[User] { u =>
    u.firstName is notEmpty
    u.firstName.size must be >= 3
    u.lastName.size must be >= 3
    u.lastName is notEmpty
    u.age.toInt must be >= 16
    u.phone.toString.size must be >= 12
    u.phone.toString is matchRegex("""\^254(\d){9}\""")
  }

  def createUser(firstName: String,
                 lastName: String,
                 age: Int,
                 phone: Long,
                 userId: Option[UserUUID] = None,
                 profilePicture: Option[Image] = None,
                 openDate: Option[Date] = None,
                 closeDate: Option[Date] = None,
                 userType: UserType): User = {
    var d = closeDateCheck(openDate, closeDate).get
    User(firstName,
         lastName,
         age,
         phone,
         userId,
         profilePicture,
         Some(d._1),
         d._2,
         userType)
  }

  def closeDateCheck(openDate: Option[Date],
                     closeDate: Option[Date]): Try[(Date, Option[Date])] = {
    val od = openDate.getOrElse(Calendar.getInstance().getTime)

    closeDate
      .map { cd =>
        if (cd before (od))
          Failure(
            new Exception(
              s"Close date $cd cannot be earlier than open date $od"))
        else
          Success((od, Some(cd)))
      }
      .getOrElse {
        Success((od, closeDate))
      }
  }
}
