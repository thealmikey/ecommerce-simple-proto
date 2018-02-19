package com.neo4scala.model

import java.util.{Calendar, Date, UUID}

import scala.util.{Failure, Success, Try}
import Common._


trait withAccountOpenAndCloseDate {
  def openDate: Date
  def closeDate: Option[Date]
}

trait User extends withAccountOpenAndCloseDate {
  def firstName: String
  def lastName: String
  def userId: UUID
  def phone: Long
  def profilePicture: Image
}

case class Customer private(firstName: String,
                    lastName: String,
                    userId: UUID,
                    phone: Long,
                    profilePicture: Image,
                    openDate: Date,
                    closeDate: Option[Date]=None)
    extends User
case class Owner private(firstName: String,
                 lastName: String,
                 userId: UUID,
                 phone: Long,
                 profilePicture: Image,
                 openDate: Date,
                 closeDate: Option[Date]=None) extends User

object User {
  def createCustomer(firstName: String,
                     lastName: String,
                     userId: UUID,
                     phone: Long,
                     profilePicture: Image,
                     openDate: Option[Date],
                     closeDate: Option[Date]): Try[User] = {
    closeDateCheck(openDate, closeDate).map { d =>
      Customer(firstName, lastName, userId, phone, profilePicture, d._1, d._2)
    }

    def createOwner(firstName: String,
                    lastName: String,
                    userId: UUID,
                    phone: Long,
                    profilePicture: Image,
                    openDate: Option[Date],
                    closeDate: Option[Date]): Try[User] = {
      closeDateCheck(openDate, closeDate).map { d =>
       Owner(firstName, lastName, userId, phone, profilePicture, d._1,d._2)
      }
    }

     def closeDateCheck(
                                openDate: Option[Date],
                                closeDate: Option[Date]): Try[(Date, Option[Date])]

    =
    {
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


