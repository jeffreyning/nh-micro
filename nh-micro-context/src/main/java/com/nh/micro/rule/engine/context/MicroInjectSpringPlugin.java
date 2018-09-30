package com.nh.micro.rule.engine.context;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import groovy.lang.GroovyObject;

import com.nh.micro.rule.engine.core.IGroovyLoadPlugin;

/**
 * 
 * @author ninghao
 *
 */
public class MicroInjectSpringPlugin implements IGroovyLoadPlugin {
	public GroovyObject execPlugIn(String name, GroovyObject groovyObject,
			GroovyObject proxyObject) throws Exception {

		ApplicationContext context=MicroContextHolder.getContext();
		AutowireCapableBeanFactory autowireCapableBeanFactory=context.getAutowireCapableBeanFactory();
		autowireCapableBeanFactory.autowireBeanProperties(groovyObject, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		return proxyObject;
	}
}
