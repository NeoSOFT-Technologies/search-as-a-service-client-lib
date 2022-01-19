package com.solr.clientwrapper.rest;

import com.solr.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrSchemaServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client-test/api/schema")
public class TestingControllerSchema {

	private final Logger log = LoggerFactory.getLogger(TestingControllerSchema.class);

	@Autowired
	SolrSchemaServicePort solrSchemaServicePort;

	@PostMapping
	@Operation(summary = "/ Associate  a new schema by passing tableName, name and attributes it will return created  schema.", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrSchemaResponseDTO> create(
			@RequestBody SolrSchemaDTO newSolrSchemaDTO) {
		log.debug("Solr Schema Create");
		log.debug("Received Schema as in Request Body: {}", newSolrSchemaDTO);
		SolrSchemaResponseDTO solrResponseDTO =
				solrSchemaServicePort.create(
						newSolrSchemaDTO.getTableName(), "default",
						newSolrSchemaDTO);
		if(solrResponseDTO.getStatusCode() == 200)
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
	}

	@DeleteMapping("/{tableName}")
	@Operation(summary = "/ Remove the schema by passing TableName and it will return deleted schema and statusCode. ", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrSchemaResponseDTO> delete(
			@PathVariable String tableName) {
		log.debug("Schema Delete");
		SolrSchemaResponseDTO solrSchemaResponseDTO = solrSchemaServicePort.delete(tableName,"default");
		if(solrSchemaResponseDTO.getStatusCode() == 200)
			return ResponseEntity.ok().body(solrSchemaResponseDTO);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrSchemaResponseDTO);
		
	}

	@PutMapping("/{tableName}")
	@Operation(summary = "/ Update schema by passing TableName.", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrSchemaResponseDTO> update(
			@PathVariable String tableName,
			@RequestBody SolrSchemaDTO newSolrSchemaDTO) {
		log.debug("Solr schema update");
		log.debug("Received Schema as in Request Body: {}", newSolrSchemaDTO);
		SolrSchemaResponseDTO solrSchemaDTO = solrSchemaServicePort.update(tableName,"default", newSolrSchemaDTO);
		SolrSchemaResponseDTO solrResponseDTO = new SolrSchemaResponseDTO(solrSchemaDTO);
		if(solrResponseDTO.getStatusCode() == 200)
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
	}

	@GetMapping("/{tableName}")
	@Operation(summary = "/ Get schema by passing TableName.", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrSchemaResponseDTO> get(
			@PathVariable String tableName) {
		log.debug("get solar schema");
		SolrSchemaResponseDTO solrResponseDTO = solrSchemaServicePort.get(tableName,"default");
		if(solrResponseDTO.getStatusCode() == 200)
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
	}

}
