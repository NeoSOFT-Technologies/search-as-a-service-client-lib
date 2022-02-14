package com.searchclient.clientwrapper.domain.service;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;
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
	
	private ResponseDTO extracted(ResponseDTO solrResponseDTO, String message) {
		log.debug(message);
		solrResponseDTO.setResponseMessage(message);
		solrResponseDTO.setStatusCode(400);
		return solrResponseDTO;
	}
	
	private void extracted(String payload, ResponseDTO solrResponseDTO, String url) {
		microserviceHttpGateway.setApiEndpoint(url);
		microserviceHttpGateway.setRequestBodyDTO(payload);
		String jsonString = microserviceHttpGateway.postRequestWithStringBody();

		JSONObject jsonObject = new JSONObject(jsonString);

		solrResponseDTO.setResponseMessage(jsonObject.get("responseMessage").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
	}


	@Override
	public ResponseDTO addDocuments(String collectionName, String payload, int clientid) {

		ResponseDTO solrResponseDTO = new ResponseDTO(collectionName);
		Map<String, Map<String, Object>> schemaKeyValuePair = documentparserUtil
				.getSchemaOfCollections(baseMicroserviceUrl, collectionName, clientid);

		if (schemaKeyValuePair == null) {

			String message = "Unable to get the Schema. Please check the collection name again!";
			return extracted(solrResponseDTO, message);
		}
		JSONArray payloadJSONArray = null;
		try {
			payloadJSONArray = new JSONArray(payload);
		} catch (Exception e) {
			// TRY BY REMOVING THE QUOTES FROM THE STRING
			try {
				payload = payload.substring(1, payload.length() - 1);
				payloadJSONArray = new JSONArray(payload);
			} catch (Exception e1) {
				log.debug(e.toString());
				log.debug(e1.toString());

				String message = "Invalid input JSON array of document.";
				return extracted(solrResponseDTO, message);
			}
		}

		// TO CHECK IF THE INPUT DOCUMENT SATISFIES THE SCHEMA
		for (int i = 0; i < payloadJSONArray.length(); i++) {

			JSONObject jsonSingleObject = (JSONObject) payloadJSONArray.get(i);

			DocumentParserUtil.DocumentSatisfiesSchemaResponse documentSatisfiesSchemaResponse = documentparserUtil
					.isDocumentSatisfySchema(schemaKeyValuePair, jsonSingleObject);

			if (!documentSatisfiesSchemaResponse.isObjectSatisfiesSchema()) {

				// ERROR IN A DOCUMENT STRUCTURE
				solrResponseDTO.setResponseMessage(documentSatisfiesSchemaResponse.getMessage());
				solrResponseDTO.setResponseMessage("The JSON input document in the array doesn't satisfy the Schema. Error: "
						+ documentSatisfiesSchemaResponse.getMessage());
				solrResponseDTO.setStatusCode(400);
				return solrResponseDTO;

			}

		}

		String url = baseMicroserviceUrl + inputDocumentMicroserviceAPI + "/ingest-nrt/" + clientid + "/" + collectionName;

		extracted(payload, solrResponseDTO, url);

		return solrResponseDTO;

	}

	


	@Override
	public ResponseDTO addDocument(String collectionName, String payload, int clientid) {
		ResponseDTO solrResponseDTO = new ResponseDTO(collectionName);
		Map<String, Map<String, Object>> schemaKeyValuePair = documentparserUtil
				.getSchemaOfCollections(baseMicroserviceUrl, collectionName, clientid);

		if (schemaKeyValuePair == null) {

			String message = "Unable to get the Schema. Please check the collection name again!";
			return extracted(solrResponseDTO, message);
		}
		JSONArray payloadJSONArray = null;
		try {
			payloadJSONArray = new JSONArray(payload);
		} catch (Exception e) {
			// TRY BY REMOVING THE QUOTES FROM THE STRING
			try {
				payload = payload.substring(1, payload.length() - 1);
				payloadJSONArray = new JSONArray(payload);
			} catch (Exception e1) {
				log.debug(e.toString());
				log.debug(e1.toString());

				String message = "Invalid input JSON array of document.";
				return extracted(solrResponseDTO, message);
			}
		}

		// TO CHECK IF THE INPUT DOCUMENT SATISFIES THE SCHEMA
		for (int i = 0; i < payloadJSONArray.length(); i++) {

			JSONObject jsonSingleObject = (JSONObject) payloadJSONArray.get(i);

			DocumentParserUtil.DocumentSatisfiesSchemaResponse documentSatisfiesSchemaResponse = documentparserUtil
					.isDocumentSatisfySchema(schemaKeyValuePair, jsonSingleObject);

			if (!documentSatisfiesSchemaResponse.isObjectSatisfiesSchema()) {

				// ERROR IN A DOCUMENT STRUCTURE
				solrResponseDTO.setResponseMessage(documentSatisfiesSchemaResponse.getMessage());
				solrResponseDTO.setResponseMessage("The JSON input document in the array doesn't satisfy the Schema. Error: "
						+ documentSatisfiesSchemaResponse.getMessage());
				solrResponseDTO.setStatusCode(400);
				return solrResponseDTO;

			}

		}



		String url = baseMicroserviceUrl + inputDocumentMicroserviceAPI + "/ingest/" + clientid + "/" + collectionName;

		extracted(payload, solrResponseDTO, url);
		return solrResponseDTO;
	}

}
