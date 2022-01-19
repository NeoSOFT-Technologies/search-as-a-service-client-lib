package com.searchclient.clientwrapper.domain.dto.solr.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputDocumentRequestBodyDTO {

	private String tableName;
	private String isNRT;
    private Object payload;
    
	public InputDocumentRequestBodyDTO(Object payload) {
		this.payload = payload;
	}

}
