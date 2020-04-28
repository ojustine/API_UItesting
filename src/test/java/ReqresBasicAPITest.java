import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ReqresBasicAPITest
{
	@BeforeClass
	public void setURI()
	{
		RestAssured.baseURI = "https://reqres.in";
	}

	@Test(description = "check availability API")
	void basicPingTest()
	{
		given()
				.log().all()
				.get()
				.then()
				.assertThat()
				.statusCode(200)
				.time(lessThan(5000L))
				.log().ifError();
	}

	Response getResponse()
	{
		return getResponse("");
	}

	Response getResponse(String additionalPath)
	{
		Response response = given().get(additionalPath);
		response.then()
				.assertThat()
				.contentType(ContentType.JSON)
				.log().ifError();
		return response;
	}
}
