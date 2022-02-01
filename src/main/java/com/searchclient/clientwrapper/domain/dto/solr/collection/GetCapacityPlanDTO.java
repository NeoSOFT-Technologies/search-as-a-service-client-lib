package com.searchclient.clientwrapper.domain.dto.solr.collection;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class GetCapacityPlanDTO {

    private List<CapacityPlanDTO> plans;

    public GetCapacityPlanDTO(List<CapacityPlanDTO> plans) {
        this.plans = plans;
    }

}
