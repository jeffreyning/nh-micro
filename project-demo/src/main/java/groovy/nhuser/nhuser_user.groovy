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

class NhuserUser extends MicroMvcTemplate{
	public String pageName="listRoleInfo.jsp";
	public String tableName="nh_micro_user";

	
	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}
	

	public void modifyPass(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){

		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String id=httpRequest.getParameter("id");
		String user_password=httpRequest.getParameter("dbcol_ext_user_password");
		Map requestMap=new HashMap();
		requestMap.put("dbcol_ext_user_password", user_password);

		Integer retStatus=updateInfoByIdService(id,"nh_micro_user",requestMap);
		gOutputParam.setResultObj(retStatus);
		return;
	}
	

		
		
}
