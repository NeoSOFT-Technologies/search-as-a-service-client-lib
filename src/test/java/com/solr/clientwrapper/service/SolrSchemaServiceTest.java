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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.searchclient.clientwrapper.domain.dto.solr.SolrFieldDTO;
import com.searchclient.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.searchclient.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;
import com.searchclient.clientwrapper.infrastructure.Enum.SolrFieldType;
import com.searchclient.clientwrapper.service.SolrSchemaService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class SolrSchemaServiceTest {

	Logger logger = LoggerFactory.getLogger(SolrSchemaServiceTest.class);

	String tableName = "gettingstarted1";
	String name = "default-config";
	SolrFieldDTO solr = new SolrFieldDTO("testField6", SolrFieldType._nest_path_, "mydefault", true, true, false, true,
			true);
	List<SolrFieldDTO> attributes = new ArrayList<SolrFieldDTO>();


	@MockBean
	SolrSchemaResponseDTO solrResponseDTO;
	
	SolrSchemaDTO solrSchemaDto;

	@MockBean
	MicroserviceHttpGateway microserviceHttpGateway;

	@InjectMocks
	SolrSchemaService solrSchemaService;

	JSONObject jsonObject;

	@BeforeEach
	void setUp() {
		attributes.add(solr);
		solrSchemaDto = new SolrSchemaDTO(tableName, name, attributes);
	}

	public void setMockitoSucccessResponseForService() {
		solrResponseDTO = new SolrSchemaResponseDTO(tableName, name, attributes);
		solrResponseDTO.setStatusCode(200);

		jsonObject = new JSONObject(solrResponseDTO);

		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.postRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.putRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.deleteRequest()).thenReturn(jsonObject);

	}

	public void setMockitoBadResponseForService() {
		SolrSchemaResponseDTO solrResponseDTO = new SolrSchemaResponseDTO(tableName, name, attributes);
		solrResponseDTO.setStatusCode(400);
		jsonObject = new JSONObject(solrResponseDTO);
		Mockito.when(microserviceHttpGateway.getRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.postRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.putRequest()).thenReturn(jsonObject);
		Mockito.when(microserviceHttpGateway.deleteRequest()).thenReturn(jsonObject);

	}

	@Test
	void testGetSchema() {

		// GET EXISTING SCHEMA
		setMockitoSucccessResponseForService();
		SolrSchemaResponseDTO receivedsolrSchemaResponseDto = solrSchemaService.get("Demo");
		assertEquals(solrResponseDTO.getStatusCode(), receivedsolrSchemaResponseDto.getStatusCode());

		// GET NON EXISTING SCHEMA
		setMockitoBadResponseForService();
		receivedsolrSchemaResponseDto = solrSchemaService.get("DEMO2");
		assertNotEquals(solrResponseDTO.getStatusCode(), receivedsolrSchemaResponseDto.getStatusCode());
	}

	@Test
	void testCreateSchema() {

		logger.info("Create Solr Schema service test is started....");

		// CREATE SCHEMA
		setMockitoSucccessResponseForService();
		SolrSchemaResponseDTO receivedResponse = solrSchemaService.create(tableName, solrSchemaDto);
		assertEquals(solrResponseDTO.getStatusCode(), receivedResponse.getStatusCode());

		// CREATE SCHEMA WITH SAME NAME
		setMockitoBadResponseForService();
		SolrSchemaResponseDTO receivedResponse2 = solrSchemaService.create(tableName, solrSchemaDto);
		assertNotEquals(solrResponseDTO.getStatusCode(), receivedResponse2.getStatusCode());

	}

	@Test
	void testDeleteSchema() {

		// DELETE A NON EXISTING SCHEMA
		setMockitoBadResponseForService();
		SolrSchemaResponseDTO receivedResponse2 = solrSchemaService.delete(tableName);
		assertNotEquals(solrResponseDTO.getStatusCode(), receivedResponse2.getStatusCode());

		// DELETE A SCHEMA
		setMockitoSucccessResponseForService();
		SolrSchemaResponseDTO receivedResponse = solrSchemaService.delete(tableName);
		assertEquals(solrResponseDTO.getStatusCode(), receivedResponse.getStatusCode());
	}

	@Test
	void testUpdateSchema() {

		// Update Schema
		setMockitoSucccessResponseForService();
		SolrSchemaResponseDTO receivedResponse2 = solrSchemaService.update(tableName, solrSchemaDto);
		assertEquals(solrResponseDTO.getStatusCode(), receivedResponse2.getStatusCode());

		// Update Not Existing Schema
		setMockitoBadResponseForService();
		SolrSchemaResponseDTO receivedResponseDTO = solrSchemaService.update("DEMO2", solrSchemaDto);
		assertNotEquals(solrResponseDTO.getStatusCode(), receivedResponseDTO.getStatusCode());
	}

}
