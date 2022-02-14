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
public class ResponseDTO {

    private int statusCode;
    private String name;
    private String responseMessage;

    public ResponseDTO(String name) {
        this.name = name;
    }
}
