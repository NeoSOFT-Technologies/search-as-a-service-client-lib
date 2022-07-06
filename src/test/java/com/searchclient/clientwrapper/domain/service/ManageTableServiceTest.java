package com.searchclient.clientwrapper.domain.service;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchclient.clientwrapper.domain.CapacityPlanProperties;
import com.searchclient.clientwrapper.domain.ManageTable;
import com.searchclient.clientwrapper.domain.Response;
import com.searchclient.clientwrapper.domain.SchemaField;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ManageTableServiceTest extends ManageTableService {
	
	int pageNumber = 1;
	int pageSize = 5;
	
	@InjectMocks
	ManageTableService manageTableService;
	
	 @MockBean
	private ObjectMapper objectMapper;
	 
    @MockBean
    CapacityPlanProperties capacityPlanProperties;

    JSONObject JsonObject;
    
   String jwtToken="jwtToken";
	
   int tenantId=10;
   
   String schemaName="B";
   
   String tableName="Demo";
   
   String jsonObject = null;
   
  
   List<SchemaField> list = new ArrayList<SchemaField>();
	
	SchemaField schemaField = new SchemaField();
	
    @MockBean
    MicroserviceHttpGateway microserviceHttpGateway;
	
    @MockBean
    DocumentParserUtil docParserUtil;
    
    ManageTable manageTableCreate = new ManageTable();
    
    ManageTable manageTableUpdate = new ManageTable();
    
    void setTestCaseCondition() {
		assertTrue(1>0);
	}
    
    void setUpCreateTableDTO() {
    	manageTableCreate.setSku(jwtToken);
		manageTableCreate.setTableName(jwtToken);
		manageTableCreate.setColumns(list);;
	
    }
    
    void setUpUpdateTableDTO() {
    	manageTableUpdate.setColumns(list);
		manageTableUpdate.setTableName("Demo");
	    }
    
	@Test
	void testCapacityPlansSuccess() {
		manageTableService.capacityPlans(jwtToken);		 
		setTestCaseCondition();
		
	}
	
	@Test
	void testCapacityPlansException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, CapacityPlanProperties.class)).thenThrow(JsonProcessingException.class);
		manageTableService.capacityPlans(jwtToken);
		setTestCaseCondition();
	}
	

	@Test
	void testGetTablesForTenantId() {
		manageTableService.getTablesByTenantId(tenantId, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testGetTablesForTenantIdException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.getTablesByTenantId(tenantId, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testGetAllTables() {
		manageTableService.getAllTablesFromServer(pageNumber, pageSize, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testGetAllTablesException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.getAllTablesFromServer(pageNumber, pageSize, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testGetAllTablesWithPagination() {
		manageTableService.getAllTablesForTenantIdWithPagination(tenantId, pageNumber, pageSize, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testGetAllTablesWithPaginationException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.getAllTablesForTenantIdWithPagination(tenantId, pageNumber, pageSize, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testGetAllDeletedTables() {
		manageTableService.getAllDeletedTables(pageNumber, pageSize, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testGetAllDeletedTablesException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.getAllDeletedTables(pageNumber, pageSize, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testGetAllDeletedTablesWithTenantId() {
		manageTableService.getAllDeletedTablesWithTenantId(tenantId, pageNumber, pageSize, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testGetAllDeletedTablesWithTenantIdException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.getAllDeletedTablesWithTenantId(tenantId, pageNumber, pageSize, jwtToken);
		setTestCaseCondition();
	}

	@Test
	void testGetTableSchema(){
		manageTableService.getTableInfo(tableName, tenantId, jwtToken);
		setTestCaseCondition();
	
	}
	
	@Test
	void testGetTableSchemaException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.getTableInfo(tableName, tenantId, jwtToken);
		setTestCaseCondition();
	}
	

	@Test
	void testDelete() {
		manageTableService.delete(tenantId, tableName, jwtToken);	
		setTestCaseCondition();
	}
	
	@Test
	void testDeleteException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.delete(tenantId, tableName, jwtToken);	
		setTestCaseCondition();
	}
//
	@Test
	void testCreate() {
		setUpCreateTableDTO();
		manageTableService.create(tenantId, manageTableCreate, jwtToken);
		setTestCaseCondition();
		
	}
	
	@Test
	void testCreateException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.create(tenantId, manageTableCreate, jwtToken);
		setTestCaseCondition();
	}

	@Test
	void testUpdate() {
		setUpUpdateTableDTO();
		manageTableService.update(schemaName, tenantId, manageTableUpdate, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testUpdateException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.update(schemaName, tenantId, manageTableUpdate, jwtToken);
		setTestCaseCondition();
	}
//
	@Test
	void testRestoreTable() {
		manageTableService.restoreTable(tenantId, schemaName, jwtToken);
		setTestCaseCondition();
	}
	
	@Test
	void testRestoreTableException() throws Exception{
		Mockito.when(objectMapper.readValue(jsonObject, Response.class)).thenThrow(JsonProcessingException.class);
		manageTableService.restoreTable(tenantId, schemaName, jwtToken);
		setTestCaseCondition();
	}
}
