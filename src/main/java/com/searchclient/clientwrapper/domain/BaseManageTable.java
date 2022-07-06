package com.searchclient.clientwrapper.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseManageTable {
	protected String tableName;
	protected TableInfo tableInfo;
	protected List<SchemaField> columns;
	

}


