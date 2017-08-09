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
	public String tableName="t_front_recharge";


	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}


	public void rechargeBank(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode",
			gInputParam,gOutputParam,gContextParam);
		String bankPay=httpRequest.getParameter("bankPay");
		
		//调用三方支付接口
		GroovyExecUtil.execGroovyRetObj("front_pay_api", "startQuickPay", gInputParam, gOutputParam, gContextParam);

		
		//创建充值流水
		Map tranMap=new HashMap();
		SimpleDateFormat sdf_tran=new SimpleDateFormat("yyyyMMddHHmmss");
		String rechargeNumber=sdf_tran.format(new Date());
		tranMap.put("inner_recharge_number", rechargeNumber);
		tranMap.put("recharge_money",bankPay);
		tranMap.put("recharge_user_code",nhUserName);
		tranMap.put("recharge_type","3");
		tranMap.put("recharge_status","1");
		createInfoService(tranMap,"t_front_recharge");
		
		//返回流水id
		gOutputParam.setResultObj(rechargeNumber);
	}
	
	public void getMyRechargeListAll(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode", 
			gInputParam,gOutputParam,gContextParam);
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Map sortMap=new HashMap();
		sortMap.put("sort", "create_time");
		sortMap.put("order", "desc");
		Map paramMap=new HashMap();
		paramMap.put("recharge_user_code", nhUserName);

		List retList=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoListAllService",paramMap, "t_front_recharge",sortMap);
	
		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();
	
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	public void openRechargeGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode",
			gInputParam,gOutputParam,gContextParam);
		
		Map accountInfo=getInfoByBizIdService(nhUserName,"t_front_account","user_code");
		httpRequest.setAttribute("accountInfo", accountInfo);
		
		Map cardInfo=getInfoByBizIdService(nhUserName,"t_front_user_bankcard","user_code");
		httpRequest.setAttribute("cardInfo", cardInfo);
		
		Map userInfo=getInfoByBizIdService(nhUserName,"t_front_user","user_code");
		httpRequest.setAttribute("userInfo", userInfo);
		
		httpRequest.getRequestDispatcher("/front-page/recharge_mode.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
	}
	
	public void confirmQuickPayGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		String userCode=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode",
			gInputParam,gOutputParam,gContextParam);
		String rechargeNumber=httpRequest.getParameter("rechargeNumber");

		Map dataMap=getInfoByBizIdService(rechargeNumber,"t_front_recharge","inner_recharge_number");
		String bankPay=dataMap.get("recharge_money");
		
		//调用三方支付确认接口
		GroovyExecUtil.execGroovyRetObj("front_pay_api", "confirmQuickPay", gInputParam, gOutputParam, gContextParam);		
		
		//账务处理
		GroovyExecUtil.execGroovyRetObj("front_account_api", "addBalance", userCode, bankPay);
		
		//修改充值流水状态
		Map rechargeMap=new HashMap();
		rechargeMap.put("recharge_status","1");
		updateInfoByBizIdService(rechargeNumber,"t_front_recharge","inner_recharge_number",rechargeMap);
		httpRequest.getRequestDispatcher("/front-page/rechargeSuccess.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
	}
	
}
