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
	public String tableName="t_front_bid";


	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

	public void getInfoListAll(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){

		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");

		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Map sortMap=new HashMap();
		sortMap.put("sort", sort);
		sortMap.put("order", order);
		List retList=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoListAllService",requestParamMap, tableName,sortMap);

		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();

		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));

		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}

	public void productDetailGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){

		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		Map requestParamMap=getRequestParamMap(httpRequest);
		String productCode=httpRequest.getParameter("productCode");
		Map productInfo=getInfoByBizIdService(productCode,"t_front_bid","bid_code");
		httpRequest.setAttribute("productInfo", productInfo);
		
		
		String nhUserCode=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode",
			gInputParam,gOutputParam,gContextParam);
		Map accountInfo=getInfoByBizIdService(nhUserCode,"t_front_account","user_code");
		httpRequest.setAttribute("accountInfo", accountInfo);
		
		httpRequest.getRequestDispatcher("/front-page/regularProductDetail.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}

	public void productPayGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){

		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		String nhUserCode=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode", 
		gInputParam,gOutputParam,gContextParam);
		Map requestParamMap=getRequestParamMap(httpRequest);
		String productCode=httpRequest.getParameter("productCode");
		String investAmount=httpRequest.getParameter("investAmount");
		Map productInfo=getInfoByBizIdService(productCode,"t_front_bid","bid_code");
		//httpRequest.setAttribute("productInfo", productInfo);
		String orderNumber=GroovyExecUtil.execGroovyRetObj("front_invest_api", "createInvestInfo", nhUserCode,productCode,investAmount);
		Map investInfo=getInfoByBizIdService(orderNumber,"t_front_invest","order_number");
		httpRequest.setAttribute("investInfo", investInfo);
		
		Map accountInfo=getInfoByBizIdService(nhUserCode,"t_front_account","user_code");
		httpRequest.setAttribute("accountInfo", accountInfo);
		
		httpRequest.getRequestDispatcher("/front-page/pay.jsp").forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	

	
}
