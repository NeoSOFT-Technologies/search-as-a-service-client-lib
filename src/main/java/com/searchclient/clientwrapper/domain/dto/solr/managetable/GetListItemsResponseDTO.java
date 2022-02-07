package com.searchclient.clientwrapper.domain.dto.solr.managetable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GetListItemsResponseDTO {
    private int statusCode;
    private String message;
    private List<String> items;
}
