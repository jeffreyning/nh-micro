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


class NhuserMenu extends MicroMvcTemplate{
	private static Logger logger=Logger.getLogger(NhuserMenu.class);
	public String pageName="";
	public String tableName="nh_micro_menu";

	
	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

	public void getInfoListAll4UserId(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Map sortMap=new HashMap();
		sortMap.put("sort", sort);
		sortMap.put("order", order);

/*		String sql="select menu.* from nh_micro_menu menu "+
		"where  menu.meta_key in ("+
		"select distinct(menu_unit.meta_key) from nh_micro_menu menu_unit left join nh_micro_ref_menu_role menu_role on menu_unit.meta_key=menu_role.meta_key "+
		"left join nh_micro_ref_user_role user_role on menu_role.meta_name=user_role.meta_name where user_role.meta_key=? or user_role.meta_key is null"+
		" )";*/
		String sql="select menu.* from nh_micro_menu menu "+
		"where  menu.meta_key in ("+
		"select distinct(menu_unit.meta_key) from nh_micro_menu menu_unit left join nh_micro_ref_menu_role menu_role on menu_unit.meta_key=menu_role.meta_key "+
		"left join nh_micro_ref_user_role user_role on menu_role.meta_name=user_role.meta_name where user_role.meta_key=? or (user_role.meta_key is null and menu_role.meta_name is null)"+
		" )";
		String userId=httpSession.getAttribute("nhUserName");
		//List retList=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoListAllService",requestParamMap, tableName,sortMap);
		List placeList=new ArrayList();
		placeList.add(userId);
		List retList=getInfoListAllServiceBySql(sql,placeList);
		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();

		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
}
