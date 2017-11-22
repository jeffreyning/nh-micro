package com.nh.micro.rule.engine.context;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author ninghao
 *
 */
public class MicroContextHolder implements ApplicationContextAware {
	private static ApplicationContext context;
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context=context;
	}
	public static ApplicationContext getContext(){
		 return context;
	}
	private static Map contextMap=new HashMap();
	public static Map getContextMap() {
		return contextMap;
	}
	public void setContextMap(Map contextMap) {
		MicroContextHolder.contextMap = contextMap;
	}
	
	
}
