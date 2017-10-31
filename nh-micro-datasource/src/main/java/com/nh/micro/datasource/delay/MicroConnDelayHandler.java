package com.nh.micro.datasource.delay;

import org.apache.log4j.Logger;
import com.nh.micro.datasource.MicroXaDataSource;
import com.nh.micro.datasource.MicroXaDataSourceFactory;

/**
 * 
 * @author ninghao
 *
 */
public class MicroConnDelayHandler implements MicroDelayHandler {
	private static final Logger log = Logger.getLogger(MicroConnDelayHandler.class.getName());
	public String dataSourceId="default";
	
	@Override
	public void doDelayRemove(String key, Object value) {
		log.debug("micro xa conn delay begin rollback key="+key);
		MicroXaDataSourceFactory.getDataSourceInstance(dataSourceId).rollback(key);
		
	}

}
