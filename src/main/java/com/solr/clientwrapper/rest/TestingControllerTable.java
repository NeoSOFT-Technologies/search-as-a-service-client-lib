package com.solr.clientwrapper.rest;


import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrCreateCollectionDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrGetCapacityPlanDTO;
import com.solr.clientwrapper.domain.dto.solr.collection.SolrGetCollectionsResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrCollectionServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client-test/api/table")
public class TestingControllerTable {

	private final Logger log = LoggerFactory.getLogger(TestingControllerTable.class);

	@Autowired
	SolrCollectionServicePort solrCollectionServicePort;

	@GetMapping("/capacity-plans")
	@Operation(summary = "/Get all  the capacity plans.")
	public ResponseEntity<SolrGetCapacityPlanDTO> capacityPlans() {

		log.debug("Get capacity plans");

		SolrGetCapacityPlanDTO solrGetCapacityPlanDTO=solrCollectionServicePort.capacityPlans();

		return ResponseEntity.status(HttpStatus.OK).body(solrGetCapacityPlanDTO);

	}

	@PostMapping
	@Operation(summary = "/ Associate the Table by passing collectionName and capacity plan and return message.", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrResponseDTO> create(@RequestBody SolrCreateCollectionDTO solrCreateCollectionDTO) {

		log.debug("Solr Collection create");

		SolrResponseDTO solrResponseDTO=solrCollectionServicePort.create(solrCreateCollectionDTO.getCollectionName(), solrCreateCollectionDTO.getSku());

		if(solrResponseDTO.getStatusCode()==200){
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
		}

	}

	@DeleteMapping("/{tableName}")
	@Operation(summary = "/ Remove the table by passing tablename and it will return statusCode and message.", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrResponseDTO> delete(@PathVariable String tableName) {

		log.debug("Solr Collection delete");

		SolrResponseDTO solrResponseDTO=solrCollectionServicePort.delete(tableName);

		if(solrResponseDTO.getStatusCode()==200){
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
		}

	}


	@GetMapping
	@Operation(summary = "/ Get all the tables and it will return statusCode, message and all the collections.", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrGetCollectionsResponseDTO> collections() {

		log.debug("Get all collections");

		SolrGetCollectionsResponseDTO solrGetCollectionsResponseDTO=solrCollectionServicePort.getCollections();

		if(solrGetCollectionsResponseDTO.getStatusCode()==200){
			return ResponseEntity.status(HttpStatus.OK).body(solrGetCollectionsResponseDTO);
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrGetCollectionsResponseDTO);
		}

	}

	@GetMapping("/isTableExists/{tableName}")
	@Operation(summary = "/ For check table is exists by passing collectionName and it will return statusCode and message.", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<SolrResponseDTO> isCollectionExits(@PathVariable String tableName) {

		log.debug("isCollectionExits");

		SolrResponseDTO solrResponseDTO=solrCollectionServicePort.isCollectionExists(tableName);

		if(solrResponseDTO.getStatusCode()==200){
			return ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO);
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO);
		}

	}

	@GetMapping("/details/{tableName}")
	@Operation(summary = "/ Get the table details like Shards, Nodes & Replication Factor.", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<String> getCollectionDetails(@PathVariable String tableName) {

		log.debug("getCollectionDetails");

		JSONObject response=solrCollectionServicePort.getCollectionDetails(tableName);

		if(!response.has("Error")){
			return ResponseEntity.status(HttpStatus.OK).body(response.toString());
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
		}

	}

}
