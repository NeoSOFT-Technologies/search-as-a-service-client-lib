package com.searchclient.clientwrapper.domain.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchclient.clientwrapper.domain.error.JwtAuthenticationFailureException;
import com.searchclient.clientwrapper.domain.error.MicroserviceConnectionException;

import lombok.Data;

@Data
@Service
public class MicroserviceHttpGateway {

	private final Logger log = LoggerFactory.getLogger(MicroserviceHttpGateway.class);
    private static final String CONTENT_TYPE = "Content-type";
    private static final String ACCEPT = "Accept";
    private static final String AUTHORIZATION = "Authorization";
    private static final String APPLICATION_JSON = "application/json";
    private static final String RESPONSE = "RESPONSE: {}";
    private static final String API_ENDPOINT = "API Endpoint : {}";
	private String apiEndpoint;
	private Object requestBodyDTO;
	//@Autowired DocumentParserUtil documentParserUtil;

	public String postRequestWithStringBody(String jwtToken) {

		String jsonObject = null;
		log.debug("Post Request String Method Called in MicroserviceHttpGateway");
		log.debug("API Endpoint - {} ",apiEndpoint);
		log.debug("Request Body - {}",requestBodyDTO);
		isTokenNullOrValid(jwtToken);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost http = new HttpPost(apiEndpoint);

		try {

			String objJackson = requestBodyDTO.toString();

			StringEntity entity = new StringEntity(objJackson);

			

			http.setEntity(entity);
			http.setHeader(ACCEPT, APPLICATION_JSON );
			http.setHeader(CONTENT_TYPE, APPLICATION_JSON );
			http.setHeader(AUTHORIZATION, jwtToken);
			CloseableHttpResponse response = client.execute(http);
			HttpEntity entityResponse = response.getEntity();
			String result = EntityUtils.toString(entityResponse);

			log.debug(RESPONSE ,result);

			jsonObject = result;

			client.close();

		} catch (Exception e) {
			handleException(e);
			e.printStackTrace();
			log.error(e.toString());

		}

		return jsonObject;

	}

	public String postRequest(String jwtToken) {

		log.debug("Post Request Method Called in MicroserviceHttpGateway");
		log.debug(API_ENDPOINT ,apiEndpoint);
		log.debug("REQUEST BODY : {}", requestBodyDTO);
		isTokenNullOrValid(jwtToken);


		String result = "{}";

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost http = new HttpPost(apiEndpoint);

		ObjectMapper objectMapper = new ObjectMapper();
		

		try {

			String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyDTO);
	

			StringEntity entity = new StringEntity((String) objJackson);

			http.setEntity(entity);
			http.setHeader(ACCEPT, APPLICATION_JSON );
			http.setHeader(CONTENT_TYPE, APPLICATION_JSON );
			http.setHeader(AUTHORIZATION, jwtToken);
			log.debug("Sending POST request");

			CloseableHttpResponse response = client.execute(http);
			HttpEntity entityResponse = response.getEntity();
			 result = EntityUtils.toString(entityResponse);

			log.debug(RESPONSE , result);


			client.close();

		} catch (Exception e) {
			handleException(e);
			e.printStackTrace();
			log.error(e.toString());

		}

		return result;

	}

	public String putRequest(String jwtToken) {
		log.debug("Put Request Method Called in MicroserviceHttpGateway");
		log.debug(API_ENDPOINT, apiEndpoint);
		isTokenNullOrValid(jwtToken);
		String result = "{}";

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPut http = new HttpPut(apiEndpoint);

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyDTO);
			StringEntity entity = new StringEntity(objJackson);

			http.setEntity(entity);
			http.setHeader(ACCEPT, APPLICATION_JSON );
			http.setHeader(CONTENT_TYPE, APPLICATION_JSON );
			http.setHeader(AUTHORIZATION, jwtToken);
			log.debug("Sending DELETE request");

			CloseableHttpResponse response = client.execute(http);
			HttpEntity entityResponse = response.getEntity();
			 result = EntityUtils.toString(entityResponse);
			log.debug(RESPONSE ,result);


			client.close();

		} catch (Exception e) {
			handleException(e);
			e.printStackTrace();
			log.error(e.toString());

		}

		return result;

	}

	public String deleteRequest(String jwtToken) {
		log.debug("Delete Request Method Called in MicroserviceHttpGateway");
		log.debug(API_ENDPOINT, apiEndpoint);
		isTokenNullOrValid(jwtToken);
		String result = "{}";

		CloseableHttpClient client = HttpClients.createDefault();
		HttpDelete http = new HttpDelete(apiEndpoint);

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyDTO);
			StringEntity entity = new StringEntity(objJackson);

			http.setHeader(ACCEPT, APPLICATION_JSON );
			http.setHeader(CONTENT_TYPE, APPLICATION_JSON );
			http.setHeader(AUTHORIZATION, jwtToken);
			CloseableHttpResponse response = client.execute(http);
			HttpEntity entityResponse = response.getEntity();
			 result = EntityUtils.toString(entityResponse);
			log.debug(RESPONSE, result);


			client.close();

		} catch (Exception e) {
			handleException(e);
			e.printStackTrace();
			log.error(e.toString());

		}

		return result;


	}

	public JSONObject getRequest(String jwtToken) {

		JSONObject jsonObject = null;
		isTokenNullOrValid(jwtToken);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet http = new HttpGet(apiEndpoint);

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyDTO);
			StringEntity entity = new StringEntity(objJackson);

			http.setHeader(ACCEPT, APPLICATION_JSON );
			http.setHeader(CONTENT_TYPE, APPLICATION_JSON );
			http.setHeader(AUTHORIZATION, jwtToken);
			CloseableHttpResponse response = client.execute(http);
		
			HttpEntity entityResponse = response.getEntity();
			String result = EntityUtils.toString(entityResponse);
			log.debug(RESPONSE ,result);

			jsonObject = new JSONObject(result);

			client.close();

		} catch (Exception e) {
			handleException(e);
			e.printStackTrace();
			log.error(e.toString());

		}

		return jsonObject;

	}
	
	public String getRequestV2(String jwtToken) {

        String result = "{}";
        isTokenNullOrValid(jwtToken);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet http = new HttpGet(apiEndpoint);

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            String objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBodyDTO);
            StringEntity entity = new StringEntity(objJackson);

            http.setHeader(ACCEPT, APPLICATION_JSON );
            http.setHeader(CONTENT_TYPE, APPLICATION_JSON );
            http.setHeader(AUTHORIZATION, jwtToken);
            CloseableHttpResponse response = client.execute(http);
        
            HttpEntity entityResponse = response.getEntity();
             result = EntityUtils.toString(entityResponse);
            log.debug(RESPONSE ,result);


            client.close();

        } catch (Exception e) {
        	handleException(e);
            e.printStackTrace();
            log.error(e.toString());

        }

        return result;

    }

	private void handleException(Exception exception) {
		if(exception instanceof HttpHostConnectException) {
			throw new MicroserviceConnectionException(HttpStatus.SC_SERVICE_UNAVAILABLE,"Unable to connect Microservice");
		}
	}

	public void isTokenNullOrValid(String jwtToken) {
    	if((null == jwtToken || jwtToken.isEmpty() || jwtToken.isBlank() || !jwtToken.startsWith("Bearer "))?true:false)
    		throw new JwtAuthenticationFailureException(HttpStatus.SC_FORBIDDEN,"Provide valid token");
    }
}
