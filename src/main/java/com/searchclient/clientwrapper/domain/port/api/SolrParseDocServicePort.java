package com.searchclient.clientwrapper.domain.port.api;

import org.springframework.web.multipart.MultipartFile;

public interface SolrParseDocServicePort {
	
	String multipartUploder(MultipartFile file);
	
}