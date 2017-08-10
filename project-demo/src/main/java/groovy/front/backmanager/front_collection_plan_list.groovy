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
public String tableName="t_front_collection_plan";


public String getPageName(HttpServletRequest httpRequest){
	return pageName;
}
public String getTableName(HttpServletRequest httpRequest){
	return tableName;
}

public void createPlan(String investId){
	Map investMap=getInfoByBizIdService(investId,"t_front_invest","order_number");
	String userCode=investMap.get("user_code");
	String investAmount=investMap.get("invest_amount");
	String expireProfit=investMap.get("expire_profit");
	String perNum=investMap.get("periods");
	String payDate=investMap.get("interrest_date");
	String repayDay=investMap.get("reback_date");
	String orderRate=investMap.get("order_rate");
	

	Map paramMap=new HashMap();
	paramMap.put("payDate",payDate);
	paramMap.get("repayDay",repayDay);
	paramMap.get("contractAmt",investAmount);
	paramMap.get("monthRate",orderRate);
	paramMap.get("contractPeriods",perNum);
	List<Map> planList=GroovyExecUtil.execGroovyRetObj("front_repayplan_date", "calcuRepayplan", paramMap);
	if(planList==null){
		return;
	}
	int size=planList.size();
	for(int i=0;i<size;i++){
		Map rowMap=planList.get(i);
		String lixi=rowMap.get("lixi");
		String benjin=rowMap.get("benjin");
		String shouAmt=(new BigDecimal(lixi)).add(new BigDecimal(benjin)).toString();
		Map inputMap=new HashMap();
		inputMap.put("user_code", userCode);
		inputMap.put("order_no", investId);
		inputMap.put("per_num", Integer.valueOf(i+1).toString());

		inputMap.put("shou_pri", benjin);
		inputMap.put("shou_int", lixi);
		inputMap.put("shou_amt", shouAmt);
		createInfoService(inputMap,"t_front_collection_plan");
	}
}
			
}
