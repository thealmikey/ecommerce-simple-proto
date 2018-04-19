package com.neo4scala.service

import java.util.UUID

import com.neo4scala.model.{Customer, User, UserType}

trait UserService {
  def createNewUser(firstName: String,
                    lastName: String,
                    age: Int,
                    phone: Long,
                    userUUID: UUID,
                    userType: UserType): User
}
