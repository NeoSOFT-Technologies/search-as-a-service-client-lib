package com.searchclient.clientwrapper.domain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Response extends BaseResponse{
	
    private String name;
    private List<String> data;
    private List<TableListResponse> tableList;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TableListResponse {
    	private String tenantName;
		private int tenantId;
		private String tableName;
		
	}
}
