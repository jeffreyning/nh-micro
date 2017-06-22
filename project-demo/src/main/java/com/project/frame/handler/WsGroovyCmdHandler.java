package com.project.frame.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.esb.core.INhCmdConst;
import com.nh.esb.core.INhCmdHandler;
import com.nh.esb.core.NhCmdRequest;
import com.nh.esb.core.NhCmdResult;
import com.nh.esb.service.servlet.NhServletCmdContextHolder;

@Component
public class WsGroovyCmdHandler implements INhCmdHandler {

	@Override
	public void execHandler(NhCmdRequest request, NhCmdResult result) {
		String groovyName=request.getSubName();
		String inputData=request.getCmdData();
		Map inMap=(Map) JSONObject.toBean(JSONObject.fromObject(inputData),Map.class);
		if(inMap==null){
			inMap=new HashMap();
		}
		Map outMap=new HashMap();
		GInputParam gInputParam=new GInputParam(inMap);
		GOutputParam gOutputParam=new GOutputParam(outMap);
		GContextParam gContextParam=new GContextParam();
		gContextParam.getContextMap().put("httpRequest", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest());
		gContextParam.getContextMap().put("httpResponse", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpResponse());
		gContextParam.getContextMap().put("httpSession", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpSession());
		
		
		String groovySubName=NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest().getParameter("groovySubName");
		if(groovySubName==null ||"".equals(groovySubName)){
			groovySubName="execGroovy";
		}else{

		}
		boolean status=GroovyExecUtil.execGroovy(groovyName, groovySubName,gInputParam, gOutputParam,gContextParam);
		if(status==false){
			result.setResultStatus(INhCmdConst.STATUS_ERROR);
			result.setErrMsg("exec_groovy_error");
			return;
		}
		
		String retStr=JSONObject.fromObject(gOutputParam).toString();
		result.setResultData(retStr);
		
	}

}
