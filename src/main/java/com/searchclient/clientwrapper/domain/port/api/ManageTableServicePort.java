package com.searchclient.clientwrapper.domain.port.api;

import org.springframework.stereotype.Service;

import com.searchclient.clientwrapper.domain.CapacityPlanProperties;
import com.searchclient.clientwrapper.domain.ManageTableCreate;
import com.searchclient.clientwrapper.domain.Response;
import com.searchclient.clientwrapper.domain.ManageTableUpdate;
import com.searchclient.clientwrapper.domain.ManageTableResponse;

@Service
public interface ManageTableServicePort {

	/*
	 * CRUD operations for managing tables
	 */
	

	

	// DELETE requests

	Response delete(int clientId,String tableName);
// get Tables requests
	Response getTables(int clientId);

	// get capacityPlans requests
	CapacityPlanProperties capacityPlans();

    ManageTableResponse getTable(String tableName, int clientId);
    // Create request
    Response create(int clientId, ManageTableCreate manageTableDTO);
 // Update requests
    Response update(String tableName, int clientId, ManageTableUpdate tableSchema);
    
    //UNDO TABLE DELET requests
    Response restoreTable(int clientId, String tableName);

}
