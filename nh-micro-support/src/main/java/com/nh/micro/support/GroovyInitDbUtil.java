package com.nh.micro.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import org.apache.log4j.Logger;

import com.nh.micro.db.MicroMetaDao;
import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.micro.rule.engine.core.GroovyLoadUtil;
/**
 * 
 * @author ninghao
 *
 */
public class GroovyInitDbUtil {
	private static Logger logger=Logger.getLogger(GroovyInitDbUtil.class);
	public static String dbName="default";
	
	public static String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		GroovyInitDbUtil.dbName = dbName;
	}

	public static void initGroovy() throws Exception {
		logger.debug("begin load db groovy");
		MicroMetaDao microDao=MicroMetaDao.getInstance(dbName);
		String sql="select * from nh_micro_groovy_load_list where valid_flag=1 and init_load_flag=1";
		List<Map<String, Object>> retList=microDao.queryObjJoinByCondition(sql);
		for (Map rowMap : retList) {
			String groovyContent=(String) rowMap.get("groovy_content");
			String ruleName=(String) rowMap.get("meta_key");
        	Object gobj=GroovyExecUtil.getGroovyObj(ruleName);
        	if(gobj==null){
        		GroovyLoadUtil.loadGroovy(ruleName, groovyContent);
        	}else{
        		logger.debug("skip db groovy "+ruleName);
        	}
			
		}
		logger.debug("end load db groovy");
	}


}

