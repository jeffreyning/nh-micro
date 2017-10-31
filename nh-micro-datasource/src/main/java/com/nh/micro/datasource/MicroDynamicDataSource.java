package com.nh.micro.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class MicroDynamicDataSource extends AbstractRoutingDataSource {
    @Override  
    protected Object determineCurrentLookupKey() {  
    	return DataSourceSwitcher.getDataSource(); 
    }  
}
