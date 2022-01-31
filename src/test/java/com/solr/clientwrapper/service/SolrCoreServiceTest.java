package com.solr.clientwrapper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.beans.Transient;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.searchclient.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.searchclient.clientwrapper.domain.port.api.SolrCoreServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;
import com.searchclient.clientwrapper.service.SolrCoreService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class SolrCoreServiceTest {

	Logger logger = LoggerFactory.getLogger(SolrCoreServiceTest.class);

	String coreName = "DemoCore";
	String newCoreName = "newDemoCore";

	@InjectMocks
	SolrCoreService solrcoreservice;


	
	@MockBean
	MicroserviceHttpGateway microserviceHttpGateway;

	JSONObject jsonObject;

	public void setMockitoSuccessResponseForService() {
		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);
		solrResponseDTO.setStatusCode(200);
		solrResponseDTO.setMessage("Testing");

		jsonObject = new JSONObject(solrResponseDTO);

		String expectedResponse = "Success";
		
		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.deleteRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.postRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.putRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.stringRequest()).thenReturn(expectedResponse);
	}

	public void setMockitoBadResponseForService() {
		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);
		solrResponseDTO.setStatusCode(400);
		solrResponseDTO.setMessage("Testing");

		jsonObject = new JSONObject(solrResponseDTO);
		String expectedResponse = "Failure";

		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.deleteRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.postRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.putRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.stringRequest()).thenReturn(expectedResponse);
	}

	@Test
	void testCreateSolrCore() {
		int statusCode = 200;

		// Create Core
		setMockitoSuccessResponseForService();
		SolrResponseDTO solrresponseDto = solrcoreservice.create(coreName);
		assertEquals(statusCode, solrresponseDto.getStatusCode());

		// Create core with Existing CoreName

		setMockitoBadResponseForService();
		SolrResponseDTO solrResponseDTO = solrcoreservice.create(coreName);
		assertNotEquals(statusCode, solrResponseDTO.getStatusCode());
	}

	@Test
	void testDeleteSolrCore() {
		int statusCode = 200;

		// Delete Core
		setMockitoSuccessResponseForService();
		SolrResponseDTO solrresponseDto = solrcoreservice.delete(coreName);
		assertEquals(statusCode, solrresponseDto.getStatusCode());

		// delete core with NonExisting CoreName

		setMockitoBadResponseForService();
		SolrResponseDTO solrResponseDTO = solrcoreservice.delete(coreName);
		assertNotEquals(statusCode, solrResponseDTO.getStatusCode());
	}

	@Test
	void testRenameSolrCore() {
		int statusCode = 200;

		// Rename Core
		setMockitoSuccessResponseForService();
		SolrResponseDTO solrresponseDto = solrcoreservice.rename(coreName, newCoreName);
		assertEquals(statusCode, solrresponseDto.getStatusCode());

		// rename core with NonExisting CoreName
		setMockitoBadResponseForService();
		SolrResponseDTO solrResponseDTO = solrcoreservice.rename("oldCore", newCoreName);
		assertNotEquals(statusCode, solrResponseDTO.getStatusCode());
	}

	@Test
	void testSwapSolrCore() {
		int statusCode = 200;

		// Swap Two Core with Existing Core
		setMockitoSuccessResponseForService();
		SolrResponseDTO solrresponseDto = solrcoreservice.swap(coreName, newCoreName);
		assertEquals(statusCode, solrresponseDto.getStatusCode());

		// Swap core with NonExisting Core
		setMockitoBadResponseForService();
		SolrResponseDTO solrResponseDTO = solrcoreservice.swap("oldCore", newCoreName);
		assertNotEquals(statusCode, solrResponseDTO.getStatusCode());
	}

	@Test
	void testReloadSolrCore() {
		int statusCode = 200;

		// Reload Existing Core
		setMockitoSuccessResponseForService();
		SolrResponseDTO solrresponseDto = solrcoreservice.reload(coreName);
		assertEquals(statusCode, solrresponseDto.getStatusCode());

		// Reload core with NonExisting Core
		setMockitoBadResponseForService();
		SolrResponseDTO solrResponseDTO = solrcoreservice.reload("oldCore");
		assertNotEquals(statusCode, solrResponseDTO.getStatusCode());
	}

	@Test
	void testSolrCoreStatus() {
		String expectedResponse = "Success";

		// Get Status of Existing Core
		setMockitoSuccessResponseForService();
		String response = solrcoreservice.status(coreName);
		JSONAssert.assertEquals(expectedResponse, response, true);

		// Get Status of NonExisting Core
		setMockitoBadResponseForService();
		String expectedResponse2 = "Failure";
		String response2 = solrcoreservice.status(coreName);
		JSONAssert.assertEquals(expectedResponse2, response2, true);
	}
}
