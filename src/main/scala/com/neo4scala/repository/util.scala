package com.neo4scala.repository

import org.apache.commons.configuration.BaseConfiguration
import org.apache.tinkerpop.gremlin.orientdb.OrientGraph
import gremlin.scala._


object Util {

  def createUUID = java.util.UUID.randomUUID

  var config = new BaseConfiguration()
  config.setProperty("orient-url","remote:oriental/Mike")
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
