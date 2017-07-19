package groovy.bizflow.intopiece

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



import java.text.SimpleDateFormat;


class MicroManager extends MicroMvcTemplate{
	private static Logger logger=Logger.getLogger(MicroManager.class);
	public String pageName="listRoleInfo.jsp";
	public String tableName="bizflow_intopiece_list";

	
	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

	public void createInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sTime = sf.format(new Date());
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String bizId = sf2.format(new Date());
		requestParamMap.put("create_time",sTime);
		requestParamMap.put("meta_key",bizId);
		Integer retStatus=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "createInfoService",requestParamMap, tableName);
		gOutputParam.setResultObj(retStatus);
		return;
	}
	
	
		
	public void viewTaskDataGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		String operFlag=httpRequest.getParameter("operFlag");
		Map infoMap=null;
		if(id!=null && !"".equals(id)){
			infoMap=getInfoByIdService(id,"bizflow_intopiece_list");
		}
		httpRequest.setAttribute("formdata", infoMap);
		httpRequest.setAttribute("operFlag", operFlag);
		String toPage="/nh-micro-jsp/bizflow-page/intoPiece/intoPieceInfo.jsp";
		//String toPage="/nh-micro-jsp/cus-jsp/intoPieceInfo.jsp";
		httpRequest.getRequestDispatcher(toPage).forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;

	}

	public void viewQaApprovalGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		String operFlag=httpRequest.getParameter("operFlag");
		Map infoMap=null;
		infoMap=getInfoByIdService(id,"bizflow_intopiece_list");

		httpRequest.setAttribute("formdata", infoMap);
		httpRequest.setAttribute("operFlag", operFlag);
		String toPage="/nh-micro-jsp/bizflow-page/intoPiece/intoPieceQaApprovalInfo.jsp";
		httpRequest.getRequestDispatcher(toPage).forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;

	}
	
	
	
	public void startFlow(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		GroovyExecUtil.execGroovyRetObj("bizflow_intopiece_engine", "execStart", id,null);
		return;

	}
	
	public void viewTaskUploadGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		String operFlag=httpRequest.getParameter("operFlag");
		Map infoMap=null;
		if(id!=null && !"".equals(id)){
			infoMap=getInfoByIdService(id,"bizflow_intopiece_list");
		}
		changeJsonMap(infoMap);
		httpRequest.setAttribute("formdata", infoMap);
		httpRequest.setAttribute("operFlag", operFlag);

		System.out.println(infoMap);
		//String toPage="/nh-micro-jsp/bizflow-page/intoPiece/intoPieceInfo.jsp";
		String toPage="/nh-micro-jsp/bizflow-page/intoPiece/intoPieceResourceInfo.jsp";
		httpRequest.getRequestDispatcher(toPage).forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;

	}
	

	
	
	public void appendResource(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		String fileId=httpRequest.getParameter("fileId");
		
		appendNodeData4ListService(fileId,"dbcol_ext_fileid_json",id,"bizflow_intopiece_list","meta_content","id","");
		
/*		Map infoMap=null;
		if(id!=null && !"".equals(id)){
			infoMap=getInfoByIdService(id,"bizflow_intopiece_list");
		}
		MicroMetaDao microDao=MicroMetaDao.getInstance();
		String sql="update bizflow_intopiece_list set meta_content=JSON_ARRAY_APPEND(meta_content,?,?) where id='"+id+"'";
		Object[] paramArray=new Object[2];
		String filter="\$.dbcol_ext_fileid_json";
		paramArray[0]=filter;
		paramArray[1]=fileId;
		microDao.updateObjByCondition(sql, paramArray);*/

		return;

	}
	
}
