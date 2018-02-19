package com.neo4scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import org.neo4j.driver.v1._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn
import scala.util.Success

object MainOne extends App{

  implicit val actorSystem:ActorSystem = ActorSystem("mikesys")
  implicit val actorMaterializer:ActorMaterializer = ActorMaterializer()


  def createLabelAndName(name:String):String=s"CREATE (n:shopOwner {name:'$name'}) RETURN n"

  def fetchAll():String =s"MATCH(n) RETURN n"

  def theExec(statement:String):List[String] ={
    var driver:Driver = GraphDatabase.driver("bolt://localhost:7687",
      AuthTokens.basic("neo4j","myNeo4j"))
    var session:Session = driver.session()
    var futRs:StatementResult = session.run(statement)
    var myList:List[String] = List.empty[String]
    driver.close()
    myList
  }


var route:Route =  path("fem" / Segment){ theName =>
    onComplete(Future{theExec(createLabelAndName(theName.toString))}){
      case Success(a) =>complete(s"I have completed saving $theName")
      case _ => complete("i has failed u ")
    }
}

  val bindFuture = Http().bindAndHandle(route,"localhost",8080)

  StdIn.readLine()

  bindFuture.flatMap(_.unbind()).onComplete(_ =>actorSystem.terminate())
//  driver.close()


}