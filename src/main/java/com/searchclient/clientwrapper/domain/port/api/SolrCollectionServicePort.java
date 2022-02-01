package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.GetCapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.GetCollectionsResponseDTO;

public interface SolrCollectionServicePort {

    ResponseDTO create(String collectionName, String sku);

    ResponseDTO delete(String collectionName);

    //SolrResponseDTO rename(String collectionName, String collectionNewName);

    GetCollectionsResponseDTO getCollections();

    GetCapacityPlanDTO capacityPlans();

    ResponseDTO isCollectionExists(String collectionName);

}
