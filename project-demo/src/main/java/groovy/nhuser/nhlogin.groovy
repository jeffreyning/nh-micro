package groovy.nhuser;

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


class NhLogin  extends MicroMvcTemplate{

	public void execLogin(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		Map inMap=gInputParam.getParamData();
		Map outMap=gOutputParam.getResultObj();

		System.out.println("this is http");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		System.out.println(httpRequest);
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		System.out.println(httpSession);
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		System.out.println(httpResponse);
		String nhUserName=httpRequest.getParameter("nhUserName");
		String nhPassWord=httpRequest.getParameter("nhPassWord");
		if(checkPassword(nhUserName,nhPassWord)){
			httpSession.setAttribute("nhUserName",nhUserName);
			gOutputParam.setResultStatus(0);
			return;
		}
		gOutputParam.setResultStatus(1);
		gOutputParam.setResultMsg("error");
		return;
	}
	public void execLoginGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		Map inMap=gInputParam.getParamData();
		Map outMap=gOutputParam.getResultObj();


		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");

		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");

		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");

		String nhUserName=httpRequest.getParameter("nhUserName");
		String nhPassWord=httpRequest.getParameter("nhPassWord");
		String nhMainPage=httpRequest.getParameter("nhMainPage");
		String nhFailPage=httpRequest.getParameter("nhFailPage");
		
		if(nhMainPage==null || "".equals(nhMainPage)){
			nhMainPage="/nh-micro-jsp/main.jsp";
		}
		if(nhFailPage==null || "".equals(nhFailPage)){
			nhFailPage="/nh-micro-jsp/info.jsp";
		}
		if(checkPassword(nhUserName,nhPassWord)){
			httpRequest.setAttribute("forwardFlag", "true");
			httpSession.setAttribute("nhUserName",nhUserName);
			
			List roleList=GroovyExecUtil.execGroovyRetObj("nhuser_ref_user_role", "getRoleListByUserId", nhUserName);
			httpSession.setAttribute("nhRoleList", roleList);
			
			httpRequest.getRequestDispatcher(nhMainPage).forward(httpRequest, httpResponse);
			return;
		}else{
		httpRequest.setAttribute("forwardFlag", "true");
			httpRequest.setAttribute("nhloginMsg", "login error");
			httpRequest.getRequestDispatcher(nhFailPage).forward(httpRequest, httpResponse);
			return;
		}
		
	}
	
	
	public void execLogOutGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		Map inMap=gInputParam.getParamData();
		Map outMap=gOutputParam.getResultObj();

		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");

		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");

		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");

		String nhLoginPage=httpRequest.getParameter("nhLoginPage");
		httpSession.removeAttribute("nhUserName");
		if(nhLoginPage==null || "".equals(nhLoginPage)){
			nhLoginPage="/nh-micro-jsp/nhlogin.jsp";
		}
		httpRequest.setAttribute("forwardFlag", "true");
		httpRequest.getRequestDispatcher(nhLoginPage).forward(httpRequest, httpResponse);
		return;

	}
	
	
	private boolean checkPassword(String user_id,String inPass){
		String oldPass=getPassword(user_id);
		if(oldPass==null|| inPass==null){
			return false;
		}
		if(inPass.equals(oldPass)){
			return true;
		}
		return false;
	}
	private String getPassword(String user_id){

		Map rowMap=getInfoByBizIdService(user_id,"nh_micro_user","meta_key");
		if(rowMap==null){
			return null;
		}
		String pass=rowMap.get("dbcol_ext_user_password");
		return pass;
	}
}
