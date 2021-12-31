package com.solr.clientwrapper.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solr.clientwrapper.domain.dto.solr.SolrFieldDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaDTO;
import com.solr.clientwrapper.domain.dto.solr.SolrSchemaResponseDTO;
import com.solr.clientwrapper.domain.port.api.SolrCollectionServicePort;
import com.solr.clientwrapper.domain.port.api.SolrSchemaServicePort;
import com.solr.clientwrapper.infrastructure.Enum.SolrFieldType;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class TestingController {

	@Autowired
	SolrCollectionServicePort solrCollectionServicePort;

	@Autowired
	SolrSchemaServicePort solrSchemaServicePort;

	String tableName = "colors";
	String name = "Ravi";

	SolrFieldDTO sfd = new SolrFieldDTO("Ravi", SolrFieldType._nest_path_, "Mangesh", false, false, false, false,
			false);
	List<SolrFieldDTO> list = new ArrayList<SolrFieldDTO>();
	SolrSchemaDTO dto = new SolrSchemaDTO(tableName, name, list);

	@GetMapping("/schemaGet")
	public SolrSchemaResponseDTO collectionTesting() throws SolrServerException, IOException, URISyntaxException,
			ParserConfigurationException, InterruptedException, TransformerException, org.xml.sax.SAXException {

		return solrSchemaServicePort.get(tableName, name);
	}

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

	@DeleteMapping("/schema/delete/{tableName}/{name}")
	public SolrSchemaResponseDTO delete(@PathVariable String tableName, @PathVariable String name) {
		return solrSchemaServicePort.delete(tableName, name);
	}

}
