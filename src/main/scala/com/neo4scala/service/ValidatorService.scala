object UserValidate {

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
}
