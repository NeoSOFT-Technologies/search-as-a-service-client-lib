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

import com.searchclient.clientwrapper.config.CapacityPlanProperties;
import com.searchclient.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.SolrCreateCollectionDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.SolrGetCapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.SolrGetCollectionsResponseDTO;
import com.searchclient.clientwrapper.domain.port.api.SolrCollectionServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
public class SolrCollectionService implements SolrCollectionServicePort {

    private final Logger log = LoggerFactory.getLogger(SolrCollectionService.class);

    // http://localhost:8983/solr
    @Value("${base-solr-url}")
    private String baseSolrUrl;

    @Value("${base-microservice-url}")
    private String baseMicroserviceUrl;

    private String apiEndpoint = "/api/table";

    @Autowired
    CapacityPlanProperties capacityPlanProperties;
    @Autowired
    MicroserviceHttpGateway microserviceHttpGateway;

    @Override
    public SolrGetCapacityPlanDTO capacityPlans() {
        List<CapacityPlanProperties.Plan> capacityPlans = capacityPlanProperties.getPlans();

        return new SolrGetCapacityPlanDTO(capacityPlans);
    }

    @Override
    public SolrResponseDTO create(String collectionName, String sku) {

        SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);

        List<CapacityPlanProperties.Plan> capacityPlans = capacityPlanProperties.getPlans();
        CapacityPlanProperties.Plan selectedCapacityPlan = null;

        for (CapacityPlanProperties.Plan capacityPlan : capacityPlans) {
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

        SolrCreateCollectionDTO solrCreateCollectionDTO = new SolrCreateCollectionDTO(collectionName, sku);

//        MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint);
        microserviceHttpGateway.setRequestBodyDTO(solrCreateCollectionDTO);

        JSONObject jsonObject = microserviceHttpGateway.postRequest();

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;
    }

    @Override
    public SolrResponseDTO delete(String collectionName) {

        SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);

        MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/" + collectionName);

        JSONObject jsonObject = microserviceHttpGateway.deleteRequest();

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;

    }

    
    @Override
    public SolrGetCollectionsResponseDTO getCollections() {

        SolrGetCollectionsResponseDTO solrGetCollectionsResponseDTO = new SolrGetCollectionsResponseDTO();

        MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint);

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

        MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/isTableExists/" + collectionName);

        JSONObject jsonObject = microserviceHttpGateway.getRequest();

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;

    }

}
