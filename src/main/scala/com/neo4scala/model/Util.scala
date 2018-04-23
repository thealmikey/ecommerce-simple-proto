package com.neo4scala.model

import gremlin.scala.label
import gremlin.scala._

object Common {

  sealed trait DayOfWeek {
    val value: Int

    override def toString = value match {
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
    private def unsafeDayOfWeek(d: Int) = new DayOfWeek {
      val value = d
    }

    private val isValid: Int => Boolean = { i =>
      i >= 1 && i <= 7
    }

    def dayOfWeek(d: Int): Option[DayOfWeek] =
      if (isValid(d))
        Some(unsafeDayOfWeek(d))
      else None
  }
  @label("Location")
  case class Location(latitude: String, longitude: String)
  @label("image")
  case class Image(imageUrl: String)



}
