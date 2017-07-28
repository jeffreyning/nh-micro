package groovy.front

import groovy.template.MicroMvcTemplate

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;

import net.sf.json.JSONObject



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
		String nhUserName=httpSession.getAttribute("nhUserName");
		Map paramMap=new HashMap();
		paramMap.put("is_authentication", "1");
		updateInfoByBizIdService(nhUserName,"t_front_user","user_code",paramMap);
		

	}
	
	

}
