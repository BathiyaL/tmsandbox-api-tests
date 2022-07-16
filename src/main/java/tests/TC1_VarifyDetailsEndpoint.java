package tests;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import constants.APIEndPoints;
import core.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/*
 * @Author : Bathiya L
 * Test Scenario : 
 * validate response of  "v1/Categories/6327/Details.json" with below Acceptance Criteria:
 * Name = "Carbon credits"
 * CanRelist = true
 * The Promotions element with Name = "Gallery" has a Description that contains the text "Good position in category"
 */
public class TC1_VarifyDetailsEndpoint extends BaseTest{
	
	final String EXPCTED_NAME = "Carbon credits";
	final boolean EXPCTED_CANRELIST = true;	
	String responseBody = "NA";
	URI apiURL;
	
	@Parameters({ "baseURL" })
	@BeforeTest
	public void initTest(String baseurlParm) throws URISyntaxException {
		bindBaseURL(baseurlParm);
		apiURL = baseURL.resolve(APIEndPoints.DETAILS);
		log("Endpoint : " + apiURL.toString());
	}

	@Test
	public void VarifyDetailsEndpointWhenCatalogueFalse() {

		Response response = given()
								.queryParam("catalogue", "false")
								.when()
								.get(apiURL);
		
		responseBody = response.body().asPrettyString();
		Assert.assertEquals(response.statusCode(), 200,"Assert status code is 200 -> ");
		log("Status Code: " + response.statusCode());
				
		JsonPath jsonPathEvaluator = response.jsonPath();
		String actualName = jsonPathEvaluator.get("Name");		
		Assert.assertEquals(actualName, EXPCTED_NAME, "Assert \"Name\" is "+ EXPCTED_NAME +" -> ");
		log("Verified NAME = " + actualName);
		
		boolean actualCanRelist = jsonPathEvaluator.get("CanRelist");
		assertEquals(actualCanRelist, EXPCTED_CANRELIST,"Assert \"CanRelist\" is "+ EXPCTED_CANRELIST +" -> ");
		log("Verified CanRelis = " + actualCanRelist);
		
		int promotionsCount = jsonPathEvaluator.getInt("Promotions.size()");		
		String expectedPromotionName = "Gallery";

		boolean isGalleryContainsGoodPosition = false;
		String expectdPromotionDescription = "Good position in category";

		if(promotionsCount > 0) {
			for(int i=0;i<promotionsCount;i++)
			{		
			    String actualPromotionName = jsonPathEvaluator.getString("Promotions["+i+"].Name");
			    if(actualPromotionName.equals(expectedPromotionName))
			    {
			    	log("Promotions.Name : " + actualPromotionName);
			    	
			    	String actualPromotionDescription = jsonPathEvaluator.getString("Promotions["+i+"].Description");
			        log("Promotions.Description : " + actualPromotionDescription);
			        if(actualPromotionDescription.contains(expectdPromotionDescription)) {
			        	isGalleryContainsGoodPosition = true;
			        	break;
			        }
			    }
			}
			assertEquals(isGalleryContainsGoodPosition, true,"Assert that Promotions element with Name=\"" +expectedPromotionName+ "\" has a Description that contains the text \""+expectdPromotionDescription+"\" -> ");
			log("Verified that Promotions element with Name=\"" +expectedPromotionName+ "\" has a Description that contains the text \""+expectdPromotionDescription);
		} else {			
			assertEquals(promotionsCount > 0, true,"Assert Number of Promotions > 0");			
		}		 
	}
	
	@AfterTest
	void teadDownTest(){
		log("### Response Body ###   \n \n " + responseBody + "\n");
	}
}
