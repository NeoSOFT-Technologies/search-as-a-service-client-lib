package com.searchclient.clientwrapper.domain.service;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchclient.clientwrapper.domain.CapacityPlanProperties;
import com.searchclient.clientwrapper.domain.ManageTableCreate;
import com.searchclient.clientwrapper.domain.ManageTableResponse;
import com.searchclient.clientwrapper.domain.ManageTableUpdate;
import com.searchclient.clientwrapper.domain.Response;
import com.searchclient.clientwrapper.domain.port.api.ManageTableServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
public class ManageTableService implements ManageTableServicePort {

    private final Logger log = LoggerFactory.getLogger(ManageTableService.class);

    @Value("${base-microservice-url}")
    private String baseMicroserviceUrl;

    @Value("${microservice-url.manage-table.create}")
    private String createMicroserviceAPI;

    @Value("${microservice-url.manage-table.delete}")
    private String deleteMicroserviceAPI;
    
    @Value("${microservice-url.manage-table.restore}")
    private String restoreMicroserviceAPI;

    @Value("${microservice-url.manage-table.update}")
    private String renameMicroserviceAPI;

    @Value("${microservice-url.manage-table.get-collections}")
    private String getCollectionsMicroserviceAPI;

    @Value("${microservice-url.manage-table.get-schema}")
    private String getMicroserviceAPI;

    @Value("${microservice-url.manage-table.get-capacityplan}")
    private String getcapacityplansMicroserviceAPI;

    @Autowired
    CapacityPlanProperties capacityPlanProperties;

    @Autowired
    MicroserviceHttpGateway microserviceHttpGateway;

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public CapacityPlanProperties capacityPlans() {

        CapacityPlanProperties solrgetCapacityPlans = new CapacityPlanProperties();
        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getcapacityplansMicroserviceAPI);
        String jsonObject = microserviceHttpGateway.getRequestV2();
        try {        
            solrgetCapacityPlans = objectMapper.readValue(jsonObject, CapacityPlanProperties.class);

        
        } catch (Exception e) {
            log.error(e.getMessage());
        }


        return solrgetCapacityPlans;

    }

    @Override
    public Response getTables(int clientId) {

        Response response = new Response();

        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getCollectionsMicroserviceAPI + "/" + clientId);
        String jsonObject = microserviceHttpGateway.getRequestV2();
        try {
            response = objectMapper.readValue(jsonObject, Response.class);

        } catch (Exception e) {
            log.error(e.getMessage());
            
            
        }

     

        return response;

    }

    @Override
    public ManageTableResponse getTable(String tableName, int clientId) {

        ManageTableResponse response = new ManageTableResponse();

        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getCollectionsMicroserviceAPI + "/" + clientId + "/" + tableName);
        String jsonObject = microserviceHttpGateway.getRequestV2();
        try {
            response = objectMapper.readValue(jsonObject, ManageTableResponse.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
       
        return response;

    }

    @Override
    public Response delete(int clientId,String tableName) {

        Response response = new Response();

        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + deleteMicroserviceAPI +"/"+clientId+ "/" + tableName);
        String jsonObject = microserviceHttpGateway.deleteRequest();
        try {
            response = objectMapper.readValue(jsonObject, Response.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return response;

    }

   
   

    @Override
    public Response create(int clientId, ManageTableCreate manageTableDTO) {

        Response response = new Response();

        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + createMicroserviceAPI+"/"+clientId);
        microserviceHttpGateway.setRequestBodyDTO(manageTableDTO);

        String jsonObject = microserviceHttpGateway.postRequest();
        try {
            response = objectMapper.readValue(jsonObject, Response.class);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return response;

    }
    
    @Override
    public Response update(String tableName,int clientId, ManageTableUpdate tableSchema) {
        log.debug("Update Solr Schema");

        Response response = new Response();

        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + renameMicroserviceAPI + "/"+clientId+"/" + tableName);
        microserviceHttpGateway.setRequestBodyDTO(tableSchema);

        String jsonObject = microserviceHttpGateway.putRequest();

        try {
            response = objectMapper.readValue(jsonObject, Response.class);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return response;
    }

	@Override
	public Response restoreTable(int clientId, String tableName) {
		 Response response = new Response();

	        microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + restoreMicroserviceAPI +"/"+clientId+ "/" + tableName);
	        String jsonObject = microserviceHttpGateway.putRequest();
	        try {
	            response = objectMapper.readValue(jsonObject, Response.class);
	        } catch (IOException e) {
	            log.error(e.getMessage());
	        }

	        return response;
	}
}
