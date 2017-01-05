package groovy.nhuser

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Map
import java.lang.String

import com.nh.cache.base.*
import java.util.Set
import net.sf.json.JSONObject
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

class nrule{
	public void execGroovy(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		Map inMap=gInputParam.getParamData();
		Map outMap=gOutputParam.getResultObj();
		System.out.println("test");
		if(gInputParam.getSubName().equals("login")){
			execLogin(gInputParam,gOutputParam,gContextParam);
		}
		if(gInputParam.getSubName().equals("login_go")){
			execLoginGo(gInputParam,gOutputParam,gContextParam);
		}
		return ;
	}
	public void execLogin(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		Map inMap=gInputParam.getParamData();
		Map outMap=gOutputParam.getResultObj();
		String nhUserName=inMap.get("nhUserName");
		String nhPassWord=inMap.get("nhPassWord");
		System.out.println("this is http");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		System.out.println(httpRequest);
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		System.out.println(httpSession);
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		System.out.println(httpResponse);
		if(nhUserName.equals(nhPassWord)){
			httpSession.setAttribute("nhUserName",nhUserName);
			gOutputParam.setResultStatus(0);
			return;
		}
		gOutputParam.setResultStatus(1);
		gOutputParam.setResultMsg("µÇÂ¼Ê§°Ü");
		return;
	}
	public void execLoginGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		Map inMap=gInputParam.getParamData();
		Map outMap=gOutputParam.getResultObj();
		String nhUserName=inMap.get("nhUserName");
		String nhPassWord=inMap.get("nhPassWord");
		String nhMainPage=inMap.get("nhMainPage");
		String nhFailPage=inMap.get("nhFailPage");
		System.out.println("this is http");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		System.out.println(httpRequest);
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		System.out.println(httpSession);
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		System.out.println(httpResponse);
		if(nhUserName.equals(nhPassWord)){
			httpSession.setAttribute("nhUserName",nhUserName);
			httpRequest.getRequestDispatcher(nhMainPage).forward(httpRequest, httpResponse);
			return;
		}else{
			httpRequest.getRequestDispatcher(nhFailPage).forward(httpRequest, httpResponse);
			return;
		}
		
	}
}
