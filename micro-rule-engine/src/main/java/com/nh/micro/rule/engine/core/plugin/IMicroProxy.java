package com.nh.micro.rule.engine.core.plugin;



import java.lang.reflect.InvocationHandler;
/**
 * 
 * @author ninghao
 *
 */
public interface IMicroProxy  extends InvocationHandler {
public void setOldGroovyObj(Object oldGroovyObj);
public void setGroovyObj(Object groovyObj);
public void setProperty(String property);
public void setGroovyName(String groovyName);
}
