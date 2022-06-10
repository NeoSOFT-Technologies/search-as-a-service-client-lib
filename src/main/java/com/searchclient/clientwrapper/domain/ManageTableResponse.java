package com.searchclient.clientwrapper.domain;

import java.util.List;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ManageTableResponse  extends BaseResponse{
	private TableSchemav2Data data;	
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class TableSchemav2Data extends BaseManageTable{
        public TableSchemav2Data() {
        	//Empty Constructor
        }

        @Override
        public String getTableName() {
            return tableName;
        }
        
        @Override
        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        @Override
        public List<SchemaField> getColumns() {
            return columns;
        }

        @Override
        public void setColumns(List<SchemaField> columns) {
            this.columns = columns;
        }
    }

	
}