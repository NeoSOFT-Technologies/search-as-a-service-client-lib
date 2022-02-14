package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;


public interface SolrDocumentServicePort {

	ResponseDTO addDocuments(String collectionName, String payload, int clientid);
	ResponseDTO addDocument(String collectionName, String payload, int clientid);
   
}
