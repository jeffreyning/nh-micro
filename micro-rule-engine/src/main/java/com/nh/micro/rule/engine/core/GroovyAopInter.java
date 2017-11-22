package com.nh.micro.rule.engine.core;

import groovy.lang.GroovyObject;

public abstract class GroovyAopInter {
	private GroovyAopInter nextAop=null;
	public GroovyAopInter getNextAop() {
		return nextAop;
	}
	public void setNextAop(GroovyAopInter nextAop) {
		this.nextAop = nextAop;
	}
	
	public Object execNextHandler(GroovyObject groovyObject, String GroovyName, String methodName, Object... param){
		if(nextAop!=null){
			return nextAop.invokeMethod(groovyObject, GroovyName, methodName, param);
		}else{
			return groovyObject.invokeMethod(methodName, param);
		}
	}
	abstract public Object invokeMethod(GroovyObject groovyObject, String GroovyName, String methodName, Object... param);
}
