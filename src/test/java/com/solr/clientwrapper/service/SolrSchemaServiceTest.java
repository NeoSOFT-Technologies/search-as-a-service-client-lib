package com.solr.clientwrapper.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.solr.clientwrapper.IntegrationTest;
import com.solr.clientwrapper.domain.dto.solr.SolrFieldDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrSchemaServicePort;
import com.solr.clientwrapper.domain.service.SolrSchemaService;
import com.solr.clientwrapper.infrastructure.Enum.SolrFieldType;



@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class SolrSchemaServiceTest {

	Logger logger = LoggerFactory.getLogger(SolrSchemaServiceTest.class);
	
	String tableName = "gettingstarted1";
	String name = "default-config";
	SolrFieldDTO solr = new SolrFieldDTO("testField6", SolrFieldType._nest_path_, "mydefault", true, true, false, true, true);
	List<SolrFieldDTO> attributes = new ArrayList<SolrFieldDTO>();
	String expectedGetResponse = "{\n"
			  +"\"tableName\": \"gettingstarted1\",\n"
			  +"\"name\": \"default-config\",\n"
			  +"\"attributes\": [{\n"
		      +"\"name\": \"testField6\",\n"
		      +"\"type\": \"_nest_path_\",\n"
		      +"\"default_\": \"mydefault\",\n"
		      +"\"storable\": false,\n"
		      +"\"filterable\": true,"
		      +"\"required\": true,"
		      +"\"sortable\": true,\n"
		      +"\"multiValue\": true,\n"
		      +"}],\n"
		      +"\"statusCode\": 200\n"
		      +"}";
	String expectedCreateResponse400 = "{\n"
			  +"\"tableName\": \"gettingstarted1\",\n"
			  +"\"name\": \"default-config\",\n"
			  +"\"attributes\": [{\n"
		      +"\"name\": \"testField6\",\n"
		      +"\"type\": \"_nest_path_\",\n"
		      +"\"default_\": \"mydefault\",\n"
		      +"\"storable\": false,\n"
		      +"\"filterable\": true,"
		      +"\"required\": true,"
		      +"\"sortable\": true,\n"
		      +"\"multiValue\": true,\n"
		      +"}],\n"
		      +"\"statusCode\": 400\n"
		      +"}";
	
	
	@MockBean
	SolrSchemaResponseDTO solrResponseDTO;
	@MockBean
	SolrSchemaDTO solrSchemaDto;
	
	@MockBean
	SolrSchemaServicePort solrschemaServicePort;
	
	@MockBean
	SolrSchemaService solrSchemaService;
	
	

	
	@BeforeEach
	void setUp() {
		attributes.add(solr);
		solrSchemaDto = new SolrSchemaDTO(tableName, name, attributes);
	}
	
	
	public void setMockitoSucccessResponseForService() {
		solrResponseDTO = new SolrSchemaResponseDTO(tableName, name, attributes);
		solrResponseDTO.setStatusCode(200);
		
		Mockito.when(solrschemaServicePort.create(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrschemaServicePort.delete(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrschemaServicePort.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrschemaServicePort.get(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		
		Mockito.when(solrSchemaService.create(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrSchemaService.delete(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrSchemaService.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrSchemaService.get(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
	}

	public void setMockitoBadResponseForService() {
		SolrSchemaResponseDTO solrResponseDTO = new SolrSchemaResponseDTO(tableName, name, attributes);
		solrResponseDTO.setStatusCode(400);
		Mockito.when(solrSchemaService.create(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrSchemaService.delete(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrSchemaService.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrSchemaService.get(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		
		Mockito.when(solrschemaServicePort.create(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrschemaServicePort.delete(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrschemaServicePort.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
		Mockito.when(solrschemaServicePort.get(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
	}
	
	
	@Test
	void testGetSchema() {
		
		// GET EXISTING SCHEMA
		SolrSchemaResponseDTO receivedsolrSchemaResponseDto;
		setMockitoSucccessResponseForService();
		receivedsolrSchemaResponseDto = solrschemaServicePort.get("Demo", "DemoSchema");
		assertEquals(solrResponseDTO.getStatusCode(), receivedsolrSchemaResponseDto.getStatusCode());
		
		//GET NON EXISTING SCHEMA
		setMockitoBadResponseForService();
		receivedsolrSchemaResponseDto = solrschemaServicePort.get("DEMO2", "name");
		assertNotEquals(solrResponseDTO.getStatusCode(), receivedsolrSchemaResponseDto.getStatusCode());
	}
	
	@Test
	void testCreateSchema() {
		
	  logger.info("Create Solr Schema service test is started....");
	  
	  
	  //CREATE SCHEMA
	  setMockitoSucccessResponseForService();
	  SolrSchemaResponseDTO receivedResponse  = solrschemaServicePort.create(tableName, name, solrSchemaDto);
	  assertEquals(solrResponseDTO.getStatusCode(),receivedResponse.getStatusCode());
	
	  //CREATE SCHEMA WITH SAME NAME
	  setMockitoBadResponseForService();
	  SolrSchemaResponseDTO receivedResponse2  = solrschemaServicePort.create(tableName, name, solrSchemaDto);
	  assertNotEquals(solrResponseDTO.getStatusCode(), receivedResponse2.getStatusCode());
	
	}
	
	
	@Test
	void testDeleteSchema() {
		
		// DELETE A NON EXISTING SCHEMA
		  setMockitoBadResponseForService();
		  SolrSchemaResponseDTO receivedResponse2  = solrschemaServicePort.delete(tableName, name);
		  assertNotEquals(solrResponseDTO.getStatusCode(), receivedResponse2.getStatusCode());
	
		//DELETE A SCHEMA
		  setMockitoSucccessResponseForService();
		  SolrSchemaResponseDTO receivedResponse  = solrschemaServicePort.delete(tableName, name);
		  assertEquals(solrResponseDTO.getStatusCode(),receivedResponse.getStatusCode());
	}
	
	@Test
	void testUpdateSchema(){
		
		//Update Schema
		 setMockitoSucccessResponseForService();
		 SolrSchemaResponseDTO receivedResponse2 = solrschemaServicePort.update(tableName, name, solrSchemaDto);
		 assertEquals(solrResponseDTO.getStatusCode(), receivedResponse2.getStatusCode());
	 }
	
}
