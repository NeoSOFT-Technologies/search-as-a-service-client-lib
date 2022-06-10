package com.searchclient.clientwrapper.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)


public class IngestionResponse extends BaseResponse {
	
	// Rate Limiter member variables
	private String maxRequestsAllowed; // maximum number of requests allowed for current limitRefreshPeriod
	private String currentRefreshWindow; // limitRefreshPeriod, in seconds
	private String requestTimeoutDuration; // Incoming request timeout duration

	// MaxRequestSize Limiter member variables
	private String maxAllowedRequestSize;
	private String incomingRequestSize;
	private String apiResponseData;
	
}

