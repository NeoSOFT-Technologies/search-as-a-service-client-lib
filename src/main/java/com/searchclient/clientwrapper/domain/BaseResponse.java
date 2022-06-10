package com.searchclient.clientwrapper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class BaseResponse {
	
	protected int statusCode;
	protected String message;
	protected String timestamp;

}

