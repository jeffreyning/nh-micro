package com.nh.micro.rule.engine.context;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import groovy.lang.GroovyObject;


import com.nh.micro.rule.engine.core.IGroovyLoadPlugin;

/**
 * 
 * @author ninghao
 *
 */
public class MicroInjectPlugin implements IGroovyLoadPlugin {

	public GroovyObject execPlugIn(String name, GroovyObject groovyObject,
			GroovyObject proxyObject) throws Exception {
		Field[] fields=groovyObject.getClass().getDeclaredFields();
		int size=fields.length;
		for(int i=0;i<size;i++){
			Field field=fields[i];
			Resource anno=field.getAnnotation(Resource.class);
			if(anno!=null){
				String beanId=anno.name();
				if(beanId==null || "".equals(beanId)){
					beanId=field.getName();
				}
				Object beanObj=MicroContextHolder.getContext().getBean(beanId);
				field.set(groovyObject, beanObj);
			}else{
				Autowired annoAuto=field.getAnnotation(Autowired.class);
				if(annoAuto!=null){
					Class fieldCls=field.getType();
					
					//add 201806 ning start
					if(List.class.isAssignableFrom(fieldCls)){
						Type type=field.getGenericType();
			            if(type instanceof ParameterizedType) {
			                ParameterizedType t = (ParameterizedType) type;
			                Class typeCls=(Class) t.getActualTypeArguments()[0];
			                String[] beanNames=MicroContextHolder.getContext().getBeanNamesForType(typeCls);
			                if(beanNames!=null){
			                	List beanList=new ArrayList();
			                	for(String n:beanNames){
			                		Object beanObj=MicroContextHolder.getContext().getBean(n);
			                		beanList.add(beanObj);
			                		field.set(groovyObject, beanList);
			                	}
			                }
			              //add 201806 end  
			            }else{
							Object beanObj=MicroContextHolder.getContext().getBean(fieldCls);
							field.set(groovyObject, beanObj);			            	
			            }
					}else{
						Object beanObj=MicroContextHolder.getContext().getBean(fieldCls);
						field.set(groovyObject, beanObj);
					}
				}
			}
		}
		return proxyObject;
	}

}
