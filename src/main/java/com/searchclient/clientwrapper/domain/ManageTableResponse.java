package com.searchclient.clientwrapper.domain;

import java.util.List;
import java.util.Map;

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
public class ManageTableResponse  {

	public static class TableSchemav2Data {
        private String tableName;
        private List<SchemaField> columns;
        private Map<Object, Object> tableDetails;

        public TableSchemav2Data() {
        }

       
        public String getTableName() {
            return tableName;
        }


        public void setTableName(String tableName) {
            this.tableName = tableName;
        }


        public List<SchemaField> getColumns() {
            return columns;
        }

        public void setColumns(List<SchemaField> columns) {
            this.columns = columns;
        }

        public Map<Object, Object> getTableDetails() {
            return tableDetails;
        }

        public void setTableDetails(Map<Object, Object> tableDetails) {
            this.tableDetails = tableDetails;
        }
    }

    private int statusCode;
	private String message;
	private TableSchemav2Data data = new TableSchemav2Data();

    
	
	
	
	
}