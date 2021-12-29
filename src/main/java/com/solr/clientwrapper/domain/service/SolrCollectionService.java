package com.solr.clientwrapper.domain.service;

import com.solr.clientwrapper.config.CapacityPlanProperties;
import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrCreateCollectionDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrGetCapacityPlanDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrGetCollectionsResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrCollectionServicePort;
import com.solr.clientwrapper.domain.utils.Microservice;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SolrCollectionService implements SolrCollectionServicePort {

    private final Logger log = LoggerFactory.getLogger(SolrCollectionService.class);

    //http://localhost:8983/solr
    @Value("${base-solr-url}")
    private String baseSolrUrl;

    @Value("${base-microservice-url}")
    private String baseMicroserviceUrl;

    private String apiEndpoint="/searchservice/table";


//    @Autowired
//    private ObjectMapper objectMapper;

    @Autowired
    CapacityPlanProperties capacityPlanProperties;

    @Override
    public SolrGetCapacityPlanDTO capacityPlans() {
        List<CapacityPlanProperties.Plan> capacityPlans = capacityPlanProperties.getPlans();

        return new SolrGetCapacityPlanDTO(capacityPlans);
    }


    @Override
    public SolrResponseDTO create(String collectionName, String sku) {

        SolrResponseDTO solrResponseDTO=new SolrResponseDTO(collectionName);

        List<CapacityPlanProperties.Plan> capacityPlans = capacityPlanProperties.getPlans();
        CapacityPlanProperties.Plan selectedCapacityPlan=null;

        for (CapacityPlanProperties.Plan capacityPlan : capacityPlans) {
            if(capacityPlan.getSku().equals(sku)){
                selectedCapacityPlan=capacityPlan;
            }
        }

        if(selectedCapacityPlan==null){
            //INVALD SKU
            solrResponseDTO.setStatusCode(400);
            solrResponseDTO.setMessage("Invalid SKU: "+sku);
            return solrResponseDTO;
        }


        SolrCreateCollectionDTO solrCreateCollectionDTO=new SolrCreateCollectionDTO(collectionName,sku);

        Microservice microservice =new Microservice();
        microservice.setApiEndpoint(baseMicroserviceUrl+apiEndpoint+"/create");
        microservice.setRequestBodyDTO(solrCreateCollectionDTO);

        JSONObject jsonObject= microservice.post();

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;
    }

    @Override
    public SolrResponseDTO delete(String collectionName) {

        SolrResponseDTO solrResponseDTO=new SolrResponseDTO(collectionName);

        Microservice microservice =new Microservice();
        microservice.setApiEndpoint(baseMicroserviceUrl+apiEndpoint+"/delete/"+collectionName);

        JSONObject jsonObject= microservice.delete();

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;

    }

    @Override
    public SolrResponseDTO rename(String collectionName, String collectionNewName) {
        SolrResponseDTO solrResponseDTO=new SolrResponseDTO(collectionName);

        CollectionAdminRequest.Rename request = CollectionAdminRequest.renameCollection(collectionName,collectionNewName);

        HttpSolrClient solrClient = new HttpSolrClient.Builder(baseSolrUrl).build();

        try {
            CollectionAdminResponse response = request.process(solrClient);
            solrResponseDTO.setStatusCode(200);
            solrResponseDTO.setMessage("Successfully renamed Solr Collection: "+collectionName+" to "+collectionNewName);
        } catch (Exception e) {
            log.error(e.toString());
            solrResponseDTO.setStatusCode(400);
            solrResponseDTO.setMessage("Unable to rename Solr Collection: "+collectionName+". Exception.");
        }

        return solrResponseDTO;
    }


    @Override
    public SolrGetCollectionsResponseDTO getCollections() {

        CollectionAdminRequest.List request = new CollectionAdminRequest.List();
        HttpSolrClient solrClient = new HttpSolrClient.Builder(baseSolrUrl).build();

        SolrGetCollectionsResponseDTO solrGetCollectionsResponseDTO=new SolrGetCollectionsResponseDTO();

        try {
            CollectionAdminResponse response = request.process(solrClient);

            solrGetCollectionsResponseDTO.setCollections((List<String>) response.getResponse().get("collections"));
            solrGetCollectionsResponseDTO.setStatusCode(200);
            solrGetCollectionsResponseDTO.setMessage("Successfully retrieved all Solr Collections");

        } catch (Exception e) {
            log.error(e.toString());

            solrGetCollectionsResponseDTO.setCollections(null);
            solrGetCollectionsResponseDTO.setStatusCode(400);
            solrGetCollectionsResponseDTO.setMessage("Unable to retrieve all Solr Collections");
        }

        return solrGetCollectionsResponseDTO;

    }

    @Override
    public boolean isCollectionExists(String collectionName) {
        CollectionAdminRequest.List request = new CollectionAdminRequest.List();
        HttpSolrClient solrClient = new HttpSolrClient.Builder(baseSolrUrl).build();

        try {
            CollectionAdminResponse response = request.process(solrClient);

            List<String> allCollections=(List<String>) response.getResponse().get("collections");

            return allCollections.contains(collectionName);

        } catch (Exception e) {
            log.error(e.toString());

            return false;
        }

    }

}
