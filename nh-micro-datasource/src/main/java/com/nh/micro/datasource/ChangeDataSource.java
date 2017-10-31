package com.nh.micro.datasource;

import java.lang.annotation.*; 

/**
 * 
 * @author ninghao
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface ChangeDataSource {
	String name() default "default"; 
}
