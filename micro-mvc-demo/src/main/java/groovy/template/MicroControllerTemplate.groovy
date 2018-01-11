package groovy.template;  

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional;


import com.nh.micro.rule.engine.context.MicroContextHolder;
import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.micro.db.*;
import com.nh.micro.controller.MicroUrlMapping;
import com.nh.micro.rule.engine.core.plugin.MicroAop;
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy;
import com.nh.micro.template.MicroDbProxy;
import com.nh.micro.template.MicroTMProxy;
import com.nh.micro.service.InjectGroovy;

import groovy.json.JsonBuilder
import groovy.template.MicroServiceBizTemplate;

/**
 * 
 * @author ninghao
 *
 */
class MicroControllerTemplate extends MicroServiceBizTemplate{

	Logger logger = LoggerFactory.getLogger(MicroControllerTemplate.getClass());

	public boolean checkExecAuth(String groovyName,String groovyMethod,Map paramMap){
	
		return true;
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
					if(i==0){
					value = values[i] ;
					}else{
					value = value+","+values[i];
					}
				}
				//value = value.substring(0, value.length()-1);
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
	public void getInfoList4Page(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		
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
		Map retMap=getInfoList4PageService(requestParamMap, tableName,pageMap);
		//Map retDataMap=createRetDataMap(Integer.valueOf(page),Integer.valueOf(rows),retMap);
		//JsonBuilder jsonBuilder=new JsonBuilder(retDataMap);
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();

		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));

		return;
	}
	
	public void createInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sTime = sf.format(new Date());
		Integer retStatus=createInfoService(requestParamMap, tableName);

		return;
	}
	
	
	public void updateInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Integer retStatus=updateInfoService(requestParamMap, tableName);
		return;
	}
	
	public void delInfo(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Integer retStatus=delInfoService(requestParamMap, tableName);
		return;
	}

	public void getInfoById(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Map retMap=getInfoByIdService(requestParamMap, tableName);
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		return;
	}
	
	
	public void getInfoListAll(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		String tableName=getTableName(httpRequest);
		String pageName=getPageName(httpRequest);
		Map requestParamMap=getRequestParamMap(httpRequest);
		Map sortMap=new HashMap();
		sortMap.put("sort", sort);
		sortMap.put("order", order);
		List retList=getInfoListAllService(requestParamMap, tableName,sortMap);
		JsonBuilder jsonBuilder=new JsonBuilder(retList);
		String retStr=jsonBuilder.toString();
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));

		return;
	}
	public static void removeNullValue(Map map){
		Set set = map.keySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			Object obj = (Object) iterator.next();
			Object value =(Object)map.get(obj);
			remove(value, iterator);
		}
	}
	private static void remove(Object obj,Iterator iterator){
		if(obj instanceof String){
			String str = (String)obj;
			if(str==null || "".equals(str)){  //过滤掉为null和""的值 主函数输出结果map：{2=BB, 1=AA, 5=CC, 8=  }
//            if("".equals(str.trim())){  //过滤掉为null、""和" "的值 主函数输出结果map：{2=BB, 1=AA, 5=CC}
				iterator.remove();
			}
			 
		}else if(obj instanceof Collection){
			Collection col = (Collection)obj;
			if(col==null||col.isEmpty()){
				iterator.remove();
			}
			   
		}else if(obj instanceof Map){
			Map temp = (Map)obj;
			if(temp==null||temp.isEmpty()){
				iterator.remove();
			}
			   
		}else if(obj instanceof Object[]){
			Object[] array =(Object[])obj;
			if(array==null||array.length<=0){
				iterator.remove();
			}
		}else{
			if(obj==null){
				iterator.remove();
			}
		}
	}

}
