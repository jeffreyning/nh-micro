package com.nh.micro.rule.engine.core.plugin;

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
public @interface MicroAuth {

	String checkName() default "";
	String checkMethod() default "checkExecAuth";
    String[] value() default {};
    String logical() default "and";
    String exclude() default "false";
}
