package com.nh.micro.config;



import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface MicroConfig {
public String configFile() default "micro.properties";
public String name();
}
