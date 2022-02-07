package com.searchclient.clientwrapper.domain.dto.solr.managetable;

import java.util.List;

import com.searchclient.clientwrapper.domain.dto.solr.SolrFieldDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TableSchemaResponseDTO {
	private int statusCode;
	private String message;
	private String tableName;
	private String schemaName;
	private List<SchemaFieldDTO> attributes;
	private String name;
	public TableSchemaResponseDTO(TableSchemaResponseDTO schemaResponseDTO) {
		this.statusCode = schemaResponseDTO.getStatusCode();
		this.message = schemaResponseDTO.getMessage();
		this.tableName = schemaResponseDTO.getTableName();
		this.schemaName = schemaResponseDTO.getSchemaName();
		this.attributes = schemaResponseDTO.getAttributes();
	}

	public TableSchemaResponseDTO(String tableName, String schemaName, List<SchemaFieldDTO> attributes) {
		this.tableName = tableName;
		this.schemaName = schemaName;
		this.attributes = attributes;
	}
	public TableSchemaResponseDTO(String tableName, String name) {
		this.tableName = tableName;
		this.name = name;
	}
}
