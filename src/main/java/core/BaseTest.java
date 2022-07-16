package core;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import constants.APIEndPoints;

public class BaseTest {
	
	protected URI baseURL;
	private Logger logger = null;
	
	public void log(String logMessage) {
		logger = LogManager.getLogger("Log");
		logger.info(logMessage);
	}
	
	protected void bindBaseURL(String baseurlParm) throws URISyntaxException {		
		if(baseurlParm.isEmpty() || baseurlParm.contains("param-val-not-found")) {
			baseURL = new URI(APIEndPoints.DEFAULT_BASE_URL);
			log("baseURL parameter values is null hence bind to the default base url");
		} else {
			baseURL = new URI(baseurlParm);		
		}		
		log("Base URL : " + baseURL);
	}

}
