package com.nh.micro.rule.engine.core;

import groovy.lang.GroovyObject;
import java.util.HashMap;
import java.util.Map;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;



/**
 * 
 * @author ninghao
 * 
 */
public class GroovyExecUtil {
	public static Boolean throwFlag=true;
	//protected static Log logger = LogFactory.getLog(GroovyExecUtil.class);
	private static Logger logger=Logger.getLogger(GroovyLoadUtil.class);
	private static Map<String, GroovyObject> groovyMap = new HashMap<String, GroovyObject>();

	public static Map<String, GroovyObject> getGroovyMap() {
		return groovyMap;
	}

	public static void setGroovyMap(Map<String, GroovyObject> groovyMap) {
		GroovyExecUtil.groovyMap = groovyMap;
	}

	public static GroovyObject getGroovyObj(String groovyName) {
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

	public static boolean execGroovySimple4Obj(String groovyName, GInputParam gInputParam,
			GOutputParam gOutputParam,GContextParam gContextParam) {
		return execGroovy(groovyName, "execGroovy", gInputParam, gOutputParam,gContextParam);
	}
	
	public static boolean execGroovy(String groovyName, String methodName,
			Object... paramArray) {
		try {
			GroovyObject groovyObject = (GroovyObject) getGroovyObj(groovyName);
			//groovyObject.invokeMethod(methodName, paramArray);
			//GroovyAopInter groovyAop=(GroovyAopInter) MicroContextHolder.getContextMap().get("groovyAop");
			GroovyAopInter firstAop=GroovyAopChain.getFirstAop();
			Object retObj=null;
			if(firstAop==null){
				retObj=groovyObject.invokeMethod(methodName, paramArray);
			}else{
				retObj=firstAop.invokeMethod(groovyObject,groovyName, methodName, paramArray);
			}			
			return true;
		} catch (Throwable t) {
			logger.error(t.toString(), t);
			if(throwFlag){
				throw new RuntimeException(t);
			}
			return false;
		}
	}
	
	public static Object execGroovyRetObj(String groovyName, String methodName,
			Object... paramArray) {
		try {
			GroovyObject groovyObject = (GroovyObject) getGroovyObj(groovyName);
			//GroovyAopInter groovyAop=(GroovyAopInter) MicroContextHolder.getContextMap().get("groovyAop");
			GroovyAopInter firstAop=GroovyAopChain.getFirstAop();
			Object retObj=null;
			if(firstAop==null){
				retObj=groovyObject.invokeMethod(methodName, paramArray);
			}else{
				retObj=firstAop.invokeMethod(groovyObject,groovyName, methodName, paramArray);
			}
			return retObj;
		} catch (Throwable t) {
			logger.error(t.toString(), t);
			if(throwFlag){
				throw new RuntimeException(t);
			}
			return null;
		}
	}
	
	public static Object execGroovyRetObj(GroovyObject groovyObject, String methodName,
			Object... paramArray) {
		try {
			//GroovyObject groovyObject = (GroovyObject) getGroovyObj(groovyName);
			//GroovyAopInter groovyAop=(GroovyAopInter) MicroContextHolder.getContextMap().get("groovyAop");
			GroovyAopInter firstAop=GroovyAopChain.getFirstAop();
			Object retObj=null;
			if(firstAop==null){
				retObj=groovyObject.invokeMethod(methodName, paramArray);
			}else{
				retObj=firstAop.invokeMethod(groovyObject,null, methodName, paramArray);
			}
			return retObj;
		} catch (Throwable t) {
			logger.error(t.toString(), t);
			if(throwFlag){
				throw new RuntimeException(t);
			}
			return null;
		}
	}
}
