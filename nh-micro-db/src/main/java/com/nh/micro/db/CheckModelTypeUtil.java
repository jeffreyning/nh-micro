package com.nh.micro.db;


import com.nh.micro.db.MicroDbModelEntry;
import com.nh.micro.rule.engine.core.GroovyExecUtil;


import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;



public class CheckModelTypeUtil {
	public static final String DUMMY_SPLIT="___";
	public static String oracle_date_format="yyyy-mm-dd";
	public static String oracle_time_format="yyyy-mm-dd hh24:mi:ss";
	public static String oracle_timestamp_format="yyyy-mm-dd hh24:mi:ss.ff3";
	public static String oracle_str2date="to_date";
	public static String oracle_date2str="to_char";
	
	public static String mysql_date_format="%Y-%m-%d";
	public static String mysql_time_format="%Y-%m-%d %H:%i:%s";
	public static String mysql_timestamp_format="%Y-%m-%d %H:%i:%s";
	public static String mysql_str2date="str_to_date";
	public static String mysql_date2str="date_format";	
	
	public static String java_date_format="yyyy-MM-dd";
	public static String java_time_format="yyyy-MM-dd HH:mm:ss";
	public static String java_timestamp_format="yyyy-MM-dd HH:mm:ss.SSS";	
	
	public static Map colInfHolderMap=new HashMap();
	public static Map colInfTimeMap=new HashMap();	
	public static Long disTime=10000l;
	
	public static String getToDateReplaceStr(String type,MicroDbModelEntry modelEntry){
		if("mysql".equals(type)){
			String formatStr=mysql_time_format;
			if(modelEntry.getColType().equals(java.sql.Date.class)){
				formatStr=mysql_date_format;
			}else if(modelEntry.getColType().equals(java.sql.Time.class)){
				formatStr=mysql_time_format;
			}
			String replaceStr=mysql_str2date+"('<REPLACE>','"+formatStr+"')";
			return replaceStr;
		}else{
			String formatStr=oracle_time_format;
			if(modelEntry.getColType().equals(java.sql.Date.class)){
				formatStr=oracle_date_format;
			}else if(modelEntry.getColType().equals(java.sql.Time.class)){
				formatStr=oracle_time_format;
			}
			String replaceStr=oracle_str2date+"('<REPLACE>','"+formatStr+"')";
			return replaceStr;			
		}
	}
	
	
	public static boolean isDate(MicroDbModelEntry modelEntry) {
		Class colClass = modelEntry.colType;
		if (java.sql.Date.class.equals(colClass)
				|| java.sql.Time.class.equals(colClass)
				|| java.sql.Timestamp.class.equals(colClass)) {
			return true;
		}
		return false;
	}
	public static boolean isNumber(MicroDbModelEntry modelEntry) {

		Class colClass = modelEntry.colType;
		if (java.math.BigDecimal.class.equals(colClass)
				|| java.lang.Long.class.equals(colClass)) {
			return true;
		}
		return false;
	}
	public static boolean isRealCol(MicroDbModelEntry modelEntry) {

		if (modelEntry.isMetaTable) {
			return false;
		}
		return true;
	}
	public static boolean isDynamicCol(MicroDbModelEntry modelEntry) {

		if (modelEntry.isMetaTable) {
			return true;
		}
		return false;
	}	
	public static boolean isDbCol(Map<String,MicroDbModelEntry> modelEntryMap,String colName) {

		if (modelEntryMap.get(colName)!=null) {
			return true;
		}
		return false;
	}	
	public static String getRealColName(String colName){
		if(!colName.contains("dbcol_")){
			return colName;
		}
		int start=colName.indexOf("dbcol_");
		if(start<=0){
			return colName;
		}
		String tempName=colName.substring(start);
		return tempName;
	}
	
	public static List getModelEntryList2Update(List configList,String modelName,Map paramMap) throws Exception{

		if(configList!=null && configList.size()>0){
			List retList=getModelEntryList4Config(configList);
			if(retList!=null && retList.size()>0){
				return retList;
			}
		}
		else if(modelName!=null && !"".equals(modelName)){
			List retList=getModelEntryList4Model(modelName);
			if(retList!=null && retList.size()>0){
				return retList;
			}
		}else{
			return getModelEntryList4ColName(paramMap);
		}
		return null;

	}
	
	public static List getModelEntryList2Select(List configList,String modelName,String tableName) throws Exception{

		if(configList!=null && configList.size()>0){
			List retList=getModelEntryList4Config(configList);
			if(retList!=null && retList.size()>0){
				return retList;
			}
		}
		else if(modelName!=null && !"".equals(modelName)){
			List retList=getModelEntryList4Model(modelName);
			if(retList!=null && retList.size()>0){
				return retList;
			}
		}else{
			return getModelEntryList4Db(tableName);
		}
		return null;

	}
	 
	public static List getModelEntryList4Model(String modelName){
		if(GroovyExecUtil.getGroovyObj(modelName)==null){
			return null;
		}
		Class cls=GroovyExecUtil.getGroovyObj(modelName).getClass();
		
		return (List) GroovyExecUtil.execGroovyRetObj(modelName, "getEntryList", cls);
	}
	
	public static List getModelEntryList4Config(List<Map> configList) throws Exception{
		if(configList==null){
			return null;
		}
		List retList=new ArrayList();
		List<Map> dbColInfo=configList;
		for(Map rowMap:dbColInfo){
			String colId=(String) rowMap.get("colId");
			String colName=(String) rowMap.get("colName");
			String colClass=(String) rowMap.get("colClass");
			String remark=(String) rowMap.get("remark");
			String isMetaTable=(String) rowMap.get("isMetaTable");
			Class colCls=String.class;
			if(colClass!=null || !"".equals(colClass)){
				colCls=Class.forName(colClass);
			}
			Boolean dynamicFlag=false;
			if(isMetaTable!=null || !"".equals(isMetaTable)){
				dynamicFlag=Boolean.valueOf(isMetaTable);
			}
			MicroDbModelEntry oneCol=new MicroDbModelEntry(colId,colName,colCls,"");
			oneCol.isMetaTable=dynamicFlag;
			retList.add(oneCol);
		}
		return retList;
		
	}
	
	
	public static List getModelEntryList4ColName(Map paramMap) throws Exception{
		if(paramMap==null){
			return null;
		}
		List retList=new ArrayList();
		Iterator ite=paramMap.keySet().iterator();
		while(ite.hasNext()){
			MicroDbModelEntry modelEntry=new MicroDbModelEntry("","",String.class,"");
			
			String colName=(String) ite.next();
			if(colName.contains("dbcol_ext_")){
				modelEntry.isMetaTable=true;
				int start=colName.indexOf("dbcol_ext_");
				if(start<=0){
					modelEntry.setMetaContentId("meta_content");
					modelEntry.setMetaId("meta_content->>'$."+colName+"'");
					modelEntry.setColId(colName);
					modelEntry.setColName(colName);
				}else if(start>0){
					String tempName=colName.substring(0,start);
					String realColName=colName.substring(start);
					modelEntry.setMetaId(tempName+"meta_content->>'$."+realColName+"'");
					//modelEntry.setColId(realColName);
					//modelEntry.setColName(realColName);
					//֧��joinǰ׺start
					modelEntry.setColId(colName);
					modelEntry.setColName(colName);
					//end
					
					modelEntry.setMetaContentId(tempName+"meta_content");
				}
			}else if(colName.contains("dbcol_real_")){
				modelEntry.isMetaTable=false;
				modelEntry.setColId(colName);
				modelEntry.setColName(colName);
			}else{
				continue;
			}
			
			if(colName.endsWith("_number_decimal")){
				modelEntry.setColType(java.math.BigDecimal.class);
			}else if(colName.endsWith("_number")){
				modelEntry.setColType(java.lang.Long.class);
			}else if(colName.endsWith("_date")  ){
				modelEntry.setColType(java.sql.Date.class);
			}else if(colName.endsWith("_datetime")){
				modelEntry.setColType(java.sql.Time.class);
			}else if(colName.endsWith("_timestamp")){
				modelEntry.setColType(java.sql.Timestamp.class);
			}else{
				modelEntry.setColType(java.lang.String.class);
			}
			retList.add(modelEntry);
		}
		return retList;
	}	
	
	public static void getModelEntryMap(Map modelEntryMap,List<MicroDbModelEntry> modelEntryList) throws Exception{
		if(modelEntryList==null){
			return;
		}
		for(MicroDbModelEntry modelEntry:modelEntryList){
			String colId=modelEntry.getColId();
			modelEntryMap.put(colId, modelEntry);
		}

	}	
	
	
	public static void getModelEntryMap4Update(Map modelEntryMap,List<MicroDbModelEntry> modelEntryList) throws Exception{
		if(modelEntryList==null){
			return;
		}
		for(MicroDbModelEntry modelEntry:modelEntryList){
			String colId=modelEntry.getColId();
			modelEntryMap.put(colId, modelEntry);
		}

	}	
	
	
	public static Class changeDbType(int sqlType) {
		if (Types.BIT == sqlType || Types.BIGINT == sqlType
				|| Types.INTEGER == sqlType || Types.SMALLINT == sqlType
				|| Types.TINYINT == sqlType) {
			return Long.class;
		} else if (Types.DECIMAL == sqlType || Types.DOUBLE == sqlType
				|| Types.FLOAT == sqlType || Types.NUMERIC == sqlType
				|| Types.REAL == sqlType) {
			return BigDecimal.class;
		} else if (Types.DATE == sqlType) {
			return java.sql.Date.class;
		} else if (Types.TIME == sqlType) {
			return java.sql.Time.class;
		} else if (Types.TIMESTAMP == sqlType) {
			return java.sql.Timestamp.class;
		}
		return String.class;

	}
	
	
	public static List<Map> getColInfoFromMeta(String tableName,String dbName){
		return getColInfoFromMeta(tableName,dbName,"*");
	}
	public static List<Map> getColInfoFromMeta(String tableName,String dbName,String cusSelect){
		if(dbName==null || "".equals(dbName)){
			dbName="default";
		}
		if(cusSelect==null || "".equals(cusSelect)){
			cusSelect="*";
		}
		String holdKey=dbName+"_"+tableName+"_"+cusSelect;
		Object holdValue=colInfHolderMap.get(holdKey);
		Long holdTime=(Long) colInfTimeMap.get(holdKey);
		if(holdTime!=null && holdValue!=null){
			Long nowTime=(new java.util.Date()).getTime();
			if(nowTime<holdTime){
				return (List<Map>) holdValue;
			}
		}
		
		List tableFieldList=new ArrayList();
		//add for oracle by ninghao
		String sql = "select "+cusSelect+" from "+ tableName+" where 1=2";
/*		if("mysql".equalsIgnoreCase(type)){
			sql=sql+" limit 0";
		}else{
			sql=sql+" rownum=1";
		}*/
		JdbcTemplate jdbcTemplate = (JdbcTemplate)MicroDbHolder.getDbSource(dbName);
		SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
		SqlRowSetMetaData sqlRsmd = sqlRowSet.getMetaData();
		int columnCount = sqlRsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			Map fieldMap = new HashMap<String,String>();
			//String realName=sqlRsmd.getColumnName(i);
			String realName=sqlRsmd.getColumnLabel(i);
			String showName=realName.replace(DUMMY_SPLIT, ".");
			fieldMap.put("fieldName", showName);
			//fieldMap.put("fieldLabel", sqlRsmd.getColumnLabel(i));
			fieldMap.put("fieldLabel", showName);
			fieldMap.put("fieldTypeName", sqlRsmd.getColumnTypeName(i));
			fieldMap.put("fieldType", sqlRsmd.getColumnType(i));
			fieldMap.put("fieldCatalogName", sqlRsmd.getCatalogName(i));
			fieldMap.put("fieldTableName", sqlRsmd.getTableName(i));
			tableFieldList.add(fieldMap);
		}
		long nowTime=(new java.util.Date()).getTime();
		long checkTime=nowTime+disTime;
		colInfHolderMap.put(holdKey, tableFieldList);
		colInfTimeMap.put(holdKey, checkTime);
		return tableFieldList;
	}
	
	public static List  getModelEntryList4Db(String tableName){
		return getModelEntryList4Db(tableName,"","");
	}
	public static List  getModelEntryList4Db(String tableName,String dbName){
		return getModelEntryList4Db(tableName,dbName,"");
	}
	public static List  getModelEntryList4Db(String tableName,String dbName,String cusSelect){
		List retList=new ArrayList();
		List<Map> dbColInfo=getColInfoFromMeta(tableName,dbName,cusSelect);
		for(Map rowMap:dbColInfo){
			String colId=(String) rowMap.get("fieldName");
			String colName=(String) rowMap.get("fieldLabel");
			Integer fieldType=(Integer) rowMap.get("fieldType");
			Class colClass=changeDbType(fieldType);
			MicroDbModelEntry oneCol=new MicroDbModelEntry(colId,colName,colClass,"");
			retList.add(oneCol);
		}
		return retList;
	}	
	
	private static List checkMetaContentName(Map rowMap){
		List metaNameList=new ArrayList();
		Set<String> keySet=rowMap.keySet();
		for(String key:keySet){
			if(key.contains("meta_content")){
				metaNameList.add(key);
			}
		}
		return metaNameList;
	}
	
	public static void addMetaCols(Map rowMap){
		//add 20180111 ninghao
		if(rowMap==null){
			return;
		}
		
		List<String> metaContentNameList=checkMetaContentName(rowMap);
		for(String metaContentName:metaContentNameList){
			String metaContentNamePrefix=metaContentName.replace("meta_content", "");
			String meta_content=(String) rowMap.get(metaContentName);
			if(meta_content==null || "".equals(meta_content)){
				continue;
			}
			Map metaMap=(Map) new JsonSlurper().parseText(meta_content);
			if(metaMap!=null){
				Set<String> keySet=metaMap.keySet();
				for(String key:keySet){
					Object obj=metaMap.get(key);
					if(obj instanceof Map || obj instanceof List){
						//continue;
					}
					String nkey=metaContentNamePrefix+key;
					if(!rowMap.containsKey(nkey)){
						rowMap.put(nkey, obj);
					}
				}
			
			}
		}

	}
	
	public static void addMetaCols(List<Map> rows){

		for(Map rowMap:rows){

			List<String> metaContentNameList=checkMetaContentName(rowMap);
			for(String metaContentName:metaContentNameList){
				String metaContentNamePrefix=metaContentName.replace("meta_content", "");
				String meta_content=(String) rowMap.get(metaContentName);
				if(meta_content==null || "".equals(meta_content)){
					continue;
				}
				Map metaMap=(Map) new JsonSlurper().parseText(meta_content);
				if(metaMap!=null){
					Set<String> keySet=metaMap.keySet();
					for(String key:keySet){
						Object obj=metaMap.get(key);
						if(obj instanceof Map || obj instanceof List){
							//continue;
						}
						String nkey=metaContentNamePrefix+key;
						if(!rowMap.containsKey(nkey)){
							rowMap.put(nkey, obj);
						}
					}
				
				}
			}
		}
	}
	public static void changeDateCols(List<Map> rows){
		for(Map rowMap:rows){
			Set<String> keySet=rowMap.keySet();
			for(String key:keySet){
				Object obj=rowMap.get(key);
				if(obj instanceof java.sql.Date){
					SimpleDateFormat sf=new SimpleDateFormat(java_date_format);
					String timeStr=sf.format(obj);
					rowMap.put(key,timeStr);
				}else if(obj instanceof java.sql.Time){
					SimpleDateFormat sf=new SimpleDateFormat(java_time_format);
					String timeStr=sf.format(obj);
					rowMap.put(key,timeStr);					
				}else if(obj instanceof java.sql.Timestamp){
					SimpleDateFormat sf=new SimpleDateFormat(java_time_format);
					String timeStr=sf.format(obj);
					rowMap.put(key,timeStr);					
				}
			}
			

		}
	}
	
	public static void changeDateCols(Map rowMap){

		//add 20180111 ninghao
		if(rowMap==null){
			return;
		}
		
		Set<String> keySet=rowMap.keySet();
		for(String key:keySet){
			Object obj=rowMap.get(key);
			if(obj instanceof java.sql.Date){
				SimpleDateFormat sf=new SimpleDateFormat(java_date_format);
				String timeStr=sf.format(obj);
				rowMap.put(key,timeStr);
			}else if(obj instanceof java.sql.Time){
				SimpleDateFormat sf=new SimpleDateFormat(java_time_format);
				String timeStr=sf.format(obj);
				rowMap.put(key,timeStr);					
			}else if(obj instanceof java.sql.Timestamp){
				SimpleDateFormat sf=new SimpleDateFormat(java_time_format);
				String timeStr=sf.format(obj);
				rowMap.put(key,timeStr);					
			}
		}

	}	


	public static void changeNoStrCols(List<Map> rows){
		for(Map rowMap:rows){
			changeNoStrCols(rowMap);
		}
	}	
	
	public static void changeNoStrCols(Map rowMap){
		//add 20180111 ninghao
		if(rowMap==null){
			return;
		}
		
		Set<String> keySet=rowMap.keySet();
		for(String key:keySet){
			Object obj=rowMap.get(key);
			if(obj instanceof java.sql.Date){
				SimpleDateFormat sf=new SimpleDateFormat(java_date_format);
				String timeStr=sf.format(obj);
				rowMap.put(key,timeStr);
			}else if(obj instanceof java.sql.Time){
				SimpleDateFormat sf=new SimpleDateFormat(java_time_format);
				String timeStr=sf.format(obj);
				rowMap.put(key,timeStr);					
			}else if(obj instanceof java.sql.Timestamp){
				SimpleDateFormat sf=new SimpleDateFormat(java_time_format);
				String timeStr=sf.format(obj);
				rowMap.put(key,timeStr);					
			}else if(obj instanceof Map || obj instanceof List){
				JsonBuilder jsonBuilder=new JsonBuilder(obj);
				String retStr=jsonBuilder.toString();
				rowMap.put(key,retStr);
			}
			else if(obj!=null) {
					rowMap.put(key,obj.toString());					

			}
		}

	}	
	
}
