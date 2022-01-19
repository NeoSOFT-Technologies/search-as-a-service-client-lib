package com.searchclient.clientwrapper.domain.dto.solr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class SolrResponseDTO {

    private int statusCode;
    private String name;
    private String message;

    public SolrResponseDTO(String name) {
        this.name = name;
    }
}
