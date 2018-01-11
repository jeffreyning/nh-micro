package com.nh.micro.rule.engine.core.plugin;



import groovy.lang.GroovyObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.log4j.Logger;

import com.nh.micro.rule.engine.core.IGroovyLoadPlugin;


/**
 * 
 * @author ninghao
 *
 */
public class MicroAopPlugin implements IGroovyLoadPlugin {
	private static Logger logger=Logger.getLogger(MicroAopPlugin.class);
	@Override
	public GroovyObject execPlugIn(String name, GroovyObject groovyObject,
			GroovyObject proxyObject) throws Exception {
		MicroAop microAop= groovyObject.getClass().getAnnotation(MicroAop.class);
		if(microAop==null){
			return proxyObject;
		}
		Class[] classArray=microAop.name();
		String[] propertyArray=microAop.property();
		int size=classArray.length;
		GroovyObject proxySubject=proxyObject;
		for(int i=0;i<size;i++){
			Class cls=classArray[i];
			IMicroProxy microProxy=(IMicroProxy) cls.newInstance();
			microProxy.setGroovyObj(proxySubject);
			microProxy.setOldGroovyObj(proxyObject);
			microProxy.setGroovyName(name);
			String property=propertyArray[i];
			microProxy.setProperty(property);
			proxySubject = (GroovyObject)Proxy.newProxyInstance(GroovyObject.class.getClassLoader(), 
	    	     new Class[]{GroovyObject.class}, 
	    	     microProxy);
		}
		return proxySubject;
	}


}
