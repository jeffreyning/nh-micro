package com.nh.micro.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.nh.micro.rule.engine.core.IGroovyLoadPlugin;

import groovy.lang.GroovyObject;



/**
 * 
 * @author ninghao
 *
 */
public class MicroControllerPlugin implements IGroovyLoadPlugin {

	@Override
	public GroovyObject execPlugIn(String name, GroovyObject groovyObject,
			GroovyObject proxyObject) {
		Annotation[] annos=groovyObject.getClass().getAnnotations();
		MicroUrlMapping anno=groovyObject.getClass().getAnnotation(MicroUrlMapping.class);
		String root="";
		if(anno!=null){
			root=anno.name();
			//MicroControllerMap.setUrl(root, name);
		}else{
			return proxyObject;
		}
		Method[] methods=groovyObject.getClass().getMethods();
		int size=methods.length;
		for(int i=0;i<size;i++){
			Method method=methods[i];
			MicroUrlMapping subAnno=method.getAnnotation(MicroUrlMapping.class);
			if(subAnno==null){
				continue;
			}
			String subPath=subAnno.name();
			String version=subAnno.version();
			String methodName=method.getName();
			String fullPath=root+subPath;
			MicroControllerMap.setUrl(fullPath, name, methodName,version);
		}
		
		return proxyObject;
	}

}
