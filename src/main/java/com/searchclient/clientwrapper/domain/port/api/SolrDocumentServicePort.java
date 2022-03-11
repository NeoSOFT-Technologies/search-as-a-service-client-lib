package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.IngestionResponse;

public interface SolrDocumentServicePort {
    IngestionResponse addNRTDocuments(String collectionName, String payload, int clientid, String jwtToken);
    IngestionResponse addDocument(String collectionName, String payload, int clientid, String jwtToken);   
}
