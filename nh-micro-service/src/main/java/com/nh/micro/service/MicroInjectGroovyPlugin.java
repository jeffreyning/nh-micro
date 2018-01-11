package com.nh.micro.service;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import groovy.lang.GroovyObject;
import com.nh.micro.rule.engine.core.IGroovyLoadPlugin;

/**
 * 
 * @author ninghao
 *
 */
public class MicroInjectGroovyPlugin implements IGroovyLoadPlugin {

	@Override
	public GroovyObject execPlugIn(String name, GroovyObject groovyObject,
			GroovyObject proxyObject) throws Exception {
		Field[] fields=groovyObject.getClass().getDeclaredFields();
		int size=fields.length;
		for(int i=0;i<size;i++){
			Field field=fields[i];
			InjectGroovy anno=field.getAnnotation(InjectGroovy.class);
			if(anno==null){
				continue;
			}
			String groovyName=null;

				groovyName=anno.name();
				if(groovyName==null || "".equals(groovyName)){
					groovyName=field.getName();
				}

			Class cls=field.getType();
			InjectGroovyProxy injectGroovyProxy=new InjectGroovyProxy();
			injectGroovyProxy.setGroovyName(groovyName);
			//injectGroovyProxy.setGroovyObject(proxyObject);
		    Object proxy = Proxy.newProxyInstance(cls.getClassLoader(), 
		    	     new Class[]{cls}, 
		    	     (InvocationHandler) injectGroovyProxy);	  

			field.set(groovyObject, proxy);
			
		}
		return proxyObject;
	}

}
