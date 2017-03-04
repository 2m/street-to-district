import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.{HtmlAnchor, HtmlPage, HtmlSubmitInput, HtmlTextInput}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

object StreetToDistrict extends App {

  implicit val system = ActorSystem("StreetToDistrict")
  implicit val materializer = ActorMaterializer()

  val route =
    pathPrefix("street" / Remaining) { streetName =>
      get {
        complete(streetToDistrict(streetName))
      }
    }

  Http().bindAndHandle(route, "0.0.0.0", sys.env.getOrElse("PORT", "8080").toInt)

  def streetToDistrict(streetName: String) = {
    val webClient = new WebClient()
    webClient.getOptions.setJavaScriptEnabled(false)

    val page1 = webClient.getPage[HtmlPage]("http://vilnius21.lt")

    val form = page1.getFormByName("ieskom")

    val button = form.getInputByValue[HtmlSubmitInput]("Ieškoti")
    val textField = form.getInputByName[HtmlTextInput]("z")

    textField.setValueAttribute(streetName)

    val page2 = button.click[HtmlPage]()

    val resultLink = page2.querySelector[HtmlAnchor](".trauk a")

    val page3 = resultLink.click[HtmlPage]()

    val district = page3.querySelector[HtmlAnchor](".turinys a")
    district.asText()
  }
}