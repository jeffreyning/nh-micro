package groovy.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;


import groovy.template.MicroMvcTemplate;
import net.sf.json.JSONObject;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

class BankService extends MicroMvcTemplate {

	private String tableName="t_front_user_bankcard";

	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}


	/**
	 * 授权绑卡接口
	 * @param req
	 * @return
	 */
	public void userAuthTiedCard(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode", 
		gInputParam,gOutputParam,gContextParam);

		String cardNo=httpRequest.getParameter("cardNo");
		String phone=httpRequest.getParameter("mobile");
		
		Map<String ,String> paramMap=new HashMap();
		paramMap.put("user_code",nhUserName);
		paramMap.put("status","1");
		Map<String ,String> sortMap=new HashMap();
		sortMap.put("sort","id");
		sortMap.put("order","desc");
		List list=getInfoListAllService(paramMap,tableName,sortMap);
			

		String businessId="";
		Map map=getInfoByBizIdService("pay_businessid","nh_micro_dict_items","meta_type");
		if(map!=null){
			businessId=map.get("meta_key").toString();
		}
		Map userMap=getInfoByBizIdService(nhUserName,"t_front_user","user_code");
		if(userMap==null || userMap.get("real_name")==null || userMap.get("real_name").toString().trim().length()==0){
			gOutputParam.setResultStatus(1);
			gOutputParam.setResultMsg("请先实名认证！");
			return;
		}
		//String identifyId=GroovyExecUtil.execGroovyRetObj("serial_number_util", "getNumber","BK");
		String identifyId=UUID.randomUUID().toString();
		Map inMap=new HashMap();
		inMap.put("businessId",businessId);
		inMap.put("memberId",nhUserName);
		inMap.put("cardNo",cardNo);
		inMap.put("owner",userMap.get("real_name").toString());
		inMap.put("certType","01");
		inMap.put("certNo",userMap.get("card_no").toString());
		inMap.put("phone",phone);
		inMap.put("identifyId",identifyId);
		
		Map returnMap=new HashMap();
		int status=GroovyExecUtil.execGroovyRetObj("front_pay_api", "bindCard", inMap,returnMap);
		
		
		Map requestParamMap=new HashMap<String,String>();
		if(status==0){
			requestParamMap.put("user_code",nhUserName);
			requestParamMap.put("bank_card_no",cardNo);
			requestParamMap.put("bank_account_code",returnMap.get("bankCode"));
			requestParamMap.put("bank_phone",returnMap.get("phone"));
			requestParamMap.put("status","1");
			requestParamMap.put("is_default","1");
			requestParamMap.put("bind_id",returnMap.get("bindId"));
			requestParamMap.put("bank_name",returnMap.get("bankName"));
			requestParamMap.put("card_last",returnMap.get("cardLast"));
			requestParamMap.put("bank_card_type",returnMap.get("bankCardType"));
			requestParamMap.put("create_time","now()");
			Integer retStatus=createInfoService(requestParamMap, "t_front_user_bankcard");
			Map updateMap=new HashMap();
			updateMap.put("is_bindcard","1");
			Integer userStatus=updateInfoByBizIdService(nhUserName, "t_front_user","user_code",updateMap);
			gOutputParam.setResultMsg(returnMap.get("resultMsg"));
		}else{
			gOutputParam.setResultStatus(1);
			gOutputParam.setResultMsg(returnMap.get("resultMsg"));
		}

	}
	/**
	 * 查询已绑卡列表
	 * @param req
	 * @return
	 */
	public void getUserTiedCard(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode", 
		gInputParam,gOutputParam,gContextParam);

			Map<String ,String> paramMap=new HashMap();
			paramMap.put("user_code",nhUserName);
			paramMap.put("status","1");
			Map<String ,String> sortMap=new HashMap();
			sortMap.put("sort","id");
			sortMap.put("order","desc");
			List list=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoListAllService",paramMap,"t_front_user_bankcard",sortMap);
			if(list!=null && list.size()>0 ){
				Map map=list.get(0);
				gOutputParam.setResultObj(map);

			}else{
				gOutputParam.setResultMsg("未绑卡");
			}

	}
	
	/**
	 * 银行卡所属行
	 * @param req
	 * @return
	 */
	public  void getBankCardBelong(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");

		String businessId="";
		Map map=getInfoByBizIdService("pay_businessid","nh_micro_dict_items","meta_type");
		if(map!=null){
			businessId=map.get("meta_key").toString();
		}
		
		String cardNo=httpRequest.getParameter("cardNo");
		JSONObject json=new JSONObject();
		json.put("businessId",businessId);
		json.put("cardNo",cardNo);
		Map returnMap=new HashMap();
		if(returnMap!=null && "000000".equals(returnMap.get("resultCode").toString())
			&&returnMap.get("bankCode").toString()!=null&&returnMap.get("bankCode").toString().length()>0){
			if(!"1".equals(returnMap.get("bankCardType").toString())){//储蓄卡

				Map retMap=new HashMap();
				retMap.put("bankName", returnMap.get("bankName"));
				retMap.put("bankCode", returnMap.get("bankCode"));
				retMap.put("cardNo", returnMap.get("cardNo"));
				
				gOutputParam.setResultObj(retMap);
			}else{//信用卡
				gOutputParam.setResultCode("2");
				gOutputParam.setResultMsg("信用卡不支持绑定！");
			}
		}else{
			gOutputParam.setResultCode("2");
			gOutputParam.setResultMsg(returnMap.get("resultMsg").toString());
		}
		return ;
	}
	
	
	public void bankCardSetGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		Map requestParamMap=getRequestParamMap(httpRequest);
		
		String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode",
			gInputParam,gOutputParam,gContextParam);
		
/*		String productCode=httpRequest.getParameter("productCode");
		Map productInfo=getInfoByBizIdService(productCode,"t_front_product","product_code");
		httpRequest.setAttribute("productInfo", productInfo);*/
		Map userInfo=getInfoByBizIdService(nhUserName,"t_front_user","user_code");
		httpRequest.setAttribute("userInfo", userInfo);
		Map cardInfo=getInfoByBizIdService(nhUserName,"t_front_user_bankcard","user_code");
		httpRequest.setAttribute("cardInfo", cardInfo);
		

		httpRequest.getRequestDispatcher("/front-page/bankcard_set.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
}
