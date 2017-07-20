package groovy.bizflow.creditaudit

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.nh.micro.db.*;

import com.nh.micro.rule.engine.core.*;


import groovy.json.*;
import groovy.template.MicroMvcTemplate;
import net.sf.json.JSONArray
import net.sf.json.JSONObject

import com.nh.micro.template.MicroTranAopInter;


class MicroManager extends MicroMvcTemplate{
	private static Logger logger=Logger.getLogger(MicroManager.class);

	public String tableName="bizflow_creditaudit_list";

	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}
	

	public void submitApproval(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String pageName=getPageName(httpRequest);
		Map requestParamMap=new HashMap();
		requestParamMap=getRequestParamMap(httpRequest);
		String id=requestParamMap.get("id");
		updateInfoByIdService(id,"bizflow_creditaudit_list",requestParamMap);
		
		String approvalStatus=requestParamMap.get("approval_status");
		String approvalText=requestParamMap.get("approval_text");
		GroovyExecUtil.execGroovyRetObj("bizflow_creditaudit_engine","execFlow",id,"bizflow_creditaudit_first_approval",approvalStatus,approvalText,null);
		return;
	}
	
	public String execInit(String id,String nodeId,String approvalStatus,Map extParam){

	}

	
}
