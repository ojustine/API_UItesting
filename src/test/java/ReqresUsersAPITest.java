import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.*;
import static org.testng.Assert.*;

public class ReqresUsersAPITest extends ReqresBasicAPITest
{
	final String userDataPath = "data";

	@BeforeClass
	public void setBasePath()
	{
		RestAssured.basePath = "api/users";
	}

	@Test
	public void canGetOneUserTest()
	{
		Response response = getResponse("/1");
		response.then().assertThat().statusCode(200);

		User user = from(response.asString()).getObject(userDataPath, User.class);
		assertFalse(user == null || user.haveNullField());
	}

	@Test
	public void canGetUsersFromOnePageTest()
	{
		Response response = getResponse();
		response.then().assertThat().statusCode(200);

		List<User> usersList = from(response.asString()).getList(userDataPath, User.class);
		for (User user : usersList)
			assertFalse(user == null || user.haveNullField());
	}

	@Test
	public void canGetAllUsersTest()
	{
		Response response = getResponse();
		response.then().assertThat().statusCode(200);

		int totalPages = from(response.asString()).getInt("total_pages");

		for (int i = 1; i <= totalPages; i++)
		{
			List<User> usersList = from(getResponse("?page=" + i)
									.asString())
									.getList(userDataPath, User.class);

			for (User user : usersList)
				assertFalse(user == null || user.haveNullField());
		}
	}

	@Test
	public void userNotFoundTest()
	{
		Response response = getResponse();
		response.then().assertThat().statusCode(200);

		int countUsers = from(response.asString()).getInt("total");

		for (int i = 1; i < 10; i++)
			getResponse("/" + countUsers + i).then().assertThat().statusCode(404);
	}

	@Test
	public void canCreateUserTest()
	{
		String payload = "{\n  \"name\": \"morpheus\",\n  \"job\": \"leader\",\n}";

		given()
				.body(payload)
				.post()
				.prettyPeek()
				.then()
				.assertThat()
				.statusCode(201);

		given()
				.body(new LinkedHashMap<>(Map.of("name","morpheus", "job", "leader")))
				.post()
				.prettyPeek()
				.then()
				.assertThat()
				.statusCode(201);
	}

	@Test
	public void canUpdateUserTest()
	{
		String payload = "{\n  \"name\": \"morpheus\",\n  \"job\": \"zion resident\",\n}";

		given()
				.body(payload)
				.put()
				.prettyPeek()
				.then()
				.assertThat()
				.statusCode(200);

		given()
				.body(payload)
				.patch()
				.prettyPeek()
				.then()
				.assertThat()
				.statusCode(200);

		given()
				.body(new LinkedHashMap<>(Map.of("name","morpheus", "job", "leader")))
				.put()
				.prettyPeek()
				.then()
				.assertThat()
				.statusCode(200);
	}

	@Test
	public void canDeleteUser()
	{
		int countUsers = from(getResponse().asString()).getInt("total");

		given()
				.delete("/" + (int) (Math.random() * countUsers))
				.then()
				.assertThat()
				.statusCode(204);
	}

	static class User
	{
		String id;
		String email;
		String first_name;
		String last_name;
		String avatar;

		public User() {
		}

		public User(String id, String email, String first_name, String last_name, String avatar)
		{
			this.id = id;
			this.email = email;
			this.first_name = first_name;
			this.last_name = last_name;
			this.avatar = avatar;
		}

		public boolean haveNullField()
		{
			return (id == null || email == null || first_name == null || last_name == null || avatar == null);
		}

		public String getId() {
			return id;
		}

		public String getEmail() {
			return email;
		}

		public String getFirst_name() {
			return first_name;
		}

		public String getLast_name() {
			return last_name;
		}

		public String getAvatar() {
			return avatar;
		}
	}
}
