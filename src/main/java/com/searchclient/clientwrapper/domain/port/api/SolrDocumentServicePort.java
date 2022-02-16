package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.IngestionResponse;

public interface SolrDocumentServicePort {
IngestionResponse addDocuments(String collectionName, String payload, boolean isNRT);
   
}
