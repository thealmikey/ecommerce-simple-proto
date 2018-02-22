import java.util.Calendar

import cats.data.Validated
import cats.data.Validated.Valid
import com.neo4scala.model.{Customer, User}
import com.neo4scala.service.UserServiceImplementation
import com.neo4scala.service.UserValidater.DomainValidation
import org.scalatest.{FlatSpec, Matchers}
import cats.data.ValidatedNel
import cats.syntax.validated._

class UserServiceTest extends FlatSpec with Matchers{

  "UserService" should "make us a new Customer" in {

    val today = Calendar.getInstance().getTime

    var myMadeUser =Customer("mike","gikaru",25,716854639,openDate = Some(today)).valid[DomainValidation]
    var myAutoUser =UserServiceImplementation.createNewCustomer("mike","gikaru",25,716854639)
    myMadeUser match {
      case Valid(a) => myAutoUser match {
        case Valid(b) => assert(a.firstName ==b.firstName)
          assert(a.lastName==b.lastName)
          assert(a.age == b.age)
          assert(a.phone == b.phone)
      }
    }
  }
}
