package com.searchclient.clientwrapper.domain.service;

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

import com.searchclient.clientwrapper.domain.IngestionResponse;
import com.searchclient.clientwrapper.domain.Response;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil.DocumentSatisfiesSchemaResponse;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class SolrDocumentServiceTest extends SolrDocumentService {

	Logger logger = LoggerFactory.getLogger(SolrDocumentServiceTest.class);

	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;
	String collectionName = "demo1";
	String payload = "[\r\n" + "  {\r\n" + "    \"id\" : \"18\",\r\n" + "    \"color\" : \"ravi\"\r\n" + "  }\r\n"
			+ "]";
	Map<String, Map<String, Object>> schemaKeyValuePair = new HashMap();

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
		map.put("required", true);

	}

	public void setMockitoSuccessResponseForService() {
		Response solrResponseDTO = new Response();
		solrResponseDTO.setStatusCode(200);
		solrResponseDTO.setMessage("Testing");

		schemaKeyValuePair.put("id", map2);
		schemaKeyValuePair.put("color", map);
		JsonObject = new JSONObject(solrResponseDTO);
		String jsonString = JsonObject.toString();

		DocumentSatisfiesSchemaResponse doc = new DocumentSatisfiesSchemaResponse(true, "Success!");

		Mockito.when(documentParser.isDocumentSatisfySchema(Mockito.any(), Mockito.any())).thenReturn(doc);
		Mockito.when(microserviceHttpGateway.postRequestWithStringBody(Mockito.anyString())).thenReturn(jsonString);

	}

	public void setMockitoBadResponseForService() {
		Response solrResponseDTO = new Response();
		solrResponseDTO.setStatusCode(400);
		solrResponseDTO.setMessage("Testing");
		JsonObject = new JSONObject(solrResponseDTO);
		String jsonString = JsonObject.toString();

		DocumentSatisfiesSchemaResponse doc = new DocumentSatisfiesSchemaResponse(false, "Success!");
		Mockito.when(documentParser.isDocumentSatisfySchema(Mockito.any(), Mockito.any())).thenReturn(doc);
		Mockito.when(microserviceHttpGateway.postRequestWithStringBody(Mockito.anyString())).thenReturn(jsonString);
	}

	@Test
	void testadDocuments() {

		int statusCode = 200;

		setMockitoSuccessResponseForService();
		solrDocumentService.addDocument(collectionName, payload, statusCode, baseMicroserviceUrl);
		assertEquals(statusCode, solrDocumentService
				.addDocument(collectionName, payload, statusCode, baseMicroserviceUrl).getStatusCode());

		setMockitoBadResponseForService();
		IngestionResponse rsp = solrDocumentService.addDocument(collectionName, payload, statusCode, "");
		assertNotEquals(statusCode, rsp.getStatusCode());

	}

	@Test
	void addNRTDocuments() {

		int statusCode = 200;

		setMockitoSuccessResponseForService();

		assertEquals(statusCode, solrDocumentService
				.addNRTDocuments(collectionName, payload, statusCode, baseMicroserviceUrl).getStatusCode());

		setMockitoBadResponseForService();

		assertNotEquals(statusCode,
				solrDocumentService.addNRTDocuments(collectionName, payload, statusCode, "").getStatusCode());

	}
}