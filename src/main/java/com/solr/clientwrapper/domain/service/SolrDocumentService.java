package com.solr.clientwrapper.domain.service;

import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrDocumentServicePort;
import com.solr.clientwrapper.domain.utils.DocumentParserUtil;
import com.solr.clientwrapper.domain.utils.MicroserviceHttpGateway;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Map;

@Service
@Transactional
public class SolrDocumentService implements SolrDocumentServicePort {

	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;

	private final Logger log = LoggerFactory.getLogger(SolrDocumentService.class);

	@Override
	public SolrResponseDTO addDocuments(String collectionName, String payload, boolean isNRT) {

		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);

		Map<String, Map<String, Object>> schemaKeyValuePair= DocumentParserUtil.getSchemaOfCollection(baseMicroserviceUrl,collectionName);
		if(schemaKeyValuePair == null){
			String message="Unable to get the Schema. Please check the collection name again!";
			log.debug(message);
			solrResponseDTO.setMessage(message);
			solrResponseDTO.setStatusCode(400);
			return solrResponseDTO;
		}

		JSONArray payloadJSONArray=null;
		try {
			payloadJSONArray = new JSONArray(payload);
		}catch (Exception e){
			//TRY BY REMOVING THE QUOTES FROM THE STRING
			try{
				payload=payload.substring(1, payload.length() - 1);
				payloadJSONArray = new JSONArray(payload);
			}catch (Exception e1){
				log.debug(e.toString());
				log.debug(e1.toString());

				String message="Invalid input JSON array of document.";
				log.debug(message);
				solrResponseDTO.setMessage(message);
				solrResponseDTO.setStatusCode(400);
				return solrResponseDTO;
			}
		}

		//TO CHECK IF THE INPUT DOCUMENT SATISFIES THE SCHEMA
		for(int i=0;i<payloadJSONArray.length();i++){

			JSONObject jsonSingleObject= (JSONObject) payloadJSONArray.get(i);

			DocumentParserUtil.DocumentSatisfiesSchemaResponse documentSatisfiesSchemaResponse =
					DocumentParserUtil.isDocumentSatisfySchema(schemaKeyValuePair,jsonSingleObject);

			if(documentSatisfiesSchemaResponse.isObjectSatisfiesSchema()){

				SolrInputDocument solrInputSingleDocument=new SolrInputDocument();

				Iterator<String> itr = jsonSingleObject.keySet().iterator();
				while (itr.hasNext())
				{
					String payloadJsonObjectKey = itr.next();
					Object payloadJsonObjectValue = jsonSingleObject.get(payloadJsonObjectKey);

					solrInputSingleDocument.addField(payloadJsonObjectKey,payloadJsonObjectValue);
				}

			}else{
				//ERROR IN A DOCUMENT STRUCTURE
				solrResponseDTO.setMessage(documentSatisfiesSchemaResponse.getMessage());
				solrResponseDTO.setMessage("The JSON input document in the array doesn't satisfy the Schema. Error: "+ documentSatisfiesSchemaResponse.getMessage());
				solrResponseDTO.setStatusCode(400);
				return solrResponseDTO;
			}
		}


		//IF THE FLOW HAS REACHED HERE, IT MEANS THE INPUT JSON DOCUMENT SATISFIES THE SCHEMA.
		MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();

		String url=baseMicroserviceUrl + "/documents/" + collectionName;

		if(isNRT){
			url+="?isNRT=true";
		}else{
			url+="?isNRT=false";
		}

		microserviceHttpGateway.setApiEndpoint(url);
		microserviceHttpGateway.setRequestBodyDTO(payload);
		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));

		return solrResponseDTO;

	}

}
