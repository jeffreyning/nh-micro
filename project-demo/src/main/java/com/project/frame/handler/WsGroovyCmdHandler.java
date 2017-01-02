package com.project.frame.handler;

import java.util.HashMap;
import java.util.Map;



import org.springframework.stereotype.Component;


import net.sf.json.JSONObject;

import com.nh.esb.core.INhCmdConst;
import com.nh.esb.core.INhCmdHandler;
import com.nh.esb.core.NhCmdRequest;
import com.nh.esb.core.NhCmdResult;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

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
		gInputParam.setSubName((String) inMap.get("subName"));
		GOutputParam gOutputParam=new GOutputParam(outMap);
		GContextParam gContextParam=new GContextParam();
		


		boolean status=GroovyExecUtil.execGroovySimple4Obj(groovyName, gInputParam, gOutputParam,gContextParam);
		if(status==false){
			result.setResultStatus(INhCmdConst.STATUS_ERROR);
			result.setErrMsg("exec_groovy_error");
			return;
		}
		String retStr=JSONObject.fromObject(gOutputParam).toString();
		result.setResultData(retStr);
		
	}

}
