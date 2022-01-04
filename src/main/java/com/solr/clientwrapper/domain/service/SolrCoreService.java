package com.solr.clientwrapper.domain.service;

import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.dto.solr.core.SolrDoubleCoreDTO;
import com.solr.clientwrapper.domain.dto.solr.core.SolrSingleCoreDTO;
import com.solr.clientwrapper.domain.port.api.SolrCoreServicePort;
import com.solr.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
@Transactional
public class SolrCoreService implements SolrCoreServicePort {

	private final Logger log = LoggerFactory.getLogger(SolrCoreService.class);

	// http://localhost:8983/solr
	@Value("${base-solr-url}")
	private String baseSolrUrl;

	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;

	private String apiEndpoint = "/solr-core";

	@Override
	public SolrResponseDTO create(String coreName) {

		log.debug("create");

		SolrSingleCoreDTO solrSingleCore = new SolrSingleCoreDTO(coreName);

		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/create");
		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);
		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public SolrResponseDTO rename(String coreName, String newName) {

		log.debug("rename");

		SolrDoubleCoreDTO solrSingleCore = new SolrDoubleCoreDTO(coreName, newName);
		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/rename");
		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);

		JSONObject jsonObject = microserviceHttpGateway.putRequest();
		System.out.println("jsonobject" + jsonObject);
		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public SolrResponseDTO delete(String coreName) {

		log.debug("delete" + coreName);

		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/delete/" + coreName);

		JSONObject jsonObject = microserviceHttpGateway.deleteRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;

	}

	@Override
	public SolrResponseDTO swap(String coreOne, String coreTwo) {

		log.debug("swap");
		SolrDoubleCoreDTO solrSingleCore = new SolrDoubleCoreDTO(coreOne, coreTwo);
		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreOne);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/swap");
		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);

		JSONObject jsonObject = microserviceHttpGateway.putRequest();

		System.out.println("jsonobject" + jsonObject);
		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public SolrResponseDTO reload(String coreName) {

		log.debug("reload");
		SolrSingleCoreDTO solrSingleCore = new SolrSingleCoreDTO(coreName);
		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/reload");

		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);
		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public String status(String coreName) {

		log.debug("status");

		CoreAdminResponse coreAdminResponse = null;
		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/status/" + coreName);
		String jsonObject = microserviceHttpGateway.stringRequest();
		
		return jsonObject;

	}

}
