package com.searchclient.clientwrapper.domain.utils;

public enum HttpStatusCode {
	
		
	INVALID_JSON_INPUT(105,"invalid json input or json format"),
	
	UNRECOGNIZED_FIELD(106,"check sequence of fields or field name"),
	
	BAD_REQUEST_EXCEPTION(400,"Bad Request Occuured"),
	
	REQUEST_FORBIDEN(403, "Request Unable To Authorize"),
	
	SERVER_UNAVAILABLE(503,"Unable to Connect To the Server");
	
	
	
	
	private int code;
	private String message;
	
	HttpStatusCode(int code, String message) {
		this.code=code;
		this.message=message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	
	

}
