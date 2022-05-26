package com.searchclient.clientwrapper.domain.error;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MicroserviceConnectionException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private final int exceptionCode;
	private final String exceptionMessage;
}
