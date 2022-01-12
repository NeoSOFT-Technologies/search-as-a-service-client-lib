package com.solr.clientwrapper.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solr.clientwrapper.domain.dto.solr.SolrFieldDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrSchemaServicePort;
import com.solr.clientwrapper.domain.utils.MicroserviceHttpGateway;
import com.solr.clientwrapper.infrastructure.Enum.SolrFieldType;

@Service
@Transactional
@SuppressWarnings({ "deprecation", "unused" })
public class SolrSchemaService implements SolrSchemaServicePort {

	private final Logger log = LoggerFactory.getLogger(SolrSchemaService.class);

	@Value(value ="${base-microservice-url}")
	private String baseMicroserviceUrl;

	private String apiEndpoint = "/schema";

	@Override
	public SolrSchemaResponseDTO get(String tableName, String name) {
		log.debug("Get Solr Schema: {}", name);

		SolrSchemaResponseDTO solrSchemaResponsedto = new SolrSchemaResponseDTO(tableName, name);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/get/" + tableName + "/" + name);
		JSONObject jsonObject = microserviceHttpGateway.getRequest();

		log.debug("Response :" + jsonObject);

		JSONArray jsonArray = (JSONArray) jsonObject.get("attributes");

		List<SolrFieldDTO> attributes = new ArrayList<SolrFieldDTO>();
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject childJsonObject = (JSONObject) jsonArray.get(i);

			SolrFieldDTO sfd = new SolrFieldDTO();

			sfd.setMultiValue(childJsonObject.getBoolean("multiValue"));
			sfd.setDefault_(childJsonObject.getString("default_"));
			sfd.setFilterable(childJsonObject.getBoolean("filterable"));
			sfd.setName(childJsonObject.getString("name"));
			sfd.setRequired(childJsonObject.getBoolean("required"));
			sfd.setSortable(childJsonObject.getBoolean("sortable"));
			sfd.setStorable(childJsonObject.getBoolean("storable"));
			sfd.setType(SolrFieldType.fromObject(childJsonObject.get("type").toString()));

			attributes.add(sfd);

		}
		solrSchemaResponsedto.setStatusCode((int) jsonObject.get("statusCode"));
		solrSchemaResponsedto.setAttributes(attributes);
		return solrSchemaResponsedto;
	}

	@Override
	public SolrSchemaResponseDTO update(String tableName, String name, SolrSchemaDTO newSolrSchemaDTO) {
		log.debug("Update Solr Schema: {}", name);
		log.debug("Target Schema: {}", newSolrSchemaDTO);

		SolrSchemaResponseDTO solrSchemaResponseDTO = new SolrSchemaResponseDTO(tableName, name);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/update/" + tableName + "/" + name);
		microserviceHttpGateway.setRequestBodyDTO(newSolrSchemaDTO);

		JSONObject jsonObject = microserviceHttpGateway.putRequest();

		log.debug("Response :" + jsonObject);

		JSONArray jsonArray = (JSONArray) jsonObject.get("attributes");

		List<SolrFieldDTO> attributes = new ArrayList<SolrFieldDTO>();
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject childJsonObject = (JSONObject) jsonArray.get(i);

			SolrFieldDTO sfd = new SolrFieldDTO();

			sfd.setMultiValue(childJsonObject.getBoolean("multiValue"));
			sfd.setDefault_(childJsonObject.getString("default_"));
			sfd.setFilterable(childJsonObject.getBoolean("filterable"));
			sfd.setName(childJsonObject.getString("name"));
			sfd.setRequired(childJsonObject.getBoolean("required"));
			sfd.setSortable(childJsonObject.getBoolean("sortable"));
			sfd.setStorable(childJsonObject.getBoolean("storable"));
			sfd.setType(SolrFieldType.fromObject(childJsonObject.get("type").toString()));

			attributes.add(sfd);

		}
		solrSchemaResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
		solrSchemaResponseDTO.setAttributes(attributes);
		return solrSchemaResponseDTO;
	}

	@Override
	public SolrSchemaResponseDTO create(String tableName, String name, SolrSchemaDTO newSolrSchemaDTO) {
		log.debug("Create Solr Schema: {}", name);

		SolrSchemaResponseDTO solrSchemaResponseDto = new SolrSchemaResponseDTO(tableName, name,
				newSolrSchemaDTO.getAttributes());

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/create");
		microserviceHttpGateway.setRequestBodyDTO(solrSchemaResponseDto);

		JSONObject jsonObject = microserviceHttpGateway.postRequest();
		solrSchemaResponseDto.setStatusCode((int) jsonObject.get("statusCode"));

		return solrSchemaResponseDto;
	}

	@Override
	public SolrSchemaResponseDTO delete(String tableName, String name) {
		SolrSchemaResponseDTO solrSchemaResponseDto = new SolrSchemaResponseDTO(tableName, name);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/delete/" + tableName + "/" + name);

		JSONObject jsonObject = microserviceHttpGateway.deleteRequest();

		log.debug("Response :" + jsonObject);

		JSONArray jsonArray = (JSONArray) jsonObject.get("attributes");
		List<SolrFieldDTO> attributes = new ArrayList<SolrFieldDTO>();
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject childJsonObject = (JSONObject) jsonArray.get(i);

			SolrFieldDTO sfd = new SolrFieldDTO();

			sfd.setMultiValue(childJsonObject.getBoolean("multiValue"));
			sfd.setDefault_(childJsonObject.getString("default_"));
			sfd.setFilterable(childJsonObject.getBoolean("filterable"));
			sfd.setName(childJsonObject.getString("name"));
			sfd.setRequired(childJsonObject.getBoolean("required"));
			sfd.setSortable(childJsonObject.getBoolean("sortable"));
			sfd.setStorable(childJsonObject.getBoolean("storable"));
			sfd.setType(SolrFieldType.fromObject(childJsonObject.get("type").toString()));

			attributes.add(sfd);

		}
		solrSchemaResponseDto.setAttributes(attributes);
		solrSchemaResponseDto.setStatusCode((int) jsonObject.get("statusCode"));

		return solrSchemaResponseDto;
	}

}
