package com.searchclient.clientwrapper.domain.dto.solr.collection;

import com.searchclient.clientwrapper.infrastructure.Enum.SchemaFieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SchemaFieldDTO {

	String name;
	SchemaFieldType type;
	String default_;
	boolean isRequired;
	boolean isFilterable;
	boolean isStorable;
	boolean isMultiValue;
	boolean isSortable;

}
