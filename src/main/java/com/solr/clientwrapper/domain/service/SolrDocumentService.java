package com.solr.clientwrapper.domain.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrDocumentServicePort;
import com.solr.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
@Transactional
public class SolrDocumentService implements SolrDocumentServicePort {

	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;
	private final Logger log = LoggerFactory.getLogger(SolrDocumentService.class);

	@Override
	public SolrResponseDTO addDocument(String collectionName, String payload) {

		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);

		MicroserviceHttpGateway microserviceHttpgateway = new MicroserviceHttpGateway();
		microserviceHttpgateway.setApiEndpoint(baseMicroserviceUrl + "/document/" + collectionName);
		microserviceHttpgateway.setRequestBodyDTO(solrResponseDTO);
		JSONObject jsonObject = microserviceHttpgateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public SolrResponseDTO addDocuments(String collectionName, String payload) {

		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + "/documents" + collectionName);
		microserviceHttpGateway.setRequestBodyDTO(solrResponseDTO);
		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;

	}

}
