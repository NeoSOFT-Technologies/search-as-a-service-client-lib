package com.solr.clientwrapper.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solr.clientwrapper.domain.port.api.DataIngectionServicePort;
import com.solr.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
@Transactional
public class DataIngectionService implements DataIngectionServicePort {

	private final Logger log = LoggerFactory.getLogger(DataIngectionService.class);

	@Value("${base-solr-url}")
	private String baseSolrUrl;
	
	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;

	String apiEndPoint = "/ingection";
	@Override
	public String parseSolrSchemaArray(String collectionName, String jsonString) {
		
		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndPoint + "/batcharray/"+collectionName);
		microserviceHttpGateway.setRequestBodyDTO(jsonString);
		
		String jsonStringRes = microserviceHttpGateway.postRequestString();
		return jsonStringRes;
	}

	@Override
	public String parseSolrSchemaBatch(String collectionName, String jsonString) {
		
		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndPoint + "/batchcollection/"+collectionName);
		microserviceHttpGateway.setRequestBodyDTO(jsonString);
		
		String jsonStringRes = microserviceHttpGateway.postRequestString();
		return jsonStringRes;
	}

}
