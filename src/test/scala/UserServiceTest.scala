import cats.data.Validated
import com.neo4scala.model.{Customer, User}
import com.neo4scala.service.UserServiceImplementation
import com.neo4scala.service.UserValidater.DomainValidation
import org.scalatest.{FlatSpec, Matchers}
import cats.data.ValidatedNel
import cats.syntax.validated._

class UserServiceTest extends FlatSpec with Matchers{

  "UserService" should "make us a new Customer" in {

    var myMadeUser =Customer("mike","gikaru",25,716854639).valid[DomainValidation]
    assertResult(myMadeUser){
      UserServiceImplementation.createNewCustomer("mike","gikaru",25,716854639)
    }
  }
}
