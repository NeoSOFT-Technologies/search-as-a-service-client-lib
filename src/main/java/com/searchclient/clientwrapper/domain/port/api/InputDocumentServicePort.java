package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.IngestionResponse;

public interface InputDocumentServicePort {
    IngestionResponse addNRTDocuments(String collectionName, String payload, int tenantId, String jwtToken);
    IngestionResponse addDocument(String collectionName, String payload, int tenantId, String jwtToken);   
}
