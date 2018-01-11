package com.nh.micro.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ ElementType.TYPE,ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented

/**
 * 
 * @author ninghao
 *
 */
public @interface MicroUrlMapping {
public String name();
public String version() default "";
}
