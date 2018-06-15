package com.nh.micro.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import com.nh.micro.service.NServiceMapperFactory;


/**
 * 
 * @author ninghao
 *
 */
public class GroovyScanner extends ClassPathBeanDefinitionScanner {
	  public GroovyScanner(BeanDefinitionRegistry registry) {
	      super(registry);
	  }
	  public void registerDefaultFilters() {
		  this.addIncludeFilter(new TypeFilter() {
		        @Override
		        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
		        	//add 201806 ninghao
		          boolean flag=metadataReader.getAnnotationMetadata().hasAnnotation("com.nh.micro.service.InjectGroovy");
		          return flag;
		        }
		      });
		  
	  }
	  public Set<BeanDefinitionHolder> doScan(String... basePackages) {
	      Set<BeanDefinitionHolder> beanDefinitions =   super.doScan(basePackages);
	      for (BeanDefinitionHolder holder : beanDefinitions) {
	          GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
	          definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
	          definition.setBeanClass(NServiceMapperFactory.class);
	      }
	      return beanDefinitions;
	  }


	  public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
	    return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
	  }	  
}