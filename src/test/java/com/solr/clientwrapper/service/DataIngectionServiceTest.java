package com.solr.clientwrapper.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	String expected2 = "[[{\"color\":\"The Lightning Thief\",\"author\":\"Rick Riordan\",\"price\":123,\"id\":1},{\"color\":\"The Lightning Thief\",\"author\":\"Rick Riordan\",\"price\":123,\"id\":2}],[{\"actor\":\"Rick Riordan\",\"movie\":\"The Lightning Thief\",\"price\":123,\"id\":1},{\"actor\":\"Rick\",\"movie\":\"The Thief\",\"price\":623,\"id\":2}],[{\"actor\":\"Rick Riordan\",\"movie\":\"The Lightning Thief\",\"price\":123,\"id\":1},{\"actor\":\"Rick\",\"movie\":\"The Thief\",\"price\":623,\"id\":2}]]";
	String tableName = "Demo";
	@MockBean
	DataIngectionService dataIngectionService;
	
	@Autowired
	DataIngectionServicePort dataIngectionServicePort;
	
	
	//SET MOCKITO FOR SUCCESS RESPONSE
	public void setMockitoSucccessResponseForService() {
		
		Mockito.when(dataIngectionService.parseSolrSchemaArray(Mockito.any(), Mockito.any())).thenReturn(expected);
		Mockito.when(dataIngectionService.parseSolrSchemaBatch(Mockito.any(),Mockito.any())).thenReturn(expected2);
	}

	//SET MOCKITO FOR BAD REQUEST RESPONSE
	public void setMockitoBadResponseForService() {
	   Mockito.when(dataIngectionService.parseSolrSchemaArray(Mockito.any(),Mockito.any())).thenReturn(expected2);
	   Mockito.when(dataIngectionService.parseSolrSchemaBatch(Mockito.any(),Mockito.any())).thenReturn(expected);
	}
	
	@Test
	void parseSolrSchemaArrayTest(){
		setMockitoSucccessResponseForService();
		String data1 = dataIngectionServicePort.parseSolrSchemaArray(tableName, data);
		JSONAssert.assertEquals(expected, data1,true);
	}
	
	
	//FOR BADRESPONSE 
	@Test
	void parseSolrSchemaArrayfalseTest() {
		setMockitoBadResponseForService();
		String data1 = dataIngectionServicePort.parseSolrSchemaArray(tableName, data);
		JSONAssert.assertNotEquals(expected, data1,true);
	}
	
	@Test
	void parseSolrSchemaBatchTest() {
		setMockitoSucccessResponseForService();
		String data1  = dataIngectionServicePort.parseSolrSchemaBatch(tableName, batchdata);
		JSONAssert.assertEquals(expected2, data1, true);
	}
	
	//FOR BADRESPONSE
	@Test
	void parseSolrSchemaBatchFalseTest() {
		setMockitoBadResponseForService();
		String data1  = dataIngectionServicePort.parseSolrSchemaBatch(tableName, batchdata);
		JSONAssert.assertNotEquals(expected2, data1, true);
	}
}
