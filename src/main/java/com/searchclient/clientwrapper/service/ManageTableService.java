package com.searchclient.clientwrapper.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.searchclient.clientwrapper.domain.dto.solr.SolrFieldDTO;
import com.searchclient.clientwrapper.domain.dto.solr.ManageTableResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.searchclient.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.CapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.GetCapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.ManageTableDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.GetCollectionsResponseDTO;
import com.searchclient.clientwrapper.domain.port.api.ManageTableServicePort;
import com.searchclient.clientwrapper.domain.port.api.SolrCollectionServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;
import com.searchclient.clientwrapper.infrastructure.Enum.SolrFieldType;

@Service
@Transactional
public class ManageTableService implements ManageTableServicePort {

	private final Logger log = LoggerFactory.getLogger(ManageTableService.class);

	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;

	@Value("${microservice-url.table-collection.create}")
	private String createMicroserviceAPI;

	@Value("${microservice-url.table-collection.delete}")
	private String deleteMicroserviceAPI;

	@Value("${microservice-url.table-collection.rename}")
	private String renameMicroserviceAPI;

	@Value("${microservice-url.table-collection.get-collections}")
	private String getCollectionsMicroserviceAPI;

	@Value("${microservice-url.table-collection.is-collection-exists}")
	private String isCollectionExistsMicroserviceAPI;

	@Value("${microservice-url.table-collection.get-capacity-plans}")
	private String getcapacityplansMicroserviceAPI;

	@Autowired
	SolrCollectionServicePort solrCollectionServicePort;

	@Autowired
	MicroserviceHttpGateway microserviceHttpGateway;

	@Override
	public GetCapacityPlanDTO capacityPlans() {
		GetCapacityPlanDTO solrgetCapacityPlans = new GetCapacityPlanDTO();

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getcapacityplansMicroserviceAPI);
		JSONObject jsonObject = microserviceHttpGateway.getRequest();

		log.debug("Response :{}", jsonObject);

		JSONArray jsonArray = (JSONArray) jsonObject.get("plans");

		List<CapacityPlanDTO> capacityplan = new ArrayList<CapacityPlanDTO>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject childJsonObject = (JSONObject) jsonArray.get(i);
			CapacityPlanDTO cpd = new CapacityPlanDTO();
			cpd.setSku(childJsonObject.getString("sku"));
			cpd.setName(childJsonObject.getString("name"));
			cpd.setReplicas(childJsonObject.getInt("replicas"));
			cpd.setShards(childJsonObject.getInt("shards"));
			cpd.setStorage(childJsonObject.getInt("storage"));
			capacityplan.add(cpd);
		}
		solrgetCapacityPlans.setPlans(capacityplan);

		return solrgetCapacityPlans;

	}

	@Override
	public GetCollectionsResponseDTO getCollections() {

		GetCollectionsResponseDTO solrGetCollectionsResponseDTO = new GetCollectionsResponseDTO();

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getCollectionsMicroserviceAPI);
		JSONObject jsonObject = microserviceHttpGateway.getRequest();

		solrGetCollectionsResponseDTO.setMessage(jsonObject.get("message").toString());
		solrGetCollectionsResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		List<String> collections = new ArrayList<>();
		JSONArray jsonArray = (JSONArray) jsonObject.get("collections");
		for (int i = 0; i < jsonArray.length(); i++) {
			collections.add(jsonArray.getString(i));
		}
		solrGetCollectionsResponseDTO.setCollections(collections);

		return solrGetCollectionsResponseDTO;

	}

	@Override
	public ResponseDTO delete(String collectionName) {

		ResponseDTO solrResponseDTO = new ResponseDTO(collectionName);

		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + deleteMicroserviceAPI + "/" + collectionName);
		JSONObject jsonObject = microserviceHttpGateway.deleteRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;

	}

	@Override
	public ManageTableResponseDTO update(String tableName, SolrSchemaDTO newSolrSchemaDTO) {
		log.debug("Update Solr Schema");
		log.debug("Target Schema: {}", newSolrSchemaDTO);

		ManageTableResponseDTO manageTableResponseDTO = new ManageTableResponseDTO(tableName, "");

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
		manageTableResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
		manageTableResponseDTO.setAttributes(attributes);
		return manageTableResponseDTO;
	}

	@Override
	public ManageTableResponseDTO get(String tableName) {
		log.debug("Get Solr Schema");

		ManageTableResponseDTO manageTableResponseDTO = new ManageTableResponseDTO(tableName, "");

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
		manageTableResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
		manageTableResponseDTO.setAttributes(attributes);
		return manageTableResponseDTO;
	}
	

	@Override
	public ManageTableDTO create(String tableName, ManageTableDTO manageTableDTO) {

		ManageTableDTO ManageTableDTO1 = new ManageTableDTO(tableName, "", manageTableDTO.getAttributes());

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + createMicroserviceAPI);
		microserviceHttpGateway.setRequestBodyDTO(ManageTableDTO1);

		JSONObject jsonObject = microserviceHttpGateway.postRequest();
		ManageTableDTO1.setResponseStatusCode((int) jsonObject.get("statusCode"));
        return ManageTableDTO1;

	}
}
