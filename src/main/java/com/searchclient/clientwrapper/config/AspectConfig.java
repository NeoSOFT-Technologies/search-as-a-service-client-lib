package com.searchclient.clientwrapper.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jboss.logging.MDC;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;import com.searchclient.clientwrapper.domain.BaseResponse;

@Aspect
@Configuration
public class AspectConfig {

	private static Logger log = LoggerFactory.getLogger(AspectConfig.class);

	private static final String STARTED_EXECUTION = "--------Started Request of Service Name : {}, CorrelationId : {}, IpAddress : {}, MethodName : {}, TimeStamp : {}, Parameters : {}";
	private static final String SUCCESSFUL_EXECUTION = "--------Successfully Response of Service Name : {}, CorrlationId : {}, Message: {} , IpAddress : {}, MethodName : {}, TimeStamp : {}, Parameters : {}";
	private static final String FAILURE_EXECUTION = "--------Failed Response of Service Name : {}, CorrlationId : {}, Message : {}, IpAddress : {}, MethodName : {}, Parameters : {}";
	private static final String CORRELATION_ID_LOG_VAR_NAME = "CID";
	private static String ip;

	static {
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error(e.toString());
		}
	}
	
	@Before(value = "execution(* com.searchclient.clientwrapper.domain.service.*.*(..))")
	public void logStatementForService(JoinPoint joinPoint) {
		log.info(STARTED_EXECUTION, joinPoint.getTarget().getClass().getSimpleName(),
				MDC.get(CORRELATION_ID_LOG_VAR_NAME), ip, joinPoint.getSignature().getName(), utcTime(),
				joinPoint.getArgs());

	}	

	@AfterReturning(value = "execution(* com.searchclient.clientwrapper.domain.service.*.*(..))", returning = "serviceMethodResponse")
	public void logStatementForServiceAfterThrowing(JoinPoint joinPoint, BaseResponse serviceMethodResponse){
		
	    if(serviceMethodResponse.getStatusCode() == 200) {
	    	log.info(SUCCESSFUL_EXECUTION, joinPoint.getTarget().getClass().getSimpleName(), 
	    			MDC.get(CORRELATION_ID_LOG_VAR_NAME), serviceMethodResponse.getMessage(), ip, joinPoint.getSignature().getName(),utcTime(),
	    			joinPoint.getArgs());
	    }else {
	    	log.error(FAILURE_EXECUTION,
	    			joinPoint.getTarget().getClass().getSimpleName(), MDC.get(CORRELATION_ID_LOG_VAR_NAME), serviceMethodResponse.getMessage(), ip, joinPoint.getSignature().getName(), joinPoint.getArgs());
	    }
	}
	
	@AfterThrowing(value = "execution(* com.searchclient.clientwrapper.domain.service.*.*(..))", throwing = "e")
	public void logStatementForServiceAfterThrowing(JoinPoint joinPoint, Exception e) {
		log.error(FAILURE_EXECUTION,
    			joinPoint.getTarget().getClass().getSimpleName(), MDC.get(CORRELATION_ID_LOG_VAR_NAME), 
    			e.getLocalizedMessage() !=null ? e.toString() : e.getMessage(), ip, joinPoint.getSignature().getName(), joinPoint.getArgs());
	}

	
	public static DateTime utcTime() {
		DateTime now = new DateTime(); // Gives the default time zone.
		return now.toDateTime(DateTimeZone.UTC);
	}	
}
