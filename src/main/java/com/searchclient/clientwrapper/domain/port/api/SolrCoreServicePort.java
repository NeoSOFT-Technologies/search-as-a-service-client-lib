package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.dto.solr.SolrResponseDTO;

public interface SolrCoreServicePort {

    SolrResponseDTO create(String coreName);

    SolrResponseDTO rename(String coreName, String newName);

    SolrResponseDTO delete(String coreName);

    SolrResponseDTO swap(String coreOne, String coreTwo);

    SolrResponseDTO reload(String coreName);

    String status(String coreName);

}
