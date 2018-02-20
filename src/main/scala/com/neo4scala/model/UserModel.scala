package com.neo4scala.model

import java.util.{Calendar, Date, UUID}

import scala.util.{Failure, Success, Try}
import Common._

trait withAccountOpenAndCloseDate {
  def openDate: Option[Date]
  def closeDate: Option[Date]
}

case class UserUUID(value: UUID) extends AnyVal

trait User extends withAccountOpenAndCloseDate {
  def firstName: String
  def lastName: String
  def age: Int
  def userId: Option[UserUUID]
  def phone: Long
  def profilePicture: Option[Image]
}

case class Customer private (firstName: String,
                             lastName: String,
                             age: Int,
                             phone: Long,
                             userId: Option[UserUUID] = None,
                             profilePicture: Option[Image] = None,
                             openDate: Option[Date] = None,
                             closeDate: Option[Date] = None)
    extends User
case class Owner private (firstName: String,
                          lastName: String,
                          age: Int,
                          phone: Long,
                          userId: Option[UserUUID] = None,
                          profilePicture: Option[Image] = None,
                          openDate: Option[Date] = None,
                          closeDate: Option[Date] = None)
    extends User

object User {
  def createCustomer(firstName: String,
                     lastName: String,
                     age: Int,
                     phone: Long,
                     userId: Option[UserUUID] = None,
                     profilePicture: Option[Image] = None,
                     openDate: Option[Date] = None,
                     closeDate: Option[Date] = None): Customer = {
    var d = closeDateCheck(openDate, closeDate).get
    Customer(firstName,
             lastName,
             age,
             phone,
             userId,
             profilePicture,
             Some(d._1),
             d._2)
  }

  def createOwner(firstName: String,
                  lastName: String,
                  age: Int,
                  phone: Long,
                  userId: Option[UserUUID] = None,
                  profilePicture: Option[Image] = None,
                  openDate: Option[Date] = None,
                  closeDate: Option[Date] = None): Owner = {
    var d = closeDateCheck(openDate, closeDate).get
    Owner(firstName,
          lastName,
          age,
          phone,
          userId,
          profilePicture,
          Some(d._1),
          d._2)
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

sealed trait DayOfWeek {
  val theDay: Int

  override def toString = theDay match {
    case 1 => "Monday"
    case 2 => "Tuesday"
    case 3 => "Wednesday"
    case 4 => "Thursday"
    case 5 => "Friday"
    case 6 => "Saturday"
    case 7 => "Sunday"
  }
}

object DayOfWeek {
  private def createDayOfWeek(d: Int) = new DayOfWeek {
    override val theDay: Int = d
  }
  private def isValid: Int => Boolean = { i =>
    i >= 1 && i <= 7
  }
  def dayOfWeek(d: Int): Option[DayOfWeek] = {
    if (isValid(d)) {
      Some(createDayOfWeek(d))
    } else {
      None
    }
  }
}
