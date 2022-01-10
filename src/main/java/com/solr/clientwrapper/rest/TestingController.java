package com.solr.clientwrapper.rest;

import com.solr.clientwrapper.domain.dto.solr.SolrFieldDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;
import com.solr.clientwrapper.domain.port.api.DataIngectionServicePort;
import com.solr.clientwrapper.domain.port.api.SolrCollectionServicePort;
import com.solr.clientwrapper.domain.port.api.SolrDocumentServicePort;
import com.solr.clientwrapper.domain.port.api.SolrSchemaServicePort;
import com.solr.clientwrapper.infrastructure.Enum.SolrFieldType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestingController {

	
	String data ="{\r\n"
			+ "\"batch\":\r\n"
			+ "[\r\n"
			+ "  {\r\n"
			+ "        \"books\" :[\r\n"
			+ "  {\r\n"
			+ "    \"id\" : 1,\r\n"
			+ "    \"color\" : \"The Lightning Thief\",\r\n"
			+ "    \"author\" : \"Rick Riordan\",\r\n"
			+ "    \"price\" : 123\r\n"
			+ "  },\r\n"
			+ "  {\r\n"
			+ "    \"id\" : 2,\r\n"
			+ "    \"color\" : \"The Lightning Thief\",\r\n"
			+ "    \"author\" : \"Rick Riordan\",\r\n"
			+ "    \"price\" : 123\r\n"
			+ "  }\r\n"
			+ "],\r\n"
			+ "\"movie\" :[\r\n"
			+ "  {\r\n"
			+ "    \"id\" : 1,\r\n"
			+ "    \"movie\" : \"The Lightning Thief\",\r\n"
			+ "    \"actor\" : \"Rick Riordan\",\r\n"
			+ "    \"price\" : 123\r\n"
			+ "  },\r\n"
			+ "  {\r\n"
			+ "    \"id\" : 2,\r\n"
			+ "     \"movie\" : \"The Thief\",\r\n"
			+ "    \"actor\" : \"Rick\",\r\n"
			+ "    \"price\" : 623\r\n"
			+ "  }\r\n"
			+ "],\r\n"
			+ "\"hero\" :[\r\n"
			+ "  {\r\n"
			+ "    \"id\" : 1,\r\n"
			+ "    \"movie\" : \"The Lightning Thief\",\r\n"
			+ "    \"actor\" : \"Rick Riordan\",\r\n"
			+ "    \"price\" : 123\r\n"
			+ "  },\r\n"
			+ "  {\r\n"
			+ "    \"id\" : 2,\r\n"
			+ "     \"movie\" : \"The Thief\",\r\n"
			+ "    \"actor\" : \"Rick\",\r\n"
			+ "    \"price\" : 623\r\n"
			+ "  }\r\n"
			+ "]\r\n"
			+ "  }\r\n"
			+ "]\r\n"
			+ "}";
	@Autowired
	SolrCollectionServicePort solrCollectionServicePort;


	@Autowired
	SolrDocumentServicePort solrdocServicePort;
	@Autowired
	SolrSchemaServicePort solrSchemaServicePort;


	@Autowired
	DataIngectionServicePort dataIngectionServicePort;
	
	String tableName = "colors";
	String name = "Ravi";

	SolrFieldDTO sfd = new SolrFieldDTO("Ravi", SolrFieldType._nest_path_, "Mangesh", false, false, false, false,
			false);
	List<SolrFieldDTO> list = new ArrayList<SolrFieldDTO>();
	SolrSchemaDTO dto = new SolrSchemaDTO(tableName, name, list);

	

	@PutMapping("/schemaupdate")
	public SolrSchemaResponseDTO SchemaUpdate(@RequestBody SolrSchemaDTO newSolrSchemaDTO) {
		list.add(sfd);
		return solrSchemaServicePort.update(tableName, name, newSolrSchemaDTO);
	}

	@PostMapping("/schema/create")
	public SolrSchemaResponseDTO create() {
		list.add(sfd);
		return solrSchemaServicePort.create(tableName, name, dto);

	}

	@PostMapping("/Documents/{collectionName}")
	@Operation(summary = "/Documents", security = @SecurityRequirement(name = "bearerAuth"))
	public SolrResponseDTO documents(@PathVariable String collectionName, @RequestParam boolean isNRT) {
//		System.out.println("PAYLOAD - "+payload);
		String payload="[{'name':'karthik3'}]";
		return solrdocServicePort.addDocuments(collectionName, payload, isNRT);
	}

	@DeleteMapping("/schema/delete/{tableName}/{name}")
	public SolrSchemaResponseDTO delete(@PathVariable String tableName, @PathVariable String name) {
		return solrSchemaServicePort.delete(tableName, name);
	}
	@PostMapping("/batcharray/{collectionName}")
	@Operation(summary = "/add-document", security = @SecurityRequirement(name = "bearerAuth"))
	public String batcharray(@PathVariable String collectionName)  {

	
		return dataIngectionServicePort.parseSolrSchemaArray(collectionName, data);
		
			// File is EXISTS
			 
		

	}

	@PostMapping("/batchcollection/{collectionName}")
	@Operation(summary = "/add-document", security = @SecurityRequirement(name = "bearerAuth"))
	public String batchcollection(@PathVariable String collectionName) {

	
		return dataIngectionServicePort.parseSolrSchemaBatch(collectionName, data);
		
			

	}
}
