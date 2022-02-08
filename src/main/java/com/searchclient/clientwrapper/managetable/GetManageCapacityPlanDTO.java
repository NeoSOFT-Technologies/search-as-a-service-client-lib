package com.searchclient.clientwrapper.managetable;

import java.util.List;

import com.searchclient.clientwrapper.domain.dto.solr.collection.CapacityPlanDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class GetManageCapacityPlanDTO {

	 private List<CapacityPlanDTO> plans;

	    public GetManageCapacityPlanDTO(List<CapacityPlanDTO> plans) {
	        this.plans = plans;
	    }

}
