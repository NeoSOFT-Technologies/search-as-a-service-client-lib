package com.searchclient.clientwrapper.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchclient.clientwrapper.domain.CapacityPlanProperties;
import com.searchclient.clientwrapper.domain.ManageTableCreate;
import com.searchclient.clientwrapper.domain.ManageTableUpdate;
import com.searchclient.clientwrapper.domain.SchemaField;
import com.searchclient.clientwrapper.domain.error.BadRequestOccurredException;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;



@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ManageTableServiceTest extends ManageTableService {
	
	@InjectMocks
	ManageTableService manageTableService;
	
	 @Mock
	    private ObjectMapper objectMapper;
	 
    @MockBean
    CapacityPlanProperties capacityPlanProperties;

    JSONObject JsonObject;
    
   String jwtToken="jwtToken";
	
   int clientId=10;
   
   String schemaName="B";
   
   String tableName="Demo";
  
   List<SchemaField> list = new ArrayList<SchemaField>();
	
	SchemaField schemaField = new SchemaField();
	
	public void setMockitoSuccessResponseForService() {
	          schemaField.setDefault_("");
			schemaField.setFilterable(true);
			schemaField.setMultiValue(true);
			schemaField.setName("ok");
			schemaField.setPartialSearch(true);
			schemaField.setRequired(true);
			schemaField.setSortable(true);
			schemaField.setStorable(true);
			schemaField.setType("string");
			
			list.add(schemaField);
	}
    @MockBean
    MicroserviceHttpGateway microserviceHttpGateway;
	
    @MockBean
    DocumentParserUtil docParserUtil;
    
    ManageTableCreate manageTableCreate = new ManageTableCreate();
    
    ManageTableUpdate manageTableUpdate = new ManageTableUpdate();
    
	@Test
	void testCapacityPlans() {

		
		try {

		 manageTableService.capacityPlans(jwtToken);
		} catch (BadRequestOccurredException e) {
			assertEquals(400, e.getExceptionCode());
		}
	
	}

	@Test
	void testGetTables() {

		try {

			manageTableService.getTables(clientId, jwtToken);
			} catch (BadRequestOccurredException e) {
				assertEquals(400, e.getExceptionCode());
			}
		

	}
//
	@Test
	void testGetTable() {
		
		try {

			manageTableService.getTable(tableName, clientId, jwtToken);
			} catch (BadRequestOccurredException e) {
				assertEquals(400, e.getExceptionCode());
			}
	
	}

	@Test
	void testDelete() {
	
		try {

			manageTableService.delete(clientId, tableName, jwtToken);
			
			} catch (BadRequestOccurredException e) {
				assertEquals(400, e.getExceptionCode());
			}
	}
//
	@Test
	void testCreate() {
		
		manageTableCreate.setSchemaName(jwtToken);
		manageTableCreate.setSku(jwtToken);
		manageTableCreate.setTableName(jwtToken);
		manageTableCreate.setColumns(list);;
		

		
	
	try {

		 manageTableService.create(clientId, manageTableCreate, jwtToken);
		
		} catch (BadRequestOccurredException e) {
			assertEquals(400, e.getExceptionCode());
		}
	}

	@Test
	void testUpdate() {
		
		manageTableUpdate.setColumns(list);
		manageTableUpdate.setSchemaName(schemaName);
		manageTableUpdate.setTableDetails(null);
		manageTableUpdate.setTableName("Demo");
		

		
	try {

		manageTableService.update(schemaName, clientId, manageTableUpdate, jwtToken);
		
		} catch (BadRequestOccurredException e) {
			assertEquals(400, e.getExceptionCode());
		}
	}
//
	@Test
	void testRestoreTable() {

		
	try {

		manageTableService.restoreTable(clientId, schemaName, jwtToken);
		
		} catch (BadRequestOccurredException e) {
			assertEquals(400, e.getExceptionCode());
		}
	}
}
