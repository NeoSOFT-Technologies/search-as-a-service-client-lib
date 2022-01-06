package com.solr.clientwrapper.domain.service;

import com.solr.clientwrapper.config.CapacityPlanProperties;
import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrCreateCollectionDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrGetCapacityPlanDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrGetCollectionsResponseDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrRenameCollectionDTO;
import com.solr.clientwrapper.domain.port.api.SolrCollectionServicePort;
import com.solr.clientwrapper.domain.utils.MicroserviceHttpGateway;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        MicroserviceHttpGateway microserviceHttpGateway =new MicroserviceHttpGateway();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl+apiEndpoint+"/create");
        microserviceHttpGateway.setRequestBodyDTO(solrCreateCollectionDTO);

        JSONObject jsonObject= microserviceHttpGateway.postRequest();

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;
    }

    @Override
    public SolrResponseDTO delete(String collectionName) {

        SolrResponseDTO solrResponseDTO=new SolrResponseDTO(collectionName);

        MicroserviceHttpGateway microserviceHttpGateway =new MicroserviceHttpGateway();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl+apiEndpoint+"/delete/"+collectionName);

        JSONObject jsonObject= microserviceHttpGateway.deleteRequest();

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;

    }

    @Override
    public SolrResponseDTO rename(String collectionName, String collectionNewName) {
        SolrResponseDTO solrResponseDTO=new SolrResponseDTO(collectionName);

        SolrRenameCollectionDTO solrRenameCollectionDTO=new SolrRenameCollectionDTO(collectionName,collectionNewName);

        MicroserviceHttpGateway microserviceHttpGateway =new MicroserviceHttpGateway();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl+apiEndpoint+"/rename");
        microserviceHttpGateway.setRequestBodyDTO(solrRenameCollectionDTO);

        JSONObject jsonObject= microserviceHttpGateway.putRequest();

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;

    }

    @Override
    public SolrGetCollectionsResponseDTO getCollections() {

        SolrGetCollectionsResponseDTO solrGetCollectionsResponseDTO=new SolrGetCollectionsResponseDTO();

        MicroserviceHttpGateway microserviceHttpGateway =new MicroserviceHttpGateway();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl+apiEndpoint+"/collections/");

        JSONObject jsonObject= microserviceHttpGateway.getRequest();

        solrGetCollectionsResponseDTO.setMessage(jsonObject.get("message").toString());
        solrGetCollectionsResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        List<String> collections=new ArrayList<>();
        JSONArray jsonArray=(JSONArray)jsonObject.get("collections");
        for (int i=0;i<jsonArray.length();i++){
            collections.add(jsonArray.getString(i));
        }
        solrGetCollectionsResponseDTO.setCollections(collections);

        return solrGetCollectionsResponseDTO;

    }

    @Override
    public SolrResponseDTO isCollectionExists(String collectionName) {

        SolrResponseDTO solrResponseDTO=new SolrResponseDTO(collectionName);

        MicroserviceHttpGateway microserviceHttpGateway =new MicroserviceHttpGateway();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl+apiEndpoint+"/isCollectionExists/"+collectionName);

        JSONObject jsonObject= microserviceHttpGateway.getRequest();

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;

    }

}
