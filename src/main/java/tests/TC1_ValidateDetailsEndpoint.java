package tests;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

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
public class TC1_ValidateDetailsEndpoint extends BaseTest{
	
	private final String EXPCTED_NAME = "Carbon credits";
	private final boolean EXPCTED_CANRELIST = true;	
	private final String EXPECTED_PROMOTION_DESCRIPTION = "Good position in category";		
	private final String EXPECTED_PROMOTION_NAME = "Gallery";
	
	private String responseBody;
	private URI apiURL;
	
	@BeforeTest
	@Parameters({"baseURL"})	
	public void initTest(String baseurlParm) throws URISyntaxException {
		bindBaseURL(baseurlParm);
		apiURL = baseURL.resolve(APIEndPoints.DETAILS);
		log("Endpoint : " + apiURL.toString());
	}

	@Test
	public void validateDetailsEndpointWhenCatalogueFalse() {

		Response response = given()
								.queryParam("catalogue", "false")
								.when()
								.get(apiURL);
		
		responseBody = response.body().asPrettyString();
		assertEquals(response.statusCode(), 200,"Assert status code is 200 -> ");
		log("Status Code: " + response.statusCode());
		
		// Acceptance Criteria 1  : Verifying Name = "Carbon credits"
		JsonPath jsonPathEvaluator = response.jsonPath();
		String actualName = jsonPathEvaluator.get("Name");		
		assertEquals(actualName, EXPCTED_NAME, "Acceptance Criteria 1 Fail: Assert \"Name\" is "+ EXPCTED_NAME +" -> ");
		log("Acceptance Criteria 1: Validated NAME = " + actualName);
		
		// Acceptance Criteria 2  : CanRelist = true
		boolean actualCanRelist = jsonPathEvaluator.get("CanRelist");
		assertEquals(actualCanRelist, EXPCTED_CANRELIST,"Acceptance Criteria 2 Fail: Assert \"CanRelist\" is "+ EXPCTED_CANRELIST +" -> ");
		log("Acceptance Criteria 2: Validated CanRelist = " + actualCanRelist);
				
		// Acceptance Criteria 3  : The Promotions element with Name = "Gallery" has a Description that contains the text "Good position in category"
		boolean isGalleryContainsGoodPosition = false;
		
		int promotionsCount = jsonPathEvaluator.getInt("Promotions.size()");
		assertEquals(promotionsCount > 0, true, "Assert Number of Promotions > 0");

		for (int i = 0; i < promotionsCount; i++) {
			String actualPromotionName = jsonPathEvaluator.getString("Promotions[" + i + "].Name");
			if (actualPromotionName.equals(EXPECTED_PROMOTION_NAME)) {
				log("Promotions.Name : " + actualPromotionName);

				String actualPromotionDescription = jsonPathEvaluator.getString("Promotions[" + i + "].Description");
				log("Promotions.Description : " + actualPromotionDescription);
				if (actualPromotionDescription.contains(EXPECTED_PROMOTION_DESCRIPTION)) {
					isGalleryContainsGoodPosition = true;
					break;
				}
			}
		}
		assertEquals(isGalleryContainsGoodPosition, true,
				"Acceptance Criteria 3 Fail: Assert that Promotions element with Name=\"" + EXPECTED_PROMOTION_NAME
						+ "\" has a Description that contains the text \"" + EXPECTED_PROMOTION_DESCRIPTION + "\" -> ");
		log("Acceptance Criteria 3: Validated that Promotions element with Name=\"" + EXPECTED_PROMOTION_NAME
				+ "\" has a Description that contains the text \"" + EXPECTED_PROMOTION_DESCRIPTION);
	}
	
	@AfterTest
	void teadDownTest(){
		log("### Response Body ###   \n \n " + responseBody + "\n");
	}
}
