package com.nh.micro.rule.engine.core.plugin;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)  
/**
 * 
 * @author ninghao
 *
 */
public @interface MicroAop {
public Class[] name() ;
public String[] property() ;
}
