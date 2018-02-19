package com.neo4scala.model

import java.util.UUID

trait Category {
  def categoryId: UUID
  def categoryName: String
  def subCategories: Option[List[Category]]
  def synonyms: Option[List[String]]
}

case class PlainCategory(categoryId: UUID,
                    categoryName: String,
                    subCategories: Option[List[Category]],
                    synonyms: Option[List[String]])
    extends Category
