package com.searchclient.clientwrapper.domain.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.searchclient.clientwrapper.domain.error.CustomException;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class MicroserviceHttpGatewayTest {

	private final Logger log = LoggerFactory.getLogger(MicroserviceHttpGatewayTest.class);

	@Mock
	CloseableHttpClient client = HttpClientBuilder.create().build();

	@InjectMocks
	MicroserviceHttpGateway microserviceHttpGateway;

	String apiEndPoint = "demo";

	String jwtToken = "Bearer sadasdasdasdasd";

	String requestBodyDTO = "string";

	String response1 = "OK";

//	@MockBean
	HttpPost httpPost;

	@BeforeEach
	void setUp() {
		microserviceHttpGateway.setApiEndpoint(apiEndPoint);
		microserviceHttpGateway.setRequestBodyDTO(requestBodyDTO);

	}

	public void setMockitoSucccessResponseForService() throws ClientProtocolException, IOException {

		CloseableHttpClient mockHttpClient = Mockito.mock(CloseableHttpClient.class);
		HttpPost mockHttpPost = Mockito.mock(HttpPost.class);
		// Mockito.when(client.execute(mockHttpPost)).thenReturn((CloseableHttpResponse)
		// ResponseEntity.status(HttpStatus.OK));
	}

	@Test
	void postRequestWithStringBodyTets() throws ClientProtocolException, IOException {

		try {

			microserviceHttpGateway.postRequestWithStringBody(jwtToken);

		} catch (CustomException e) {
			assertEquals(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode() , e.getExceptionCode());
		}
		// assertEquals(response1, response.toString());
	}

	@Test
	void postRequestWithStringBody() throws ClientProtocolException, IOException {

		try {

			microserviceHttpGateway.postRequestWithStringBody(jwtToken);

		} catch (CustomException e) {
			assertEquals(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode() , e.getExceptionCode());
		}
	}

	@Test
	void postRequest() {

		try {

			microserviceHttpGateway.postRequest(jwtToken);

		} catch (CustomException e) {
			assertEquals(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode() , e.getExceptionCode());
		}
	}

	@Test
	void putRequest() throws ClientProtocolException, IOException {

		try {

			microserviceHttpGateway.putRequest(jwtToken);

		} catch (CustomException e) {
			assertEquals(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode() , e.getExceptionCode());
		}
	}

	@Test
	void deleteRequest() {

		try {

			microserviceHttpGateway.deleteRequest(jwtToken);

		} catch (CustomException e) {
			assertEquals(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode() , e.getExceptionCode());
		}
	}

	@Test
	void getRequest() throws ClientProtocolException, IOException {

		try {

			microserviceHttpGateway.getRequest(jwtToken);

		} catch (CustomException e) {
			assertEquals(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode() , e.getExceptionCode());
		}

	}

	@Test
	void getRequestV2() throws ClientProtocolException, IOException {

		try {

			microserviceHttpGateway.getRequestV2(jwtToken);

		} catch (CustomException e) {
			assertEquals(400, e.getExceptionCode());
		}
	}
}
