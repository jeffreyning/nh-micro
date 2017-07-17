package groovy.product

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


class nrule extends MicroMvcTemplate{
public String pageName="listDictionaryInfo";
public String tableName="nh_micro_product_center_list";


public String getPageName(HttpServletRequest httpRequest){
	return pageName;
}
public String getTableName(HttpServletRequest httpRequest){
	return tableName;
}


	public void repayPlanTrial(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String payDate=httpRequest.getParameter("payDate");
		String dueDate=httpRequest.getParameter("dueDate");
		String repayDay=httpRequest.getParameter("repayDay");
		String contractAmt=httpRequest.getParameter("contractAmt");

		String contractPeriods=httpRequest.getParameter("contractPeriods");
		String productId=httpRequest.getParameter("productId");
		
		Map infoMap=getInfoByBizIdService(productId,"nh_micro_product_center_list","meta_key");
		String singleGeneralRate=infoMap.get("dbcol_ext_singleGeneralRate");
		String monthRate=infoMap.get("dbcol_ext_monthRate");
		
		if(productId==null || "".equals(productId)){
			return;
		}
		
		GInputParam algInputParam=new GInputParam();
		Map inputMap=new HashMap();
		inputMap.put("payDate", payDate);
		inputMap.put("dueDate", dueDate);
		inputMap.put("repayDay", repayDay);
		inputMap.put("contractAmt", contractAmt);
		inputMap.put("monthRate", monthRate);
		inputMap.put("contractPeriods", contractPeriods);
		inputMap.put("productId", productId);
		inputMap.put("singleGeneralRate", singleGeneralRate);

		
		
		algInputParam.setParamData(inputMap);
		GOutputParam algOutputParam=new GOutputParam();
		Map outputMap=new HashMap();
		algOutputParam.setResultObj(outputMap);
		GroovyExecUtil.execGroovyRetObj("product_algo_repayplan","execGroovy",algInputParam,algOutputParam);
		List retData=null;
		retData=outputMap.get("result");
		Map retMap=new HashMap();
		if(retData==null){
			retData=new ArrayList();
		}
		retMap.put("rows", retData);
		retMap.put("total", retData.size());
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		httpRequest.setAttribute("forwardFlag", "true");
	}
					
}
