package com.nh.micro.rule.engine.core.plugin;



import groovy.lang.GroovyObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class MicroDefaultLogProxy implements IMicroProxy {
	private static Logger logger=Logger.getLogger(MicroDefaultLogProxy.class);
	public GroovyObject targetObject=null;
	public String groovyName=null;
	public String property="";

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		String methodName=(String) args[0];
		String preLog=groovyName+" "+methodName;
		logger.debug(preLog+" begin");
		Object ret=method.invoke(targetObject, args);
		logger.debug(preLog+" end");
		return ret;
	}
	@Override
	public void setGroovyObj(Object groovyObj) {
		this.targetObject=(GroovyObject) groovyObj;
		
	}
	@Override
	public void setProperty(String property) {
		this.property=property;
		
	}
	@Override
	public void setGroovyName(String groovyName) {
		this.groovyName=groovyName;
		
	}
	@Override
	public void setOldGroovyObj(Object OldGroovyObj) {
		// TODO Auto-generated method stub
		
	}

}
