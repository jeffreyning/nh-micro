package groovy.db

import com.alibaba.druid.pool.DruidDataSource;

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


class MicroDbList extends MicroMvcTemplate{

public String tableName="nh_micro_dbconf";

public String getTableName(HttpServletRequest httpRequest){
	return tableName;
}

public void restartDb(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	String id=httpRequest.getParameter("id");
	Map infoMap=getInfoByIdService(id,"nh_micro_dbconf");
	String metaKey=infoMap.get("meta_key");
	JdbcTemplate jdbcTemplate=MicroDbHolder.getDbSource(metaKey);
	 
	 if(jdbcTemplate!=null){
		 DruidDataSource dataSource=jdbcTemplate.getDataSource();
		 dataSource.close();
	 }
	 createDataSource(infoMap);
}
public void closeDb(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	String id=httpRequest.getParameter("id");
	Map infoMap=getInfoByIdService(id,"nh_micro_dbconf");
	String metaKey=infoMap.get("meta_key");
	JdbcTemplate jdbcTemplate=MicroDbHolder.getDbSource(metaKey);
	 if(jdbcTemplate!=null){
		 DruidDataSource dataSource=jdbcTemplate.getDataSource();
		 dataSource.close();
	 }
	 MicroDbHolder.getDbHolder().remove(metaKey);
}
	private createDataSource(Map row){
    	String metaKey=(String) row.get("meta_key");

    	String dbUser=(String) row.get("db_user");
    	String dbPassWord=(String) row.get("db_password");
    	if(dbPassWord.startsWith("nhjm-")){
    		String temp=dbPassWord.substring(5);
    		dbPassWord=DESUtil.decrypt(temp);
    	}
    	String dbUrl=(String) row.get("db_url");
    	DruidDataSource ds=new DruidDataSource();
    	ds.setUsername(dbUser);
    	ds.setPassword(dbPassWord);
    	ds.setUrl(dbUrl);
    	ds.setFilters("stat");
    	ds.setMaxActive(10);
    	ds.setInitialSize(1);
    	ds.setMaxWait(60000);
    	ds.setMinIdle(1);
    	ds.setTimeBetweenEvictionRunsMillis(60000);
    	ds.setMinEvictableIdleTimeMillis(60000);
    	ds.setValidationQuery("SELECT 'x'");
    	ds.setTestWhileIdle(true);
    	ds.setTestOnBorrow(false);
    	ds.setTestOnReturn(false);
    	ds.setPoolPreparedStatements(true);
    	ds.setMaxPoolPreparedStatementPerConnectionSize(10);
    	ds.init();
    	JdbcTemplate jdbcTemplate=new JdbcTemplate();
    	jdbcTemplate.setDataSource(ds);
    	MicroDbHolder.getDbHolder().put(metaKey, jdbcTemplate);
	}
public void delInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	String tableName=getTableName(httpRequest);
	String pageName=getPageName(httpRequest);
	Map requestParamMap=getRequestParamMap(httpRequest);
	
	String id=httpRequest.getParameter("id");
	Map infoMap=getInfoByIdService(id,"nh_micro_dbconf");
	String metaKey=infoMap.get("meta_key");
	JdbcTemplate jdbcTemplate=MicroDbHolder.getDbSource(metaKey);
	 if(jdbcTemplate!=null){
		 DruidDataSource dataSource=jdbcTemplate.getDataSource();
		 dataSource.close();
	 }
	 MicroDbHolder.getDbHolder().remove(metaKey);
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
	updateInfoByIdService(id,"nh_micro_dbconf",paramMap);
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
	public void test(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		MicroMetaDao microDao=MicroMetaDao.getInstance("d2");
		List list=microDao.queryObjJoinByCondition("select * from nh_micro_dbconf");
		System.out.println(list);
	}
}
