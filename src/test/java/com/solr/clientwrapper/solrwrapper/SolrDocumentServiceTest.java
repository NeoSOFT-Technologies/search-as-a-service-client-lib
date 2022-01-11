package com.solr.clientwrapper.solrwrapper;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import com.solr.clientwrapper.IntegrationTest;
import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrDocumentServicePort;
import com.solr.clientwrapper.domain.service.SolrDocumentService;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class SolrDocumentServiceTest {

	@Autowired
	SolrDocumentService solrDocumentService;
	
	@MockBean
	SolrDocumentServicePort solrDocumentServicePort;
	
	@BeforeEach
	void setUp() {
		SolrResponseDTO solrResponseDto = SolrResponseDTO.builder().statusCode(200).name("Demo").message("Successful").build();
		
		Mockito.when(solrDocumentServicePort.addDocument("Demo", "string"))
		.thenReturn(solrResponseDto);
			
	}
	
	@Test
	public void addDocument() {
		
	}
}
