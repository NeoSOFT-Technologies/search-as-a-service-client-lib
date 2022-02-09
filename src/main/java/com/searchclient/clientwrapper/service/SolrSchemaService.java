package com.searchclient.clientwrapper.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.searchclient.clientwrapper.domain.dto.solr.SolrFieldDTO;
import com.searchclient.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.searchclient.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;
import com.searchclient.clientwrapper.domain.port.api.SolrSchemaServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;
import com.searchclient.clientwrapper.infrastructure.Enum.SolrFieldType;

@Service
public class SolrSchemaService implements SolrSchemaServicePort {

	private final Logger log = LoggerFactory.getLogger(SolrSchemaService.class);

	@Value(value ="${base-microservice-url}")
	private String baseMicroserviceUrl;

	private String apiEndpoint = "/api/schema";
	
	@Autowired
	MicroserviceHttpGateway microserviceHttpGateway;

	@Override
	public SolrSchemaResponseDTO get(String tableName) {
		log.debug("Get Solr Schema");

		SolrSchemaResponseDTO solrSchemaResponsedto = new SolrSchemaResponseDTO(tableName, "");

		
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/" + tableName);
		JSONObject jsonObject = microserviceHttpGateway.getRequest();

		log.debug("Response :{}", jsonObject);

		JSONArray jsonArray = (JSONArray) jsonObject.get("attributes");

		List<SolrFieldDTO> attributes = new ArrayList<SolrFieldDTO>();
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject childJsonObject = (JSONObject) jsonArray.get(i);

			SolrFieldDTO sfd = new SolrFieldDTO();

			sfd.setMultiValued(childJsonObject.getBoolean("multiValued"));
			sfd.setDefault_(childJsonObject.getString("default_"));
			sfd.setIndexed(childJsonObject.getBoolean("indexed"));
			sfd.setName(childJsonObject.getString("name"));
			sfd.setRequired(childJsonObject.getBoolean("required"));
			sfd.setDocValues(childJsonObject.getBoolean("docValues"));
			sfd.setStorable(childJsonObject.getBoolean("storable"));
			sfd.setType(SolrFieldType.fromObject(childJsonObject.get("type").toString()));

			attributes.add(sfd);

		}
		solrSchemaResponsedto.setStatusCode((int) jsonObject.get("statusCode"));
		solrSchemaResponsedto.setAttributes(attributes);
		return solrSchemaResponsedto;
	}

	@Override
	public SolrSchemaResponseDTO update(String tableName, SolrSchemaDTO newSolrSchemaDTO) {
		log.debug("Update Solr Schema");
		log.debug("Target Schema: {}", newSolrSchemaDTO);

		SolrSchemaResponseDTO solrSchemaResponseDTO = new SolrSchemaResponseDTO(tableName, "");

	
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/" + tableName);
		microserviceHttpGateway.setRequestBodyDTO(newSolrSchemaDTO);

		JSONObject jsonObject = microserviceHttpGateway.putRequest();

		log.debug("Response :{}", jsonObject);

		JSONArray jsonArray = (JSONArray) jsonObject.get("attributes");

		List<SolrFieldDTO> attributes = new ArrayList<SolrFieldDTO>();
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject childJsonObject = (JSONObject) jsonArray.get(i);

			SolrFieldDTO sfd = new SolrFieldDTO();

			sfd.setMultiValued(childJsonObject.getBoolean("multiValued"));
			sfd.setDefault_(childJsonObject.getString("default_"));
			sfd.setIndexed(childJsonObject.getBoolean("indexed"));
			sfd.setName(childJsonObject.getString("name"));
			sfd.setRequired(childJsonObject.getBoolean("required"));
			sfd.setDocValues(childJsonObject.getBoolean("docValues"));
			sfd.setStorable(childJsonObject.getBoolean("storable"));
			sfd.setType(SolrFieldType.fromObject(childJsonObject.get("type").toString()));

			attributes.add(sfd);

		}
		solrSchemaResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
		solrSchemaResponseDTO.setAttributes(attributes);
		return solrSchemaResponseDTO;
	}

	@Override
	public SolrSchemaResponseDTO create(String tableName, SolrSchemaDTO newSolrSchemaDTO) {
		log.debug("Create Solr Schema");

		SolrSchemaResponseDTO solrSchemaResponseDto = new SolrSchemaResponseDTO(tableName, "",
				newSolrSchemaDTO.getAttributes());


		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint);
		microserviceHttpGateway.setRequestBodyDTO(solrSchemaResponseDto);

		JSONObject jsonObject = microserviceHttpGateway.postRequest();
		solrSchemaResponseDto.setStatusCode((int) jsonObject.get("statusCode"));

		return solrSchemaResponseDto;
	}

	@Override
	public SolrSchemaResponseDTO delete(String tableName) {
		log.debug("Delete Solr Schema");
		SolrSchemaResponseDTO solrSchemaResponseDto = new SolrSchemaResponseDTO(tableName, "");


		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/" + tableName);

		JSONObject jsonObject = microserviceHttpGateway.deleteRequest();

		log.debug("Response :{}", jsonObject);

		JSONArray jsonArray = (JSONArray) jsonObject.get("attributes");
		List<SolrFieldDTO> attributes = new ArrayList<SolrFieldDTO>();
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject childJsonObject = (JSONObject) jsonArray.get(i);

			SolrFieldDTO sfd = new SolrFieldDTO();

			sfd.setMultiValued(childJsonObject.getBoolean("multiValued"));
			sfd.setDefault_(childJsonObject.getString("default_"));
			sfd.setIndexed(childJsonObject.getBoolean("indexed"));
			sfd.setName(childJsonObject.getString("name"));
			sfd.setRequired(childJsonObject.getBoolean("required"));
			sfd.setDocValues(childJsonObject.getBoolean("docValues"));
			sfd.setStorable(childJsonObject.getBoolean("storable"));
			sfd.setType(SolrFieldType.fromObject(childJsonObject.get("type").toString()));

			attributes.add(sfd);

		}
		solrSchemaResponseDto.setAttributes(attributes);
		solrSchemaResponseDto.setStatusCode((int) jsonObject.get("statusCode"));

		return solrSchemaResponseDto;
	}

}
