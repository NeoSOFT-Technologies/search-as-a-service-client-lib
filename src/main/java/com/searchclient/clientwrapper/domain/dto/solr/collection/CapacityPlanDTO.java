package com.searchclient.clientwrapper.domain.dto.solr.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacityPlanDTO {
	String sku;
	String name;
	int replicas;
	int shards;
	int storage;
}
