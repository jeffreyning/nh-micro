package groovy.template;  


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import groovy.json.*;
import com.nh.micro.db.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.nh.micro.rule.engine.core.*;
import groovy.template.MicroServiceBizTemplate;

import org.apache.log4j.Logger;

class MicroMvcTemplate extends MicroServiceBizTemplate{

	//Logger logger = LoggerFactory.getLogger(MicroMvcTemplate.getClass());

	public boolean checkExecAuth(String groovyName,String groovyMethod,Map paramMap){
		if(groovyName.equals("nhlogin")){
			return true;
		}
		HttpServletRequest httpRequest = paramMap.get("httpRequest");
		HttpSession httpSession=httpRequest.getSession();
		String nhUserName=httpSession.getAttribute("nhUserName");
		if(nhUserName!=null ){
			return true;
		}
	
		return false;
	}

	public Map getRequestParamMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if(null == valueObj){
				value = null;
			}else if(valueObj instanceof String[]){
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++){
					value = values[i] + ",";
				}
				value = value.substring(0, value.length()-1);
			}else{
				value = valueObj.toString();
			}
			//String dbName=name.substring("search_".length()-1);
			String dbName=name;
			returnMap.put(dbName, value);
		}
		return returnMap;
	}

	
	public String getPageName(HttpServletRequest httpRequest){
		return httpRequest.getParameter("pageName");
	}
	public String getTableName(HttpServletRequest httpRequest){
		//return httpRequest.getParameter("tableName");
		return "xxx";
	}
	public Map createRetDataMap(int page,int rows, Map retMap){
		Map retDataMap=new HashMap();
		retDataMap.put("curPage", page);
		retDataMap.put("pageSizes", rows);
		int totalRecords=retMap.get("total");

		int result = totalRecords / rows;
		int totalPages = (totalRecords % rows == 0) ? result : (result + 1);
		retDataMap.put("totalRecords", totalRecords);
		retDataMap.put("totalPages", totalPages);
		retDataMap.put("viewJsonData", retMap.get("rows"));
		return retDataMap;
	}
	public void getInfoList4Page(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page=httpRequest.getParameter("page");
		String rows=httpRequest.getParameter("rows");
		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);

		Map requestParamMap=getRequestParamMap(httpRequest);
		Map pageMap=new HashMap();
		pageMap.put("page", page);
		pageMap.put("rows", rows);
		pageMap.put("sort", sort);
		pageMap.put("order", order);
		Map retMap=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoList4PageService",requestParamMap, tableName,pageMap);
		//Map retDataMap=createRetDataMap(Integer.valueOf(page),Integer.valueOf(rows),retMap);
		//JsonBuilder jsonBuilder=new JsonBuilder(retDataMap);
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();

		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	public void createInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){

		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sTime = sf.format(new Date());
		//requestParamMap.put("create_time",sTime);

		Integer retStatus=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "createInfoService",requestParamMap, tableName);
		gOutputParam.setResultObj(retStatus);
		return;
	}
	
	
	public void updateInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){

		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);

		Integer retStatus=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "updateInfoService",requestParamMap, tableName);
		gOutputParam.setResultObj(retStatus);
		return;
	}
	
	public void delInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		
		
		Integer retStatus=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "delInfoService",requestParamMap, tableName);
		gOutputParam.setResultObj(retStatus);
		return;
	}
	
	public void getInfoById(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");

		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);

		Map retMap=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoByIdService",requestParamMap, tableName);
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	
	public void getInfoListAll(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");

		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Map sortMap=new HashMap();
		sortMap.put("sort", sort);
		sortMap.put("order", order);
		List retList=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoListAllService",requestParamMap, tableName,sortMap);

		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();

		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
		

}
