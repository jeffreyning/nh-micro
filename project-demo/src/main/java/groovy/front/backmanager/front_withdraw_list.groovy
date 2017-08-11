package groovy.front.backmanager

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import groovy.json.*;
import com.nh.micro.db.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.nh.micro.cache.base.*;
import com.nh.micro.db.Cutil;
import com.nh.micro.db.Cobj;
import com.nh.micro.db.MicroDbHolder;


import org.springframework.jdbc.support.rowset.*;
import groovy.template.MicroMvcTemplate;


class FrontProduct extends MicroMvcTemplate{
public String pageName="listDictionaryInfo";
public String tableName="t_front_withdraw";


public String getPageName(HttpServletRequest httpRequest){
	return pageName;
}
public String getTableName(HttpServletRequest httpRequest){
	return tableName;
}



public void withdrawApproval(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
	String id=httpRequest.getParameter("id");
	String approve_status=httpRequest.getParameter("approve_status");
	String refuse_reason=httpRequest.getParameter("refuse_reason");
	
	Map paramMap=new HashMap();
	paramMap.put("id", id);
	paramMap.put("approve_status", approve_status);
	paramMap.put("refuse_reason", refuse_reason);
	
	updateInfoByIdService(id,"t_front_withdraw",paramMap);
	
	Map withdrawMap=getInfoByIdService(id,"t_front_withdraw");
	
	String withdrawNumber=withdrawMap.get("withdraw_number");
	Map inMap=new HashMap();
	inMap.put("recharge_status", "2");
	updateInfoByBizIdService(withdrawNumber,"t_front_recharge","inner_recharge_number",inMap);
	

	return;
}

}
