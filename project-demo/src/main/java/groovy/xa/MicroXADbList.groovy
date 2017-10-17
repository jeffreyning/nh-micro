package groovy.xa

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import groovy.json.*;
import groovy.lang.GroovyObject;

import com.nh.micro.db.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.nh.micro.cache.base.*;
import com.nh.micro.db.Cutil;
import com.nh.micro.db.Cobj;
import com.nh.micro.db.MicroDbHolder;


import com.project.util.DESUtil;
import com.project.util.xa.XADataSourceHolder;

import org.springframework.jdbc.support.rowset.*;
import groovy.template.MicroMvcTemplate;


class MicroXADbList extends MicroMvcTemplate{

public String tableName="nh_micro_xadbconf";

public String getTableName(HttpServletRequest httpRequest){
	return tableName;
}
	public void restartDb(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String id=httpRequest.getParameter("id");
		Map infoMap=getInfoByIdService(id,"nh_micro_xadbconf");
		String resourceName=infoMap.get("unique_resource_name");
		 AtomikosDataSourceBean dataSource=XADataSourceHolder.getDataSource(resourceName);
		 if(dataSource!=null){
			 dataSource.close();
		 }
		 createDataSource(infoMap);
	}
	public void closeDb(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String id=httpRequest.getParameter("id");
		Map infoMap=getInfoByIdService(id,"nh_micro_xadbconf");
		String resourceName=infoMap.get("unique_resource_name");
		 AtomikosDataSourceBean dataSource=XADataSourceHolder.getDataSource(resourceName);
		 if(dataSource!=null){
			 dataSource.close();
		 }
		 XADataSourceHolder.holderMap.remove(resourceName);
	}
	private createDataSource(Map row){
		//String metaKey=(String) row.get("meta_key");
		String uniqueResourceName=(String) row.get("unique_resource_name");
		String xaDataSourceClassName=(String) row.get("xa_datasource_classname");
		String dbUser=(String) row.get("db_user");
		String dbPassWord=(String) row.get("db_password");
		if(dbPassWord.startsWith("nhjm-")){
			String temp=dbPassWord.substring(5);
			dbPassWord=DESUtil.decrypt(temp);
		}
		String dbUrl=(String) row.get("db_url");
		AtomikosDataSourceBean ads=new AtomikosDataSourceBean();
		ads.setUniqueResourceName(uniqueResourceName);
		ads.setXaDataSourceClassName(xaDataSourceClassName);
		ads.setPoolSize(3);
		ads.setReapTimeout(10);
		Properties xaProperties=new Properties();
		xaProperties.setProperty("user", dbUser);
		xaProperties.setProperty("password", dbPassWord);
		xaProperties.setProperty("url", dbUrl);
		ads.setXaProperties(xaProperties);
		ads.init();
		XADataSourceHolder.holderMap.put(uniqueResourceName, ads);
	}
	public void delInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		
		String id=httpRequest.getParameter("id");
		Map infoMap=getInfoByIdService(id,"nh_micro_xadbconf");
		String resourceName=infoMap.get("unique_resource_name");
		 AtomikosDataSourceBean dataSource=XADataSourceHolder.getDataSource(resourceName);
		 if(dataSource!=null){
			 dataSource.close();
		 }
		 XADataSourceHolder.holderMap.remove(resourceName);
		Integer retStatus=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "delInfoService",requestParamMap, tableName);
		gOutputParam.setResultObj(retStatus);
		return;
	}
	public void modifyPass(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String id=httpRequest.getParameter("id");
		String password=httpRequest.getParameter("db_password");
		String dbpass=DESUtil.encrypt(password);
		dbpass="nhjm-"+dbpass;
		Map paramMap=new HashMap();
		paramMap.put("db_password", dbpass);
		updateInfoByIdService(id,"nh_micro_xadbconf",paramMap);
	}
	
	public void createInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sTime = sf.format(new Date());
		requestParamMap.put("create_time",sTime);
		String password=httpRequest.getParameter("db_password");
		String dbpass=DESUtil.encrypt(password);
		dbpass="nhjm-"+dbpass;
		requestParamMap.put("db_password", dbpass);
		Integer retStatus=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "createInfoService",requestParamMap, tableName);
		gOutputParam.setResultObj(retStatus);
		return;
	}
	
}
