package com.solr.clientwrapper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.searchclient.clientwrapper.domain.service.SolrDocumentService;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil.DocumentSatisfiesSchemaResponse;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
 class SolrDocumentServiceTest {

	Logger logger = LoggerFactory.getLogger(SolrDocumentServiceTest.class);

	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;
	String collectionName = "demo";
	int clientid = 1;
	String payload = "[\r\n" + "  {\r\n" + "    \"id\" : \"18\",\r\n" + "    \"color\" : \"ravi\"\r\n" + "  }\r\n"
			+ "]";
	Map<String, Map<String, Object>> schemaKeyValuePair = new HashMap();

	Map<String, Map<String, Object>> schemaKeyValuePair1 = new HashMap();
	
	
	Map<String, Object> map = new HashMap();
	Map<String, Object> map2 = new HashMap();
	@InjectMocks
	SolrDocumentService solrDocumentService;

	@MockBean
	MicroserviceHttpGateway microserviceHttpGateway;
	@MockBean
	DocumentParserUtil documentParser;

	JSONObject JsonObject;

	@BeforeEach
	public void init() {

		map.put("name", "color");
		map.put("type", "string");
		map.put("multivalued", false);
		map.put("required", true);

		map2.put("name", "id");
		map2.put("type", "string");
		map2.put("multivalued", false);
		map2.put("required", true);

	}

	
	  public void setMockitoSuccessResponseForService()
	  { 
		  ResponseDTO solrResponseDTO = new ResponseDTO(collectionName);
	  solrResponseDTO.setStatusCode(200);
	  solrResponseDTO.setResponseMessage("Testing");
	  
	  schemaKeyValuePair.put("id", map2); 
	  schemaKeyValuePair.put("color", map);
	  JsonObject = new JSONObject(solrResponseDTO); 
	  String jsonString = JsonObject.toString();
	  
	  DocumentSatisfiesSchemaResponse doc = new	  DocumentSatisfiesSchemaResponse(true, "Success!");
	  
	  Mockito.when(documentParser.getSchemaOfCollections(baseMicroserviceUrl, collectionName, clientid)) .thenReturn(schemaKeyValuePair);
	  
	  Mockito.when(documentParser.isDocumentSatisfySchema(Mockito.any(),
	  Mockito.any())).thenReturn(doc);
	  Mockito.when(microserviceHttpGateway.postRequestWithStringBody()).thenReturn(
	  jsonString);
	  
	  }
	  
	  public void setMockitoBadResponseForService() { ResponseDTO
	  solrResponseDTO = new ResponseDTO(collectionName);
	  solrResponseDTO.setStatusCode(400); 
	  solrResponseDTO.setResponseMessage("Testing");
	  JsonObject = new JSONObject(solrResponseDTO); 
	  String jsonString =  JsonObject.toString();
	  
	  Mockito.when(documentParser.getSchemaOfCollections(baseMicroserviceUrl,
	  collectionName,clientid)) .thenReturn(schemaKeyValuePair1);
	  DocumentSatisfiesSchemaResponse doc = new
	  DocumentSatisfiesSchemaResponse(false, "Success!");
	  Mockito.when(documentParser.isDocumentSatisfySchema(Mockito.any(),
	  Mockito.any())).thenReturn(doc);	 
	  Mockito.when(microserviceHttpGateway.postRequestWithStringBody()).thenReturn(
	  jsonString);
	  }
	 
	
	  @Test 
	  void testadDocuments() {
	  
	  int statusCode = 200;
	  
	  setMockitoSuccessResponseForService(); 
	  ResponseDTO solrresponseDto =
	  solrDocumentService.addDocuments(collectionName, payload, clientid);
	  assertEquals(statusCode, solrresponseDto.getStatusCode());
	  
		
	  setMockitoBadResponseForService(); ResponseDTO solrResponseDto =
	  solrDocumentService.addDocuments(collectionName, payload, clientid);
	  assertNotEquals(statusCode, solrResponseDto.getStatusCode());
	  
	  setMockitoSuccessResponseForService(); 
	  ResponseDTO solrrsponseDto =
	  solrDocumentService.addDocument(collectionName, payload, clientid);
	  assertEquals(statusCode, solrrsponseDto.getStatusCode());
		 
		  
	  setMockitoBadResponseForService();
	  ResponseDTO ResponseDto =
			  solrDocumentService.addDocument(collectionName, payload, clientid);
			  assertNotEquals(statusCode, ResponseDto.getStatusCode());
	  }
	 
}