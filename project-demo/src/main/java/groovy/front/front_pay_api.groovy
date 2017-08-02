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

class FrontPayApi extends MicroMvcTemplate{
	public String pageName="listDictionaryInfo";
	public String tableName="t_front_invest";

	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

	public void startQuickPay(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
	}
	
	public void confirmQuickPayGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		String userCode=httpSession.getAttribute("nhUserName");
		String orderNumber=httpRequest.getParameter("orderNumber");
		
		Map investInfo=getInfoByBizIdService(orderNumber,"t_front_invest","order_number");
		String bankPay=investInfo.get("bank_pay");
				
		GroovyExecUtil.execGroovyRetObj("front_account_api", "addBalance", userCode, bankPay);
		
		GroovyExecUtil.execGroovyRetObj("front_invest_api", "investProductGo", gInputParam, gOutputParam, gContextParam);
	}

	
}
