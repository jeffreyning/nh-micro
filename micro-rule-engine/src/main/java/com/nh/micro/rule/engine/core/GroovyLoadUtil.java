package com.nh.micro.rule.engine.core;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author ninghao
 * 
 */
public class GroovyLoadUtil {

	protected static Log logger = LogFactory.getLog(GroovyLoadUtil.class);

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
		logger.info("groovy content=" + content);
		ClassLoader parent = GroovyLoadUtil.class.getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		Class<?> groovyClass = loader.parseClass(content);
		GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
		GroovyExecUtil.getGroovyMap().put(name, groovyObject);
		logger.info("finish load groovy name=" + name);
	}

}
