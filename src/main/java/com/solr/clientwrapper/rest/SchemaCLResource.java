package com.solr.clientwrapper.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solr.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrSchemaServicePort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
public class SchemaCLResource {

	private final Logger log = LoggerFactory.getLogger(SchemaCLResource.class);

	private final SolrSchemaServicePort solrSchemaServicePort;

	public SchemaCLResource(SolrSchemaServicePort solrSchemaServicePort) {
		this.solrSchemaServicePort = solrSchemaServicePort;
	}			

	@PostMapping("/schema")
	@Operation(summary = "/create-schema", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrSchemaResponseDTO> create(
			@RequestBody SolrSchemaDTO newSolrSchemaDTO) {
		log.debug("Solr Schema Create endpoint just got a hit");
		log.debug("Received Schema as in Request Body: {}", newSolrSchemaDTO);
		SolrSchemaResponseDTO solrResponseDTO = 
				solrSchemaServicePort.create(
						newSolrSchemaDTO.getTableName(), 
						newSolrSchemaDTO);
		if(solrResponseDTO.getStatusCode() == 200)
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
	}

	@DeleteMapping("/schema/{tableName}/{name}")
	@Operation(summary = "/delete-schema", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrSchemaResponseDTO> delete(
			@PathVariable String tableName, 
			@PathVariable String name) {
		log.debug("Schema Delete endpoint just got a hit");
		SolrSchemaResponseDTO solrSchemaResponseDTO = solrSchemaServicePort.delete(tableName);
		if(solrSchemaResponseDTO.getStatusCode() == 200)
			return ResponseEntity.ok().body(solrSchemaResponseDTO);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrSchemaResponseDTO);
		
	}

	@PutMapping("/update/{tableName}/{name}")
	@Operation(summary = "/update-schema", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrSchemaResponseDTO> update(
			@PathVariable String tableName, 
			@PathVariable String name, 
			@RequestBody SolrSchemaDTO newSolrSchemaDTO) {
		log.debug("Solr schema update endpoint just got a hit");
		log.debug("Received Schema as in Request Body: {}", newSolrSchemaDTO);
		SolrSchemaResponseDTO solrSchemaDTO = solrSchemaServicePort.update(tableName, newSolrSchemaDTO);
		SolrSchemaResponseDTO solrResponseDTO = new SolrSchemaResponseDTO(solrSchemaDTO);
		if(solrResponseDTO.getStatusCode() == 200)
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
	}

	@GetMapping("/get/{tableName}/{name}")
	@Operation(summary = "/get-schema", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrSchemaResponseDTO> get(
			@PathVariable String tableName, 
			@PathVariable String name) {
		log.debug("get solar schema endpoint just got a hit");
		SolrSchemaResponseDTO solrResponseDTO = solrSchemaServicePort.get(tableName);
		if(solrResponseDTO.getStatusCode() == 200)
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
	}
}
