package com.searchclient.clientwrapper.domain.dto.solr.collection;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class SolrGetCapacityPlanDTO {

    private List<CapacityPlanDTO> plans;

    public SolrGetCapacityPlanDTO(List<CapacityPlanDTO> plans) {
        this.plans = plans;
    }

}
