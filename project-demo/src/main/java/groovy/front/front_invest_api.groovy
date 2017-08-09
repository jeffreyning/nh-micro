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
	public String tableName="t_front_invest";


	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

/*	public String createInvestInfo(String userCode,String productCode,String investAmount){
		//生成投资记录
		Map investMap=new HashMap();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String orderNumber=sdf.format(new Date());
		investMap.put("order_number", orderNumber);
		investMap.put("user_code", userCode);
		investMap.put("user_name", userCode);

		investMap.put("bid_name", productCode);
		investMap.put("bid_code", productCode);
		investMap.put("product_name", productCode);
		investMap.put("product_code", productCode);

		investMap.put("invest_amount",investAmount);
		investMap.put("create_time", "now()");
		String tradeStatus="4";
		investMap.put("trade_status",tradeStatus);
		createInfoService(investMap,"t_front_invest");
		return orderNumber;
	}*/

	public void investProductGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){

		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
			String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode", 
		gInputParam,gOutputParam,gContextParam);
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		String orderNumber=httpRequest.getParameter("orderNumber");

		Map investMap=getInfoByBizIdService(orderNumber,"t_front_invest","order_number");
		String investAmount=investMap.get("invest_amount");
		String bidCode=investMap.get("bid_code");
		
		Map updateMap=new HashMap();
		updateMap.put("trade_status", "1");
		updateInfoByBizIdService(orderNumber,"t_front_invest","order_number",updateMap);


		//添加投资总额
		String subAccountSql0="update t_front_account set total_investment=total_investment+? where user_code=?"
		List placeList0=new ArrayList();
		placeList0.add(investAmount);
		placeList0.add(nhUserName);
		updateInfoServiceBySql(subAccountSql0,placeList0);
		
		//扣除账户金额
		String subAccountSql="update t_front_account set available_balance=available_balance-?  where user_code=?"
		List placeList=new ArrayList();
		placeList.add(investAmount);
		placeList.add(nhUserName);
		updateInfoServiceBySql(subAccountSql,placeList);
		
		//修改标的金额
		String subAccountSql2="update t_front_bid set surplus_invest_money=surplus_invest_money-? , have_money=have_money+?  where bid_code=?"
		List placeList2=new ArrayList();
		placeList2.add(investAmount);
		placeList2.add(investAmount);
		placeList2.add(bidCode);
		updateInfoServiceBySql(subAccountSql2,placeList2);
		

		//生成资金流水记录
		Map tranMap=new HashMap();
		SimpleDateFormat sdf_tran=new SimpleDateFormat("yyyyMMddHHmmss");
		String tranId=sdf_tran.format(new Date());
		tranMap.put("inner_recharge_number", tranId);
		tranMap.put("recharge_money",investAmount);
		tranMap.put("recharge_user_code",nhUserName);
		tranMap.put("recharge_type","3");
		tranMap.put("recharge_status","2");
		tranMap.put("create_time", "now()");
		createInfoService(tranMap,"t_front_recharge");
		
		httpRequest.getRequestDispatcher("/front-page/paymentSuccess.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}

	public void investProductBank(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){

		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
			String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode", 
		gInputParam,gOutputParam,gContextParam);
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		String orderNumber=httpRequest.getParameter("orderNumber");
		String accountPay=httpRequest.getParameter("accountPay");
		String bankPay=httpRequest.getParameter("bankPay");
		
		Map userInfo=getInfoByBizIdService(nhUserName,"t_front_user","user_code");
		String bindFlag=userInfo.get("is_bindcard");
		if(bindFlag==null || !"1".equals(bindFlag)){
			gOutputParam.setResultStatus(1);
			gOutputParam.setResultMsg("请先绑卡后才能快捷支付");
			gOutputParam.setResultCode("0001");
			return;
		}


		Map investMap=new HashMap();
		//String investAmount=investMap.get("invest_amount");
		investMap.put("account_pay",accountPay);
		investMap.put("bank_pay",bankPay);
		investMap.put("trade_status","4");
		 
		updateInfoByBizIdService(orderNumber,"t_front_invest","order_number",investMap);
		GroovyExecUtil.execGroovyRetObj("front_pay_api", "startQuickPay", gInputParam, gOutputParam, gContextParam);


		return;
	}
	
	public void getMyInvestListAll(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
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
		paramMap.put("user_code", nhUserName);

		List retList=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoListAllService",paramMap, "t_front_invest",sortMap);
	
		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();
	
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	public void confirmQuickPayGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
			String userCode=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode",
		gInputParam,gOutputParam,gContextParam);
		String orderNumber=httpRequest.getParameter("orderNumber");
		
		Map investInfo=getInfoByBizIdService(orderNumber,"t_front_invest","order_number");
		String bankPay=investInfo.get("bank_pay");
		String investAmount=investInfo.get("invest_amount");
		
		//调用三方支付确认接口
		GroovyExecUtil.execGroovyRetObj("front_pay_api", "confirmQuickPay", gInputParam, gOutputParam, gContextParam);
		
		//增加充值金额
		GroovyExecUtil.execGroovyRetObj("front_account_api", "addBalance", userCode, bankPay);
	
		//扣除账户金额
		String subAccountSql="update t_front_account set available_balance=available_balance-? where user_code=?"
		List placeList=new ArrayList();
		placeList.add(investAmount);
		placeList.add(userCode);
		updateInfoServiceBySql(subAccountSql,placeList);
				
		//生成资金流水记录
		Map tranMap=new HashMap();
		SimpleDateFormat sdf_tran=new SimpleDateFormat("yyyyMMddHHmmss");
		String tranId=sdf_tran.format(new Date());
		tranMap.put("inner_recharge_number", tranId);
		tranMap.put("recharge_money",investAmount);
		tranMap.put("recharge_user_code",userCode);
		tranMap.put("recharge_type","3");
		tranMap.put("recharge_status","2");
		tranMap.put("create_time", "now()");
		createInfoService(tranMap,"t_front_recharge");
		
		//添加投资总额
		String subAccountSql0="update t_front_account set total_investment=total_investment+? where user_code=?"
		List placeList0=new ArrayList();
		placeList0.add(investAmount);
		placeList0.add(userCode);
		updateInfoServiceBySql(subAccountSql0,placeList0);
		
		httpRequest.getRequestDispatcher("/front-page/paymentSuccess.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;
		
		Map updateMap=new HashMap();
		updateMap.put("trade_status", "1");
		updateInfoByBizIdService(orderNumber,"t_front_invest","order_number",updateMap);
		
		}
}
