package com.solr.clientwrapper.rest;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solr.clientwrapper.domain.dto.solr.SolrFieldDTO;

import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrDocumentServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/test")
public class TestingController {

	@Autowired
	SolrDocumentServicePort solrdocServicePort;

	@PostMapping("/documents/{collectionName}")
	@Operation(summary = "/add-documents", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrResponseDTO> documents(@PathVariable String collectionName, @RequestBody String payload, @RequestParam boolean isNRT) {

		Instant start = Instant.now();

		SolrResponseDTO solrResponseDTO=solrdocServicePort.addDocuments(collectionName, payload, isNRT);

		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds or "+(timeElapsed.toMillis()/1000)+" seconds");

		if(solrResponseDTO.getStatusCode()==200){
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
		}

	}

}
