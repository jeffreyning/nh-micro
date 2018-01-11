package com.nh.micro.rule.engine.core;



import groovy.lang.GroovyObject;

public interface IGroovyLoadPlugin {
	public GroovyObject execPlugIn(String name, GroovyObject groovyObject, GroovyObject proxyObject) throws Exception;
}
