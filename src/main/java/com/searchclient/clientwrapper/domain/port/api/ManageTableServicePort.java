package com.searchclient.clientwrapper.domain.port.api;

import org.springframework.stereotype.Service;

import com.searchclient.clientwrapper.domain.dto.solr.ManageTableResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.ManageTableDTO;
import com.searchclient.clientwrapper.managetable.GetListItemsResponseDTO;
import com.searchclient.clientwrapper.managetable.GetManageCapacityPlanDTO;
import com.searchclient.clientwrapper.managetable.SchemaDTO;
import com.searchclient.clientwrapper.managetable.TableSchemaResponseDTO;

@Service
public interface ManageTableServicePort {

	/*
	 * CRUD operations for managing tables
	 */
	
	// Create request
	ManageTableDTO create(String tableName,ManageTableDTO manageTableDTO);

	// Update requests
	ManageTableResponseDTO update(String tableName, SchemaDTO schemaDTO);

	// DELETE requests

	ResponseDTO delete(String collectionName);

	// Get Schema requests
	TableSchemaResponseDTO getTableSchemaIfPresent(String tableName);

	// get Tables requests
	GetListItemsResponseDTO getTables();

	// get capacityPlans requests
	GetManageCapacityPlanDTO capacityPlans();

}
