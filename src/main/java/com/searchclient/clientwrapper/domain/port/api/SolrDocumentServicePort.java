package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.dto.solr.SolrResponseDTO;

public interface SolrDocumentServicePort {

	SolrResponseDTO addDocuments(String collectionName, String payload, int clientid);
	SolrResponseDTO addDocument(String collectionName, String payload, int clientid);
   
}
