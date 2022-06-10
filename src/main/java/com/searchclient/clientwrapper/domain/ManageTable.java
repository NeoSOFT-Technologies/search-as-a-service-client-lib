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
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL) 

public class ManageTable extends BaseManageTable{
	
    private String sku;	
	public ManageTable(ManageTable manageTableDTO) {
		this.tableName = manageTableDTO.getTableName();
		this.sku = manageTableDTO.getSku();
		this.columns=manageTableDTO.getColumns();	
	}
	
	public ManageTable(String tableName, List<SchemaField> attributes) {
		this.tableName = tableName;
		this.columns = attributes;
	}

	
}