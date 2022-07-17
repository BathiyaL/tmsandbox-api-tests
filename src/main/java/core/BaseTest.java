package core;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import constants.APIEndPoints;
import constants.ProgramConstants;

/*
 * @Author : Bathiya L
 * All tests extends BaseTest class.
 * Functionalities that common to all tests classes can implement here
 */
public class BaseTest {
	
	protected URI baseURL;
	private Logger logger = null;
	
	public void log(String logMessage) {
		logger = LogManager.getLogger("Log");
		logger.info(logMessage);
	}
	
	/*
	 * @Author : Bathiya L
	 * @Paremeter : baseurlParameter
	 * If base url empty, Null, or not passed from xml Test Plan, this will use the defined default URL
	 */
	protected void bindBaseURL(String baseurlParameter) throws URISyntaxException {		
		if(baseurlParameter==null || baseurlParameter.isEmpty() || baseurlParameter.contains(ProgramConstants.TESTNG_PARM_VALUE_NOT_FOUND_MSG)) {
			baseURL = new URI(APIEndPoints.DEFAULT_BASE_URL);
			log("baseURL parameter values is null, empty or not passed from a Test Plan hence bind to the default base url");
		} else {
			baseURL = new URI(baseurlParameter);
		}
		log("Base URL : " + baseURL);
	}

}
