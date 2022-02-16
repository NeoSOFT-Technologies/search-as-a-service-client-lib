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
	@JsonIgnore
	private String schemaName;
	private List<SchemaField> columns;
	@JsonIgnore
	private Map<Object, Object> tableDetails;

	public ManageTableUpdate(ManageTableUpdate schemaDTO) {
		this.tableName = schemaDTO.getTableName();
		this.schemaName = schemaDTO.getSchemaName();
		this.columns=schemaDTO.getColumns();
	
	}
	
	public ManageTableUpdate(String tableName, String schemaName, List<SchemaField> attributes) {
		this.tableName = tableName;
		this.schemaName = schemaName;
		this.columns = attributes;
	}
	
	

}
