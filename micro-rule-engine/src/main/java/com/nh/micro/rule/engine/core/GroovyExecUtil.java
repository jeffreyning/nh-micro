package com.nh.micro.rule.engine.core;

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
public class GroovyExecUtil {

	protected static Log logger = LogFactory.getLog(GroovyExecUtil.class);
	private static Map<String, GroovyObject> groovyMap = new HashMap<String, GroovyObject>();

	public static Map<String, GroovyObject> getGroovyMap() {
		return groovyMap;
	}

	public static void setGroovyMap(Map<String, GroovyObject> groovyMap) {
		GroovyExecUtil.groovyMap = groovyMap;
	}

	private static GroovyObject getGroovyObj(String groovyName) {
		GroovyObject groovyObject = getGroovyMap().get(groovyName);
		return groovyObject;
	}

	public static boolean execGroovySimple(String groovyName, Map inMap,
			Map outMap) {
		return execGroovy(groovyName, "execGroovy", inMap, outMap);
	}

	public static boolean execGroovySimple4Obj(String groovyName, GInputParam gInputParam,
			GOutputParam gOutputParam) {
		return execGroovy(groovyName, "execGroovy", gInputParam, gOutputParam);
	}
	
	public static boolean execGroovy(String groovyName, String methodName,
			Object... paramArray) {
		try {
			GroovyObject groovyObject = (GroovyObject) getGroovyObj(groovyName);
			groovyObject.invokeMethod(methodName, paramArray);
			return true;
		} catch (Throwable t) {
			logger.error(t.toString(), t);
			return false;
		}
	}

}
