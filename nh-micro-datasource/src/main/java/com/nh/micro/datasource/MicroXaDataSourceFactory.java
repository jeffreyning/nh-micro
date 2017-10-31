package com.nh.micro.datasource;

import java.util.HashMap;
import java.util.Map;

public class MicroXaDataSourceFactory {
public static Map dataSourceHolder=new HashMap();
public static MicroXaDataSource getDataSourceInstance(String dataSourceId){
	MicroXaDataSource dataSource=(MicroXaDataSource) dataSourceHolder.get(dataSourceId);
	return dataSource;
}
public static MicroXaDataSource createDataSource(String dataSourceId){
	MicroXaDataSource dataSource=new MicroXaDataSource();
	dataSourceHolder.put(dataSourceId, dataSource);
	return dataSource;
}
}
