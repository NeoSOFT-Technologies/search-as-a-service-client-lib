package com.searchclient.clientwrapper.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchclient.clientwrapper.domain.error.CustomException;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentParserUtil {

    @Autowired
    MicroserviceHttpGateway microserviceHttpGateway;
    private static final String STATUS_CODE = "statusCode";
    @Autowired
    ObjectMapper objectMapper;

    @Value("${base-solr-url}")
    private String baseSearchUrl;

	private static final String TENANT_ID_REQUEST_PARAM = "?tenantId=";
   
    public DocumentSatisfiesSchemaResponse checkIfRequiredFieldsAreAvailable(Map<String, Map<String, Object>> schemaKeyValuePair, JSONObject payloadJSON) {
        for (Map.Entry<String, Map<String, Object>> entry : schemaKeyValuePair.entrySet()) {
            if (entry.getValue().containsKey("required") && entry.getValue().get("required").toString().equals("true") && !payloadJSON.has(entry.getKey())) {
                        return new DocumentSatisfiesSchemaResponse(false, "Required field is missing in document. Field: " + entry.getKey());
              
            }
        }

        return new DocumentSatisfiesSchemaResponse(true, "All the required fields are available.");

    }

    public Map<String, Map<String, Object>> getSchemaOfCollection(String baseMicroserviceUrl, String collectionName, int tenantId, String jwtToken) {

        // THIS METHOD HITS THE GET SCHEMA METHOD OF THE MICROSERVICE AND GETS
        // THE
        // SCHEMA
        // THE "ATTRIBUTE" NODE CONSISTS OF THE LIST OF ALL THE SCHEMA FIELDS
        // (ATTRIBUTES)

        Logger log = LoggerFactory.getLogger(DocumentParserUtil.class);

        String url = baseMicroserviceUrl + "/api/v1/manage/table/" +  collectionName + TENANT_ID_REQUEST_PARAM + tenantId;
        log.debug("calling url {} ",url);
        microserviceHttpGateway.setApiEndpoint(url);
        microserviceHttpGateway.setRequestBodyDTO(null);
        JSONObject jsonObject = microserviceHttpGateway.getRequest(jwtToken);
        int statusCode = jsonObject.getInt(STATUS_CODE);
        Map<String, Map<String, Object>> schemaKeyValuePair = new HashMap<>();
        if(statusCode == 200) {
        JSONObject data= (JSONObject) jsonObject.get("data");

        JSONArray jsonArrayOfAttributesFields = (JSONArray) data.get("columns");
        log.debug("jsonArrayOfAttributesFields : {}", jsonArrayOfAttributesFields);
     
        ObjectMapper objectMapper1 = new ObjectMapper();
        List<Map<String, Object>> schemaResponseFields = jsonArrayOfAttributesFields.toList().stream().map(eachField -> (Map<String, Object>) objectMapper1.convertValue(eachField, Map.class))
                .collect(Collectors.toList());

        // Converting response schema from Search Server to HashMap for quick access
        // Key contains the field name and value contains the object which has
        // schema
        // description of that key eg. multivalued etc
         schemaResponseFields.forEach(fieldObject -> schemaKeyValuePair.put(fieldObject.get("name").toString(), fieldObject));
        }
        else {
        	Map<String, Object> errorResponse = new HashMap<>();
        	errorResponse.put(STATUS_CODE, jsonObject.getInt(STATUS_CODE));
        	errorResponse.put("message", jsonObject.getString("message"));
            schemaKeyValuePair.put("error",errorResponse);
        }
        return schemaKeyValuePair;
    }

    @Data
    public static class DocumentSatisfiesSchemaResponse {
        boolean isObjectSatisfiesSchema;
        String message;

        public DocumentSatisfiesSchemaResponse(boolean isObjectSatisfiesSchema, String message) {
            this.isObjectSatisfiesSchema = isObjectSatisfiesSchema;
            this.message = message;
        }
    }
    
    public void isJwtAuthenticationError(String jsonString) {
    	JSONObject obj = new JSONObject(jsonString);
    	if((obj.has("Unauthorized")) && obj.getString("Unauthorized").contains("Invalid token"))
    		throw new CustomException(HttpStatusCode.REQUEST_FORBIDEN.getCode(), 
    				HttpStatusCode.REQUEST_FORBIDEN, "Invalid Token");
    }
}
