package com.searchclient.clientwrapper.domain.dto.solr;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SolrSchemaDTO {

	String tableName;
	String name;
	List<SolrFieldDTO>attributes;

	public SolrSchemaDTO(SolrSchemaDTO solrSchemaDTO) {
		this.tableName = solrSchemaDTO.getTableName();
		this.name = solrSchemaDTO.getName();
		this.attributes=solrSchemaDTO.getAttributes();
	}
}