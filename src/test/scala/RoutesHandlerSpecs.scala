
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.routing.HttpService
import spray.http.StatusCodes._
import com.knoldus.sprayservices.RoutesHandler
import spray.http.HttpResponse
import spray.http.StatusCode
import spray.http.StatusCodes
/**
 * @author knoldus
 */
class RoutesHandlerSpecs extends Specification with Specs2RouteTest with RoutesHandler with HttpService {
  def actorRefFactory = system

  "The service" should {

    "return a greeting for GET requests to the index path" in {
      Get("/index") ~> myRoute ~> check {
        responseAs[String] must contain("Hello")
      }
    }
  }

  "return a 60 for GET requests to the add path" in {
    Get("/add?num1=45&num2=15") ~> myRoute ~> check {
      responseAs[String] must contain("60")
    }
  }

}