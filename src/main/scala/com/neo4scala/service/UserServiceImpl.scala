package com.neo4scala.service

import java.util.UUID

import com.neo4scala.model.{Customer, User, UserType, UserUUID}

object UserServiceImpl extends UserService {
  def createNewUser(firstName: String,
                    lastName: String,
                    age: Int,
                    phone: Long,
                    userUUID: UUID,
                    userType: UserType): User =
    User.createUser(firstName,
                    lastName,
                    age,
                    phone,
                    Option(UserUUID(userUUID)),
                    userType = userType)
}
