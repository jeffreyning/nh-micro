package groovy.front;

import java.util.HashMap;
import java.util.Map;
import java.util.Calendar
import java.util.Date;
import java.util.GregorianCalendar

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import groovy.json.*;
import groovy.template.MicroMvcTemplate;
import net.sf.json.JSONArray

import java.text.SimpleDateFormat;

class nrule extends MicroMvcTemplate{

	public String tableName="t_front_withdraw";
	
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}
	

	public void createWithdrawInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		String nhUserName=httpSession.getAttribute("nhUserName");
		
		String withdrawMoney=httpRequest.getParameter("withdraw_money");
		String firstWithdraw=httpRequest.getParameter("first_withdraw");
		String bankProvinceCode=httpRequest.getParameter("bank_province_code");
		String bankCityCode=httpRequest.getParameter("bank_city_code");
		String bankBranch=httpRequest.getParameter("bank_branch");
		String bankSubbranch=httpRequest.getParameter("bank_subbranch");
		String verifyCode=httpRequest.getParameter("verify_code");

		Map rechargeMap=new HashMap();
		
		String tableName=getTableName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String withdrawNumber=sdf.format(new Date());
		requestParamMap.put("apply_user_code",nhUserName);
		requestParamMap.put("withdraw_number",withdrawNumber); 
		requestParamMap.put("approve_status","0");
		requestParamMap.put("withdraw_status","1");
		//添加手续费start
		String fee="1.00";
		requestParamMap.put("withdraw_fee",fee);
		//添加手续费end
		
		BigDecimal withdrawMoneyUp=(new BigDecimal(withdrawMoney)).subtract(new BigDecimal(fee));
		withdrawMoney = withdrawMoneyUp.toString();

		
		//获取用户绑卡信息start
		String table_2="t_front_user_bankcard";
		String where_2=" user_code='"+nhUserName+"' and status='1' ";
		String select_2=" bank_card_no,bank_name ";
		Map sortMap=new HashMap();
		sortMap.put("sort","id");
		sortMap.put("order","desc");
		List klist=(List)GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoListAllServiceInner",new HashMap(),table_2,sortMap,where_2,select_2,"",1);
		if(klist!=null&&klist.size()>0){
			Map tmp=klist.get(0);
			requestParamMap.put("dbcol_ext_withdraw_user_bank",tmp.get("bank_name").toString());
			requestParamMap.put("dbcol_ext_withdraw_user_account",tmp.get("bank_card_no").toString());
			rechargeMap.put("dbcol_ext_recharge_user_bank",tmp.get("bank_name").toString());
			rechargeMap.put("dbcol_ext_recharge_user_account",tmp.get("bank_card_no").toString());
		}
		//获取获取用户绑卡信息end
		
		//更新绑卡表start
		if("1".equals(firstWithdraw)){
			String condition=" status='1' ";
			String setStr=" bank_province_code='"+bankProvinceCode+"',bank_city_code='"+bankCityCode+"',bank_branch='"+bankBranch+"',bank_subbranch='"+bankSubbranch+"' ";
			Integer status=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "updateInfoByBizIdService",nhUserName,"t_front_user_bankcard","user_code",new HashMap(),condition,setStr,"");
		}
		//更新绑卡表end

		
		//扣减账户金额start
		String sql="update t_front_account set available_balance=available_balance-? where user_code=?";
		List paramList=new ArrayList();
		paramList.add(withdrawMoney);
		paramList.add(nhUserName);
		updateInfoServiceBySql(sql,paramList);
		
		//扣减账户金额end
		
		
		//插入提现流水信息start
		rechargeMap.put("recharge_money",withdrawMoney);
		rechargeMap.put("recharge_user_code",nhUserName);
		rechargeMap.put("recharge_type","2");
		rechargeMap.put("recharge_status","1");
		rechargeMap.put("pay_way","3");
		Map map3=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoByBizIdService",nhUserName, "t_front_user","user_code");
		if(map3!=null&&map3.size()>0){
			if(map3.get("real_name")!=null&&!"".equals(map3.get("real_name"))){
				rechargeMap.put("dbcol_ext_recharge_user_account_name",map3.get("real_name"));
			}
		}
		
		
		Map map4=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoByBizIdService",nhUserName, "t_front_account","user_code");
		if(map4!=null&&map4.size()>0){
			rechargeMap.put("account_balance",((new BigDecimal(map4.get("available_balance"))).add(new BigDecimal(fee))).toString());

		}
		rechargeMap.put("inner_recharge_number",withdrawNumber);
		GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "createInfoService",rechargeMap, "t_front_recharge");
		//插入提现流水信息end
		
		//插入手续费流水信息start
		rechargeMap.put("account_balance",map4.get("available_balance"));
		rechargeMap.put("recharge_money",fee);
		rechargeMap.put("recharge_type","5");
		String feeNumber=
		rechargeMap.put("inner_recharge_number",withdrawNumber);
		rechargeMap.remove("id");
		GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "createInfoService",rechargeMap, "t_front_recharge");
		//插入手续费流水信息end
		
		//插入提现申请记录表start
		requestParamMap.put("fee_number",withdrawNumber);
		requestParamMap.put("withdraw_money",withdrawMoney);
		GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "createInfoService",requestParamMap,tableName);
		//插入提现申请记录表end
		

	}
	
}
