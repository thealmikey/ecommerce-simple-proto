package com.neo4scala.service

import cats.data.ValidatedNel
import cats.syntax.validated._

object UserValidater {

  sealed trait DomainValidation {

    def errorMessage: String
  }

  case object FirstNameHasSpecialCharacters extends DomainValidation {
    def errorMessage: String =
      "First name cannot contain spaces, numbers or special characters."
  }

  case object LastNameHasSpecialCharacters extends DomainValidation {
    def errorMessage: String =
      "Last name cannot contain spaces, numbers or special characters."
  }

  case object PhoneIsInvalid extends DomainValidation {
    def errorMessage: String =
      "Your phone number has to be a valid Kenyan number"
  }

  case object AgeIsInvalid extends DomainValidation {
    def errorMessage: String =
      "You must be aged 18 and not older than 75 to use our services."
  }

  type ValidationResult[A] = ValidatedNel[DomainValidation, A]
  def validateFirstName(firstName: String): ValidationResult[String] =
    if (firstName.matches("^[a-zA-Z]+$")) firstName.validNel
    else FirstNameHasSpecialCharacters.invalidNel

  def validateLastName(lastName: String): ValidationResult[String] =
    if (lastName.matches("^[a-zA-Z]+$")) lastName.validNel
    else LastNameHasSpecialCharacters.invalidNel

  def validateAge(age: Int): ValidationResult[Int] =
    if (age >= 18 && age <= 75) age.validNel else AgeIsInvalid.invalidNel

  def validatePhone(phone: Long): ValidationResult[Long] =
    if (phone.toString().matches("^[0-9]+$")) phone.toLong.validNel
    else PhoneIsInvalid.invalidNel

}
