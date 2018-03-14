package com.neo4scala.service

import java.util.UUID

import com.neo4scala.model.{Customer, User, UserUUID}

object UserServiceImpl extends UserService {
  def createNewCustomer(firstName: String,
                        lastName: String,
                        age: Int,
                        phone: Long,
                        userUUID:UUID
                       ): Customer =
    User.createCustomer(firstName, lastName, age, phone,Option(UserUUID(userUUID)))
}
