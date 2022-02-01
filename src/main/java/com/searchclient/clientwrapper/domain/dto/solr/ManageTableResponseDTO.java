package com.searchclient.clientwrapper.domain.dto.solr;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ManageTableResponseDTO {

	private String tableName;
	private String name;
	private List<SolrFieldDTO> attributes;
	private int statusCode;
	public ManageTableResponseDTO(ManageTableResponseDTO solrSchemaResponseDto) {
		this.tableName = solrSchemaResponseDto.getTableName();
		this.name=solrSchemaResponseDto.getName();
		this.attributes=solrSchemaResponseDto.getAttributes();
		this.statusCode=solrSchemaResponseDto.getStatusCode();
	}
	
	public ManageTableResponseDTO(String tableName, String name, List<SolrFieldDTO> attributes) {
		this.tableName = tableName;
		this.name = name;
		this.attributes = attributes;
	}

	public ManageTableResponseDTO(String tableName, String name) {
		this.tableName = tableName;
		this.name = name;
	}
	
	
}