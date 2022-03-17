package com.searchclient.clientwrapper.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchclient.clientwrapper.domain.error.BadRequestOccurredException;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil.DocumentSatisfiesSchemaResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class DocumentParserUtilTest {
	Logger logger = LoggerFactory.getLogger(DocumentParserUtilTest.class);
	
	@InjectMocks
	DocumentParserUtil documentParserUtil;
	 @Mock
	    private ObjectMapper objectMapper;
	
	String baseMicroserviceUrl="http://localhost:8983/solr";
	
	 String collectionName="Demo";
			 int clientid=10;
			 String jwtToken="sdodsadpasdasd";
	
	@MockBean
	MicroserviceHttpGateway microserviceHttpgateway;

	Map<String, Map<String, Object>> getSchemaKeyValuePair(){
        Map<String, Map<String, Object>> schemaKeyValuePair = new HashMap<>();

        Map<String, Object> id=new HashMap<String, Object>(){{
            put("name" , "id");
            put("type" , "string");
            put("multiValued", "true");
            put("uninvertible" , "true");
            put("indexed" , "true");
            put("stored" , "true");
            //put("required", "true");
        }};
        schemaKeyValuePair.put("id",id);
        
        Map<String, Object> color=new HashMap<String, Object>(){{
            put("name" , "color");
            put("type" , "string");
            put("uninvertible" , "true");
            put("indexed" , "true");
            put("stored" , "true");
        }};
        schemaKeyValuePair.put("color",color);
        Map<String, Object> custom_field_strings=new HashMap<String, Object>(){{
            put("name" , "custom_field_strings");
            put("type" , "strings");
            put("uninvertible" , "true");
            put("multiValued", "true");
            put("indexed" , "true");
            put("stored" , "true");
        }};
        schemaKeyValuePair.put("custom_field_strings",custom_field_strings);

        Map<String, Object> custom_field_boolean=new HashMap<String, Object>(){{
            put("name" , "custom_field_boolean");
            put("type" , "boolean");
            put("uninvertible" , "true");
            put("indexed" , "true");
            put("stored" , "true");
        }};
        schemaKeyValuePair.put("custom_field_boolean",custom_field_boolean);

        Map<String, Object> custom_field_booleans=new HashMap<String, Object>(){{
            put("name" , "custom_field_booleans");
            put("type" , "boolean");
            put("uninvertible" , "true");
            put("multiValued", "true");
            put("indexed" , "true");
            put("stored" , "true");
        }};
        schemaKeyValuePair.put("custom_field_booleans",custom_field_booleans);

        Map<String, Object> custom_field_plong=new HashMap<String, Object>(){{
            put("name" , "custom_field_plong");
            put("type" , "plong");
            put("uninvertible" , "true");
            put("indexed" , "true");
            put("stored" , "true");
        }};
        schemaKeyValuePair.put("custom_field_plong",custom_field_plong);

        Map<String, Object> custom_field_plongs=new HashMap<String, Object>(){{
            put("name" , "custom_field_plongs");
            put("type" , "plong");
            put("uninvertible" , "true");
            put("multiValued", "true");
            put("indexed" , "true");
            put("stored" , "true");
        }};
        schemaKeyValuePair.put("custom_field_plongs",custom_field_plongs);

        Map<String, Object> custom_required_field_boolean=new HashMap<String, Object>(){{
            put("name" , "custom_required_field_boolean");
            put("type" , "boolean");
            put("uninvertible" , "true");
            put("required" , "true");
            put("indexed" , "true");
            put("stored" , "true");
        }};
        schemaKeyValuePair.put("custom_required_field_boolean",custom_required_field_boolean);
       
        return schemaKeyValuePair;
    }
	
	DocumentSatisfiesSchemaResponse doc;
	Map<String, Map<String, Object>> schemaKeyValuePair=getSchemaKeyValuePair();

	String inputString="{\n" +
            "\"id\":1," +
            "\"color\":\"ravi\"," +
            "\"custom_field_boolean\":\"1\"," +
            "\"custom_field_booleans\":[1,0,\"1\"]," +
            "\"custom_field_plong\":60," +
            "\"custom_field_plongs\":[22,56,59]," +
          
            "\"custom_field_strings\":[\"kar\",\"mani\",\"sara\"]," +
            "\"custom_required_field_boolean\":\"true\"," +
            "}";

    JSONObject payloadJSON  = new JSONObject(inputString);
    
    String json3="{\"data\":{\"tableName\":\"Demo\",\"columns\":[{\"name\":\"filterable\",\"type\":\"booleans\",\"required\":false,\"multiValue\":false,\"sortable\":false,\"storable\":false,\"filterable\":false,\"partialSearch\":false},{\"name\":\"id\",\"type\":\"string\",\"required\":true,\"multiValue\":false,\"sortable\":false,\"storable\":true,\"filterable\":true,\"partialSearch\":false},{\"name\":\"multiValue\",\"type\":\"booleans\",\"required\":false,\"multiValue\":false,\"sortable\":false,\"storable\":false,\"filterable\":false,\"partialSearch\":false},{\"name\":\"name\",\"type\":\"string\",\"required\":false,\"multiValue\":false,\"sortable\":false,\"storable\":false,\"filterable\":false,\"partialSearch\":false},{\"name\":\"partialSearch\",\"type\":\"booleans\",\"required\":false,\"multiValue\":false,\"sortable\":false,\"storable\":false,\"filterable\":false,\"partialSearch\":false},{\"name\":\"required\",\"type\":\"booleans\",\"required\":false,\"multiValue\":false,\"sortable\":false,\"storable\":false,\"filterable\":false,\"partialSearch\":false},{\"name\":\"sortable\",\"type\":\"booleans\",\"required\":false,\"multiValue\":false,\"sortable\":false,\"storable\":false,\"filterable\":false,\"partialSearch\":false},{\"name\":\"storable\",\"type\":\"booleans\",\"required\":false,\"multiValue\":false,\"sortable\":false,\"storable\":false,\"filterable\":false,\"partialSearch\":false},{\"name\":\"type\",\"type\":\"string\",\"required\":false,\"multiValue\":false,\"sortable\":false,\"storable\":false,\"filterable\":false,\"partialSearch\":false}]}}"; 
    
    
   
    public void setMockitoSuccessResponseForService() {
        JSONObject jsonobject = new JSONObject(json3);
		doc = new DocumentSatisfiesSchemaResponse(true, "Successful!");
		Mockito.when(microserviceHttpgateway.getRequest(Mockito.anyString())).thenReturn(jsonobject);
	}
    
    public void setMockitoBadResponseForService() {
    	JSONObject jsonObject = new JSONObject(json3);
    	doc = new DocumentSatisfiesSchemaResponse(false, "Successful!");
		Mockito.when(microserviceHttpgateway.getRequest(Mockito.anyString())).thenReturn(jsonObject);
    }
	
	@Test
	void checkIfRequiredFieldsAreAvailableTest() {
		
		setMockitoSuccessResponseForService();
        DocumentSatisfiesSchemaResponse documentsatisfiesschemaresponse = documentParserUtil.checkIfRequiredFieldsAreAvailable(schemaKeyValuePair,payloadJSON );
		assertEquals(doc.isObjectSatisfiesSchema, documentsatisfiesschemaresponse.isObjectSatisfiesSchema);
		
		setMockitoBadResponseForService();
		DocumentSatisfiesSchemaResponse documentsatisfiesschemaresponse2 = documentParserUtil.checkIfRequiredFieldsAreAvailable(schemaKeyValuePair,payloadJSON );
		assertNotEquals(doc.isObjectSatisfiesSchema, documentsatisfiesschemaresponse2.isObjectSatisfiesSchema);
	
	}
	
	@Test
	void isDocumentSatisfySchemaTest() {
		
		setMockitoSuccessResponseForService();
        DocumentSatisfiesSchemaResponse documentsatisfiesschemaresponse = documentParserUtil.isDocumentSatisfySchema(schemaKeyValuePair,payloadJSON );
		assertEquals(doc.isObjectSatisfiesSchema, documentsatisfiesschemaresponse.isObjectSatisfiesSchema);
		
		setMockitoBadResponseForService();
		DocumentSatisfiesSchemaResponse documentsatisfiesschemaresponse2 = documentParserUtil.isDocumentSatisfySchema(schemaKeyValuePair,payloadJSON );
		assertNotEquals(doc.isObjectSatisfiesSchema, documentsatisfiesschemaresponse2.isObjectSatisfiesSchema);
	
	}
	
	@Test
	void getSchemaofCollectionTest() {
		setMockitoSuccessResponseForService();
       //  Mockito.when(microserviceHttpgateway.getRequest()).thenReturn(new JSONObject(jsonResponseForGetSchema));

		 try {

			 documentParserUtil.getSchemaOfCollection(baseMicroserviceUrl, collectionName, clientid, jwtToken);
			
			} catch (BadRequestOccurredException e) {
				assertEquals(400, e.getExceptionCode());
			}
	}


}
