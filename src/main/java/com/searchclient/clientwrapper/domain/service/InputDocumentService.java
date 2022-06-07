package com.searchclient.clientwrapper.domain.service;

import java.util.Map;
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
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String STATUS_CODE = "statusCode";
    @Autowired
    MicroserviceHttpGateway microserviceHttpGateway;

    @Autowired
    DocumentParserUtil documentparserUtil;

    @Override
    public IngestionResponse addNRTDocuments(String tableName, String payload,int tenantId, String jwtToken) {

        IngestionResponse ingestionResponseDTO = new IngestionResponse();

        Map<String, Map<String, Object>> schemaKeyValuePair = documentparserUtil.getSchemaOfCollection(baseMicroserviceUrl, tableName,tenantId, jwtToken);

        if (schemaKeyValuePair.containsKey(ERROR)) {

        	ingestionResponseDTO.setStatusCode((int) schemaKeyValuePair.get(ERROR).get(STATUS_CODE));
        	String responseMessage = schemaKeyValuePair.get(ERROR).get(MESSAGE).toString();
        	generateResponse(ingestionResponseDTO, responseMessage);
            return ingestionResponseDTO;
        }

        String url = baseMicroserviceUrl + inputDocumentMicroserviceAPI  + "/" +tableName
				+TENANT_ID_REQUEST_PARAM + tenantId;

        JSONObject jsonObject = uploadData( payload,url,jwtToken);

        ingestionResponseDTO.setMessage(jsonObject.get(MESSAGE).toString());
        ingestionResponseDTO.setStatusCode((int) jsonObject.get(STATUS_CODE));

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
    }

    @Override
    public IngestionResponse addDocument(String tableName, String payload, int tenantId, String jwtToken) {

        IngestionResponse ingestionResponseDTO = new IngestionResponse();
        Map<String, Map<String, Object>> schemaKeyValuePair = documentparserUtil.getSchemaOfCollection(baseMicroserviceUrl, tableName,tenantId, jwtToken);
        if (schemaKeyValuePair.containsKey(ERROR)) {

        	String responseMessage = schemaKeyValuePair.get(ERROR).get(MESSAGE).toString();
        	ingestionResponseDTO.setStatusCode((int) schemaKeyValuePair.get(ERROR).get(STATUS_CODE));
            generateResponse(ingestionResponseDTO, responseMessage);
            return ingestionResponseDTO;
        }

        String url = baseMicroserviceUrl + inputDocumentBatchMicroserviceAPI  + "/" +tableName
				+TENANT_ID_REQUEST_PARAM + tenantId;
        JSONObject jsonObject = uploadData(payload,url, jwtToken);

        ingestionResponseDTO.setMessage(jsonObject.get(MESSAGE).toString());
        ingestionResponseDTO.setStatusCode((int) jsonObject.get(STATUS_CODE));

        return ingestionResponseDTO;

    }
}
