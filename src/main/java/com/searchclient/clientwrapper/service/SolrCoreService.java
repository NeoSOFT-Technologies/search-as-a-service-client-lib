package com.searchclient.clientwrapper.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.core.SolrDoubleCoreDTO;
import com.searchclient.clientwrapper.domain.dto.solr.core.SolrSingleCoreDTO;
import com.searchclient.clientwrapper.domain.port.api.SolrCoreServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
public class SolrCoreService implements SolrCoreServicePort {

	private final Logger log = LoggerFactory.getLogger(SolrCoreService.class);

	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;

	@Value("${microservice-url.core.create}")
	private String createMicroserviceAPI;

	@Value("${microservice-url.core.rename}")
	private String renameMicroserviceAPI;

	@Value("${microservice-url.core.delete}")
	private String deleteMicroserviceAPI;

	@Value("${microservice-url.core.swap}")
	private String swapMicroserviceAPI;

	@Value("${microservice-url.core.reload}")
	private String reloadMicroserviceAPI;

	@Value("${microservice-url.core.status}")
	private String statusMicroserviceAPI;
	@Autowired
	MicroserviceHttpGateway microserviceHttpGateway;

	@Override
	public ResponseDTO create(String coreName) {

		log.debug("create");

		SolrSingleCoreDTO solrSingleCore = new SolrSingleCoreDTO(coreName);

		ResponseDTO solrResponseDTO = new ResponseDTO(coreName);

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + createMicroserviceAPI);
		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);
		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public ResponseDTO rename(String coreName, String newName) {

		log.debug("rename");

		SolrDoubleCoreDTO solrSingleCore = new SolrDoubleCoreDTO(coreName, newName);
		ResponseDTO solrResponseDTO = new ResponseDTO(coreName);

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + renameMicroserviceAPI);
		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);

		JSONObject jsonObject = microserviceHttpGateway.putRequest();
		System.out.println("jsonobject" + jsonObject);
		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public ResponseDTO delete(String coreName) {

		log.debug("delete" + coreName);

		ResponseDTO solrResponseDTO = new ResponseDTO(coreName);

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + deleteMicroserviceAPI + "/" + coreName);

		JSONObject jsonObject = microserviceHttpGateway.deleteRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;

	}

	@Override
	public ResponseDTO swap(String coreOne, String coreTwo) {

		log.debug("swap");
		SolrDoubleCoreDTO solrSingleCore = new SolrDoubleCoreDTO(coreOne, coreTwo);
		ResponseDTO solrResponseDTO = new ResponseDTO(coreOne);

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + swapMicroserviceAPI);
		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);

		JSONObject jsonObject = microserviceHttpGateway.putRequest();

		System.out.println("jsonobject" + jsonObject);
		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public ResponseDTO reload(String coreName) {

		log.debug("reload");
		SolrSingleCoreDTO solrSingleCore = new SolrSingleCoreDTO(coreName);
		ResponseDTO solrResponseDTO = new ResponseDTO(coreName);

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + reloadMicroserviceAPI);

		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);
		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public String status(String coreName) {

		log.debug("status");

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + statusMicroserviceAPI + "/" + coreName);
		String jsonObject = microserviceHttpGateway.stringRequest();

		return jsonObject;

	}

}
