package com.searchclient.clientwrapper.domain.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class MicroserviceHttpGatewayTest {
	
	private final Logger log = LoggerFactory.getLogger(MicroserviceHttpGatewayTest.class);

	@MockBean
	CloseableHttpClient client;
	
	@InjectMocks
	MicroserviceHttpGateway microserviceHttpGateway;
	
	String apiEndPoint = "demo";
	
	String requestBodyDTO = "string";
	
	String response1 = "OK";
	
	HttpPost httpPost;
	@BeforeEach
	void setUp() {
		microserviceHttpGateway.setApiEndpoint(apiEndPoint);
		microserviceHttpGateway.setRequestBodyDTO(requestBodyDTO);

	}
	
	public void setMockitoSucccessResponseForService() throws ClientProtocolException, IOException {
		Mockito.when(client.execute(httpPost)).thenReturn((CloseableHttpResponse) ResponseEntity.status(HttpStatus.OK));
	}
	
//	@Test
	void postRequestWithStringBodyTets() throws ClientProtocolException, IOException {
		
		setMockitoSucccessResponseForService();
		 String response = microserviceHttpGateway.postRequestWithStringBody();
		assertEquals(response1,response.toString());
	}
}
