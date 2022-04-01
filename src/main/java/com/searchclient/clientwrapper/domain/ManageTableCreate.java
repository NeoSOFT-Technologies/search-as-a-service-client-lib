package com.searchclient.clientwrapper.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL) 
public class ManageTableCreate {
    private String tableName;
    private String sku;
    private List<SchemaField> columns;
	
	public ManageTableCreate(ManageTableCreate manageTableDTO) {
		this.tableName = manageTableDTO.getTableName();
		this.sku = manageTableDTO.getSku();
		this.columns=manageTableDTO.getColumns();	
	}
	
	public ManageTableCreate(String tableName, List<SchemaField> attributes) {
		this.tableName = tableName;;
		this.columns = columns;
	}

	
}