package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;

public interface SolrDocumentServicePort {

	ResponseDTO addDocuments(String collectionName, String payload, boolean isNRT);
   
}
