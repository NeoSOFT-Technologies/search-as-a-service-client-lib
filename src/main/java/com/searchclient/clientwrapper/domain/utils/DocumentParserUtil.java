package com.searchclient.clientwrapper.domain.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchclient.clientwrapper.service.SolrDocumentService;

import lombok.Data;

@Service
public class DocumentParserUtil {

	@Autowired
	MicroserviceHttpGateway microserviceHttpGateway;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Value("${base-solr-url}")
	private String baseSolrUrl;

	private final Logger log = LoggerFactory.getLogger(SolrDocumentService.class);

	private static boolean isNumeric(String string) {
		Logger log = LoggerFactory.getLogger(DocumentParserUtil.class);

		long longValue;

		log.debug(String.format("Parsing string: \"%s\"", string));

		if (string == null || string.equals("")) {
			log.debug("String cannot be parsed, it is null or empty.");
			return false;
		}
		try {
			longValue = Long.parseLong(string);
			return true;
		} catch (NumberFormatException e) {
			log.debug("Input String cannot be parsed to Integer.");
		}
		return false;
	}

	private static boolean isBoolean(Object object) {
		Logger log = LoggerFactory.getLogger(DocumentParserUtil.class);

		String objectAsString = object.toString();

		log.debug(String.format("isBoolean Check: %s , Object as String: %s ", object, objectAsString));

		if (object.getClass().equals(Boolean.class) || objectAsString.equals("true") || objectAsString.equals("True")
				|| objectAsString.equals("false") || objectAsString.equals("False")) {
			return true;
		} else if (isNumeric(objectAsString)) {
			if (Integer.parseInt(objectAsString) == 1 || Integer.parseInt(objectAsString) == 0) {
				return true;
			}
		}

		return false;

	}

	public DocumentSatisfiesSchemaResponse checkIfRequiredFieldsAreAvailable(
			Map<String, Map<String, Object>> schemaKeyValuePair, JSONObject payloadJSON) {
		for (Map.Entry<String, Map<String, Object>> entry : schemaKeyValuePair.entrySet()) {
			if (entry.getValue().containsKey("required")) {
				if (entry.getValue().get("required").toString().equals("true")) {
					if (!payloadJSON.has(entry.getKey())) {
						return new DocumentSatisfiesSchemaResponse(false,
								"Required field is missing in document. Field: " + entry.getKey());
					}
				}
			}
		}

		return new DocumentSatisfiesSchemaResponse(true, "All the required fields are available.");

	}

	public DocumentSatisfiesSchemaResponse isDocumentSatisfySchema(Map<String, Map<String, Object>> schemaKeyValuePair,
			JSONObject payloadJSON) {
		Logger log = LoggerFactory.getLogger(DocumentParserUtil.class);

		DocumentSatisfiesSchemaResponse checkIfRequireFieldsAreAvailableResponse = checkIfRequiredFieldsAreAvailable(
				schemaKeyValuePair, payloadJSON);

		if (!checkIfRequireFieldsAreAvailableResponse.isObjectSatisfiesSchema()) {
			// A REQUIRED FIELD IS MISSING IN THE INPUT JSON DOCUMENT

			return checkIfRequireFieldsAreAvailableResponse;
		}

		Iterator<String> itr = payloadJSON.keySet().iterator();

		// ITERATE THROUGH EACH KEY IN THE INPUT JSON OBJECT PAYLOAD
		while (itr.hasNext()) {

			String payloadJsonObjectKey = itr.next();
			Object payloadJsonObjectValue = payloadJSON.get(payloadJsonObjectKey);

			log.debug(payloadJsonObjectKey + "=" + payloadJsonObjectValue);
			// log.debug(schemaKeyValuePair.get(payloadJsonObjectKey).toString());

			if (schemaKeyValuePair.containsKey(payloadJsonObjectKey)) {

				if (!payloadJsonObjectKey.equals("id")) {

					Map<String, Object> fieldValueForTheKey = schemaKeyValuePair.get(payloadJsonObjectKey);

					if (fieldValueForTheKey.get("type") == null) {

						String message = "key is not define in schema";
						log.debug(message);
						return new DocumentSatisfiesSchemaResponse(true, message);
					} else {

						String fieldTypeDefinedInSchema = fieldValueForTheKey.get("type").toString();

						boolean isMultivaluedField = false;
						if (fieldValueForTheKey.containsKey("multiValued")) {
							if (fieldValueForTheKey.get("multiValued").toString().equals("true")) {
								isMultivaluedField = true;
							}
						}
						if (fieldValueForTheKey.containsKey("multiValue")) {
							if (fieldValueForTheKey.get("multiValue").toString().equals("true")) {
								isMultivaluedField = true;
							}
						}

						// log.debug(fieldTypeDefinedInSchema);

						switch (fieldTypeDefinedInSchema) {
						case "string":
						case "strings":
							if (isMultivaluedField) {
								if (!payloadJsonObjectValue.getClass().equals(JSONArray.class)) {
									String message = "Multivalued string field should be a JSONArray of strings.";
									log.debug(message);
									return new DocumentSatisfiesSchemaResponse(false, message);
								}
							} else {
								if (!payloadJsonObjectValue.getClass().equals(String.class)) {
									String message = "String field cannot accept non-string values or array of strings.";
									log.debug(message);
									return new DocumentSatisfiesSchemaResponse(false, message);
								}
							}
							break;

						case "plong":
						case "plongs":
							if (isMultivaluedField) {
								if (!payloadJsonObjectValue.getClass().equals(JSONArray.class)) {
									String message = "Multivalued long field should be a JSONArray of longs.";
									log.debug(message);
									return new DocumentSatisfiesSchemaResponse(false, message);
								} else {
									log.debug("JSONArray of Long Numbers/Strings");
									JSONArray jsonArrayOfLongOrStrings = (JSONArray) payloadJsonObjectValue;

									for (int i = 0; i < jsonArrayOfLongOrStrings.length(); i++) {
										if (!isNumeric(jsonArrayOfLongOrStrings.get(i).toString())) {
											String message = "Multivalued long field's JSONArray contains non-long value.";
											log.debug(message);
											return new DocumentSatisfiesSchemaResponse(false, message);
										}
									}
								}
							} else {
								if (!isNumeric(payloadJsonObjectValue.toString())) {
									String message = "Long field cannot accept non-long values or array of longs.";
									log.debug(message);
									return new DocumentSatisfiesSchemaResponse(false, message);
								}
							}
							break;

						case "boolean":
						case "boolean_":
						case "booleans":
							if (isMultivaluedField) {
								if (!payloadJsonObjectValue.getClass().equals(JSONArray.class)) {
									String message = "Multivalued boolean field should be a JSONArray of booleans.";
									log.debug(message);
									return new DocumentSatisfiesSchemaResponse(false, message);
								} else {
									log.debug("JSONArray of Booleans/Strings");
									JSONArray jsonArrayOfBooleanOrStrings = (JSONArray) payloadJsonObjectValue;

									for (int i = 0; i < jsonArrayOfBooleanOrStrings.length(); i++) {
										if (!isBoolean(jsonArrayOfBooleanOrStrings.get(i))) {
											String message = "Multivalued boolean field's JSONArray contains non-boolean value.";
											log.debug(message);
											return new DocumentSatisfiesSchemaResponse(false, message);
										}
									}
								}
							} else {
								if (!isBoolean(payloadJsonObjectValue)) {
									String message = "Boolean field cannot accept non-boolean values or array of booleans.";
									log.debug(message);
									return new DocumentSatisfiesSchemaResponse(false, message);
								}
							}
							break;

						default:
							return new DocumentSatisfiesSchemaResponse(true, "allow all data-types");
						}
					}
				} else {
					// OBJECT KEY IS "id" and hence it can be string or long but ultimately
					// converted into long
					log.debug(
							"Object key IS \"id\" and hence it can be string or long but ultimately converted into long");
				}
			} else {
				String message = "Input JSON Object's key doesn't exists in the Schema. Please check the input document or add new field to the schema.";
				log.debug(message);

				return new DocumentSatisfiesSchemaResponse(false, message);
			}
		}

		return new DocumentSatisfiesSchemaResponse(true, "Success!");
	}

	public Map<String, Map<String, Object>> getSchemaOfCollection(String baseMicroserviceUrl,
			String collectionName) {

		// THIS METHOD HITS THE GET SCHEMA METHOD OF THE MICROSERVICE AND GETS THE
		// SCHEMA
		// THE "ATTRIBUTE" NODE CONSISTS OF THE LIST OF ALL THE SCHEMA FIELDS
		// (ATTRIBUTES)

		Logger log = LoggerFactory.getLogger(DocumentParserUtil.class);

		//MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		String url = baseMicroserviceUrl + "/api/schema/" + collectionName;

		microserviceHttpGateway.setApiEndpoint(url);
		microserviceHttpGateway.setRequestBodyDTO(null);

		JSONObject jsonObject = microserviceHttpGateway.getRequest();

		JSONArray jsonArrayOfAttributesFields = (JSONArray) jsonObject.get("attributes");

		
		
		ObjectMapper objectMapper = new ObjectMapper();

		List<Map<String, Object>> schemaResponseFields = jsonArrayOfAttributesFields.toList().stream()
				.map(eachField -> (Map<String, Object>) objectMapper.convertValue(eachField, Map.class))
				.collect(Collectors.toList());
		
		// Converting response schema from Solr to HashMap for quick access
		// Key contains the field name and value contains the object which has schema
		// description of that key eg. multivalued etc
		Map<String, Map<String, Object>> schemaKeyValuePair = new HashMap<>();
		schemaResponseFields
				.forEach((fieldObject) -> schemaKeyValuePair.put(fieldObject.get("name").toString(), fieldObject));

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

}