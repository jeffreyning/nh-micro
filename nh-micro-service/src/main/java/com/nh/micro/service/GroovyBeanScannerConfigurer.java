package com.nh.micro.service;



import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author ninghao
 *
 */
public class GroovyBeanScannerConfigurer implements  BeanFactoryPostProcessor, ApplicationContextAware {
	private ApplicationContext applicationContext;
	private String scanPath;
	
	public String getScanPath() {
		return scanPath;
	}
	public void setScanPath(String scanPath) {
		this.scanPath = scanPath;
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	    this.applicationContext = applicationContext;
	}
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	    GroovyScanner scanner = new GroovyScanner((BeanDefinitionRegistry) beanFactory);
	    scanner.setResourceLoader(this.applicationContext);
	    String[] pathArray=scanPath.split(",");
	    scanner.scan(pathArray);
	}
}