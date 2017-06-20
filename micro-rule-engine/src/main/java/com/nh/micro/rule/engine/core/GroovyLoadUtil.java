package com.nh.micro.rule.engine.core;



import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.nh.micro.rule.engine.core.GroovyExecUtil;


/**
 * 
 * @author ninghao
 * 
 */
public class GroovyLoadUtil {
	public static final String LOADTYPE_FILE="file";
	public static final String LOADTYPE_DB="db";
	public static final String LOADTYPE_JAR="jar";
	//protected static Log logger = LogFactory.getLog(GroovyLoadUtil.class);
	private static Logger logger=Logger.getLogger(GroovyLoadUtil.class);
	//加载file时记录类型
	private static Map<String, String> fileMap = new HashMap();
	public static Map<String, String> getFileMap() {
		return fileMap;
	}

	public static void setFileMap(Map<String, String> fileMap) {
		GroovyLoadUtil.fileMap = fileMap;
	}

	public static void setTypeFlag(String name,String type){
		fileMap.put(name, type);
	}
	public static String getTypeFlag(String name){
		return fileMap.get(name);
	}	
	
	public static boolean canDbLoad(String name){
		String type=fileMap.get(name);
		if(type==null || type.equals("db")){
			return true;
		}
		return false;
	}
	
	/**
	 * 加载时修改stamp的Map
	 */
	private static Map<String, String> stampMap = new HashMap();

	public static Map<String, String> getStampMap() {
		return stampMap;
	}

	public static String getStampByName(String name) {
		return getStampMap().get(name);
	}

	public static String setStampByName(String name, String stamp) {
		return getStampMap().put(name, stamp);
	}

	public static boolean checkStamp(String name,String stamp){
		String oldStamp = getStampMap().get(name);
		if (oldStamp != null && oldStamp.equals(stamp)) {
			return true;
		}	
		return false;
	}
	
	public static void loadGroovyStampCheck(String name, String content,
			String stamp) throws Exception {
		if (stamp == null) {
			logger.debug("stamp is null");
			throw new RuntimeException("stamp is null");
		}
		String oldStamp = getStampMap().get(name);
		if (oldStamp != null && oldStamp.equals(stamp)) {
			logger.debug("old stamp equals new stamp");
			return;
		}
		logger.info("stamp is not same begin load groovy name=" + name
				+ " oldstamp=" + oldStamp + " newstamp=" + stamp);
		getStampMap().put(name, stamp);
		loadGroovy(name, content);
	}

	public static void loadGroovy(String name, String content) throws Exception {
		logger.info("begin load groovy name=" + name);
		logger.debug("groovy content=" + content);
		if(name.toLowerCase().contains("abstract")){
			logger.info("skip load groovy name=" + name);
			return;
		}
		ClassLoader parent = GroovyLoadUtil.class.getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		Class<?> groovyClass = loader.parseClass(content,name+".groovy");
		GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
		GroovyExecUtil.getGroovyMap().put(name, groovyObject);
		logger.info("finish load groovy name=" + name);
	}

}
