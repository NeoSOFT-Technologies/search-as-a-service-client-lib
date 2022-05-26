package com.searchclient.clientwrapper.domain.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.searchclient.clientwrapper.domain.utils.HttpStatusCode;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestControllerAdvice {

	  private final Logger log = LoggerFactory.getLogger(RestControllerAdvice.class);
	 
	  @ExceptionHandler(CustomException.class)
		public ResponseEntity<Object> handleGenericException(CustomException exception) {

			return new ResponseEntity<>(new RestApiErrorHandling(

					exception.getExceptionCode(), exception.getStatus(),
					exception.getExceptionMessage()), HttpStatus.BAD_REQUEST);
		}

	  @ExceptionHandler(Exception.class)
		public ResponseEntity<Object> handleUncaughtException(Exception exception) {
			
			log.error("Uncaught Error Occured: {}", exception.getMessage());
			return frameRestApiException(new RestApiError(HttpStatus.BAD_REQUEST, exception.getMessage()));
		}

		private ResponseEntity<Object> frameRestApiException(RestApiError err) {
			return new ResponseEntity<>(err, err.getStatus());
		}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception){
		String fieldName = "";
		if(exception.getCause() instanceof UnrecognizedPropertyException) {
			UnrecognizedPropertyException ex = (UnrecognizedPropertyException)exception.getCause();
			fieldName = ex.getPropertyName();
			return new ResponseEntity<>(new RestApiErrorHandling(
					HttpStatusCode.UNRECOGNIZED_FIELD.getCode(), HttpStatusCode.UNRECOGNIZED_FIELD,
					HttpStatusCode.UNRECOGNIZED_FIELD.getMessage()+ " : "+fieldName), HttpStatus.BAD_REQUEST);
		}else if(exception.getCause() instanceof InvalidFormatException) {
			InvalidFormatException ex = (InvalidFormatException)exception.getCause();
			if (ex.getPath() != null && !ex.getPath().isEmpty()) {
		        JsonMappingException.Reference path = ex.getPath().get(ex.getPath().size() - 1);
		       fieldName = (null != path)?path.getFieldName():"";
		    }
			String value = (null != ex.getValue())?ex.getValue().toString():"";
			return frameRestApiException(new RestApiError(HttpStatus.BAD_REQUEST, "Value for field : "+fieldName+" is not expected as : "+value));
		}else if(exception.getCause() instanceof JsonMappingException) {
			JsonMappingException ex = (JsonMappingException)exception.getCause();
			if(ex.getCause() instanceof CustomException) {
				CustomException exc = (CustomException)ex.getCause();
				return frameRestApiException(new RestApiError(HttpStatus.BAD_REQUEST, exc.getExceptionMessage()));
			}else {
				return new ResponseEntity<>(new RestApiErrorHandling(
						HttpStatusCode.INVALID_JSON_INPUT.getCode(), HttpStatusCode.INVALID_JSON_INPUT,
						HttpStatusCode.INVALID_JSON_INPUT.getMessage()), HttpStatus.BAD_REQUEST); 
			}}
			else {
			return new ResponseEntity<>(new RestApiErrorHandling(

					HttpStatusCode.INVALID_JSON_INPUT.getCode(), HttpStatusCode.INVALID_JSON_INPUT,
					HttpStatusCode.INVALID_JSON_INPUT.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception){
		String fieldName = "";
		String requiredType = "";
		if(exception.getCause() instanceof NumberFormatException) {
			try {
				fieldName = exception.getName();
				Class<?> exceptionRequiredType=exception.getRequiredType();
				if(exceptionRequiredType!=null){
					requiredType = exceptionRequiredType.getName();
				}
				}catch(Exception e) {
					log.error("Something Went Wrong!" , e);
				}
			}
			return frameRestApiException(
					new RestApiError(HttpStatus.BAD_REQUEST, fieldName + " must be of type " + requiredType));
		}
	
	@ExceptionHandler(MicroserviceConnectionException.class)
	public ResponseEntity<Object> handleMicroserviceConnectionException(MicroserviceConnectionException exception){
		return frameRestApiException(new RestApiError(HttpStatus.SERVICE_UNAVAILABLE, exception.getExceptionMessage()));
	}
	
	@ExceptionHandler(JwtAuthenticationFailureException.class)
	public ResponseEntity<Object> handleJwtAuthenticationFailureException(JwtAuthenticationFailureException exception){
		return frameRestApiException(new RestApiError(HttpStatus.FORBIDDEN, exception.getExceptionMessage()));
	}
}
