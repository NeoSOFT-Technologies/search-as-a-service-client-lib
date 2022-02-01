package com.solr.clientwrapper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.CapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.GetCapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.GetCollectionsResponseDTO;
import com.searchclient.clientwrapper.domain.port.api.SolrCollectionServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;
import com.searchclient.clientwrapper.service.SolrCollectionService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class SolrCollectionServiceTest {

	Logger logger = LoggerFactory.getLogger(SolrCollectionServiceTest.class);

	String collectionName = "Demo";

	List<String> collections = new ArrayList<String>();
	@InjectMocks
	SolrCollectionService solrCollectionService;
	@MockBean
	private GetCapacityPlanDTO capacityPlanDto;
	@MockBean
	MicroserviceHttpGateway microserviceHttpGateway;

	@MockBean
	SolrCollectionServicePort solrCollectionServicePort;
	List<CapacityPlanDTO> capacityPlans;
	JSONObject jsonObject;

	JSONObject jsonObject2;

	CapacityPlanDTO plan = new CapacityPlanDTO();

	@BeforeEach
	public void init() {
		capacityPlans = new ArrayList<CapacityPlanDTO>();

		plan.setName("s1");
		plan.setSku("s1");
		capacityPlans.add(plan);

		collections.add("Demo");
		collections.add("Demo2");

		Mockito.when(solrCollectionServicePort.capacityPlans()).thenReturn(capacityPlanDto);
		Mockito.when(capacityPlanDto.getPlans()).thenReturn(capacityPlans);

	}

	public void setMockitoSuccessResponseForService() {
		ResponseDTO solrResponseDTO = new ResponseDTO(collectionName);
		solrResponseDTO.setStatusCode(200);
		solrResponseDTO.setMessage("Testing");

		jsonObject = new JSONObject();
		jsonObject.put("message", "success");
		jsonObject.put("statusCode", 200);

		GetCollectionsResponseDTO solrGetCollectionsResponseDTO = new GetCollectionsResponseDTO();
		solrGetCollectionsResponseDTO.setStatusCode(200);
		solrGetCollectionsResponseDTO.setMessage("Testing");
		solrGetCollectionsResponseDTO.setCollections(collections);

		jsonObject2 = new JSONObject(solrGetCollectionsResponseDTO);

		Mockito.when(microserviceHttpGateway.postRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.deleteRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject2);
		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject);

	}

	public void setMockitoBadResponseForService() {
		ResponseDTO solrResponseDTO = new ResponseDTO(collectionName);
		solrResponseDTO.setStatusCode(400);
		solrResponseDTO.setMessage("Testing");

		jsonObject = new JSONObject();
		jsonObject.put("message", "failure");
		jsonObject.put("statusCode", 400);

		GetCollectionsResponseDTO solrGetCollectionsResponseDTO = new GetCollectionsResponseDTO();
		solrGetCollectionsResponseDTO.setStatusCode(400);
		solrGetCollectionsResponseDTO.setMessage("Testing");
		solrGetCollectionsResponseDTO.setCollections(collections);

		jsonObject2 = new JSONObject(solrGetCollectionsResponseDTO);
		Mockito.when(microserviceHttpGateway.postRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.deleteRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject2);
		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject);
	}

	@Test
	void testCapacityPlans() {

		GetCapacityPlanDTO solrgetCapacityPlanDTO = new GetCapacityPlanDTO();
		solrgetCapacityPlanDTO.setPlans(capacityPlans);
		JSONObject jsonObject3 = new JSONObject(solrgetCapacityPlanDTO);
		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject3);

		// VALID CAPACITY PLAN
		GetCapacityPlanDTO solrCapacityPlanDTO = solrCollectionService.capacityPlans();
		assertEquals(plan.getName(), solrCapacityPlanDTO.getPlans().get(0).getName());

		// INVALID CAPACITY PLAN
		String planName = "C1";
		GetCapacityPlanDTO solrGetCapacityPlanDTO2 = solrCollectionService.capacityPlans();
		assertNotEquals(planName, solrGetCapacityPlanDTO2.getPlans().get(0).getName());

	}

	@Test
	void testCreateSolrCollection() {
		int statusCode = 200;

		// CREATE COLLECTION

		setMockitoSuccessResponseForService();
		ResponseDTO solrresponseDto = solrCollectionService.create(collectionName, "s1");
		assertEquals(statusCode, solrresponseDto.getStatusCode());

		// CREATE COLLECTION WITH SAME NAME
		setMockitoBadResponseForService();
		ResponseDTO solrresponseDto2 = solrCollectionService.create(collectionName, "S1");
		assertNotEquals(statusCode, solrresponseDto2.getStatusCode());
	}

	//@Test
	void testDeleteSolrCollection() {
		int statusCode = 200;
		// DELETE COLLECTION
		setMockitoSuccessResponseForService();
		ResponseDTO solrresponseDto = solrCollectionService.delete(collectionName);
		assertEquals(statusCode, solrresponseDto.getStatusCode());

		// DELETE NOT EXISTING COLLECTION
		setMockitoBadResponseForService();
		ResponseDTO solrresponseDto2 = solrCollectionService.delete(collectionName);
		assertNotEquals(statusCode, solrresponseDto2.getStatusCode());
	}

	@Test
	void testGetSolrCollections() {
		int statusCode = 200;
		setMockitoSuccessResponseForService();
		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject2);
		GetCollectionsResponseDTO solrresponseDto = solrCollectionService.getCollections();
		assertEquals(statusCode, solrresponseDto.getStatusCode());

		setMockitoBadResponseForService();
		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject2);
		GetCollectionsResponseDTO solrresponseDto2 = solrCollectionService.getCollections();
		assertNotEquals(statusCode, solrresponseDto2.getStatusCode());
	}

	@Test
	void testIsCollectionExists() {
		int statusCode = 200;

		setMockitoSuccessResponseForService();
		ResponseDTO solrresponseDto = solrCollectionService.isCollectionExists(collectionName);
		assertEquals(statusCode, solrresponseDto.getStatusCode());

		setMockitoBadResponseForService();
		ResponseDTO solrresponseDto2 = solrCollectionService.isCollectionExists(collectionName);
		assertNotEquals(statusCode, solrresponseDto2.getStatusCode());

	}
}
