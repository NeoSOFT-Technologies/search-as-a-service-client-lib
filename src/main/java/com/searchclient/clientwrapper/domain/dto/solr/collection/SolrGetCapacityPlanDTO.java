package com.searchclient.clientwrapper.domain.dto.solr.collection;

import java.util.List;

import com.searchclient.clientwrapper.config.CapacityPlanProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class SolrGetCapacityPlanDTO {

    private List<CapacityPlanProperties.Plan> plans;

    public SolrGetCapacityPlanDTO(List<CapacityPlanProperties.Plan> plans) {
        this.plans = plans;
    }

}
