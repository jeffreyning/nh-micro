package com.nh.micro.support;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.nh.micro.db.MicroMetaDao;
import com.nh.micro.rule.engine.core.GroovyLoadUtil;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 
 * @author ninghao
 * 
 */
public class GroovyDbLoadTimer {

	private static Logger logger=Logger.getLogger(GroovyDbLoadTimer.class);
	public static String dbName="default";
	
	public static String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		GroovyInitDbUtil.dbName = dbName;
		//add 201806 ning
		GroovyDbLoadTimer.dbName=dbName;
	}	
	
	public void doJob() throws Exception {
		MicroMetaDao microDao=MicroMetaDao.getInstance(GroovyDbLoadTimer.dbName);
		String sql="select * from nh_micro_groovy_load_list where valid_flag=1 and time_load_flag=1";
		List<Map<String, Object>> retList=microDao.getMicroJdbcTemplate().queryForList(sql);
		for (Map rowMap : retList) {
			String groovyContent=(String) rowMap.get("groovy_content");
			String ruleName=(String) rowMap.get("meta_key");
        	String publishTime=(String) rowMap.get("publish_time");
        	try{
        		if(GroovyLoadUtil.canDbLoad(ruleName)==false){
        			continue;
        		}
        		boolean checkFlag=GroovyLoadUtil.checkStamp(ruleName,publishTime);
        		if(checkFlag==true){
        			continue;
        		}
        		GroovyLoadUtil.loadGroovyStampCheck(ruleName, groovyContent,publishTime);
        	}catch(Exception e){
        		logger.error("load db groovy error name="+ruleName+" publishTime="+publishTime, e);
        	}
		}		
	}
}
