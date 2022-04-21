package com.searchclient.clientwrapper.domain.port.api;


import org.springframework.stereotype.Service;
import com.searchclient.clientwrapper.domain.CapacityPlanProperties;
import com.searchclient.clientwrapper.domain.ManageTableCreate;
import com.searchclient.clientwrapper.domain.ManageTableResponse;
import com.searchclient.clientwrapper.domain.ManageTableUpdate;
import com.searchclient.clientwrapper.domain.Response;

@Service
public interface ManageTableServicePort {

	/*
	 * CRUD operations for managing tables
	 */

	// DELETE requests

	Response delete(int tenantId,String tableName, String jwtToken);
// get Tables requests
	Response getTables(int tenantId, String jwtToken);

	// get capacityPlans requests
	CapacityPlanProperties capacityPlans(String jwtToken);

    ManageTableResponse getTable(String tableName, int tenantId, String jwtToken);
    // Create request
    Response create(int tenantId, ManageTableCreate manageTableDTO, String jwtToken);
 // Update requests
    Response update(String tableName, int tenantId, ManageTableUpdate tableSchema, String jwtToken);
    
    //UNDO TABLE DELET requests
    Response restoreTable(int tenantId, String tableName, String jwtToken);

}
