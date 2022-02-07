package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.SolrGetCapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.SolrGetCollectionsResponseDTO;
import org.json.JSONObject;

public interface SolrCollectionServicePort {

    SolrResponseDTO create(String collectionName, String sku);

    SolrResponseDTO delete(String collectionName);

    //SolrResponseDTO rename(String collectionName, String collectionNewName);

    SolrGetCollectionsResponseDTO getCollections();

    SolrGetCapacityPlanDTO capacityPlans();

    SolrResponseDTO isCollectionExists(String collectionName);

    JSONObject getCollectionDetails(String collectionName);

}
