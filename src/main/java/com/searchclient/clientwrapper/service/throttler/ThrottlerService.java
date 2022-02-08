package com.searchclient.clientwrapper.service.throttler;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.searchclient.clientwrapper.domain.dto.logger.CorrelationID;
import com.searchclient.clientwrapper.domain.dto.throttler.ThrottlerMaxRequestSizeResponseDTO;
import com.searchclient.clientwrapper.domain.dto.throttler.ThrottlerRateLimitResponseDTO;
import com.searchclient.clientwrapper.domain.port.api.ThrottlerServicePort;

@Service
public class ThrottlerService implements ThrottlerServicePort {
	private final Logger logger = LoggerFactory.getLogger(ThrottlerService.class);
    @Value("${base-solr-url}")
	String baseSolrUrl;
    
    // Rate Limiter configuration values
    @Value("${resilience4j.ratelimiter.instances.solrDataInjectionRateLimitThrottler.limitForPeriod}")
    String maxRequestAllowedForCurrentWindow;
    @Value("${resilience4j.ratelimiter.instances.solrDataInjectionRateLimitThrottler.limitRefreshPeriod}")
    String currentRefreshWindow;
    @Value("${resilience4j.ratelimiter.instances.solrDataInjectionRateLimitThrottler.timeoutDuration}")
    String requestRetryWindow;
    // Max request size configuration values
    @Value("${resilience4j.maxRequestSize.maxAllowedRequestSize}")
    String maxAllowedRequestSize;
    
    CorrelationID correlationID=new CorrelationID();
    
    @Autowired
    HttpServletRequest request;
    
    ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
    
    private String servicename = "Throttler_Service";
    
    private String username = "Username";
	@Override
	public ThrottlerRateLimitResponseDTO dataInjectionRateLimiter() {
        logger.info("Max request limit is applied, no further calls are accepted");
        String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		String correlationid = correlationID.generateUniqueCorrelationId();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CorrelationID.CORRELATION_ID_HEADER_NAME, correlationid); 	
		String ipaddress=request.getRemoteAddr();
		String timestamp=utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logger.info("--------Started Request of Service Name : {} , Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",servicename,username,correlationid,ipaddress,timestamp,nameofCurrMethod);

        // prepare Rate Limiting Response DTO
        ThrottlerRateLimitResponseDTO rateLimitResponseDTO = new ThrottlerRateLimitResponseDTO();
        rateLimitResponseDTO.setResponseMsg(
        		"Too many requests made! "
        		+ "No further calls are accepted right now");
        rateLimitResponseDTO.setStatusCode(429);
        rateLimitResponseDTO.setMaxRequestsAllowed(maxRequestAllowedForCurrentWindow);
        rateLimitResponseDTO.setCurrentRefreshWindow(currentRefreshWindow);
        rateLimitResponseDTO.setRequestTimeoutDuration(requestRetryWindow);
        logger.info("-----------Successfully Resopnse Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",username,correlationid,ipaddress,timestamp,nameofCurrMethod);
        return rateLimitResponseDTO;
	}

	@Override
	public ThrottlerMaxRequestSizeResponseDTO applyDataInjectionRequestSizeLimiter(
			ThrottlerMaxRequestSizeResponseDTO throttlerMaxRequestSizeResponseDTO) {
		/*
		 * This method can apply Request Size Limiter Filter
		 * accepting the ThrottlerMaxRequestSizeResponseDTO as argument  
		 */
		logger.info("Max request size limiter is under process...");
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		String correlationid = correlationID.generateUniqueCorrelationId();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CorrelationID.CORRELATION_ID_HEADER_NAME, correlationid); 	
		String ipaddress=request.getRemoteAddr();
		String timestamp=utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logger.info("--------Started Request of Service Name : {} , Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",servicename,username,correlationid,ipaddress,timestamp,nameofCurrMethod);

		if(isRequestSizeExceedingLimit(throttlerMaxRequestSizeResponseDTO)) {
			throttlerMaxRequestSizeResponseDTO.setStatusCode(405);
			throttlerMaxRequestSizeResponseDTO.setResponseMessage(
					"Incoming request size exceeded the limit! "
					+ "This request can't be processed");
		} else {
			throttlerMaxRequestSizeResponseDTO.setStatusCode(202);
			throttlerMaxRequestSizeResponseDTO.setResponseMessage(
					"Incoming request size is under the limit, can be processed");
		}
		logger.info("Max request size limiting has been applied");
		logger.info("-----------Successfully Resopnse Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",username,correlationid,ipaddress,timestamp,nameofCurrMethod);
		return throttlerMaxRequestSizeResponseDTO;
	}
	
	@Override
	public ThrottlerMaxRequestSizeResponseDTO dataInjectionRequestSizeLimiter(String incomingData) {
		/*
		 * This method can apply Request Size Limiter Filter
		 * accepting the raw incoming data in form of string as argument  
		 */
		logger.info("Max request size limiter is under process...");
		String nameofCurrMethod = new Throwable().getStackTrace()[0].getMethodName();
		String correlationid = correlationID.generateUniqueCorrelationId();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CorrelationID.CORRELATION_ID_HEADER_NAME, correlationid); 	
		String ipaddress=request.getRemoteAddr();
		String timestamp=utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logger.info("--------Started Request of Service Name : {} , Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",servicename,username,correlationid,ipaddress,timestamp,nameofCurrMethod);

    	double incomingRequestSizeInKBs = ThrottlerUtils.getSizeInkBs(incomingData);
    	ThrottlerMaxRequestSizeResponseDTO throttlerMaxRequestSizeResponseDTO
    		= new ThrottlerMaxRequestSizeResponseDTO();
    	throttlerMaxRequestSizeResponseDTO.setIncomingRequestSize(incomingRequestSizeInKBs+"kB");
    	
    	// Max Request Size Limiter Logic
		throttlerMaxRequestSizeResponseDTO.setMaxAllowedRequestSize(maxAllowedRequestSize);
		if(isRequestSizeExceedingLimit(throttlerMaxRequestSizeResponseDTO)) {
			throttlerMaxRequestSizeResponseDTO.setStatusCode(405);
			throttlerMaxRequestSizeResponseDTO.setResponseMessage(
					"Incoming request size exceeded the limit! "
					+ "This request can't be processed");
		} else {
			throttlerMaxRequestSizeResponseDTO.setStatusCode(202);
			throttlerMaxRequestSizeResponseDTO.setResponseMessage(
					"Incoming request size is under the limit, can be processed");
		}
		logger.info("Max request size limiting has been applied");
		logger.info("-----------Successfully Resopnse Username : {}, Corrlation Id : {}, IP Address : {}, TimeStamp : {}, Method name : {}",username,correlationid,ipaddress,timestamp,nameofCurrMethod);
		return throttlerMaxRequestSizeResponseDTO;
	}

	@Override
	public boolean isRequestSizeExceedingLimit(ThrottlerMaxRequestSizeResponseDTO throttlerMaxRequestSizeResponseDTO) {
		return (ThrottlerUtils.formatRequestSizeStringToDouble(throttlerMaxRequestSizeResponseDTO.getIncomingRequestSize())
				> ThrottlerUtils.formatRequestSizeStringToDouble(throttlerMaxRequestSizeResponseDTO.getMaxAllowedRequestSize()));
	}
}
