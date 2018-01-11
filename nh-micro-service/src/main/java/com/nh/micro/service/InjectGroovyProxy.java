package com.nh.micro.service;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

/**
 * 
 * @author ninghao
 *
 */
public class InjectGroovyProxy implements InvocationHandler {

	public String groovyName=null;
//	public GroovyObject groovyObject=null; 
	public String getGroovyName() {
		return groovyName;
	}
/*	public GroovyObject getGroovyObject() {
		return groovyObject;
	}
	public void setGroovyObject(GroovyObject groovyObject) {
		this.groovyObject = groovyObject;
	}*/
	public void setGroovyName(String groovyName) {
		this.groovyName = groovyName;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		String methodName=method.getName();
		return GroovyExecUtil.execGroovyRetObj(groovyName, methodName, args);

	}

}
