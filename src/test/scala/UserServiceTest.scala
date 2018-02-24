import java.util.Calendar

import cats.data.Validated
import cats.data.Validated.Valid
import com.neo4scala.model.{Customer, Owner, User}
import com.neo4scala.service.UserServiceImplementation
import com.neo4scala.service.UserValidater.DomainValidation
import org.scalatest.{FlatSpec, Matchers}
import cats.data.ValidatedNel
import cats.syntax.validated._
import com.wix.accord._
import com.wix.accord.dsl._
import com.wix.accord.Descriptions._ // For assertions

class UserServiceTest extends FlatSpec with Matchers {

  "UserService" should "make us a new Customer" in {

    val today = Calendar.getInstance().getTime

    var myMadeUser = Owner("mike", "", 10, 716854639, openDate = Some(today))
      .valid[DomainValidation]
    myMadeUser.map { a =>
      var theAns = User.userValidator(a)
      println(theAns.toString)
      "Yo"
    }
  }
}
