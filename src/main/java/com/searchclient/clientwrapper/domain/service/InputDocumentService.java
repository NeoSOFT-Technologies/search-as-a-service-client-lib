package com.searchclient.clientwrapper.domain.service;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.searchclient.clientwrapper.domain.IngestionResponse;
import com.searchclient.clientwrapper.domain.port.api.InputDocumentServicePort;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
public class InputDocumentService implements InputDocumentServicePort {

    @Value("${base-microservice-url}")
    private String baseMicroserviceUrl;

    @Value("${microservice-url.document.input}")
    private String inputDocumentMicroserviceAPI;
    @Value("${microservice-url.document.inputbatch}")
    private String inputDocumentBatchMicroserviceAPI;
    private final Logger log = LoggerFactory.getLogger(InputDocumentService.class);
    private static final String TENANT_ID_REQUEST_PARAM = "?tenantId=";
    @Autowired
    MicroserviceHttpGateway microserviceHttpGateway;

    @Autowired
    DocumentParserUtil documentparserUtil;

    @Override
    public IngestionResponse addNRTDocuments(String tableName, String payload,int tenantId, String jwtToken) {

        IngestionResponse ingestionResponseDTO = new IngestionResponse();

        Map<String, Map<String, Object>> schemaKeyValuePair = documentparserUtil.getSchemaOfCollection(baseMicroserviceUrl, tableName,tenantId, jwtToken);

        if (schemaKeyValuePair.containsKey("error")) {

        	 generateResponse(ingestionResponseDTO, "Table "+tableName+ " Having TenantID: "+tenantId + " Not Found");
             return ingestionResponseDTO;
        }

        String message = verify(payload, schemaKeyValuePair);

        if (!message.equalsIgnoreCase("")) {
            generateResponse(ingestionResponseDTO, message);
            return ingestionResponseDTO;
        }
        String url = baseMicroserviceUrl + inputDocumentMicroserviceAPI  + "/" +tableName
				+TENANT_ID_REQUEST_PARAM + tenantId;

        JSONObject jsonObject = uploadData( payload,url,jwtToken);

        ingestionResponseDTO.setMessage(jsonObject.get("message").toString());
        ingestionResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return ingestionResponseDTO;

    }

    private JSONObject uploadData(String payload,String url, String jwtToken) {

       
        log.debug("calling url: {} ",url);
        microserviceHttpGateway.setApiEndpoint(url);
        microserviceHttpGateway.setRequestBodyDTO(payload);
        String jsonString = microserviceHttpGateway.postRequestWithStringBody(jwtToken);
        documentparserUtil.isJwtAuthenticationError(jsonString);
        return new JSONObject(jsonString);
    }

    private void generateResponse(IngestionResponse ingestionResponseDTO, String message) {
        log.debug(message);
        ingestionResponseDTO.setMessage(message);
        ingestionResponseDTO.setStatusCode(108);
    }

    private String verify(String payload, Map<String, Map<String, Object>> schemaKeyValuePair) {
        JSONArray payloadJSONArray = null;
        try {
            payloadJSONArray = new JSONArray(payload);
        } catch (Exception e) {
            // TRY BY REMOVING THE QUOTES FROM THE STRING
            try {
                payload = payload.substring(1, payload.length() - 1);
                payloadJSONArray = new JSONArray(payload.substring(1, payload.length() - 1));
            } catch (Exception e1) {
                log.error(e.getMessage());
                return "Invalid input JSON array of document.";
            }
        }

        // TO CHECK IF THE INPUT DOCUMENT SATISFIES THE SCHEMA
        for (int i = 0; i < payloadJSONArray.length(); i++) {

            JSONObject jsonSingleObject = (JSONObject) payloadJSONArray.get(i);

            DocumentParserUtil.DocumentSatisfiesSchemaResponse documentSatisfiesSchemaResponse = documentparserUtil.isDocumentSatisfySchema(schemaKeyValuePair, jsonSingleObject);

            if (!documentSatisfiesSchemaResponse.isObjectSatisfiesSchema()) {

                // ERROR IN A DOCUMENT STRUCTURE
                return "The JSON input document in the array doesn't satisfy the Schema. Error: " + documentSatisfiesSchemaResponse.getMessage();
            }

        }
        return "";

    }

    @Override
    public IngestionResponse addDocument(String tableName, String payload, int tenantId, String jwtToken) {

        IngestionResponse ingestionResponseDTO = new IngestionResponse();

        Map<String, Map<String, Object>> schemaKeyValuePair = documentparserUtil.getSchemaOfCollection(baseMicroserviceUrl, tableName,tenantId, jwtToken);
        if (schemaKeyValuePair.containsKey("error")) {

            generateResponse(ingestionResponseDTO, "Table "+tableName+ " Having TenantID: "+tenantId + " Not Found");
            return ingestionResponseDTO;
        }

        String message = verify(payload, schemaKeyValuePair);

        if (!message.equalsIgnoreCase("")) {
            generateResponse(ingestionResponseDTO, message);
            return ingestionResponseDTO;
        }
        String url = baseMicroserviceUrl + inputDocumentBatchMicroserviceAPI  + "/" +tableName
				+TENANT_ID_REQUEST_PARAM + tenantId;
        JSONObject jsonObject = uploadData(payload,url, jwtToken);

        ingestionResponseDTO.setMessage(jsonObject.get("message").toString());
        ingestionResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return ingestionResponseDTO;

    }
}
