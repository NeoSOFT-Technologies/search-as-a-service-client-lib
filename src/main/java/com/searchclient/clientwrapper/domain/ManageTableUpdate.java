package com.searchclient.clientwrapper.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManageTableUpdate {

	@JsonIgnore
	private String tableName;
	private List<SchemaField> columns;
	@JsonIgnore
	private Map<Object, Object> tableDetails;

	public ManageTableUpdate(ManageTableUpdate schemaDTO) {
		this.tableName = schemaDTO.getTableName();
		this.columns=schemaDTO.getColumns();
	
	}
	
	public ManageTableUpdate(String tableName, List<SchemaField> attributes) {
		this.tableName = tableName;
		this.columns = attributes;
	}
	
	

}
