package com.solr.clientwrapper.domain.port.api;

import com.solr.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;

public interface SolrSchemaServicePort {

	/*
	 * CRUD operations on solr schema
	 */
	SolrSchemaResponseDTO create(String tableName, SolrSchemaDTO newSolrSchemaDTO);

	SolrSchemaResponseDTO update(String tableName, SolrSchemaDTO solrSchemaDTO);

	SolrSchemaResponseDTO delete(String tableName);

	SolrSchemaResponseDTO get(String tableName);

}
