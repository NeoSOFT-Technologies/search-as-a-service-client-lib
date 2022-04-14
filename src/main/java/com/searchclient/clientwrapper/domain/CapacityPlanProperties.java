package com.searchclient.clientwrapper.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "capacity-plan")
@Getter
@Setter
@Service
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CapacityPlanProperties {

	private int statusCode;
	private String message;
	private String timestamp;
	private List<Plan> plans;

	@Getter
	@Setter
	public static class Plan {
		private String sku;
		private String name;
		private int replicas;
		private int shards;
		private int storage;
	}

}
