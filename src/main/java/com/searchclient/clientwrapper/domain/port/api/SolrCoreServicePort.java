package com.searchclient.clientwrapper.domain.port.api;

import com.searchclient.clientwrapper.domain.dto.solr.ResponseDTO;

public interface SolrCoreServicePort {

    ResponseDTO create(String coreName);

    ResponseDTO rename(String coreName, String newName);

    ResponseDTO delete(String coreName);

    ResponseDTO swap(String coreOne, String coreTwo);

    ResponseDTO reload(String coreName);

    String status(String coreName);

}
