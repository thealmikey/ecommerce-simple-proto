package com.neo4scala.repository

import java.util.UUID

import com.steelbridgelabs.oss.neo4j.structure.{Neo4JGraphConfigurationBuilder, Neo4JGraphFactory}
import com.steelbridgelabs.oss.neo4j.structure.providers.DatabaseSequenceElementIdProvider
import org.apache.commons.configuration.BaseConfiguration
import org.apache.tinkerpop.gremlin.orientdb.OrientGraph
import gremlin.scala._
import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph
import org.neo4j.driver.v1.{AuthTokens, Driver, GraphDatabase}

object Util {
//  var driver: Driver =
//    GraphDatabase.driver("bolt://localhost", AuthTokens.basic("", ""))

  def createUUID = java.util.UUID.randomUUID

  import com.steelbridgelabs.oss.neo4j.structure.Neo4JElementIdProvider
  import com.steelbridgelabs.oss.neo4j.structure.providers.Neo4JNativeElementIdProvider

  //val provider = new DatabaseSequenceElementIdProvider(driver)

  var config = new BaseConfiguration()
  config.setProperty("orient-url","remote:localhost/Mike")
  config.setProperty("orient-user","root")
  config.setProperty("orient-pass","root")
//  var TheGraphConfig = Neo4JGraphConfigurationBuilder
//    .connect("localhost", "", "")
//    .withName(s"neo4j-bolt-${UUID.randomUUID.toString}")
//    .withVertexIdProvider(classOf[Neo4JNativeElementIdProvider])
//    .withEdgeIdProvider(classOf[Neo4JNativeElementIdProvider])
//    .withElementIdProvider(classOf[Neo4JNativeElementIdProvider])
//    .build()


  val TheGraph: ScalaGraph = OrientGraph.open(config).asScala

}
