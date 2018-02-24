package com.neo4scala.model

import java.util.UUID
import com.wix.accord._
import com.wix.accord.dsl._

case class CategoryUUID(value:UUID) extends AnyVal
trait Category {
  def categoryId: CategoryUUID
  def categoryName: String
  def subCategories: Option[List[Category]]
  def synonyms: Option[List[String]]
}

case class PlainCategory(categoryId: CategoryUUID,
                    categoryName: String,
                    subCategories: Option[List[Category]],
                    synonyms: Option[List[String]])
    extends Category

object Category{
  implicit val categoryValidator = validator[Category] { category =>
    category.categoryId is notEmpty
    category.categoryName is notEmpty
    category.synonyms is notEmpty
  }
}
