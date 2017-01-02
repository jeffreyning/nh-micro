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
  return ;
 }
public void execLogin(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
Map inMap=gInputParam.getParamData();
Map outMap=gOutputParam.getResultObj();
String nhUserName=inMap.get("nhUserName");
String nhPassWord=inMap.get("nhPassWord");
HttpServletRequest httpRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
HttpServletResponse httpResponse=((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
HttpSession httpSession=httpRequest.getSession();
if(nhUserName.equals(nhPassWord)){
httpSession.setAttribute("nhUserName",nhUserName);
}

}
}
