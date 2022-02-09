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

import com.searchclient.clientwrapper.domain.dto.solr.ManageTableResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.CapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.ManageTableDTO;
import com.searchclient.clientwrapper.domain.port.api.ManageTableServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;
import com.searchclient.clientwrapper.managetable.CapacityPlanProperties;
import com.searchclient.clientwrapper.managetable.GetListItemsResponseDTO;
import com.searchclient.clientwrapper.managetable.GetManageCapacityPlanDTO;
import com.searchclient.clientwrapper.managetable.SchemaDTO;
import com.searchclient.clientwrapper.managetable.SchemaFieldDTO;
import com.searchclient.clientwrapper.managetable.TableSchemaResponseDTO;

@Service
@Transactional
public class ManageTableService implements ManageTableServicePort {

	private final Logger log = LoggerFactory.getLogger(ManageTableService.class);

	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;

	@Value("${microservice-url.manage-table.create}")
	private String createMicroserviceAPI;

	@Value("${microservice-url.manage-table.delete}")
	private String deleteMicroserviceAPI;

	@Value("${microservice-url.manage-table.update}")
	private String renameMicroserviceAPI;

	@Value("${microservice-url.manage-table.get-collections}")
	private String getCollectionsMicroserviceAPI;

	@Value("${microservice-url.manage-table.get-schema}")
	private String getMicroserviceAPI;

	@Value("${microservice-url.manage-table.get-capacity-plan}")
	private String getcapacityplansMicroserviceAPI;

	@Autowired
	CapacityPlanProperties capacityPlanProperties;

	@Autowired
	MicroserviceHttpGateway microserviceHttpGateway;

	@Override
	public GetManageCapacityPlanDTO capacityPlans() {

		GetManageCapacityPlanDTO solrgetCapacityPlans = new GetManageCapacityPlanDTO();

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getcapacityplansMicroserviceAPI);
		JSONObject jsonObject = microserviceHttpGateway.getRequest();

		List<CapacityPlanProperties.Plan> capacityPlans = capacityPlanProperties.getPlans();

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
	public GetListItemsResponseDTO getTables() {

		GetListItemsResponseDTO solrGetCollectionsResponseDTO = new GetListItemsResponseDTO();

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getCollectionsMicroserviceAPI);
		JSONObject jsonObject = microserviceHttpGateway.getRequest();

		solrGetCollectionsResponseDTO.setMessage(jsonObject.get("message").toString());
		solrGetCollectionsResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		List<String> items = new ArrayList<>();
		JSONArray jsonArray = (JSONArray) jsonObject.get("items");
		for (int i = 0; i < jsonArray.length(); i++) {
			items.add(jsonArray.getString(i));
		}
		solrGetCollectionsResponseDTO.setItems(items);

		return solrGetCollectionsResponseDTO;

	}

	@Override
	public ResponseDTO delete(String tableName) {

		ResponseDTO solrResponseDTO = new ResponseDTO(tableName);


		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + deleteMicroserviceAPI + "/" + tableName);
		JSONObject jsonObject = microserviceHttpGateway.deleteRequest();



		return solrResponseDTO;

	}

	@Override
	public ManageTableResponseDTO update(String tableName, SchemaDTO schemaDTO) {
		log.debug("Update Solr Schema");
		log.debug("Target Schema: {}", schemaDTO);

		ManageTableResponseDTO solrSchemaResponseDTO = new ManageTableResponseDTO(tableName, "");

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + renameMicroserviceAPI + "/" + tableName);
		microserviceHttpGateway.setRequestBodyDTO(schemaDTO);

		JSONObject jsonObject = microserviceHttpGateway.putRequest();

		log.debug("Response :{}", jsonObject);


		solrSchemaResponseDTO.setStatusCode((int) jsonObject.get("responseStatusCode"));

		return solrSchemaResponseDTO;
	}

	@Override
	public TableSchemaResponseDTO getTableSchemaIfPresent(String tableName) {
		log.debug("Get Solr Schema");

		TableSchemaResponseDTO solrSchemaResponsedto = new TableSchemaResponseDTO(tableName, "");

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getMicroserviceAPI + "/" + tableName);
		JSONObject jsonObject = microserviceHttpGateway.getRequest();

		log.debug("Response :{}", jsonObject);

		JSONArray jsonArray = (JSONArray) jsonObject.get("attributes");

		List<SchemaFieldDTO> attributes = new ArrayList<SchemaFieldDTO>();
		for (int i = 0; i < jsonArray.length(); i++) {

			JSONObject childJsonObject = (JSONObject) jsonArray.get(i);

			SchemaFieldDTO sfd = new SchemaFieldDTO();

			sfd.setMultiValue(childJsonObject.getBoolean("multiValue"));
			sfd.setDefault_(childJsonObject.getString("default_"));
		
			sfd.setName(childJsonObject.getString("name"));
			sfd.setRequired(childJsonObject.getBoolean("required"));
		
			sfd.setStorable(childJsonObject.getBoolean("storable"));


			attributes.add(sfd);

		}
		solrSchemaResponsedto.setStatusCode((int) jsonObject.get("statusCode"));
		solrSchemaResponsedto.setAttributes(attributes);
		return solrSchemaResponsedto;
	}

	@Override
	public ManageTableDTO create(String tableName, ManageTableDTO manageTableDTO) {

		ManageTableDTO ManageTableDTO1 = new ManageTableDTO(tableName, "", manageTableDTO.getAttributes());

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + createMicroserviceAPI);
		microserviceHttpGateway.setRequestBodyDTO(manageTableDTO);

		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		ManageTableDTO1.setResponseStatusCode((int) jsonObject.get("responseStatusCode"));
		return ManageTableDTO1;

	}
}
