/*
 * package com.solr.clientwrapper.rest;
 * 
 * 
 * import com.solr.clientwrapper.domain.dto.solr.SolrResponseDTO; import
 * com.solr.clientwrapper.domain.port.api.SolrCollectionServicePort; import
 * com.solr.clientwrapper.domain.port.api.SolrDocumentServicePort; import
 * com.solr.clientwrapper.domain.service.SolrDocumentService;
 * 
 * import io.swagger.v3.oas.annotations.Operation; import
 * io.swagger.v3.oas.annotations.security.SecurityRequirement; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import java.time.Duration; import java.time.Instant;
 * 
 * @RestController
 * 
 * @RequestMapping("/test") public class TestingController {
 * 
 * @Autowired SolrDocumentService solrDocumentService;
 * 
 * @PostMapping("/documents/{tableName}")
 * 
 * @Operation(summary =
 * "/ For add documents we have to pass the tableName and isNRT and it will return statusCode and message."
 * , security = @SecurityRequirement(name = "bearerAuth")) public
 * ResponseEntity<SolrResponseDTO> documents(@PathVariable String
 * tableName, @RequestBody String payload, @RequestParam boolean isNRT) {
 * 
 * 
 * SolrResponseDTO solrResponseDTO=solrDocumentService.addDocuments(tableName,
 * payload, isNRT);
 * 
 * if(solrResponseDTO.getStatusCode()==200){ return
 * ResponseEntity.status(HttpStatus.OK).body(solrResponseDTO); }else{ return
 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body(solrResponseDTO); }
 * 
 * }
 * 
 * 
 * 
 * 
 * }
 */