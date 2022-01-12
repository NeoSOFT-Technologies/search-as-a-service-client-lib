package com.solr.clientwrapper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrDocumentServicePort;
import com.solr.clientwrapper.domain.service.SolrDocumentService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class SolrDocumentServiceTest {

	Logger logger = LoggerFactory.getLogger(SolrDocumentServiceTest.class);
	
	String collectionName = "Demo";
	String payload = "\r\n" + "{\r\n" + "\"books\" :[\r\n" + "  {\r\n" + "    \"id\" : 1,\r\n"
			+ "    \"color\" : \"The Lightning Thief\",\r\n" + "    \"author\" : \"Rick Riordan\",\r\n"
			+ "    \"price\" : 123\r\n" + "  }\r\n" + "]\r\n" + "}";
	
	@MockBean
	SolrDocumentService solrDocumentService;
	
	@Autowired
	SolrDocumentServicePort solrDocumentServicePort;
	
	public void setMockitoSuccessResponseForService() {
		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);
        solrResponseDTO.setStatusCode(200);
        solrResponseDTO.setMessage("Testing");
        
        Mockito.when(solrDocumentService.addDocuments(Mockito.any(), Mockito.any(), Mockito.anyBoolean())).thenReturn(solrResponseDTO);
	}
	
	public void setMockitoBadResponseForService() {
        SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);
        solrResponseDTO.setStatusCode(400);
        solrResponseDTO.setMessage("Testing");
        
        Mockito.when(solrDocumentService.addDocuments(Mockito.any(), Mockito.any(), Mockito.anyBoolean())).thenReturn(solrResponseDTO);
	}

	@Test
	public void testadDocuments() {
		
		int statusCode =200;
		
		setMockitoSuccessResponseForService();
		SolrResponseDTO solrresponseDto = solrDocumentServicePort.addDocuments(collectionName, payload, true);
		assertEquals(statusCode, solrresponseDto.getStatusCode());
		
		setMockitoBadResponseForService();
		SolrResponseDTO solrResponseDto = solrDocumentServicePort.addDocuments(collectionName, payload, true);
		assertNotEquals(statusCode, solrResponseDto.getStatusCode());
		
	}
}
