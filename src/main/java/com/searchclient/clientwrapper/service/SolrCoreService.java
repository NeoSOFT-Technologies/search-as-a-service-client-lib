package com.searchclient.clientwrapper.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.searchclient.clientwrapper.domain.dto.logger.CorrelationID;
import com.searchclient.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.core.SolrDoubleCoreDTO;
import com.searchclient.clientwrapper.domain.dto.solr.core.SolrSingleCoreDTO;
import com.searchclient.clientwrapper.domain.port.api.SolrCoreServicePort;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;

@Service
public class SolrCoreService implements SolrCoreServicePort {

	private final Logger log = LoggerFactory.getLogger(SolrCoreService.class);


	@Value("${base-microservice-url}")
	private String baseMicroserviceUrl;

	private String apiEndpoint = "/solr-core";

	@Autowired
	MicroserviceHttpGateway microserviceHttpGateway;
	
    CorrelationID correlationID=new CorrelationID();
    
    @Autowired
    HttpServletRequest request;
    
    ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
    
    private String servicename = "Solr_Core_Service";
    
    private String username = "Username";  

	@Override
	public SolrResponseDTO create(String coreName) {

		log.debug("create");
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		String correlationid = correlationID.generateUniqueCorrelationId();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CorrelationID.CORRELATION_ID_HEADER_NAME, correlationid); 	
		String ipaddress=request.getRemoteAddr();
		String timestamp=utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("--------Started Request of Service Name : {} , Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",servicename,username,correlationid,ipaddress,timestamp,nameofCurrMethod);

		SolrSingleCoreDTO solrSingleCore = new SolrSingleCoreDTO(coreName);

		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);

		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/create");
		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);
		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
		log.info("-----------Successfully Resopnse Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",username,correlationid,ipaddress,timestamp,nameofCurrMethod);
		return solrResponseDTO;
	}

	@Override
	public SolrResponseDTO rename(String coreName, String newName) {

		log.debug("rename");
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		String correlationid = correlationID.generateUniqueCorrelationId();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CorrelationID.CORRELATION_ID_HEADER_NAME, correlationid); 	
		String ipaddress=request.getRemoteAddr();
		String timestamp=utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("--------Started Request of Service Name : {} , Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",servicename,username,correlationid,ipaddress,timestamp,nameofCurrMethod);


		SolrDoubleCoreDTO solrSingleCore = new SolrDoubleCoreDTO(coreName, newName);
		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);

		//MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/rename");
		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);

		JSONObject jsonObject = microserviceHttpGateway.putRequest();
		System.out.println("jsonobject" + jsonObject);
		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
		log.info("-----------Successfully Resopnse Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",username,correlationid,ipaddress,timestamp,nameofCurrMethod);
		return solrResponseDTO;
	}

	@Override
	public SolrResponseDTO delete(String coreName) {

		log.debug("delete" + coreName);
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		String correlationid = correlationID.generateUniqueCorrelationId();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CorrelationID.CORRELATION_ID_HEADER_NAME, correlationid); 	
		String ipaddress=request.getRemoteAddr();
		String timestamp=utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("--------Started Request of Service Name : {} , Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",servicename,username,correlationid,ipaddress,timestamp,nameofCurrMethod);


		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);

		//MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/delete/" + coreName);

		JSONObject jsonObject = microserviceHttpGateway.deleteRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
		log.info("-----------Successfully Resopnse Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",username,correlationid,ipaddress,timestamp,nameofCurrMethod);
		return solrResponseDTO;

	}

	@Override
	public SolrResponseDTO swap(String coreOne, String coreTwo) {

		log.debug("swap");
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		String correlationid = correlationID.generateUniqueCorrelationId();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CorrelationID.CORRELATION_ID_HEADER_NAME, correlationid); 	
		String ipaddress=request.getRemoteAddr();
		String timestamp=utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("--------Started Request of Service Name : {} , Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",servicename,username,correlationid,ipaddress,timestamp,nameofCurrMethod);

		SolrDoubleCoreDTO solrSingleCore = new SolrDoubleCoreDTO(coreOne, coreTwo);
		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreOne);

		//MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/swap");
		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);

		JSONObject jsonObject = microserviceHttpGateway.putRequest();

		System.out.println("jsonobject" + jsonObject);
		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
		log.info("-----------Successfully Resopnse Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",username,correlationid,ipaddress,timestamp,nameofCurrMethod);
		return solrResponseDTO;
	}

	@Override
	public SolrResponseDTO reload(String coreName) {

		log.debug("reload");
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		String correlationid = correlationID.generateUniqueCorrelationId();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CorrelationID.CORRELATION_ID_HEADER_NAME, correlationid); 	
		String ipaddress=request.getRemoteAddr();
		String timestamp=utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("--------Started Request of Service Name : {} , Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",servicename,username,correlationid,ipaddress,timestamp,nameofCurrMethod);

		SolrSingleCoreDTO solrSingleCore = new SolrSingleCoreDTO(coreName);
		SolrResponseDTO solrResponseDTO = new SolrResponseDTO(coreName);

		//MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/reload");

		microserviceHttpGateway.setRequestBodyDTO(solrSingleCore);
		JSONObject jsonObject = microserviceHttpGateway.postRequest();

		solrResponseDTO.setMessage(jsonObject.get("message").toString());
		solrResponseDTO.setStatusCode((int) jsonObject.get("statusCode"));
		log.info("-----------Successfully Resopnse Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",username,correlationid,ipaddress,timestamp,nameofCurrMethod);
		return solrResponseDTO;
	}

	@Override
	public String status(String coreName) {

		log.debug("status");
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		String correlationid = correlationID.generateUniqueCorrelationId();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CorrelationID.CORRELATION_ID_HEADER_NAME, correlationid); 	
		String ipaddress=request.getRemoteAddr();
		String timestamp=utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("--------Started Request of Service Name : {} , Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",servicename,username,correlationid,ipaddress,timestamp,nameofCurrMethod);


		//CoreAdminResponse coreAdminResponse = null;
		//MicroserviceHttpGateway microserviceHttpGateway = new MicroserviceHttpGateway();
		microserviceHttpGateway.setApiEndpoint(baseMicroserviceUrl + apiEndpoint + "/status/" + coreName);
		String jsonObject = microserviceHttpGateway.stringRequest();
		log.info("-----------Successfully Resopnse Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",username,correlationid,ipaddress,timestamp,nameofCurrMethod);
		return jsonObject;

	}

}
