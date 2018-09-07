package com.nh.micro.config;

import groovy.lang.GroovyObject;

import java.lang.reflect.Field;

import com.nh.micro.rule.engine.context.MicroContextHolder;
import com.nh.micro.rule.engine.core.IGroovyLoadPlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

/**
 * 
 * @author ninghao
 * 
 */
public class MicroConfigValuePlugin implements IGroovyLoadPlugin {
	
	
	@Override
	public GroovyObject execPlugIn(String name, GroovyObject groovyObject,
			GroovyObject proxyObject) throws Exception {
		String[] beanNames=MicroContextHolder.getContext().getBeanNamesForType(Environment.class);
		String beanId=beanNames[0];
		Environment environment=(Environment) MicroContextHolder.getContext().getBean(beanId);
		Field[] fields = groovyObject.getClass().getDeclaredFields();
		int size = fields.length;
		for (int i = 0; i < size; i++) {
			Field field = fields[i];
			Value anno = field.getAnnotation(Value.class);
			if (anno == null) {
				continue;
			}
			String attrName = null;
			attrName = anno.value();
			if(attrName!=null){
				if(attrName.startsWith("$")){
					int length=attrName.length();
					int sublen=length-1;
					String keyName=attrName.substring(2, sublen);
					String value=environment.getProperty(keyName);
					field.set(groovyObject, value);
				}

			}

		}
		return proxyObject;
	}
}
