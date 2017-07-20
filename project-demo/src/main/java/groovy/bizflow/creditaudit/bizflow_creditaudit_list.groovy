package groovy.bizflow.creditaudit

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

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

	
	public void getBizInfoList4Page(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page=httpRequest.getParameter("page");
		String rows=httpRequest.getParameter("rows");
		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);

		Map requestParamMap=getRequestParamMap(httpRequest);
		Map pageMap=new HashMap();
		pageMap.put("page", page);
		pageMap.put("rows", rows);
		pageMap.put("sort", sort);
		pageMap.put("order", order);
		String sql="";
		sql="select credit.*,piece.meta_key as piece_meta_key,piece.remark as piece_remark,piece.meta_content as piece_meta_content from bizflow_creditaudit_list credit left join bizflow_intopiece_list piece on credit.meta_name=piece.meta_key";
		Map retMap=getInfoList4PageServiceByMySql(sql,pageMap);

		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();

		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	
	public void getBizInfoList4Page4First(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page=httpRequest.getParameter("page");
		String rows=httpRequest.getParameter("rows");
		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);

		Map requestParamMap=getRequestParamMap(httpRequest);
		Map pageMap=new HashMap();
		pageMap.put("page", page);
		pageMap.put("rows", rows);
		pageMap.put("sort", sort);
		pageMap.put("order", order);
		String sql="";
		sql="select credit.*,piece.meta_key as piece_meta_key,piece.remark as piece_remark,piece.meta_content as piece_meta_content from bizflow_creditaudit_list credit left join bizflow_intopiece_list piece on credit.meta_name=piece.meta_key";
		sql=sql+" where credit.meta_content->>'\$.dbcol_ext_bizflow_creditaudit_flow_curnode'='bizflow_creditaudit_first_approval'";
		Map retMap=getInfoList4PageServiceByMySql(sql,pageMap);

		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();

		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	
	public void getBizInfoList4Page4Final(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page=httpRequest.getParameter("page");
		String rows=httpRequest.getParameter("rows");
		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);

		Map requestParamMap=getRequestParamMap(httpRequest);
		Map pageMap=new HashMap();
		pageMap.put("page", page);
		pageMap.put("rows", rows);
		pageMap.put("sort", sort);
		pageMap.put("order", order);
		String sql="";
		sql="select credit.*,piece.meta_key as piece_meta_key,piece.remark as piece_remark,piece.meta_content as piece_meta_content from bizflow_creditaudit_list credit left join bizflow_intopiece_list piece on credit.meta_name=piece.meta_key";
		sql=sql+" where credit.meta_content->>'\$.dbcol_ext_bizflow_creditaudit_flow_curnode'='bizflow_creditaudit_final_approval'";
		Map retMap=getInfoList4PageServiceByMySql(sql,pageMap);

		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();

		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	public void viewTaskDataGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		String operFlag=httpRequest.getParameter("operFlag");
		Map creditMap=getInfoByIdService(id,"bizflow_creditaudit_list");
		String formId=creditMap.get("meta_name");
		Map infoMap=null;
		infoMap=getInfoByBizIdService(formId,"bizflow_intopiece_list","meta_key");
		httpRequest.setAttribute("formdata", infoMap);
		httpRequest.setAttribute("operFlag", operFlag);
		String toPage="/nh-micro-jsp/bizflow-page/creditAudit/creditAuditFormInfo.jsp";
		httpRequest.getRequestDispatcher(toPage).forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;

	}

	public void viewFirstApprovalGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		String operFlag=httpRequest.getParameter("operFlag");
		Map infoMap=null;
		infoMap=getInfoByIdService(id,"bizflow_creditaudit_list");

		httpRequest.setAttribute("formdata", infoMap);
		httpRequest.setAttribute("operFlag", operFlag);
		String toPage="/nh-micro-jsp/bizflow-page/creditAudit/creditAuditFirstApprovalInfo.jsp";
		httpRequest.getRequestDispatcher(toPage).forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;

	}
	
	public void viewFinalApprovalGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		String operFlag=httpRequest.getParameter("operFlag");
		Map infoMap=null;
		infoMap=getInfoByIdService(id,"bizflow_creditaudit_list");

		httpRequest.setAttribute("formdata", infoMap);
		httpRequest.setAttribute("operFlag", operFlag);
		String toPage="/nh-micro-jsp/bizflow-page/creditAudit/creditAuditFinalApprovalInfo.jsp";
		httpRequest.getRequestDispatcher(toPage).forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;

	}
	
	public void startFlow(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		GroovyExecUtil.execGroovyRetObj("bizflow_creditaudit_engine", "execStart", id,null);
		return;
	}
	
}
