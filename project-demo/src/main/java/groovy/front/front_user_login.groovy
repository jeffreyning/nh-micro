package groovy.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.rowset.*;


import com.minxin.micro.cache.base.*;
import com.minxin.micro.db.*;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;


import groovy.json.*;
import groovy.template.MicroServiceTemplate;
import com.project.util.CacheUtils;


class front_user_login extends MicroServiceTemplate{
private Log loger = LogFactory.getLog(getClass());
public String tableName="t_front_user_login";

public String getTableName(HttpServletRequest httpRequest){
	return tableName;
}

public void login(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam) throws Exception{ 
	  HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	  HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
	  HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	  String ip=httpRequest.getRemoteAddr();
	  String userCode=httpRequest.getParameter("userCode");
	  String password=httpRequest.getParameter("password");
	  String source=httpRequest.getParameter("source");
	  if(source==null){
		  source="";
	  }
	  Map map = getInfoByBizIdService(userCode,"t_front_user","user_code");
	  if(map!=null&&map.size()>0){
		  if("1".equals(map.get("user_status"))){
			  gOutputParam.setResultStatus(1);
			  gOutputParam.setResultMsg("该账户已锁定，请联系客服人员！");
			  return ;
		  }else if(!map.get("password").toString().equals(password)){
			  //updateLoginFailCount(map);
			  int loginFailCount=Integer.parseInt(map.get("login_fail_count"));
			  gOutputParam.setResultStatus(1);
			  if(loginFailCount==10){
				  gOutputParam.setResultMsg("该账户已锁定，请联系客服人员！");
			  }else{
				  gOutputParam.setResultMsg("账户名与密码不匹配，请重新输入！");
			  }
			  return ;
		  }else{
			  
			  //saveUserLogin(userCode, ip);
			  // 更新用户信息
			  //updateUser(map);
			  // 保存tokenid
			  String tokenId=UUID.randomUUID().toString();
			  addtokenId(userCode,tokenId,source);
			  httpSession.setAttribute("tokenId",tokenId);
		  }
	  }	  
		  
	  return ;
}


public void logoutGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam) throws Exception{
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
	HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	String tokenId=httpRequest.getParameter("tokenId");
	String source=httpRequest.getParameter("source");
	deletetokenId("",tokenId,source);
	httpSession.removeAttribute("tokenId");
	httpRequest.getRequestDispatcher("/front-page/regularFinancialList.jsp").forward(httpRequest, httpResponse);
	httpRequest.setAttribute("forwardFlag", "true");
}


public void regUser(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
	HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	String regTel=httpRequest.getParameter("regTel");
	String password=httpRequest.getParameter("password");
	String smsCode=httpRequest.getParameter("smsCode");
	Map userInfo=getInfoByBizIdService(regTel,"t_front_user","user_code");
	if(userInfo!=null){
		gOutputParam.setResultStatus(1);
		gOutputParam.setResultMsg("注册失败-用户已经存在");
		return;
	}
	Map paramMap=new HashMap();
	paramMap.put("user_code",regTel);
	paramMap.put("user_phone",regTel);
	paramMap.put("password",password);
	createInfoService(paramMap,"t_front_user");
	
	Map accountMap=new HashMap();
	accountMap.put("user_code",regTel);
	createInfoService(accountMap,"t_front_account");
	
	

}

public String getUserCode(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	String tokenId=httpRequest.getParameter("tokenId");
	if(tokenId==null || "".equals(tokenId)){
		tokenId=httpSession.getAttribute("tokenId");
	}
	String source=httpRequest.getParameter("source");
	if(source==null){
		source="";
	}
	String userCode=getUserCodeByTokenId(tokenId,source);
	return userCode;
}

public void saveUserLogin(String usercode,String ip)throws Exception{
	Map userLogin = getInfoByBizIdService(usercode.toString(),tableName,"user_code");
	if(userLogin!=null&&userLogin.size()>0){
		userLogin.put("number",(String)(Integer.valueOf(userLogin.get("number").toString())+1));
		userLogin.put("ip",ip);
		userLogin.put("loginTime","now()");
		updateInfoService(userLogin,tableName);
	}else{
		userLogin = new HashMap<String, String>();
		userLogin.put("number","1");
		userLogin.put("ip",ip);
		userLogin.put("loginTime","now()");
		userLogin.put("user_code",usercode);
		createInfoService(userLogin,tableName);
	}
}

public void addtokenId(String usercode, String tokenId,String source) {
	
	if(source.equals("1")){
		CacheUtils.set("TOKEN_"+source+tokenId,usercode,3600);//存入redis并设置有效期为1个小时
	}else{
		CacheUtils.set("TOKEN_"+source+tokenId,usercode,604800);//app端存入redis并设置有效期为1周
	}
	
}

public void deletetokenId(String usercode, String tokenId,String source) {
	CacheUtils.del("TOKEN_"+source+tokenId);
}

public boolean existstokenId(String tokenId,String source) throws Exception{
	return CacheUtils.exists("TOKEN_"+source+tokenId);
}

public String getUserCodeByTokenId(String tokenId,String source) throws Exception{
	if(tokenId==null || source==null ){
		return null;
	}
	Object obj = CacheUtils.get("TOKEN_"+source+tokenId);
	if(obj==null){
		return null;
	}else{
		return obj.toString();
	}
}
/**
 * 检验是否登录，并返回usecode
 */
public String checkAndgetUserCodeByTokenId(String tokenId,String source) throws Exception{
	if(!existstokenId(tokenId,source)){
		return null;
	}
	Object obj = CacheUtils.get("TOKEN_"+source+tokenId);
	if(obj==null){
		return null;
	}else{
		return obj.toString();
	}
}
 /**
 * 修改用户失败登录次数
 */
  public void updateLoginFailCount(Map userMap){
	 int loginFailCount=Integer.parseInt(userMap.get("login_fail_count"));
	 int newLoginFailCount=loginFailCount+1;
	 int loginCount=10;

	 if(newLoginFailCount>=loginCount){
		 userMap.put("user_status","1");
		 userMap.put("login_fail_count",String.valueOf(loginCount));
	 }else {
		 userMap.put("login_fail_count",String.valueOf(newLoginFailCount));
	 }
	 updateInfoService(userMap,"t_front_user");
  }
  /**
   * 修改用户登录失败次数为0
   * @param userMap
   */
  public void updateUser(Map userMap){
	  userMap.put("login_fail_count","0");
	  updateInfoService(userMap,"t_front_user");
  }
}
