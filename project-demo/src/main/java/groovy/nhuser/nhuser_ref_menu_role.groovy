package groovy.nhuser

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.nh.micro.rule.engine.core.*;
import groovy.template.MicroMvcTemplate;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import org.apache.log4j.Logger;

import groovy.json.*;


class MicroManager extends MicroMvcTemplate{
	private static Logger logger=Logger.getLogger(MicroManager.class);
	public String pageName="listRoleInfo.jsp";
	public String tableName="nh_micro_ref_menu_role";

	
	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

	public void getInfoList4Ref(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");

		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");

		String pageName=getPageName(httpRequest);
		Map requestParamMap=new HashMap();
		Map sortMap=new HashMap();
		String userId=httpRequest.getParameter("user_id");
		requestParamMap.put("ref.meta_key", userId);

		String tableName="nh_micro_ref_menu_role ref left join nh_micro_role role on ref.meta_name=role.meta_key";
		List retList=getInfoListAllService(requestParamMap, tableName,sortMap,"","ref.id,ref.meta_key,role.meta_name");

		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();

		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	
	
}
