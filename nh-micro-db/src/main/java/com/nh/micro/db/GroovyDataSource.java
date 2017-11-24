package com.nh.micro.db;

import java.lang.annotation.*; 

/**
 * 
 * @author ninghao
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface GroovyDataSource {
	String name() default "default"; 
	String oldName() default ""; 
}
