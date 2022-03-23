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
import com.searchclient.clientwrapper.domain.utils.DocumentParserUtil;
import com.searchclient.clientwrapper.domain.utils.LoggerUtils;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;
import com.searchclient.clientwrapper.dto.logger.LoggersDTO;

@Service
public class ManageTableService implements ManageTableServicePort {
	private final Logger logger = LoggerFactory.getLogger(ManageTableService.class);

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

	@Autowired
	DocumentParserUtil docParserUtil;

	ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private String servicename = "Manage_Table_Service";
	private String username = "Username";

	LoggersDTO loggersDTO = new LoggersDTO();
	
	
	private void Logger(String timestamp ,String nameofCurrMethod) {
		
		loggersDTO = LoggerUtils.getRequestLoggingInfo(username, servicename, nameofCurrMethod, timestamp);

		LoggerUtils.printlogger(loggersDTO, true, false);

		
		
	}

	@Override
	public CapacityPlanProperties capacityPlans(String jwtToken) {

		logger.debug("capacity Plans");

		String timestamp = LoggerUtils.utcTime().toString();

		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

		Logger(timestamp, nameofCurrMethod);
		
		CapacityPlanProperties solrgetCapacityPlans = new CapacityPlanProperties();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getcapacityplansMicroserviceAPI);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			solrgetCapacityPlans = objectMapper.readValue(jsonObject, CapacityPlanProperties.class);
			timestamp = LoggerUtils.utcTime().toString();
			loggersDTO.setTimestamp(timestamp);
			LoggerUtils.printlogger(loggersDTO, false, false);
			return solrgetCapacityPlans;
		} catch (Exception e) {
			logger.error(e.getMessage());
			timestamp = LoggerUtils.utcTime().toString();
			loggersDTO.setTimestamp(timestamp);
			LoggerUtils.printlogger(loggersDTO, false, true);
			return solrgetCapacityPlans;
		}

	}

	@Override
	public Response getTables(int clientId, String jwtToken) {

		logger.debug("get Tables");

		String timestamp = LoggerUtils.utcTime().toString();
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

		Logger(timestamp, nameofCurrMethod);

		Response response = new Response();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + getCollectionsMicroserviceAPI + "/" + clientId);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		timestamp = LoggerUtils.utcTime().toString();
		loggersDTO.setTimestamp(timestamp);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			LoggerUtils.printlogger(loggersDTO, false, false);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			LoggerUtils.printlogger(loggersDTO, false, true);
			return response;
		}

	}

	@Override
	public ManageTableResponse getTable(String tableName, int clientId, String jwtToken) {

		logger.debug("get Table ");

		String timestamp = LoggerUtils.utcTime().toString();
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

	Logger(timestamp, nameofCurrMethod);
		timestamp = LoggerUtils.utcTime().toString();
		loggersDTO.setTimestamp(timestamp);

		ManageTableResponse response = new ManageTableResponse();
		microserviceHttpGateway
				.setApiEndpoint(baseMicroserviceUrl + getCollectionsMicroserviceAPI + "/" + clientId + "/" + tableName);
		String jsonObject = microserviceHttpGateway.getRequestV2(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, ManageTableResponse.class);
			LoggerUtils.printlogger(loggersDTO, false, false);
			return response;
		} catch (IOException e) {
			logger.error(e.getMessage());
			LoggerUtils.printlogger(loggersDTO, false, true);
			return response;
		}

	}

	@Override
	public Response delete(int clientId, String tableName, String jwtToken) {
		logger.debug("Delete Table ");

		String timestamp = LoggerUtils.utcTime().toString();
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

	Logger(timestamp, nameofCurrMethod);
		timestamp = LoggerUtils.utcTime().toString();
		loggersDTO.setTimestamp(timestamp);

		Response response = new Response();
		microserviceHttpGateway
				.setApiEndpoint(baseMicroserviceUrl + deleteMicroserviceAPI + "/" + clientId + "/" + tableName);
		String jsonObject = microserviceHttpGateway.deleteRequest(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			LoggerUtils.printlogger(loggersDTO, false, false);
			return response;
		} catch (IOException e) {
			logger.error(e.getMessage());
			LoggerUtils.printlogger(loggersDTO, false, true);
			return response;
		}

	}

	@Override
	public Response create(int clientId, ManageTableCreate manageTableDTO, String jwtToken) {
		logger.debug("create Table ");

		String timestamp = LoggerUtils.utcTime().toString();
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

	Logger(timestamp, nameofCurrMethod);

		timestamp = LoggerUtils.utcTime().toString();
		loggersDTO.setTimestamp(timestamp);
		Response response = new Response();

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + createMicroserviceAPI + "/" + clientId);
		microserviceHttpGateway.setRequestBodyDTO(manageTableDTO);

		String jsonObject = microserviceHttpGateway.postRequest(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			LoggerUtils.printlogger(loggersDTO, false, false);
			return response;

		} catch (Exception e) {
			logger.error(e.getMessage());
			LoggerUtils.printlogger(loggersDTO, false, true);
			return response;
		}

	}

	@Override
	public Response update(String tableName, int clientId, ManageTableUpdate tableSchema, String jwtToken) {
		logger.debug("update Table ");

		String timestamp = LoggerUtils.utcTime().toString();
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

		loggersDTO = LoggerUtils.getRequestLoggingInfo(username, servicename, nameofCurrMethod, timestamp);
		LoggerUtils.printlogger(loggersDTO, true, false);

		timestamp = LoggerUtils.utcTime().toString();
		loggersDTO.setTimestamp(timestamp);

		Response response = new Response();

		microserviceHttpGateway
				.setApiEndpoint(baseMicroserviceUrl + renameMicroserviceAPI + "/" + clientId + "/" + tableName);
		microserviceHttpGateway.setRequestBodyDTO(tableSchema);

		String jsonObject = microserviceHttpGateway.putRequest(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			LoggerUtils.printlogger(loggersDTO, false, false);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage());
			LoggerUtils.printlogger(loggersDTO, false, true);
			return response;
		}

	}

	@Override
	public Response restoreTable(int clientId, String tableName, String jwtToken) {

		logger.debug("restore Table ");

		String timestamp = LoggerUtils.utcTime().toString();
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();

		Logger(timestamp, nameofCurrMethod);

		timestamp = LoggerUtils.utcTime().toString();
		loggersDTO.setTimestamp(timestamp);

		Response response = new Response();

		microserviceHttpGateway
				.setApiEndpoint(baseMicroserviceUrl + restoreMicroserviceAPI + "/" + clientId + "/" + tableName);
		String jsonObject = microserviceHttpGateway.putRequest(jwtToken);
		docParserUtil.isJwtAuthenticationError(jsonObject);
		try {
			response = objectMapper.readValue(jsonObject, Response.class);
			LoggerUtils.printlogger(loggersDTO, false, false);
			return response;
		} catch (IOException e) {
			logger.error(e.getMessage());
			LoggerUtils.printlogger(loggersDTO, false, true);
			return response;
		}

	}
}
