package com.nh.micro.rule.engine.core;

import java.util.ArrayList;
import java.util.List;

import groovy.lang.GroovyObject;

public class GroovyAopImpl extends GroovyAopInter {


	@Override
	public Object invokeMethod(GroovyObject groovyObject, String groovyName, String methodName,Object... param) {
		Object retObj=groovyObject.invokeMethod(methodName, param);
		return retObj;
	}

}
