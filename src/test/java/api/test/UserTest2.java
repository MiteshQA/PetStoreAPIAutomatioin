package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.endpoints.UserEndPoints2;
import api.payload.User;
import io.restassured.response.Response;
import jdk.internal.org.jline.utils.Log;

public class UserTest2 {

	Faker faker;
	User userPayload;
	
	public Logger logger;
	
	@BeforeClass
	public void setupData()
	{
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassowrd(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
		
		logger = LogManager.getLogger(this.getClass());
	}
	
	@Test(priority=1)
	public void testPostUser()
	{
		logger.info("************** Createing User Info ******************");
		Response response = UserEndPoints2.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("************** Created User Info ******************");
	}
	
	@Test(priority=2)
	public void testGetUserByName()
	{
		logger.info("************** Reading User Info *****************");
		Response response = UserEndPoints2.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("**************User Info is displayed ******************");
	}
	
	@Test(priority=3)
	public void testUpdateUser()
	{
		//Update user data
		logger.info("********************* Updateing User Info ****************");
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = UserEndPoints2.updateUser(this.userPayload.getUsername(),userPayload);
		response.then().log().body();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		//Checking data after update
		Response responseAfterupdate = UserEndPoints2.readUser(this.userPayload.getUsername());
				
		Assert.assertEquals(responseAfterupdate.getStatusCode(), 200);
		logger.info("************** Updated User Info ******************");
		
	}
	
	@Test(priority=4)
	public void testDeleteUserByName()
	{
		logger.info("**************** Deleteing User Info ********************");
		Response response = UserEndPoints2.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.statusCode(), 200);
		logger.info("************** Deleted User Info ******************");
	}
	
}
