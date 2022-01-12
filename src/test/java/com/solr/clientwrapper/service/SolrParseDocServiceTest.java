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

import com.solr.clientwrapper.domain.port.api.SolrParseDocServicePort;
import com.solr.clientwrapper.domain.service.SolrParseDocService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class SolrParseDocServiceTest {

	Logger logger = LoggerFactory.getLogger(SolrParseDocServiceTest.class);
	
	@MockBean
	SolrParseDocService solrparseDocservice;
	
	@Autowired
	SolrParseDocServicePort solrparseDocServiceport;
	
	public void setMockitoSuccessResponseForService() {
		Mockito.when(solrparseDocservice.MultipartUploder(Mockito.any())).thenReturn("Success");
	}
	
	public void setMockitoBadResponseForService() {
		Mockito.when(solrparseDocservice.MultipartUploder(Mockito.any())).thenReturn("Failure");
	}
	@Test
	void testMutipartUploader() {
		String exactresponse = "Success";
		setMockitoSuccessResponseForService();
		String response = solrparseDocServiceport.MultipartUploder(null);
		JSONAssert.assertEquals(exactresponse, response, true);
		
		
		setMockitoBadResponseForService();
		String exactresponse2 = "Failure";
		String response2 = solrparseDocServiceport.MultipartUploder(null);
		JSONAssert.assertEquals(exactresponse2, response2, false);
		
	}
	
}
	