package com.solr.clientwrapper.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.solr.clientwrapper.domain.port.api.DataIngectionServicePort;
import com.solr.clientwrapper.domain.service.DataIngectionService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class DataIngectionServiceTest {

	Logger logger = LoggerFactory.getLogger(DataIngectionServiceTest.class);
	
	String data = "\r\n" + "{\r\n" + "\"books\" :[\r\n" + "  {\r\n" + "    \"id\" : 1,\r\n"
			+ "    \"color\" : \"The Lightning Thief\",\r\n" + "    \"author\" : \"Rick Riordan\",\r\n"
			+ "    \"price\" : 123\r\n" + "  }\r\n" + "]\r\n" + "}";

	String batchdata = "{\r\n" + "\"batch\":\r\n" + "[\r\n" + "  {\r\n" + "        \"books\" :[\r\n" + "  {\r\n"
			+ "    \"id\" : 1,\r\n" + "    \"color\" : \"The Lightning Thief\",\r\n"
			+ "    \"author\" : \"Rick Riordan\",\r\n" + "    \"price\" : 123\r\n" + "  },\r\n" + "  {\r\n"
			+ "    \"id\" : 2,\r\n" + "    \"color\" : \"The Lightning Thief\",\r\n"
			+ "    \"author\" : \"Rick Riordan\",\r\n" + "    \"price\" : 123\r\n" + "  }\r\n" + "],\r\n"
			+ "\"movie\" :[\r\n" + "  {\r\n" + "    \"id\" : 1,\r\n" + "    \"movie\" : \"The Lightning Thief\",\r\n"
			+ "    \"actor\" : \"Rick Riordan\",\r\n" + "    \"price\" : 123\r\n" + "  },\r\n" + "  {\r\n"
			+ "    \"id\" : 2,\r\n" + "     \"movie\" : \"The Thief\",\r\n" + "    \"actor\" : \"Rick\",\r\n"
			+ "    \"price\" : 623\r\n" + "  }\r\n" + "],\r\n" + "\"hero\" :[\r\n" + "  {\r\n" + "    \"id\" : 1,\r\n"
			+ "    \"movie\" : \"The Lightning Thief\",\r\n" + "    \"actor\" : \"Rick Riordan\",\r\n"
			+ "    \"price\" : 123\r\n" + "  },\r\n" + "  {\r\n" + "    \"id\" : 2,\r\n"
			+ "     \"movie\" : \"The Thief\",\r\n" + "    \"actor\" : \"Rick\",\r\n" + "    \"price\" : 623\r\n"
			+ "  }\r\n" + "]\r\n" + "  }\r\n" + "]\r\n" + "}";

	

	String expected = "[{\"color\":\"The Lightning Thief\",\"author\":\"Rick Riordan\",\"price\":123,\"id\":1}]";
	String tableName = "Demo";
	@MockBean
	DataIngectionService dataIngectionService;
	
	@MockBean
	DataIngectionServicePort dataIngectionServicePort;
	
	public void setMockitoSucccessResponseForService() {
		
		
		Mockito.when(dataIngectionServicePort.parseSolrSchemaArray(Mockito.any(),Mockito.any())).thenReturn(expected);
		
		Mockito.when(dataIngectionService.parseSolrSchemaArray(Mockito.any(), Mockito.any())).thenReturn(expected);
//		Mockito.when(solrschemaServicePort.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		Mockito.when(solrschemaServicePort.get(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);

//		Mockito.when(solrSchemaService.delete(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		Mockito.when(solrSchemaService.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		Mockito.when(solrSchemaService.get(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
	}

//	public void setMockitoBadResponseForService() {
//		SolrSchemaResponseDTO solrResponseDTO = new SolrSchemaResponseDTO(tableName, name, attributes);
//		solrResponseDTO.setStatusCode(400);
//		Mockito.when(solrSchemaService.create(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		Mockito.when(solrSchemaService.delete(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		Mockito.when(solrSchemaService.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		Mockito.when(solrSchemaService.get(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		
//		Mockito.when(solrschemaServicePort.create(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		Mockito.when(solrschemaServicePort.delete(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		Mockito.when(solrschemaServicePort.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//		Mockito.when(solrschemaServicePort.get(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
//	}
	
	@Test
	void parseSolrSchemaArrayTest(){
		setMockitoSucccessResponseForService();
		String data1 = dataIngectionServicePort.parseSolrSchemaArray(tableName, data);
				JSONAssert.assertEquals(expected, data1, true);
	}
}
