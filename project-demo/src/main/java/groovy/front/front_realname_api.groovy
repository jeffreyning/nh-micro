package groovy.front

import groovy.template.MicroMvcTemplate

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;

import net.sf.json.JSONObject
import com.nh.micro.rule.engine.core.GroovyExecUtil;


class front_realname_api extends MicroMvcTemplate{

	public String pageName="listUserInfo";
	public String tableName="t_athena_user";
	
	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}
	
	
	
	public void realName(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam) throws Exception{
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode", 
		gInputParam,gOutputParam,gContextParam);
		String realName=httpRequest.getParameter("realname");
		String idCard=httpRequest.getParameter("idcard");
		Map paramMap=new HashMap();
		paramMap.put("is_authentication", "1");
		paramMap.put("real_name", realName);
		paramMap.put("card_no", idCard);
		updateInfoByBizIdService(nhUserName,"t_front_user","user_code",paramMap);
		

	}
	
	

}
