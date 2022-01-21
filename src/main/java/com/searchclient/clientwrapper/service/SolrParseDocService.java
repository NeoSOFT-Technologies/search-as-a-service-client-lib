package com.searchclient.clientwrapper.service;


import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.searchclient.clientwrapper.domain.port.api.SolrParseDocServicePort;

@Service
public class SolrParseDocService implements SolrParseDocServicePort {
	
	   private final Logger log = LoggerFactory.getLogger(SolrParseDocService.class);

	@Override
	public String MultipartUploder(MultipartFile file) {
		
		if (!file.isEmpty()) {
	        try {
	            byte[] bytes = null;
				try {
					bytes = file.getBytes();
				} catch (IOException e) {

					e.printStackTrace();
				}
	            String completeData = new String(bytes);
	            log.debug(completeData);
	            
	         JSONObject jsonObject = new JSONObject(completeData);
	   
	        
	            System.out.println(jsonObject);
	           
	         
	        }finally {}
		}
		return null;
	
		
	}

}