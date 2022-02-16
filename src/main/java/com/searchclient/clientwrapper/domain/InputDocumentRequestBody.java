package com.searchclient.clientwrapper.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputDocumentRequestBody {

	private String tableName;
	private String isNRT;
    private Object payload;
    
	public InputDocumentRequestBody(Object payload) {
		this.payload = payload;
	}

}
