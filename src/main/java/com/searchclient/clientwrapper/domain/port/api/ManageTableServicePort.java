package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.dto.solr.ManageTableResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.GetCapacityPlanDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.GetCollectionsResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.ManageTableDTO;

public interface ManageTableServicePort {

	/*
	 * CRUD operations for managing tables
	 */
	
	// Create request
	ManageTableDTO create(String tableName,ManageTableDTO manageTableDTO);

	// Update requests
	ManageTableResponseDTO update(String tableName, SolrSchemaDTO solrSchemaDTO);

	// DELETE requests

	ResponseDTO delete(String collectionName);

	// Get requests
	ManageTableResponseDTO get(String tableName);

	// get Table requests
	GetCollectionsResponseDTO getCollections();

	// get capacityPlans requests
	GetCapacityPlanDTO capacityPlans();

}
