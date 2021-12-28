package com.solr.clientwrapper.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solr.clientwrapper.config.CapacityPlanProperties;
import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrCreateCollectionDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrGetCapacityPlanDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrGetCollectionsResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrCollectionServicePort;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    @Autowired
    private ObjectMapper objectMapper;

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

        //ABOVE VALIDATIONS WILL BE THERE IN CLIENT LIBRARY

        //BELOW CODE THAT IS COMMENTED WILL ONLY BE THERE IN THE MICROSERVICE. THE CODE THAT INTERACTS WITH SOLR REMAINS IN MICROSERVICE

//        CollectionAdminRequest.Create request = CollectionAdminRequest.createCollection(collectionName, selectedCapacityPlan.getShards(), selectedCapacityPlan.getReplicas());
//
//        HttpSolrClient solrClient = new HttpSolrClient.Builder(baseSolrUrl).build();
//
//
//        request.setMaxShardsPerNode(selectedCapacityPlan.getShards()*selectedCapacityPlan.getReplicas());
//
//        try {
//            CollectionAdminResponse response = request.process(solrClient);
//            solrResponseDTO.setStatusCode(200);
//            solrResponseDTO.setMessage("Successfully created Solr Collection: "+collectionName);
//        } catch (Exception e) {
//            log.error(e.toString());
//            solrResponseDTO.setStatusCode(400);
//            solrResponseDTO.setMessage("Unable to create Solr Collection: "+collectionName+". Exception.");
//        }

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(baseMicroserviceUrl+"/searchservice/table"+"/create");

        SolrCreateCollectionDTO solrCreateCollectionDTO=new SolrCreateCollectionDTO(collectionName,sku);

        String objJackson=null;
        try {
            objJackson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(solrCreateCollectionDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(objJackson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpPost);
            HttpEntity entityResponse = response.getEntity();
            String result = EntityUtils.toString(entityResponse);
            System.out.println("RESPONSE: "+ result);

            JSONObject jsonObject= new JSONObject(result );

            solrResponseDTO.setMessage(jsonObject.get("message").toString());
            solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

            client.close();

            return solrResponseDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //IN PLACE OF THE ABOVE CODE, THE SKU NAME AND THE COLLECTION NAME WILL BE SENT TO THE MICROSERVICE USING HTTP REQUEST
        // THE HTTP URL, PORT OF THE MICROSERVICE SHOULD BE STORED IN APPLICATION.YML CONFIG FILE. THE PATH EG. /CREATE/COLLECTION/{name} CAN BE DIRECTLY ADDED HERE
        //WHATEVER THE RESPONSE IS FROM THE MICRO SERVICE, RETURN IT HERE.
        //RESPONSE WILL BE IN JSON FORMAT, SO CONVERT THAT INTO SOLR RESPONSE DTO AND THEN RETURN IT FROM THIS FUNCTION

        //ITS BETTER KEEP LESS VALIDATION ON THE MICROSERVICE SIDE, BUT KEEP THE ESSENTIAL MICROSERVICES
        //- KARTHIK PILLAI

        return solrResponseDTO;
    }

    @Override
    public SolrResponseDTO delete(String collectionName) {

        SolrResponseDTO solrResponseDTO=new SolrResponseDTO(collectionName);

        CollectionAdminRequest.Delete request = CollectionAdminRequest.deleteCollection(collectionName);
        CollectionAdminRequest.DeleteAlias deleteAliasRequest=CollectionAdminRequest.deleteAlias(collectionName);

        HttpSolrClient solrClient = new HttpSolrClient.Builder(baseSolrUrl).build();

        try {
            CollectionAdminResponse response = request.process(solrClient);
            CollectionAdminResponse deleteAliasResponse = deleteAliasRequest.process(solrClient);

            solrResponseDTO.setStatusCode(200);
            solrResponseDTO.setMessage("Successfully deleted Solr Collection: "+collectionName);
        } catch (Exception e) {
            log.error(e.toString());
            solrResponseDTO.setStatusCode(400);
            solrResponseDTO.setMessage("Unable to delete Solr Collection: "+collectionName+". Exception.");
        }

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
