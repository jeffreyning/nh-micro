package com.nh.micro.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.springframework.beans.factory.FactoryBean;


/**
 * 
 * @author ninghao
 *
 */
public class NServiceMapperFactory implements FactoryBean {
	public String groovyName;
	
	public String getGroovyName() {
		return groovyName;
	}

	public void setGroovyName(String groovyName) {
		this.groovyName = groovyName;
	}

	public Class mapperInterface=null;

	public Class getMapperInterface() {
		return mapperInterface;
	}

	public void setMapperInterface(Class mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	@Override
	public Object getObject() throws Exception {
		InjectGroovyProxy injectGroovyProxy=new InjectGroovyProxy();
		injectGroovyProxy.setGroovyName(groovyName);	
		if(groovyName==null || "".equals(groovyName)){
			InjectGroovy anno=(InjectGroovy) mapperInterface.getAnnotation(InjectGroovy.class);
			if(anno!=null){
				String tempGroovyName=anno.name();
				injectGroovyProxy.setGroovyName(tempGroovyName);
			}
		}		
	    Object proxy = Proxy.newProxyInstance(mapperInterface.getClassLoader(), 
	    	     new Class[]{mapperInterface}, 
	    	     (InvocationHandler) injectGroovyProxy);	  
	    
		return proxy;
	}

	@Override
	public Class getObjectType() {
		// TODO Auto-generated method stub
		return mapperInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
