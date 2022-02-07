package com.searchclient.clientwrapper.domain.dto.solr.collection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class GetCollectionsResponseDTO {

    private int statusCode;
    private String message;
    private List<String> items;

}
