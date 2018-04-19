package com.neo4scala.model

import java.util.UUID

import com.wix.accord._
import com.wix.accord.dsl._
import gremlin.scala.label

case class CategoryUUID(value: UUID) extends AnyVal

sealed trait CategoryType
case object RegularCategory extends CategoryType

trait CategoryTrait {
  def categoryId: CategoryUUID
  def categoryName: String
  def subCategories: Option[List[Category]]
  def synonyms: Option[List[String]]
  def categoryType: CategoryType
}

@label("category")
case class Category(categoryId: CategoryUUID,
                    categoryName: String,
                    subCategories: Option[List[Category]],
                    synonyms: Option[List[String]],
                    categoryType: CategoryType)
    extends CategoryTrait

object Category {
  implicit val categoryValidator = validator[Category] { category =>
    category.categoryName is notEmpty
    category.synonyms is notEmpty
  }
}
