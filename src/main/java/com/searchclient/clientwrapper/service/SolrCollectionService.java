package com.searchclient.clientwrapper.service;

import com.searchclient.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.CapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.SolrCreateCollectionDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.SolrGetCapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.SolrGetCollectionsResponseDTO;
import com.searchclient.clientwrapper.domain.port.api.SolrCollectionServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolrCollectionService implements SolrCollectionServicePort {

	private final Logger log = LoggerFactory.getLogger(SolrCollectionService.class);

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

	@Value("${microservice-url.table-collection.get-collection-details}")
	private String getCollectionDetailsMicroserviceAPI;

	@Autowired
	SolrCollectionServicePort solrCollectionServicePort;

    
    @Autowired
    MicroserviceHttpGateway microserviceHttpGateway;



	@Override
	public SolrGetCapacityPlanDTO capacityPlans() {
		SolrGetCapacityPlanDTO solrgetCapacityPlans = new SolrGetCapacityPlanDTO();

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
	public SolrResponseDTO create(String collectionName, String sku) {

		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);

		SolrGetCapacityPlanDTO solrgetCapacityPlanDTO = solrCollectionServicePort.capacityPlans();
        log.debug("Response :{}", solrgetCapacityPlanDTO);

		List<CapacityPlanDTO> capacityPlans = solrgetCapacityPlanDTO.getPlans();
		CapacityPlanDTO selectedCapacityPlan = null;
		for (CapacityPlanDTO capacityPlan : capacityPlans) {
			if (capacityPlan.getSku().equals(sku)) {
				selectedCapacityPlan = capacityPlan;
			}
		}

		if (selectedCapacityPlan == null) {
			// INVALD SKU
			solrResponseDTO.setStatusCode(400);
			solrResponseDTO.setMessage("Invalid SKU: " + sku);
			return solrResponseDTO;
		}


//        MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
   //     microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint);
    //    microserviceHttpGateway.setRequestBodyDTO(solrCreateCollectionDTO);

		SolrCreateCollectionDTO solrCreateCollectionDTO = new SolrCreateCollectionDTO(collectionName, sku);


		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + createMicroserviceAPI);
		microserviceHttpGateway.setRequestBodyDTO(solrCreateCollectionDTO);

		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;
	}

	@Override
	public SolrResponseDTO delete(String collectionName) {


        //MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
    //    microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/" + collectionName);

		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);


		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + deleteMicroserviceAPI + "/" + collectionName);
		JSONObject jsonObject = microserviceHttpGateway.deleteRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;

	}

	@Override
	public SolrGetCollectionsResponseDTO getCollections() {

		SolrGetCollectionsResponseDTO solrGetCollectionsResponseDTO = new SolrGetCollectionsResponseDTO();



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
	public SolrResponseDTO isCollectionExists(String collectionName) {

		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);
		microserviceHttpGateway
				.setApiEndpoint(baseMicroserviceUrl + isCollectionExistsMicroserviceAPI + "/" + collectionName);

		JSONObject jsonObject = microserviceHttpGateway.getRequest();

       // MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
   //     microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/isTableExists/" + collectionName);

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));


		return solrResponseDTO;

	}

	@Override
	public JSONObject getCollectionDetails(String collectionName) {
		MicroserviceHttpGateway microserviceHttpGateway =new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl+getCollectionDetailsMicroserviceAPI+"/" +collectionName);

		return microserviceHttpGateway.getRequest();
	}

}
