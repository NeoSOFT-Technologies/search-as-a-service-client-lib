package com.searchclient.clientwrapper.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchclient.clientwrapper.domain.CapacityPlanProperties;
import com.searchclient.clientwrapper.domain.ManageTable;
import com.searchclient.clientwrapper.domain.ManageTableResponse;
import com.searchclient.clientwrapper.domain.Response;
import com.searchclient.clientwrapper.domain.port.api.ManageTableServicePort;
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
public class ManageTableService implements ManageTableServicePort {
	private final Logger logger = LoggerFactory.getLogger(ManageTableService.class);

	private static final String TENANT_ID_REQUEST_PARAM = "?tenantId=";
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

	@Value("${microservice-url.manage-table.get-capacityplan}")
	private String getcapacityplansMicroserviceAPI;
	
	@Value("${microservice-url.manage-table.get-all-collections}")
	private String getAllCollectionsMicroserviceAPI;
	
	@Value("${microservice-url.manage-table.get-deleted-collections}")
	private String getAllDeletedCollectionsMicroserviceAPI;
	
	@Value("${microservice-url.manage-table.get-deleted-collections-tenantId}")
	private String getAllDeletedCollectionsWithTenantIdMicroserviceAPI;
	
	@Value("${microservice-url.manage-table.get-collections-pagination}")
	private String getAllCollectionsWithPaginationMicroserviceAPI;

	private static final String PAGE_NUMBER = "?pageNumber=";
	private static final String PAGE_SIZE = "&pageSize=";
	@Autowired
	CapacityPlanProperties capacityPlanProperties;

	@Autowired
	MicroserviceHttpGateway microserviceHttpGateway;

	@Autowired
	DocumentParserUtil docParserUtil;

	ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@Override
	public CapacityPlanProperties capacityPlans(String jwtToken) {

		logger.debug("capacity Plans");		
		CapacityPlanProperties getCapacityPlans = new CapacityPlanProperties();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getcapacityplansMicroserviceAPI);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			getCapacityPlans = objectMapper.readValue(jsonObject, CapacityPlanProperties.class);
			return getCapacityPlans;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return getCapacityPlans;
		}

	}

	@Override
	public Response getTablesByTenantId(int tenantId, String jwtToken) {

		logger.debug("Get Tables for TenantID: {}", tenantId);
		Response response = new Response();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getCollectionsMicroserviceAPI + "/" 
				+TENANT_ID_REQUEST_PARAM + tenantId);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}
	
	@Override
	public Response getAllTablesFromServer(int pageNumber, int pageSize, String jwtToken) {

		logger.debug("Get All Tables From The Server");
		Response response = new Response();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getAllCollectionsMicroserviceAPI
				+ PAGE_NUMBER + pageNumber + PAGE_SIZE + pageSize);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);

		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}
	
	@Override
	public Response getAllTablesForTenantIdWithPagination(int tenantId, int pageNumber, int pageSize, String jwtToken) {

		logger.debug("Get All Tables");
		Response response = new Response();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getAllCollectionsWithPaginationMicroserviceAPI+
				TENANT_ID_REQUEST_PARAM	+ tenantId + PAGE_NUMBER.replace("?", "&") + pageNumber + PAGE_SIZE + pageSize);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}
	
	@Override
	public Response getAllDeletedTables(int pageNumber, int pageSize, String jwtToken) {

		logger.debug("Get All Tables Under Deletion");
		Response response = new Response();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getAllDeletedCollectionsMicroserviceAPI +
				PAGE_NUMBER + pageNumber + PAGE_SIZE + pageSize);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}

	@Override
	public Response getAllDeletedTablesWithTenantId(int tenantId, int pageNumber, int pageSize, String jwtToken) {

		logger.debug("Get All Tables Under Deletion With TenantId");
		Response response = new Response();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getAllDeletedCollectionsWithTenantIdMicroserviceAPI 
				+ TENANT_ID_REQUEST_PARAM	+ tenantId + PAGE_NUMBER.replace("?", "&") + PAGE_SIZE + pageSize);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}
	
	@Override
	public ManageTableResponse getTableInfo(String tableName, int tenantId, String jwtToken) {

		logger.debug("get Table ");
		ManageTableResponse response = new ManageTableResponse();
		microserviceHttpGateway
				.setApiEndpoint(baseMicroserviceUrl + getCollectionsMicroserviceAPI + "/" +tableName
						+TENANT_ID_REQUEST_PARAM + tenantId);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, ManageTableResponse.class);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}

	@Override
	public Response delete(int tenantId, String tableName, String jwtToken) {
		logger.debug("Delete Table ");
		Response response = new Response();
		microserviceHttpGateway
				.setApiEndpoint(baseMicroserviceUrl + deleteMicroserviceAPI + "/" +tableName
						+TENANT_ID_REQUEST_PARAM + tenantId);
		String jsonObject = microserviceHttpGateway.deleteRequest(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}

	@Override
	public Response create(int tenantId, ManageTable manageTableDTO, String jwtToken) {
		logger.debug("create Table ");
		Response response = new Response();

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + createMicroserviceAPI + "/"
				+TENANT_ID_REQUEST_PARAM + tenantId);
		microserviceHttpGateway.setRequestBodyDTO(manageTableDTO);

		String jsonObject = microserviceHttpGateway.postRequest(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			return response;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}

	@Override
	public Response update(String tableName, int tenantId, ManageTable tableSchema, String jwtToken) {
		logger.debug("update Table ");
		Response response = new Response();

		microserviceHttpGateway
				.setApiEndpoint(baseMicroserviceUrl + renameMicroserviceAPI + "/" +tableName
						+TENANT_ID_REQUEST_PARAM + tenantId);
		microserviceHttpGateway.setRequestBodyDTO(tableSchema);

		String jsonObject = microserviceHttpGateway.putRequest(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}

	@Override
	public Response restoreTable(int tenantId, String tableName, String jwtToken) {

		logger.debug("restore Table ");

		Response response = new Response();

		microserviceHttpGateway
				.setApiEndpoint(baseMicroserviceUrl + restoreMicroserviceAPI + "/" +tableName
						+TENANT_ID_REQUEST_PARAM + tenantId);
		String jsonObject = microserviceHttpGateway.putRequest(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return response;
		}

	}
}
