package groovy.controller;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;


import foo.repository.*;
import foo.service.TestService;
import groovy.json.*;
import com.minxin.micro.db.*;
import com.nh.micro.controller.MicroUrlMapping;
import com.nh.micro.db.GroovyDataSource;
import com.nh.micro.rule.engine.core.plugin.MicroAop;
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy;
import com.nh.micro.service.InjectGroovy;
import com.nh.micro.template.MicroDbProxy;
import com.nh.micro.template.MicroTMProxy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.transaction.annotation.Transactional;
import groovy.template.MicroControllerTemplate;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author ninghao
 *
 */

@MicroAop(name=[MicroDefaultLogProxy.class,MicroTMProxy.class,MicroDbProxy.class], property=["","",""])
@MicroUrlMapping(name="/TestController")
class TestController extends MicroControllerTemplate  {  

	
	@InjectGroovy(name="TestService")
	public TestService testService;
	
	public String tableName="micro_test5";

	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	} 
	
	
	@MicroUrlMapping(name="/test")
	public void test(HttpServletRequest request, HttpServletResponse response){

		testService.test("111");
		
	}

	@GroovyDataSource(name="default")
	@MicroUrlMapping(name="/getInfoById")
	@Transactional
	public void getInfoById(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.getInfoById(httpRequest,httpResponse);
	}
	
	@MicroUrlMapping(name="/delInfo")
	public void delInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		super.delInfo(httpRequest, httpResponse);
	}
}
