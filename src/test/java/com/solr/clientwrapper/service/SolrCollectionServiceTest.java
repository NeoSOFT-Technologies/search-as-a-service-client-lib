package com.solr.clientwrapper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.searchclient.clientwrapper.config.CapacityPlanProperties;
import com.searchclient.clientwrapper.domain.dto.solr.SolrResponseDTO;
import com.searchclient.clientwrapper.domain.dto.solr.collection.SolrGetCollectionsResponseDTO;
import com.searchclient.clientwrapper.domain.utils.MicroserviceHttpGateway;
import com.searchclient.clientwrapper.service.SolrCollectionService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class SolrCollectionServiceTest {

    Logger logger = LoggerFactory.getLogger(SolrCollectionServiceTest.class);

    String collectionName = "Demo";

    @InjectMocks
    SolrCollectionService solrCollectionService;
    @MockBean
    private CapacityPlanProperties capacityPlanProperties;
    @MockBean
    MicroserviceHttpGateway microserviceHttpGateway;

    List<CapacityPlanProperties.Plan> capacityPlans;
    JSONObject jsonObject;

    @BeforeEach
    public void init() {
        capacityPlans = new ArrayList<CapacityPlanProperties.Plan>();
        CapacityPlanProperties.Plan plan = new CapacityPlanProperties.Plan();
        plan.setName("s1");
        plan.setSku("s1");
        capacityPlans.add(plan);
        SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);
        solrResponseDTO.setStatusCode(200);
        solrResponseDTO.setMessage("Testing");
        jsonObject = new JSONObject();
        jsonObject.put("message", "success");
        jsonObject.put("statusCode", 200);

    }

    public void setMockitoSuccessResponseForService() {
        SolrResponseDTO solrResponseDTO = new SolrResponseDTO(collectionName);
        solrResponseDTO.setStatusCode(200);
        solrResponseDTO.setMessage("Testing");

        SolrGetCollectionsResponseDTO solrGetCollectionsResponseDTO = new SolrGetCollectionsResponseDTO();
        solrGetCollectionsResponseDTO.setStatusCode(200);
        solrGetCollectionsResponseDTO.setMessage("Testing");

        Mockito.when(solrCollectionService.create(Mockito.any(), Mockito.any())).thenReturn(solrResponseDTO);
        Mockito.when(solrCollectionService.delete(Mockito.any())).thenReturn(solrResponseDTO);

        // Mockito.when(solrCollectionService.rename(Mockito.any(),Mockito.any())).thenReturn(solrResponseDTO);
        Mockito.when(solrCollectionService.getCollections()).thenReturn(solrGetCollectionsResponseDTO);
        Mockito.when(solrCollectionService.isCollectionExists(Mockito.any())).thenReturn(solrResponseDTO);
    }

    @Test
    void testCreateSolrCollection() {
        int statusCode = 200;
        // CREATE COLLECTION
        // init();

        Mockito.when(capacityPlanProperties.getPlans()).thenReturn(capacityPlans);
        Mockito.when(microserviceHttpGateway.postRequest()).thenReturn(jsonObject);

        SolrResponseDTO solrresponseDto = solrCollectionService.create(collectionName, "s1");
        assertEquals(statusCode, solrresponseDto.getStatusCode());

    }

}
