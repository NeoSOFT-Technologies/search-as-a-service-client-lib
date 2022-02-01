package com.searchclient.clientwrapper.domain.dto.solr.collection;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ApiResponseDTO {

	private int responseStatusCode;

	private String responseMessage;

	
}
