package com.searchclient.clientwrapper.domain.port.api;


import org.springframework.stereotype.Service;
import com.searchclient.clientwrapper.domain.CapacityPlanProperties;
import com.searchclient.clientwrapper.domain.ManageTable;
import com.searchclient.clientwrapper.domain.ManageTableResponse;
import com.searchclient.clientwrapper.domain.Response;

@Service
public interface ManageTableServicePort {

	/*
	 * CRUD operations for managing tables
	 */

	// DELETE requests

	Response delete(int tenantId,String tableName, String jwtToken);
   // get Tables requests
	Response getTablesByTenantId(int tenantId, String jwtToken);

	// get capacityPlans requests
	CapacityPlanProperties capacityPlans(String jwtToken);

    ManageTableResponse getTableInfo(String tableName, int tenantId, String jwtToken);
    // Create request
    Response create(int tenantId, ManageTable manageTableDTO, String jwtToken);
 // Update requests
    Response update(String tableName, int tenantId, ManageTable tableSchema, String jwtToken);
    
    //UNDO TABLE DELET requests
    Response restoreTable(int tenantId, String tableName, String jwtToken);
    
    //GET ALL TABLES FROM SERVER
    Response getAllTables(int pageNumber, int pageSize, String jwtToken);
    
    //GET ALL TABLES UNDER DELETION
    Response getAllDeletedTables(int pageNumber, int pageSize, String jwtToken);

}
