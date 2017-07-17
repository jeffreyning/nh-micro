package groovy.template.structdata;

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Map
import java.lang.String


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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import groovy.json.*;
import groovy.template.MicroMvcTemplate;
import com.nh.micro.db.*;
import com.google.gson.Gson;
class nrule extends MicroMvcTemplate{
	
	//显示某节点的参数列表(struct)
	public void getParamInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id=httpRequest.getParameter("dept_id");
		List<Map> deptList=getDeptListByPageId(page_id);
		List paramList=new ArrayList();
		for(Map rowMap:deptList){
			String deptId=rowMap.get("dept_id");
			if(deptId.equals(dept_id)){
				paramList=rowMap.get("dept_param");
				break;
			}
			
		}
		Map retMap=new HashMap();
		if(paramList==null){
			paramList=new ArrayList();
		}
		retMap.put("rows", paramList);
		retMap.put("total", paramList.size());
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	//修改某节点参数列表(struct)
	public void updateParamInfo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id=httpRequest.getParameter("dept_id");
		String dept_param=httpRequest.getParameter("dept_param");
		Gson gson = new Gson();

		List deptParamList=gson.fromJson(dept_param,List.class);
		List<Map> deptList=getDeptListByPageId(page_id);
		List paramList=new ArrayList();
		for(Map rowMap:deptList){
			String deptId=rowMap.get("dept_id");
			if(deptId.equals(dept_id)){
				rowMap.put("dept_param", deptParamList);
				break;
			}
			
		}
		
		Integer retStatus=updateDeptListByPageId(page_id,deptList);
		gOutputParam.setResultObj(retStatus);
	}

	//页面获取指定节点数据(data)
	public void getDeptData(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id=httpRequest.getParameter("dept_id");
		//返回的一定是个map
		Map parentMap=getParentMap(page_id,dept_id);
		if(parentMap!=null){
		gOutputParam.setResultObj(parentMap.get(dept_id));
		}
		
		return;
	}
	
	//设置指定位置数据(data)
	public void putDeptData(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id=httpRequest.getParameter("dept_id");
		String param_data=httpRequest.getParameter("param_data");
		String dept_path=httpRequest.getParameter("dept_path");
		String dataTableName=httpRequest.getParameter("data_table_name");
		if(dataTableName==null || "".equals(dataTableName) || "undefined".equals(dataTableName)){
			dataTableName="nh_micro_editpage_list";
		}
		Object paramData=null;
		if(param_data.indexOf("{")>=0 || param_data.indexOf("[")>=0){
			paramData=new JsonSlurper().parseText(param_data);
		}else{
			paramData=param_data;
		}
		putParentMap(page_id,dept_id,dept_path,paramData,dataTableName);
		return ;
	}
	
	//编辑grid类型节点的数据(data)
	public void updateDeptDataList(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id=httpRequest.getParameter("dept_id");
		String dept_data=httpRequest.getParameter("dept_data");
		String dept_path=httpRequest.getParameter("dept_path");
		String dataTableName=httpRequest.getParameter("data_table_name");
		Object deptData=new JsonSlurper().parseText(dept_data);
		if(dataTableName==null || "".equals(dataTableName) || "undefined".equals(dataTableName)){
			dataTableName="cms_page_list";
		}
		putParentMap(page_id,dept_id,dept_path,deptData,dataTableName);
		return ;
	}
	
	
	//取结构树数据，包括引用类型(tree-show)
	public void getDept(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		List deptList=getDeptListByPageId(page_id);
		Map retMap=	mergeDept(page_id,false);
		List retList=new ArrayList();
		if(retMap.get("dept_id")==null){
			retMap.put("dept_id", "root");
			retMap.put("dept_name", "root");
			retMap.put("dept_type","map");
			List structList=new ArrayList();
			structList.add(retMap);
			updateDeptListByPageId(page_id,structList);
		}
		retList.add(retMap);
		
		gOutputParam.setResultObj(retList);
		return;
	}

	//取结构树不考虑引用ref问题(tree-show)
	public void getDept4Struct(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		List deptList=getDeptListByPageId(page_id);

		Map retMap=new HashMap();
		String rootId=mergeDeptRoot(page_id);
		noMergeDept(deptList,rootId,retMap,page_id);

		if(retMap.get("dept_id")==null){
			retMap.put("dept_id", "root");
			retMap.put("dept_name", "root");
			retMap.put("dept_type","map");
			List structList=new ArrayList();
			structList.add(retMap);
			updateDeptListByPageId(page_id,structList);
		}
		List retList=new ArrayList();
		retList.add(retMap);
		
		gOutputParam.setResultObj(retList);
		return;
	}

	
	//更新节点结构数据(struct)
	public void updateDept(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder.getDbSource("default");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String dept_id=httpRequest.getParameter("dept_id");
		String dept_name=httpRequest.getParameter("dept_name");
		String dept_type=httpRequest.getParameter("dept_type");
		String dept_remark=httpRequest.getParameter("dept_remark");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id_old=httpRequest.getParameter("dept_id_old");
		String dept_ref=httpRequest.getParameter("dept_ref");
		
		Map contentMap=new HashMap();
		contentMap.put("dept_id", dept_id);
		contentMap.put("dept_name", dept_name);
		contentMap.put("dept_type", dept_type);
		contentMap.put("dept_remark",dept_remark);
		contentMap.put("dept_ref", dept_ref);
		//JsonBuilder jsonBuilder=new JsonBuilder(contentMap);
		//String meta_content=jsonBuilder.toString();
		List structList=getDeptListByPageId(page_id);
		int size=structList.size();
		for(int i=0;i<size;i++){
			Map rowMap=structList.get(i);
			//String structStr=structList.get(i);
			//Map rowMap=new JsonSlurper().parseText(structStr);
			String rowId=rowMap.get("dept_id");
			if(rowId.equals(dept_id_old)){
				Map mergeMap=new HashMap();
				mergeMap.putAll(rowMap);
				mergeMap.putAll(contentMap);
				structList.set(i, mergeMap);
			}
		}
		Integer retStatus=updateDeptListByPageId(page_id,structList);
		gOutputParam.setResultObj(retStatus);
		return;
	}
	
	//创建新节点(struct)
	public void createDept(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder.getDbSource("default");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String dept_id=httpRequest.getParameter("dept_id");
		String dept_name=httpRequest.getParameter("dept_name");
		String dept_type=httpRequest.getParameter("dept_type");
		String dept_remark=httpRequest.getParameter("dept_remark");
		String parent_id=httpRequest.getParameter("parent_id");
		String page_id=httpRequest.getParameter("page_id");
		Map contentMap=new HashMap();
		contentMap.put("dept_id", dept_id);
		contentMap.put("dept_name", dept_name);
		contentMap.put("dept_type", dept_type);
		contentMap.put("dept_remark",dept_remark);
		contentMap.put("parent_id", parent_id);
		//JsonBuilder jsonBuilder=new JsonBuilder(contentMap);
		//String meta_content=jsonBuilder.toString();

		List structList=getDeptListByPageId(page_id);
		structList.add(contentMap);
		Integer retStatus=updateDeptListByPageId(page_id,structList);
		gOutputParam.setResultObj(retStatus);
		return;
	}
	//删除子节点树(struct)
	public void delChildDept(List deptList,String deptId){
		int size=deptList.size();
		for(int i=0;i<size;i++){

			Map rowMap=deptList.get(i);
			if(deptId.equals(rowMap.get("dept_id"))){
				deptList.remove(i);
				break;
			}
		}
		List subList=getSubDeptList(deptList,deptId);
		int subSize=subList.size();
		for(int i=0;i<subSize;i++){
			Map tempMap=subList.get(i);
			String tempId=tempMap.get("dept_id");
			delChildDept(deptList,tempId);
		}
	}
	
	//删除节点(struct)
	public void delDept(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder.getDbSource("default");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String dept_id=httpRequest.getParameter("dept_id");
		String page_edit_id=httpRequest.getParameter("page_id");
		List<String> deptList=getDeptListByPageId(page_edit_id);
		delChildDept(deptList,dept_id);
		Integer retStatus=updateDeptListByPageId(page_edit_id,deptList);

		gOutputParam.setResultObj(retStatus);
		return;
	}

	
	//跳转到map类型或list类型数据显示页面
	public void toStructDataForm(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		String dept_id=httpRequest.getParameter("dept_id");
		String page_id=httpRequest.getParameter("page_id");
		String dept_path=httpRequest.getParameter("dept_path");
		String dept_ref=httpRequest.getParameter("dept_ref");
		String dept_ref_id=httpRequest.getParameter("dept_ref_id");
		String dept_type=httpRequest.getParameter("dept_type");
		String data_model_id=httpRequest.getParameter("data_model_id");
		String dataTableName=httpRequest.getParameter("data_table_name");
		if(dataTableName==null || "".equals(dataTableName) || "undefined".equals(dataTableName)){
			dataTableName="nh_micro_editpage_list";
		}
		
		httpRequest.setAttribute("dept_id",dept_id);
		httpRequest.setAttribute("page_id",page_id);
		httpRequest.setAttribute("dept_path", dept_path);
		httpRequest.setAttribute("dept_ref", dept_ref);
		httpRequest.setAttribute("data_table_name", dataTableName);
		
		Object deptData=null;
		Map parentMap=getParentMapByPath(page_id,dept_path,dataTableName);
		if(parentMap!=null){
			deptData=parentMap.get(dept_id);
		}
		//deptData=getParentObjByPath(page_id,dept_path);
		httpRequest.setAttribute("deptData",deptData);
		
		List paramList=getParamListByDeptId(data_model_id,dept_id,dept_ref,dept_ref_id);
		if(paramList==null){
			paramList=new ArrayList();
		}
		httpRequest.setAttribute("paramList", paramList);
		
		//String dataType=getParamTypeByDeptId(page_id,dept_id,dept_ref,dept_ref_id);
		String dataType=dept_type;
		String tourl="";
		if("list".equals(dataType)){
			tourl="/nh-micro-jsp/edit-page/structDataGridPage.jsp";
			
		}else{
			tourl="/nh-micro-jsp/edit-page/structDataFormPage.jsp";
		}
		httpRequest.getRequestDispatcher(tourl).forward(httpRequest, httpResponse);
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	//显示grid类型的data数据(data)
	public void getDeptDataList(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id=httpRequest.getParameter("dept_id");
		String dept_path=httpRequest.getParameter("dept_path");
		String dataTableName=httpRequest.getParameter("data_table_name");
		if(dataTableName==null || "".equals(dataTableName) || "undefined".equals(dataTableName)){
			dataTableName="cms_page_list";
		}
		List deptData=new ArrayList();
		Map parentMap=getParentMapByPath(page_id,dept_path,dataTableName);
		if(parentMap!=null){
			deptData=parentMap.get(dept_id);
		}
		
		Map retMap=new HashMap();
		if(deptData==null){
			deptData=new ArrayList();
		}
		retMap.put("rows", deptData);
		retMap.put("total", deptData.size());
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	
	/*
	 * *******************************************************************
	 * *******************************************************************
	 */
			
	//根据页面id获取结构树 refidflag为是否强制设置refid=deptid	(struct)
	private Map mergeDept(String pageId,boolean refIdFlag){
		List deptList=getDeptListByPageId(pageId);
		Map retMap=new HashMap();
		String rootId=mergeDeptRoot(pageId);
		mergeDept(deptList,rootId,retMap,pageId,refIdFlag);
		retMap.put("dept_ref", pageId);
		return retMap;
	}
	
	//根据结构id取根节点id(struct)
	private String mergeDeptRoot(String pageId){
		List deptList=getDeptListByPageId(pageId);
		Map retMap=new HashMap();

		int size=deptList.size();
		for(int i=0;i<size;i++){
			Map rowMap=deptList.get(i);
			String deptId=rowMap.get("dept_id");
			String parentIdTemp=rowMap.get("parent_id");
			if(parentIdTemp==null || parentIdTemp.equals("") ){
				return deptId;
			}
		}
		return "root";
	}
	
	//显示结构树不需要考虑引用问题(struct)
	private void noMergeDept(List deptList,String rootId,Map retMap,String pageId){
		List retList=new ArrayList();
		
		//从节点列表中获取指定的节点信息
		Map curMap=getDeptNode(deptList,rootId);
		if(curMap!=null){
			String deptName=curMap.get("dept_name");
			String deptId=curMap.get("dept_id");
			String deptType=curMap.get("dept_type");
			retMap.put("dept_id", deptId);
			retMap.put("dept_name", deptName);
			retMap.put("dept_type", deptType);
		}else{
			return;
		}
		
		//获取子节点列表信息
		List subList=getSubDeptList(deptList,rootId);
		int size=subList.size();
		for(int i=0;i<size;i++){
			Map rowMapTemp=subList.get(i);
			Map rowMap=new HashMap();
			rowMap.putAll(rowMapTemp);
			String deptIdTemp=rowMap.get("dept_id");
			retList.add(rowMap);

			noMergeDept(deptList,deptIdTemp,rowMap,pageId);
		}
		
		retMap.put("children", retList);
	}
	
	//根据节点取结构树，包括引用类型(struct)
	private void mergeDept(List deptList,String rootId,Map retMap,String pageId,boolean refIdFlag){
		List retList=new ArrayList();
		
		//从节点列表中获取指定的节点信息
		Map curMap=getDeptNode(deptList,rootId);
		if(curMap!=null){
			String deptName=curMap.get("dept_name");
			String deptId=curMap.get("dept_id");
			String deptType=curMap.get("dept_type");
			retMap.put("dept_id", deptId);
			retMap.put("dept_name", deptName);
			retMap.put("dept_type", deptType);
			if(refIdFlag){
				retMap.put("dept_ref_id", deptId);
			}
			
			String deptRef=curMap.get("dept_ref");
			
			//如果节点是引用类型的，则取父类型节点树并返回
			if(deptRef!=null && !"".equals(deptRef)){
				Map tempMap=mergeDept(deptRef,true);
				retMap.putAll(tempMap);
				retMap.put("dept_id", deptId);
				retMap.put("dept_name", deptName);
				retMap.put("dept_ref", deptRef);
				retMap.put("dept_ref_id", tempMap.get("dept_id"));
				retMap.put("dept_type", deptType);
				return;
			}
			

		}
		
		//获取子节点列表信息
		List subList=getSubDeptList(deptList,rootId);
		int size=subList.size();
		for(int i=0;i<size;i++){
			Map rowMapTemp=subList.get(i);
			Map rowMap=new HashMap();
			rowMap.putAll(rowMapTemp);
			String deptIdTemp=rowMap.get("dept_id");
			retList.add(rowMap);
			rowMap.put("dept_ref", pageId);
			if(refIdFlag){
				rowMap.put("dept_ref_id", deptIdTemp);
			}

			mergeDept(deptList,deptIdTemp,rowMap,pageId,refIdFlag);
		}
		
		//查看参数中是否有引用类型ref //取消
/*		if(rootId!=null && !"".equals(rootId)){

			if(curMap!=null){
				List refs=mergeRefDept(curMap);
				if(refs!=null){
					retList.addAll(refs);
				}
			}
		}*/
		
		retMap.put("children", retList);
	}
	
	//循环查看某节点paramlist中是否存在引用型param(struct)
	private List mergeRefDept(Map paramMap){

		List retList=new ArrayList();

		List paramList=paramMap.get("dept_param");
		if(paramList==null){
			return retList;
		}
		int size=paramList.size();
		for(int i=0;i<size;i++){
			Map oneMap=paramList.get(i);
			String paramRef=oneMap.get("param_ref");
			if(paramRef==null || "".equals(paramRef)){
				continue;
			}
			String deptName=oneMap.get("param_title");
			Map tempMap=mergeDept(paramRef,true);
			tempMap.put("dept_name", deptName);
			String dept_ref_id=tempMap.get("dept_id");
			tempMap.put("dept_id", oneMap.get("param_key"));
			tempMap.put("dept_ref_id", dept_ref_id);
			retList.add(tempMap);
		}
		return retList;
	}

	//根据当前节点id，获取当前节点map(struct)
	private Map getDeptNode(List deptList,String nodeId){
		
		List retList=new ArrayList();
		int size=deptList.size();
		for(int i=0;i<size;i++){
			Map rowMap=deptList.get(i);
			String deptId=rowMap.get("dept_id");
			if(nodeId.equals(deptId)){
				return rowMap;
			}
		}
		return null;
	}
	
	//根据当前节点id，获取子节点list(struct)
	private List getSubDeptList(List deptList,String rootId){
		
		List retList=new ArrayList();
		int size=deptList.size();
		for(int i=0;i<size;i++){
			Map rowMap=deptList.get(i);
			String parentId=rowMap.get("parent_id");
			if(parentId.equals(rootId)){
				retList.add(rowMap);
			}
		}
		return retList;
	}
	

	//根据页面id获取结构list(struct)
	public List getDeptListByPageId(String pageId){
		List structList=new ArrayList();
		Map data=getInfoByBizIdService(pageId,"nh_micro_editpage_list","meta_key");
		if(data==null){
			return structList;
		}
		String structStr=data.get("dbcol_ext_struct");
		if(structStr==null){
			return structList;
		}
		Gson gson = new Gson();
		structList=gson.fromJson(structStr,List.class);
	
		return structList;
	}

	//根据页面id获取数据data(data)
	public Map getDeptDataListByPageId(String pageId,String dataTableName){
		Map structDataMap=new HashMap();
		Map data=getInfoByBizIdService(pageId,dataTableName,"meta_key");
		if(data==null){
			return structDataMap;
		}
		String structStr=data.get("dbcol_ext_sdata");
		if(structStr==null){
			return structDataMap;
		}
		Gson gson = new Gson();
		structDataMap=gson.fromJson(structStr,Map.class);
		
		return structDataMap;
	}
	
	//获取指定节点数据(data)
	private Map getParentMap(String page_id,String dept_id,String dataTableName){
		List parentList=getParentDeptListByDeptId(page_id,dept_id);
		Map dataMap=getDeptDataListByPageId(page_id,dataTableName);
		int size=parentList.size();
		Object obj=null;
		Map tempMap=dataMap;
		for(int i=0;i<size;i++){
			String key=parentList.get(i);
			Map subObj=tempMap.get(key);
			if(subObj==null){
				return null;
			}
			if(i==size-1){
				return subObj;
			}
			tempMap=subObj;
		}
		return dataMap;
	}
	
	//根据path获取指定位置数据(data)
	private Map getParentMapByPath4Map(Map dataMap,String dept_path){
		String[] pathNode=dept_path.split("/");
		int size=pathNode.length;
		if(size<0){
			return dataMap;
		}
		Object subObj=null;
		Object rowObj=dataMap;
		if(dept_path.equals("")){
			return dataMap;
		}
		for(int i=0;i<size;i++){
			String key=pathNode[i];
			if(key.contains("[")){
				int start=key.indexOf("[");
				int end=key.indexOf("]");
				String realKey=key.substring(0,start);
				String temp=key.substring(start+1, end);
				int index=0;
				if(temp!=null && !"".equals(temp)){
					index=Integer.valueOf(temp);
				}
				List tempList=rowObj.get(realKey);
				if(tempList==null){
					tempList=new ArrayList();

					rowObj.put(realKey, tempList);
				}
				int lsize=tempList.size();
				while(lsize<index+1){
					tempList.add(new HashMap());
					lsize=tempList.size();
				}
				subObj=tempList.get(index);

			}else{
				subObj=rowObj.get(key);
				if(subObj==null){
					subObj=new HashMap();
					rowObj.put(key, subObj);
				}
			}

/*			if(subObj==null){
				return null;
			}*/

			rowObj=subObj;
		}
		return subObj;
	}
	
	//根据path获取，数据(data)
	private Map getParentMapByPath(String page_id,String dept_path,String dataTableName){
		//String[] pathNode=dept_path.split("/");
		
		Map dataMap=getDeptDataListByPageId(page_id,dataTableName);
		return getParentMapByPath4Map(dataMap,dept_path);

	}
	
	//设置指定位置数据(data)
	private int putParentMap(String page_id,String dept_id,String dept_path,Object paramObj,String dataTableName){
		List parentList=getParentDeptListByDeptId(page_id,dept_id);
		Map dataMap=getDeptDataListByPageId(page_id,dataTableName);
		Map pMap=getParentMapByPath4Map(dataMap,dept_path);
		int index=getDeptIdIndex(dept_id);
		if(index<0){
			pMap.put(dept_id, paramObj);
		}else{
			String realDeptId=getRealDeptId(dept_id);
			List tempList=pMap.get(realDeptId);
			getDataFromList(tempList,index);
			tempList.set(index,paramObj);
		}
		updateDataMapByPageId(page_id,dataMap,dataTableName);
		return 1;
	}
	
	//获取指定节点位置数据(data)
	private List getParentDeptListByDeptId(String pageId,String deptId){
		List structList=getDeptListByPageId(pageId);
		LinkedList parentList=new LinkedList();
		String oneId=deptId;
		for(int i=0;i<50;i++){
			String parentId=getParentId(structList,oneId);
			if(parentId!=null && !"".equals(parentId)){
				parentList.addFirst(parentId);
				oneId=parentId;
			}else{
				break;
			}
		}
		return parentList;
	}
	
	//根据节点id获取父节点id(struct)
	private String getParentId(List structList,String deptId){
		for(Map rowMap:structList){
			String dept_id=rowMap.get("dept_id");
			if(deptId.equals(dept_id)){
				String parent_id=rowMap.get("parent_id");
				return parent_id;
			}
		}
		return null;
	}
	
	//更新节点结构(struct)
	public Integer updateDeptListByPageId(String pageId,List deptList){
		String jsonStr=new JsonBuilder(deptList).toString();
		Map paramMap=new HashMap();
		paramMap.put("dbcol_ext_struct", jsonStr);
		Integer status=updateInfoByBizIdService(pageId,"nh_micro_editpage_list","meta_key",paramMap);
	}
	
	//更新节点data数据(data)
	public Integer updateDataMapByPageId(String pageId,Map dataMap,String dataTableName){
		String jsonStr=new JsonBuilder(dataMap).toString();
		Map paramMap=new HashMap();
		paramMap.put("dbcol_ext_sdata", jsonStr);
		Integer status=updateInfoByBizIdService(pageId,dataTableName,"meta_key",paramMap);
	}
	
	//根据节点id获取此节点中参数列表(struct to show)
	private List getParamListByDeptId(String page_id,String dept_id,String dept_ref,String dept_ref_id){
		String id=page_id;
		String matchId=dept_id;
		if(dept_ref_id!=null && !"".equals(dept_ref_id) && !"undefined".equals(dept_ref_id)){
			id=dept_ref;
			matchId=dept_ref_id;
		}

		List<Map> deptList=getDeptListByPageId(id);
		List paramList=new ArrayList();
		for(Map rowMap:deptList){
			String deptId=rowMap.get("dept_id");
			if(deptId.equals(matchId)){
				paramList=rowMap.get("dept_param");
				break;
			}
		}
		return paramList;
	}
	
	//按照节点id获取此节点的类型map还是list(struct to show)
	//to-do 
	private String getParamTypeByDeptId(String page_id,String dept_id,String dept_ref,String dept_ref_id){
		String id=page_id;
		String matchId=dept_id;
		if(dept_ref_id!=null && !"".equals(dept_ref_id) && !"undefined".equals("dept_ref_id")){
			id=dept_ref;
			matchId=dept_ref_id;
		}
		List<Map> deptList=getDeptListByPageId(id);
		for(Map rowMap:deptList){
			String deptId=rowMap.get("dept_id");
			if(deptId.equals(matchId)){
				String paramType=rowMap.get("dept_type");
				return paramType;
			}
		}
		return null;
	}
	
	//显示的data数据(data)
	public void getDeptDataObj(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_fullpath=httpRequest.getParameter("dept_fullpath");
		String dataTableName=httpRequest.getParameter("data_table_name");
		if(dataTableName==null || "".equals(dataTableName) || "undefined".equals(dataTableName)){
			dataTableName="cms_page_list";
		}
/*		String[] paths=dept_fullpath.split("/");
		int size=paths.length;
		String dept_id="";
		if(size>0){
			dept_id=paths[size-1];
		}
		String dept_path="";
		int dept_id_size=dept_id.length();
		dept_path=dept_fullpath.subSequence(0, dept_fullpath.length()-dept_id_size);
		Object deptData=null;
		Map parentMap=getParentMapByPath(page_id,dept_path,dataTableName);
		if(parentMap!=null){
			int haveIndex=dept_id.indexOf("[");
			if(haveIndex<=0){
				deptData=parentMap.get(dept_id);
			}else{
				String real_id=dept_id.substring(0,haveIndex);
				int lastIndex=dept_id.indexOf("]");
				String real_index=dept_id.substring(haveIndex+1,lastIndex);
				Integer index_i=Integer.valueOf(real_index);
				List tempList=parentMap.get(real_id);
				deptData=tempList.get(index_i);
			}
		}*/
		
		Object deptData=getCurDataByFull(page_id,dept_fullpath,dataTableName);
		
		JsonBuilder jsonBuilder=new JsonBuilder(deptData);
		String retStr=jsonBuilder.toString();
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	private Object getCurDataByFull(String page_id,String dept_fullpath,String data_table_name){
		String[] paths=dept_fullpath.split("/");
		int size=paths.length;
		String dept_id="";
		if(size>0){
			dept_id=paths[size-1];
		}
		String dept_path="";
		int dept_id_size=dept_id.length();
		dept_path=dept_fullpath.subSequence(0, dept_fullpath.length()-dept_id_size);
		Object deptData=null;
		Map parentMap=getParentMapByPath(page_id,dept_path,data_table_name);
		if(parentMap!=null){
			int haveIndex=dept_id.indexOf("[");
			if(haveIndex<=0){
				deptData=parentMap.get(dept_id);
			}else{
				String real_id=dept_id.substring(0,haveIndex);
				int lastIndex=dept_id.indexOf("]");
				String real_index=dept_id.substring(haveIndex+1,lastIndex);
				Integer index_i=Integer.valueOf(real_index);
				List tempList=parentMap.get(real_id);
				deptData=tempList.get(index_i);
			}
		}
		return deptData;
	}
	
	//设置指定位置数据(data)
	private int updateParentMap(String page_id,String dept_id,String dept_path,Map paramObj,String dataTableName){
		List parentList=getParentDeptListByDeptId(page_id,dept_id);
		Map dataMap=getDeptDataListByPageId(page_id,dataTableName);
		Map pMap=getParentMapByPath4Map(dataMap,dept_path);
		int index=getDeptIdIndex(dept_id);
		if(index<0){
			((Map)pMap.get(dept_id)).putAll(paramObj);
		}else{
			String realDeptId=getRealDeptId(dept_id);
			List tempList=pMap.get(realDeptId);
			Map tempMap=getDataFromList(tempList,index);
			tempMap.putAll(paramObj);
		}
		
		updateDataMapByPageId(page_id,dataMap,dataTableName);
		return 1;
	}
	
	private Object getDataFromList(List dataList,int index){
		int size=dataList.size();
		while(size<index+1){
			dataList.add(new HashMap());
			size=dataList.size();
		}
		Object retObj=dataList.get(index);
		return retObj;
	}
	
	//设置指定位置数据(data)
	public void updateDeptData(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id=httpRequest.getParameter("dept_id");
		String param_data=httpRequest.getParameter("param_data");
		String dept_path=httpRequest.getParameter("dept_path");
		String dataTableName=httpRequest.getParameter("data_table_name");
		if(dataTableName==null || "".equals(dataTableName) || "undefined".equals(dataTableName)){
			dataTableName="nh_micro_editpage_list";
		}
		Map paramData=null;
		paramData=new JsonSlurper().parseText(param_data);
		updateParentMap(page_id,dept_id,dept_path,paramData,dataTableName);
		return ;
	}
	
	private String getRealDeptId(String deptId){
		int start=deptId.indexOf("[");
		String realKey=deptId;
		if(start>0){
			realKey=deptId.substring(0,start);
		}
		return realKey;
	}
	private int getDeptIdIndex(String deptId){
		int start=deptId.indexOf("[");
		if(start<=0){
			return -1;
		}
		int end=deptId.indexOf("]");
		String temp=deptId.substring(start+1, end);
		int index=0;
		if(temp!=null && !"".equals(temp)){
			index=Integer.valueOf(temp);
		}
		return index;
	}
	
	//设置指定位置数据(data)
	public void removeDeptData(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id=httpRequest.getParameter("dept_id");
		String dept_path=httpRequest.getParameter("dept_path");
		String dataTableName=httpRequest.getParameter("data_table_name");
		if(dataTableName==null || "".equals(dataTableName) || "undefined".equals(dataTableName)){
			dataTableName="nh_micro_editpage_list";
		}
		removeParentMap(page_id,dept_id,dept_path,dataTableName);
		return ;
	}
	//设置指定位置数据(data)
	private int removeParentMap(String page_id,String dept_id,String dept_path,String dataTableName){
		List parentList=getParentDeptListByDeptId(page_id,dept_id);
		Map dataMap=getDeptDataListByPageId(page_id,dataTableName);
		Map pMap=getParentMapByPath4Map(dataMap,dept_path);
		int index=getDeptIdIndex(dept_id);
		if(index<0){
			(Map)pMap.remove(dept_id);
		}else{
			String realDeptId=getRealDeptId(dept_id);
			List tempList=pMap.get(realDeptId);
			tempList.remove(index);
		}
		
		updateDataMapByPageId(page_id,dataMap,dataTableName);
		return 1;
	}

	
	//设置指定位置数据(data)
	public void appendDeptData4List(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page_id=httpRequest.getParameter("page_id");
		String dept_id=httpRequest.getParameter("dept_id");
		String param_data=httpRequest.getParameter("param_data");
		String dept_path=httpRequest.getParameter("dept_path");
		String dataTableName=httpRequest.getParameter("data_table_name");
		if(dataTableName==null || "".equals(dataTableName) || "undefined".equals(dataTableName)){
			dataTableName="nh_micro_editpage_list";
		}
		Map paramData=null;
		paramData=new JsonSlurper().parseText(param_data);
		appendParentMap(page_id,dept_id,dept_path,paramData,dataTableName);
		return ;
	}
	//设置指定位置数据(data)
	private int appendParentMap(String page_id,String dept_id,String dept_path,Map paramObj,String dataTableName){
		List parentList=getParentDeptListByDeptId(page_id,dept_id);
		Map dataMap=getDeptDataListByPageId(page_id,dataTableName);
		Map pMap=getParentMapByPath4Map(dataMap,dept_path);
		List targetList=pMap.get(dept_id);
		if(targetList==null){
			targetList=new ArrayList();
			pMap.put(dept_id, targetList);
		}
		targetList.add(paramObj);
		updateDataMapByPageId(page_id,dataMap,dataTableName);
		return 1;
	}
	
}
