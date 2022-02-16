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
import com.searchclient.clientwrapper.domain.port.api.SolrDocumentServicePort;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
public class SolrDocumentService implements SolrDocumentServicePort {

    @Value("${base-microservice-url}")
    private String baseMicroserviceUrl;

    @Value("${microservice-url.document.input}")
    private String inputDocumentMicroserviceAPI;

    private final Logger log = LoggerFactory.getLogger(SolrDocumentService.class);

    @Autowired
    MicroserviceHttpGateway microserviceHttpGateway;

    @Autowired
    DocumentParserUtil documentparserUtil;

    @Override
    public IngestionResponse addDocuments(String collectionName, String payload, boolean isNRT) {

        IngestionResponse solrResponseDTO = new IngestionResponse();

        Map<String, Map<String, Object>> schemaKeyValuePair = documentparserUtil.getSchemaOfCollection(baseMicroserviceUrl, collectionName);

        if (schemaKeyValuePair == null) {

            generateResponse(solrResponseDTO, "Unable to get the Schema. Please check the collection name again!");
            return solrResponseDTO;
        }

        String message = verify(payload, schemaKeyValuePair);

        if (!message.equalsIgnoreCase("")) {
            generateResponse(solrResponseDTO, message);
            return solrResponseDTO;
        }

        JSONObject jsonObject = uploadData(collectionName, payload, isNRT);

        solrResponseDTO.setMessage(jsonObject.get("message").toString());
        solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

        return solrResponseDTO;

    }

    private JSONObject uploadData(String collectionName, String payload, boolean isNRT) {
        String url = baseMicroserviceUrl + inputDocumentMicroserviceAPI + "/" + collectionName;

        if (isNRT) {
            url += "?isNRT=true";
        } else {
            url += "?isNRT=false";
        }

        microserviceHttpGateway.setApiEndpoint(url);
        microserviceHttpGateway.setRequestBodyDTO(payload);
        String jsonString = microserviceHttpGateway.postRequestWithStringBody();

        return new JSONObject(jsonString);
    }

    private void generateResponse(IngestionResponse solrResponseDTO, String message) {
        log.debug(message);
        solrResponseDTO.setMessage(message);
        solrResponseDTO.setStatusCode(400);
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
}
