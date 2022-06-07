package com.searchclient.clientwrapper.domain;


import com.searchclient.clientwrapper.domain.error.CustomException;
import com.searchclient.clientwrapper.domain.utils.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class SchemaField {

	String name;
	String type;
	boolean isRequired;
	boolean isFilterable;
	boolean isStorable;
	boolean isMultiValue;
	boolean isSortable;
	boolean isPartialSearch;

	public void setFilterable(Object value) {
		if (value instanceof Boolean) {
            this.isFilterable = (Boolean) value;
        }else {
        	throw new CustomException(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode(),
        			HttpStatusCode.BAD_REQUEST_EXCEPTION, "Value for Filterable is expected as : true/false");
        }
	}

	public void setRequired(Object value) {
		if (value instanceof Boolean) {
            this.isRequired = (Boolean) value;
        }else {
        	throw new CustomException(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode(),
        			HttpStatusCode.BAD_REQUEST_EXCEPTION, "Value for Required is expected as : true/false");
        }
	}


	public void setStorable(Object value) {
		if (value instanceof Boolean) {
            this.isStorable = (Boolean) value;
        }else {
        	throw new CustomException(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode(),
        			HttpStatusCode.BAD_REQUEST_EXCEPTION, "Value for Storable is expected as : true/false");
        }
	}


	public void setMultiValue(Object value) {
		if (value instanceof Boolean) {
            this.isMultiValue = (Boolean) value;
        }else {
        	throw new CustomException(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode(),
        			HttpStatusCode.BAD_REQUEST_EXCEPTION, "Value for MultiValue is expected as : true/false");
        }
	}


	public void setSortable(Object value) {
		if (value instanceof Boolean) {
            this.isSortable = (Boolean) value;
        }else {
        	throw new CustomException(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode(),
        			HttpStatusCode.BAD_REQUEST_EXCEPTION, "Value for Sortable is expected as : true/false");
        }
	}
	
	public void setPartialSearch(Object value) {
		if (value instanceof Boolean) {
            this.isPartialSearch = (Boolean) value;
        }else {
        	throw new CustomException(HttpStatusCode.BAD_REQUEST_EXCEPTION.getCode(),
        			HttpStatusCode.BAD_REQUEST_EXCEPTION, "Value for Partial Search is expected as : true/false");
        }
	}
}
