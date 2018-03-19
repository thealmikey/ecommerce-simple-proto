package com.neo4scala.repository

import java.util.UUID

import com.neo4scala.model.{Order, OrderState}

import scala.util.Try

trait OrdertRepository[F[_]]{
    def findByUUID(property:UUID, label:String):Option[Order]
    def findByName(property:String, label:String):Option[Order]
    def add(user:Order):F[Try[Order]]
    def update(order:Order):F[Try[Order]]
    def remove(order:Order):F[Try[Order]]
  }