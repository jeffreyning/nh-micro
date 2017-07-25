package groovy.front

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
import javax.servlet.http.HttpSession;

class FrontProduct extends MicroMvcTemplate{
public String pageName="listDictionaryInfo";
public String tableName="t_front_account";


public String getPageName(HttpServletRequest httpRequest){
	return pageName;
}
public String getTableName(HttpServletRequest httpRequest){
	return tableName;
}

public void queryMyAccount(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	String nhUserName=httpSession.getAttribute("nhUserName");
	Map infoMap=getInfoByBizIdService(nhUserName,"t_front_account","user_code");
	Map retMap=new HashMap();
	retMap.put("available_balance", infoMap.get("available_balance"));
	retMap.put("total_investment", infoMap.get("total_investment"));
	retMap.put("frozen_amount", infoMap.get("frozen_amount"));
	JsonBuilder jsonBuilder=new JsonBuilder(retMap);
	String retStr=jsonBuilder.toString();
	HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
	httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	httpRequest.setAttribute("forwardFlag", "true");

}


			
}
