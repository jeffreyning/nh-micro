package com.nh.micro.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;

import com.nh.micro.config.MicroConfigPlugin;
import com.nh.micro.controller.MicroControllerPlugin;
import com.nh.micro.controller.MicroControllerServlet;
import com.nh.micro.dao.mapper.MicroInjectDaoPlugin;
import com.nh.micro.db.MicroDbHolder;
import com.nh.micro.rule.engine.context.MicroContextHolder;
import com.nh.micro.rule.engine.context.MicroInjectPlugin;
import com.nh.micro.rule.engine.core.GFileBean;
import com.nh.micro.rule.engine.core.GroovyAopChain;
import com.nh.micro.rule.engine.core.GroovyLoadUtil;
import com.nh.micro.rule.engine.core.plugin.MicroAopPlugin;
import com.nh.micro.service.MicroInjectGroovyPlugin;
import com.nh.micro.rule.engine.core.GroovyInitUtil;

/**
 * 
 * @author ninghao
 *
 */
@SpringBootConfiguration
public class NhMicroAutoConfiguration {
	@Value("${nhmicro.config.pluginFileArray:/application.properties}")
	public String configPluginFileArray;

	@Value("${nhmicro.config.ruleStamp:true}")
	public String ruleStamp;
	
	@Value("${nhmicro.config.jarFileFlag:true}")
	public String jarFileFlag;
	
	@Value("${nhmicro.config.dirFlag:true}")
	public String dirFlag;
	
	@Value("${nhmicro.config.rulePath:/groovy/}")
	public String rulePath;
	
	@Value("${nhmicro.config.mvc.prepath:micromvc}")
	public String prepath;

	
	@Bean
	@Lazy(false)
	public MicroConfigPlugin microConfigPlugin(){
		MicroConfigPlugin microConfigPlugin=new MicroConfigPlugin();
		String[] fileArray=null;
		if(configPluginFileArray!=null && !"".equals(configPluginFileArray)){
			fileArray=configPluginFileArray.split(",");
		}
		if(fileArray!=null){
			for(String filePath:fileArray){
				microConfigPlugin.fileArray.add(filePath);
			}
		}
		return microConfigPlugin;
	}

	@Bean
	@Lazy(false)
	public MicroControllerPlugin microControllerPlugin(){
		MicroControllerPlugin microControllerPlugin=new MicroControllerPlugin();

		return microControllerPlugin;
	}
	
	@Bean
	@Lazy(false)
	public MicroAopPlugin microAopPlugin(){
		MicroAopPlugin microAopPlugin=new MicroAopPlugin();

		return microAopPlugin;
	}	
	
	@Bean
	@Lazy(false)
	public MicroInjectPlugin microInjectPlugin(){
		MicroInjectPlugin microInjectPlugin=new MicroInjectPlugin();

		return microInjectPlugin;
	}	
	
	@Bean
	@Lazy(false)
	public MicroInjectGroovyPlugin microInjectGroovyPlugin(){
		MicroInjectGroovyPlugin microInjectGroovyPlugin=new MicroInjectGroovyPlugin();

		return microInjectGroovyPlugin;
	}	
	
	@Bean
	@Lazy(false)
	public MicroInjectDaoPlugin microInjectDaoPlugin(){
		MicroInjectDaoPlugin microInjectDaoPlugin=new MicroInjectDaoPlugin();
		return microInjectDaoPlugin;
	}	
	
	@Bean
	@Lazy(false)
	public MicroContextHolder microContextHolder(){
		MicroContextHolder microContextHolder=new MicroContextHolder();
		return microContextHolder;		
	}
	
	@Bean(initMethod="init")
	@Lazy(false)
	public GroovyAopChain groovyAopChain(){
		GroovyAopChain groovyAopChain=new GroovyAopChain();
		return groovyAopChain;		
	}	
	
	@Bean
	@Lazy(false)
	public GroovyLoadUtil groovyLoadUtil(){
		GroovyLoadUtil groovyLoadUtil=new GroovyLoadUtil();
		groovyLoadUtil.getPluginList().add(microControllerPlugin());
		groovyLoadUtil.getPluginList().add(microAopPlugin());
		groovyLoadUtil.getPluginList().add(microInjectPlugin());
		groovyLoadUtil.getPluginList().add(microInjectGroovyPlugin());
		groovyLoadUtil.getPluginList().add(microInjectDaoPlugin());
		groovyLoadUtil.getPluginList().add(microConfigPlugin());
		return groovyLoadUtil;		
	}	
	
	@Bean(initMethod="initGroovy")
	@Lazy(false)
	public GroovyInitUtil groovyInitUtil(){
		GroovyInitUtil groovyInitUtil=new GroovyInitUtil();
		GFileBean gFileBean=new GFileBean();
		gFileBean.setDirFlag(Boolean.valueOf(dirFlag));
		gFileBean.setJarFileFlag(Boolean.valueOf(jarFileFlag));
		gFileBean.setRuleStamp(ruleStamp);
		gFileBean.rulePath=rulePath;
		groovyInitUtil.fileList.add(gFileBean);
		return groovyInitUtil;
		
	}
	
	@Bean
	@Lazy(false)
	public MicroDbHolder microDbHolder(JdbcTemplate jdbcTemplate){
		MicroDbHolder microDbHolder=new MicroDbHolder();
		microDbHolder.getDbHolder().put("default", jdbcTemplate);
		return microDbHolder;
		
	}	
	
	@Bean
	public MicroControllerServlet microController(){
		MicroControllerServlet microControllerServlet=new MicroControllerServlet();
		microControllerServlet.setPrepath(prepath);
		return microControllerServlet;
	}
	
	@Bean
	public ServletRegistrationBean servletRegistrationBean(){
		ServletRegistrationBean servletRegistrationBean=new ServletRegistrationBean();
		servletRegistrationBean.setServlet(microController());
		servletRegistrationBean.getUrlMappings().add("/"+prepath+"/*");
		return servletRegistrationBean;
		
	}
}
