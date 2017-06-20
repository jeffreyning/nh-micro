package com.nh.micro.template;

import java.io.StringWriter;
import java.math.BigDecimal;


import com.nh.micro.db.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;


import com.nh.micro.db.Cutil;
import com.nh.micro.db.Cobj;

import com.nh.micro.db.MicroDbModelEntry;




import com.nh.micro.db.CheckModelTypeUtil;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import java.util.LinkedHashMap;

import javax.sql.DataSource;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.Gson;

/**
 * service��ģ���װ֧��
 * @author ninghao ���
 *
 */
public class MicroServiceTemplateSupport {

	
	public MicroServiceTemplateSupport(){
		
	}
	public MicroServiceTemplateSupport(String dbName){
		this.dbName=dbName;
	}
	public static Map supportHolder=new HashMap();
	
	public static Map getSupportHolder() {
		return supportHolder;
	}

	public void setSupportHolder(Map supportHolder) {
		MicroServiceTemplateSupport.supportHolder = supportHolder;
	}
	
	public static MicroServiceTemplateSupport getInstance(){
		MicroServiceTemplateSupport instance=(MicroServiceTemplateSupport) getSupportHolder().get("default");
		if(instance==null){
			instance=new MicroServiceTemplateSupport();
			getSupportHolder().put("default", instance);
		}
		return instance;
		
	}
	public static MicroServiceTemplateSupport getInstance(String dbName){
		if(dbName==null || "".equals(dbName)){
			dbName="default";
		}
		MicroServiceTemplateSupport instance=(MicroServiceTemplateSupport) getSupportHolder().get(dbName);
		if(instance==null){
			instance=new MicroServiceTemplateSupport(dbName);
			getSupportHolder().put(dbName, instance);
		}
		return instance;
		
	}	
	public String dbName="default";
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * ��ȡ���ģ��
	 * @param requestParamMap �ύ�������
	 * @param tableName �����
	 * @param modelName ģ�����
	 * @return
	 * @throws Exception
	 */
	public static Map getModelEntryMap(Map requestParamMap,String tableName,String modelName,String dbName) throws Exception{
		return getModelEntryMap( requestParamMap, tableName, modelName, dbName,"");
	}	
	public static Map getModelEntryMap(Map requestParamMap,String tableName,String modelName,String dbName,String cusSelect) throws Exception{
		Map modelEntryMap=new HashMap();
		if(requestParamMap==null || requestParamMap.isEmpty()){
			return modelEntryMap;
		}
		List entryList4Base=CheckModelTypeUtil.getModelEntryList4Model("MicroBaseModel");
		List entryList4ColName=CheckModelTypeUtil.getModelEntryList4ColName(requestParamMap);
		List entryList4ModelName=CheckModelTypeUtil.getModelEntryList4Model("Micro"+modelName+"Model");
		if(entryList4ModelName==null){
		List entryList4Db=CheckModelTypeUtil.getModelEntryList4Db(tableName,dbName,cusSelect);
		entryList4ModelName=entryList4Db;
		}
		CheckModelTypeUtil.getModelEntryMap(modelEntryMap, entryList4Base);
		CheckModelTypeUtil.getModelEntryMap(modelEntryMap, entryList4ColName);
		CheckModelTypeUtil.getModelEntryMap(modelEntryMap, entryList4ModelName);
		return modelEntryMap;
	}
	
	/**
	 * ��ݱ�ṹʵʱ��ȡ���ģ��
	 * 
	 * @param tableName �����
	 * @return
	 * @throws Exception
	 */
	public static List getColInfoFromMeta(String tableName,String dbName){
		return getColInfoFromMeta(tableName,dbName,"");
	}
	public static List getColInfoFromMeta(String tableName,String dbName,String cusSelect){
		List tableFieldList=new ArrayList();
		List modelEntryList=CheckModelTypeUtil.getModelEntryList4Db(tableName,dbName);
		for (int i = 1; i <= modelEntryList.size(); i++) {
			MicroDbModelEntry modelEntry=(MicroDbModelEntry) modelEntryList.get(i);
			Map<String,Object> fieldMap = new HashMap<String,Object>();
			fieldMap.put("fieldName", modelEntry.getColId());
			fieldMap.put("fieldType", modelEntry.getColType());

			tableFieldList.add(fieldMap);
		}
		return tableFieldList;
	}	
	/**
	 * ���ģ�ͺ��ύ����where�ַ�
	 * @param requestParamMap �ύ����
	 * @param modelEntryMap ���ģ��
	 * @return
	 * @throws Exception
	 */
	public static String createWhereInStr(Map requestParamMap,Map modelEntryMap){
		if(modelEntryMap==null){
			return null;
		}
		Cobj cobjValues=Cutil.createCobj(" and ");
		Iterator it=modelEntryMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			MicroDbModelEntry modelEntry=(MicroDbModelEntry) modelEntryMap.get(key);
			String value=(String) requestParamMap.get(key);
			String whereValue="";
			if(CheckModelTypeUtil.isNumber(modelEntry)){
				whereValue=Cutil.rep("<REPLACE>",value);
			}else if(CheckModelTypeUtil.isDate(modelEntry) ){
				whereValue=Cutil.rep("str_to_date('<REPLACE>','%Y-%m-%d %H:%i:%s')",value);
			}else{
				whereValue=Cutil.rep("'<REPLACE>'",value);
			}
			if(CheckModelTypeUtil.isRealCol(modelEntry)==false){
				String metaName=modelEntry.getMetaContentId();
				String realKey=CheckModelTypeUtil.getRealColName(key);
				cobjValues.append(Cutil.rep(metaName+"->>'$.<REPLACE>'=", realKey)+whereValue,value!=null);
			}else if(CheckModelTypeUtil.isRealCol(modelEntry)){
				cobjValues.append(Cutil.rep("<REPLACE>=", key)+whereValue,value!=null);
			}
		}

		return cobjValues.getStr();
	}

	public static String createWhereInStr(Map requestParamMap,Map modelEntryMap,List placeList){
		if(modelEntryMap==null){
			return null;
		}
		Cobj cobjValues=Cutil.createCobj(" and ");
		Iterator it=modelEntryMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			MicroDbModelEntry modelEntry=(MicroDbModelEntry) modelEntryMap.get(key);
			String value=(String) requestParamMap.get(key);
			String whereValue="";
			if(CheckModelTypeUtil.isNumber(modelEntry)){
				whereValue=Cutil.rep("<REPLACE>",value);

			}else if(CheckModelTypeUtil.isDate(modelEntry) ){
				whereValue=Cutil.rep("str_to_date('<REPLACE>','%Y-%m-%d %H:%i:%s')",value);

			}else{
				whereValue=Cutil.rep("<REPLACE>",value);

			}
			if(CheckModelTypeUtil.isRealCol(modelEntry)==false){
				String metaName=modelEntry.getMetaContentId();
				String realKey=CheckModelTypeUtil.getRealColName(key);
				cobjValues.append(Cutil.rep(metaName+"->>'$.<REPLACE>'=?", realKey),value!=null);
				if(value!=null){
					placeList.add(whereValue);
				}
			}else if(CheckModelTypeUtil.isRealCol(modelEntry)){

				cobjValues.append(Cutil.rep("<REPLACE>=?", key),value!=null);
				if(value!=null){
					placeList.add(whereValue);
				}				
			}
		}

		return cobjValues.getStr();
	}	
	
	/**
	 * ���ģ�ͺ��ύ����update-set�ַ�
	 * @param dbColMap �ύ����
	 * @param modelEntryMap ���ģ��
	 * @param placeList ռλ���滻ֵ
	 * @return
	 * @throws Exception
	 */
	public static String createUpdateInStr(Map dbColMap,Map modelEntryMap){
		if(dbColMap==null){
			return null;
		}
		List realValueList=new ArrayList();
		List extValueList=new ArrayList();
		Boolean metaFlag=false;
		Map<String,Cobj> metaFlagMap=new LinkedHashMap();
		Cobj crealValues=Cutil.createCobj();

		Iterator it=modelEntryMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			MicroDbModelEntry modelEntry=(MicroDbModelEntry) modelEntryMap.get(key);
			String value=(String) dbColMap.get(key);

			if(CheckModelTypeUtil.isDynamicCol(modelEntry)){
				String metaName=modelEntry.getMetaContentId();
				Cobj cobjValues=metaFlagMap.get(metaName);
				if(cobjValues==null){
					cobjValues=Cutil.createCobj();
					metaFlagMap.put(metaName,cobjValues);
				}
				String realKey=CheckModelTypeUtil.getRealColName(key);
				cobjValues.append(Cutil.rep("'$.<REPLACE>'", realKey),value!=null);
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					cobjValues.append(Cutil.rep("<REPLACE>",value),value!=null);
				}else if(CheckModelTypeUtil.isDate(modelEntry) ){
					
					if(value!=null ){
						if(value.toLowerCase().equals("now()")){
							cobjValues.append(Cutil.rep("<REPLACE>",value),value!=null);
						}else{
							cobjValues.append(Cutil.rep("'<REPLACE>'",value),value!=null);
						}
					}
					
				}else{
					cobjValues.append(Cutil.rep("'<REPLACE>'",value),value!=null);
				}
				if(value!=null){
					extValueList.add(value);
				}
			}else if(CheckModelTypeUtil.isRealCol(modelEntry)){
				String whereValue="";
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					whereValue=Cutil.rep("<REPLACE>",value);
				}else if(CheckModelTypeUtil.isDate(modelEntry)){
					if(value!=null ){
						if(value.toLowerCase().equals("now()")){
							whereValue=value;
						}else{
							whereValue=Cutil.rep("str_to_date('<REPLACE>','%Y-%m-%d %H:%i:%s')",value);	
						}
					}

				}else{
					whereValue=Cutil.rep("'<REPLACE>'",value);
				}
				if(value!=null ){
					crealValues.append(Cutil.rep("<REPLACE>=", key)+whereValue,value!=null);
				}

			}
		}
		
		Set<String> metaKeySet=metaFlagMap.keySet();
		for(String key:metaKeySet){
			Cobj cobj=metaFlagMap.get(key);
			String dynamic=cobj.getStr();
			crealValues.append(Cutil.rep(key+"=JSON_SET(ifnull("+key+",'{}'),<REPLACE>)",dynamic),dynamic!=null );
		}

		return crealValues.getStr();
	}	
	public static String createUpdateInStr(Map dbColMap,Map modelEntryMap,List placeList){
		if(dbColMap==null){
			return null;
		}
		List realValueList=new ArrayList();
		List extValueList=new ArrayList();
		Boolean metaFlag=false;
		Map<String,Cobj> metaFlagMap=new LinkedHashMap();
		Cobj crealValues=Cutil.createCobj();

		Iterator it=modelEntryMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			//������idֵ
			if("id".equals(key)){
				continue;
			}
			MicroDbModelEntry modelEntry=(MicroDbModelEntry) modelEntryMap.get(key);
			String value=(String) dbColMap.get(key);

			if(CheckModelTypeUtil.isDynamicCol(modelEntry)){
				String metaName=modelEntry.getMetaContentId();
				Cobj cobjValues=metaFlagMap.get(metaName);
				if(cobjValues==null){
					cobjValues=Cutil.createCobj();
					metaFlagMap.put(metaName,cobjValues);
				}
				String realKey=CheckModelTypeUtil.getRealColName(key);
				cobjValues.append(Cutil.rep("'$.<REPLACE>'", realKey),value!=null);
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					cobjValues.append(Cutil.rep("<REPLACE>","?"),value!=null);
					
				}else if(CheckModelTypeUtil.isDate(modelEntry) ){
					if(value!=null ){
						if(value.toLowerCase().equals("now()")){
							cobjValues.append(Cutil.rep("<REPLACE>",value),value!=null);
						}else{
							cobjValues.append(Cutil.rep("<REPLACE>","?"),value!=null);
						}
					}					

					
				}else{
					cobjValues.append(Cutil.rep("<REPLACE>","?"),value!=null);
					
				}
				if(value!=null ){
					if(value.toLowerCase().equals("now()")==false){
					extValueList.add(value);
					}
				}
			}else if(CheckModelTypeUtil.isRealCol(modelEntry)){

				if(value!=null ){
					if(value.toLowerCase().equals("now()")){
						crealValues.append(Cutil.rep("<REPLACE>=", key)+value,value!=null);
					}else{
					
						realValueList.add(value);
						crealValues.append(Cutil.rep("<REPLACE>=", key)+"?",value!=null);
					}
				}
				

			}
		}
		
		Set<String> metaKeySet=metaFlagMap.keySet();
		for(String key:metaKeySet){
			Cobj cobj=metaFlagMap.get(key);
			String dynamic=cobj.getStr();
			crealValues.append(Cutil.rep(key+"=JSON_SET(ifnull("+key+",'{}'),<REPLACE>)",dynamic),dynamic!=null);
		}
		placeList.addAll(realValueList);
		placeList.addAll(extValueList);
		return crealValues.getStr();
	}

	
	/**
	 * ��װ�������ַ�
	 * @param dbColMap �ύ���
	 * @param modelEntryMap ���ģ�� 
	 * @return
	 * @throws Exception
	 */	
	public static String createInsertBeforeStr4ModelEntry(Map dbColMap,Map<String,MicroDbModelEntry> modelEntryMap){
		if(dbColMap==null){
			return null;
		}
		Cobj crealValues=Cutil.createCobj();
		Map metaMap=new HashMap();
		Iterator it=dbColMap.keySet().iterator();
		Set<String> metaFlagSet=new TreeSet();
		while(it.hasNext()){
			String key=(String) it.next();
			String value=(String) dbColMap.get(key);

			MicroDbModelEntry modelEntry=modelEntryMap.get(key);
			if(modelEntry==null){
				continue;
			}
			if(CheckModelTypeUtil.isRealCol(modelEntry)==false){
				if(value!=null){
					String metaName=modelEntry.getMetaContentId();
					metaFlagSet.add(metaName);
				}
			}else {
				crealValues.append(key,value!=null);
			}
		}

		for(String metaName:metaFlagSet){
			crealValues.append(metaName);
		}
		
		return crealValues.getStr();
	}
	
	/**
	 * ��װ����value�ַ�
	 * @param dbColMap �ύ���
	 * @param modelEntryMap ���ģ�� 
	 * @return
	 * @throws Exception
	 */	
	public static String createInsertValueStr4ModelEntry(Map dbColMap,Map<String,MicroDbModelEntry> modelEntryMap){
		if(dbColMap==null){
			return null;
		}
		Cobj crealValues=Cutil.createCobj();
		Map metaFlagMap=new TreeMap();
		
		Iterator it=dbColMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			String value=(String) dbColMap.get(key);
			
			MicroDbModelEntry modelEntry=modelEntryMap.get(key);
			if(modelEntry==null){
				continue;
			}
			
			if(CheckModelTypeUtil.isRealCol(modelEntry)==false){

				String metaName=modelEntry.getMetaContentId();
				Map metaMap=(Map) metaFlagMap.get(metaName);
				String realKey=CheckModelTypeUtil.getRealColName(key);
				if(metaMap==null){
					metaMap=new HashMap();
					metaFlagMap.put(metaName, metaMap);
				}
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					if(value.contains(".")){
						metaMap.put(realKey, new BigDecimal(value));
					}else{
						metaMap.put(realKey, Long.valueOf(value));
					}
				}else if(CheckModelTypeUtil.isDate(modelEntry)){
					metaMap.put(realKey, value);
				}else{
					metaMap.put(realKey, value);
				}
			}else{
				String whereValue="";
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					whereValue=Cutil.rep("<REPLACE>",value);
				}else if(CheckModelTypeUtil.isDate(modelEntry)){
			
					whereValue=Cutil.rep("str_to_date('<REPLACE>','%Y-%m-%d %H:%i:%s')",value);
				}else{
					whereValue=Cutil.rep("'<REPLACE>'",value);
				}
				crealValues.append(whereValue,value!=null);

			}
		}
		String dynamic=null;
		Iterator iter = metaFlagMap.keySet().iterator();
		while(iter.hasNext()){
			String key=(String) iter.next();
			Map metaMap=(Map) metaFlagMap.get(key);
			
			Gson gson=new Gson();
			dynamic=gson.toJson(metaMap);

			crealValues.append(Cutil.rep("'<REPLACE>'",dynamic,dynamic!=null ));
		}
		
		return crealValues.getStr();
	}

	public static String createInsertValueStr4ModelEntry(Map dbColMap,Map<String,MicroDbModelEntry> modelEntryMap,List placeList){
		if(dbColMap==null){
			return null;
		}
		Cobj crealValues=Cutil.createCobj();
		Map metaFlagMap=new TreeMap();
		
		Iterator it=dbColMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			String value=(String) dbColMap.get(key);
			
			MicroDbModelEntry modelEntry=modelEntryMap.get(key);
			if(modelEntry==null){
				continue;
			}
			
			if(CheckModelTypeUtil.isRealCol(modelEntry)==false){

				String metaName=modelEntry.getMetaContentId();
				Map metaMap=(Map) metaFlagMap.get(metaName);
				String realKey=CheckModelTypeUtil.getRealColName(key);
				if(metaMap==null){
					metaMap=new HashMap();
					metaFlagMap.put(metaName, metaMap);
				}
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					if(value.contains(".")){
						metaMap.put(realKey, new BigDecimal(value));
					}else{
						metaMap.put(realKey, Long.valueOf(value));
					}
				}else if(CheckModelTypeUtil.isDate(modelEntry)){
					metaMap.put(realKey, value);
				}else{
					metaMap.put(realKey, value);
				}
			}else{
				String whereValue="";
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					whereValue=Cutil.rep("?",value);
				}else if(CheckModelTypeUtil.isDate(modelEntry)){
					if(value!=null){
						if(value.toLowerCase().equals("now()")){
							whereValue=value;
						}else{
							whereValue=Cutil.rep("str_to_date(?,'%Y-%m-%d %H:%i:%s')",value);
						}
					}						
					
					//whereValue=Cutil.rep("str_to_date(?,'%Y-%m-%d %H:%i:%s')",value);
				}else{
					whereValue=Cutil.rep("?",value);
				}
				crealValues.append(whereValue,value!=null);
				if(value!=null){
					if(value.toLowerCase().equals("now()")==false){
						placeList.add(value);
					}
				}

			}
		}

		Iterator iter = metaFlagMap.keySet().iterator();
		
		while(iter.hasNext()){
			String dynamic=null;
			String key=(String) iter.next();
			Map metaMap=(Map) metaFlagMap.get(key);
			
			Cobj dObj=Cutil.createCobj();
			Iterator si=metaMap.keySet().iterator();
			while(si.hasNext()){
				String skey=(String) si.next();
				Object svalue=metaMap.get(skey);
				if(svalue!=null ){
					String realk="'"+skey+"'";
					String realv="?";
					if(svalue.toString().equalsIgnoreCase("now()")){
						realv=svalue.toString();
					}else{
						placeList.add(svalue);
					}
					dObj.append(realk).append(realv);
				}
			}
			dynamic=dObj.getStr();

			crealValues.append(Cutil.rep("JSON_OBJECT(<REPLACE>)",dynamic,dynamic!=null ));
			if(dynamic!=null && !"".equals(dynamic)){
				String realDynamic="JSON_OBJECT("+dynamic+")";
				//placeList.add(dynamic);
			}
		}
		
		return crealValues.getStr();
	}	
	
	//����joinʱ��ǰ׺��where����
	public String createWhere4Join(Map requestParamMap,String joinName,String colsStr) throws Exception{

		String[] colsArray=colsStr.split(",");
		Map<String,String> colsMap=new HashMap();
		for(String colName:colsArray){
			String[] asArray=colName.split(" as ");
			String diffName=asArray[0];
			String keyName=diffName;
			int index=keyName.indexOf(".");
			keyName=keyName.substring(index);
			if(asArray.length>1){
				keyName=asArray[1];
			}
			
			colsMap.put(keyName, diffName);
		}
		Map modelEntryMap=getModelEntryMap(requestParamMap,joinName,null,dbName,colsStr);
		Map requestParamMapEx=new HashMap();
		Map modelEntryMapEx=new HashMap();
		Set<String> keySet=colsMap.keySet();
		for(String colKey:keySet){
			String diffName=colsMap.get(colKey);
			requestParamMapEx.put(diffName, requestParamMap.get(colKey));
			modelEntryMapEx.put(diffName, modelEntryMap.get(colKey));
			
		}
		String retStr= createWhereInStr(requestParamMapEx, modelEntryMapEx);
		return retStr;
	}	
	
	public String createWhere4Join(Map requestParamMap,String joinName,String colsStr,List placeList) throws Exception{

		String[] colsArray=colsStr.split(",");
		Map<String,String> colsMap=new HashMap();
		for(String colName:colsArray){
			String[] asArray=colName.split(" as ");
			String diffName=asArray[0];
			String keyName=diffName;
			int index=keyName.indexOf(".");
			keyName=keyName.substring(index);
			if(asArray.length>1){
				keyName=asArray[1];
			}
			
			colsMap.put(keyName, diffName);
		}
		Map modelEntryMap=getModelEntryMap(requestParamMap,joinName,null,dbName,colsStr);
		Map requestParamMapEx=new HashMap();
		Map modelEntryMapEx=new HashMap();
		Set<String> keySet=colsMap.keySet();
		for(String colKey:keySet){
			String diffName=colsMap.get(colKey);
			requestParamMapEx.put(diffName, requestParamMap.get(colKey));
			modelEntryMapEx.put(diffName, modelEntryMap.get(colKey));
			
		}
		String retStr= createWhereInStr(requestParamMapEx, modelEntryMapEx,placeList);
		return retStr;
	}	
	
	/**
	 * ��ҳ��ѯ
	 * @param requestParamMap �ύ����
	 * @param tableName �����
	 * @param pageMap ��ҳ����
	 * @param cusWhere ����where�ַ�
	 * @param cusSelect ����select�ַ�
	 * @param modelName ģ����� 
	 * @return
	 * @throws Exception
	 */
	private Map getInfoList4PageServiceInnerEx(Map requestParamMap,String tableName,Map pageMap,String cusWhere,String cusSelect,String modelName,List cusPlaceList) throws Exception{
		
		String page=(String) pageMap.get("page");
		String rows=(String) pageMap.get("rows");
		String sort=(String) pageMap.get("sort");
		String order=(String) pageMap.get("order");
		String cusSort=(String) pageMap.get("cusSort");
		
		Integer pageNum=Integer.valueOf(page);
		Integer rowsNum=Integer.valueOf(rows);
		
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		
		String id=(String) requestParamMap.get("id");
		String where="";

		if(cusWhere !=null && !"".equals(cusWhere)){
			where="where "+cusWhere;
		}
		
		String whereCols="";	
		
		Map whereColsMap=requestParamMap;
		if(whereColsMap!=null && !whereColsMap.isEmpty()){
			StringBuilder sb=new StringBuilder("");
			Set<String> keySet=whereColsMap.keySet();
			int i=0;
			for(String key:keySet){
				if(!key.contains(".")){
					continue;
				}
				if(key.contains("dbcol_")){
					continue;
				}				
				String colName=key;
				String realColName=colName.replace(".", CheckModelTypeUtil.DUMMY_SPLIT);
				String colsStr=colName+" as "+realColName;
				if(i==0){
					sb.append(colsStr);	
				}else{
					sb.append(",").append(colsStr);
				}
				i++;
			}
			whereCols=sb.toString();
		}
		
		Map modelEntryMap=getModelEntryMap(requestParamMap,tableName,modelName,dbName,whereCols);
		List placeList=new ArrayList();
		String whereInStr=createWhereInStr(requestParamMap,modelEntryMap,placeList);
		if(whereInStr!=null && !"".equals(whereInStr)){
			if(!where.equals("")){
				where=where+" and "+whereInStr;
			}else{
				where="where "+whereInStr;
			}
		}
		List realPlaceList=new ArrayList();
		realPlaceList.addAll(placeList);
		if(cusPlaceList==null){
			cusPlaceList=new ArrayList();
		}
		realPlaceList.addAll(cusPlaceList);
		String selectCount="select count(1) from "+tableName+" "+where;
		Integer total=(MicroMetaDao.getInstance(dbName)).queryObjJoinCountByCondition(selectCount,realPlaceList.toArray());
		String select="";
		if(cusSelect!=null && !"".equals(cusSelect)){
			select="select "+cusSelect+" from "+tableName+" "+where; 
		}else{
			select="select * from "+tableName+" "+where;
		}

		String orderSql="";
		if(cusSort!=null && !"".equals(cusSort)){
			orderSql="order by "+cusSort;
		}else if(sort!=null && !sort.equals("")){
			orderSql="order by "+sort+" "+order;
		}

		String sql=select+" "+orderSql;
		int startNum=(MicroMetaDao.getInstance(dbName)).calcuStartIndex(pageNum-1, rowsNum);
		int endNum=startNum+rowsNum;

		List infoList=(MicroMetaDao.getInstance(dbName)).queryObjJoinDataByPageCondition(sql, startNum, endNum,realPlaceList.toArray());
		if(infoList==null){
			infoList=new ArrayList();
		}
		CheckModelTypeUtil.addMetaCols(infoList);

		CheckModelTypeUtil.changeNoStrCols(infoList);
		Map retMap=new HashMap();
		retMap.put("rows", infoList);
		retMap.put("total", total);
		return retMap;
	}

	
	//sql
	public Map getInfoList4PageServiceBySql(String countSql,String sql,Map pageMap) throws Exception{
		return getInfoList4PageServiceInnerExBySql(countSql, null, sql, null, pageMap);
	}
	public Map getInfoList4PageServiceBySql(String countSql,List countPlaceList,String sql,List placeList,Map pageMap) throws Exception{
		return getInfoList4PageServiceInnerExBySql(countSql, countPlaceList, sql, placeList, pageMap);
	}
	private Map getInfoList4PageServiceInnerExBySql(String countSql,List countPlaceList,String sql,List placeList,Map pageMap) throws Exception{

		if(countPlaceList==null){
			countPlaceList=new ArrayList();
		}
		if(placeList==null){
			placeList=new ArrayList();
		}
		String page=(String) pageMap.get("page");
		String rows=(String) pageMap.get("rows");
		String sort=(String) pageMap.get("sort");
		String order=(String) pageMap.get("order");
		String cusSort=(String) pageMap.get("cusSort");
		
		Integer pageNum=Integer.valueOf(page);
		Integer rowsNum=Integer.valueOf(rows);
		
		Integer total=(MicroMetaDao.getInstance(dbName)).queryObjJoinCountByCondition(countSql,countPlaceList.toArray());

		String orderSql="";
		if(cusSort!=null && !"".equals(cusSort)){
			orderSql="order by "+cusSort;
		}else if(sort!=null && !sort.equals("")){
			orderSql="order by "+sort+" "+order;
		}		
		String realSql=sql+" "+orderSql;
		int startNum=(MicroMetaDao.getInstance(dbName)).calcuStartIndex(pageNum-1, rowsNum);
		int endNum=startNum+rowsNum;
		//int endNum=rowsNum;
		List infoList=(MicroMetaDao.getInstance(dbName)).queryObjJoinDataByPageCondition(realSql, startNum, endNum,placeList.toArray());
		if(infoList==null){
			infoList=new ArrayList();
		}
		CheckModelTypeUtil.addMetaCols(infoList);
		CheckModelTypeUtil.changeNoStrCols(infoList);	
		Map retMap=new HashMap();
		retMap.put("rows", infoList);
		retMap.put("total", total);
		return retMap;		

	}	

	public Map getInfoList4PageServiceByMySql(String sql,List placeList,Map pageMap) throws Exception{
		return getInfoList4PageServiceInnerExByMySql(sql,placeList,pageMap);
	}

	public Map getInfoList4PageServiceByMySql(String sql,Map pageMap) throws Exception{
		return getInfoList4PageServiceInnerExByMySql(sql,null,pageMap);
	}
	
	private Map getInfoList4PageServiceInnerExByMySql(String sql,List placeList,Map pageMap) throws Exception{

		if(placeList==null){
			placeList=new ArrayList();
		}
		String page=(String) pageMap.get("page");
		String rows=(String) pageMap.get("rows");
		String sort=(String) pageMap.get("sort");
		String order=(String) pageMap.get("order");
		String cusSort=(String) pageMap.get("cusSort");
		
		Integer pageNum=Integer.valueOf(page);
		Integer rowsNum=Integer.valueOf(rows);
		


		String orderSql="";
		if(cusSort!=null && !"".equals(cusSort)){
			orderSql="order by "+cusSort;
		}else if(sort!=null && !sort.equals("")){
			orderSql="order by "+sort+" "+order;
		}		
		sql="select SQL_CALC_FOUND_ROWS "+sql.substring(6) ;
		String realSql=sql+" "+orderSql;
		int startNum=(MicroMetaDao.getInstance(dbName)).calcuStartIndex(pageNum-1, rowsNum);
		int endNum=startNum+rowsNum;
		//int endNum=rowsNum;
		List infoList=(MicroMetaDao.getInstance(dbName)).queryObjJoinDataByPageCondition(realSql, startNum, endNum,placeList.toArray());
		if(infoList==null){
			infoList=new ArrayList();
		}
		CheckModelTypeUtil.addMetaCols(infoList);
		CheckModelTypeUtil.changeNoStrCols(infoList);	
		
		String countSql="SELECT FOUND_ROWS() as total";
		List tempList=(MicroMetaDao.getInstance(dbName)).queryObjJoinByCondition(countSql);
		Long total=0l;
		if(tempList!=null){
			Map tempMap=(Map) tempList.get(0);
			total=(Long) tempMap.get("total");
		}
		Map retMap=new HashMap();
		retMap.put("rows", infoList);
		retMap.put("total", total);
		return retMap;		

	}	
	
	
	//inner
	public Map getInfoList4PageServiceInner(Map requestParamMap,String tableName,Map pageMap,String cusWhere,String cusSelect,String modelName,List cusPlaceList) throws Exception{
		
		return getInfoList4PageServiceInnerEx(requestParamMap,tableName,pageMap,cusWhere,cusSelect,modelName,cusPlaceList);
	}	
	public Map getInfoList4PageServiceInner(Map requestParamMap,String tableName,Map pageMap,String cusWhere,String cusSelect,String modelName) throws Exception{
		
		return getInfoList4PageServiceInnerEx(requestParamMap,tableName,pageMap,cusWhere,cusSelect,modelName,null);
	}
	
	//
	public  Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap,String cusWhere,String cusSelect) throws Exception{
		
		return getInfoList4PageServiceInner(requestParamMap,tableName,pageMap,cusWhere,cusSelect,"");
	}
	public Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap,String cusWhere) throws Exception{
		
		return getInfoList4PageServiceInner(requestParamMap,tableName,pageMap,cusWhere,"","");
	}
	public Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap) throws Exception{

		return getInfoList4PageServiceInner(requestParamMap,tableName,pageMap,"","","");
	}

	//֧��cusPlaceList
	public  Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap,String cusWhere,String cusSelect,List cusPlaceList) throws Exception{
		
		return getInfoList4PageServiceInner(requestParamMap,tableName,pageMap,cusWhere,cusSelect,"",cusPlaceList);
	}
	public Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap,String cusWhere,List cusPlaceList) throws Exception{
		
		return getInfoList4PageServiceInner(requestParamMap,tableName,pageMap,cusWhere,"","",cusPlaceList);
	}
	public Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap,List cusPlaceList) throws Exception{

		return getInfoList4PageServiceInner(requestParamMap,tableName,pageMap,"","","",cusPlaceList);
	}
	
	/**
	 * ������ݼ�¼
	 * @param requestParamMap �ύ����
	 * @param tableName �����
	 * @param cusCol �����ֶ��ַ�
	 * @param cusValue ����value�ַ�
	 * @param modelName ģ����� 
	 * @return
	 * @throws Exception
	 */
	public Integer createInfoServiceInner(Map requestParamMap,String tableName,String cusCol,String cusValue,String modelName) throws Exception{
		boolean autoFlag=false;
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		Map modelEntryMap=getModelEntryMap(requestParamMap,tableName,modelName,dbName);
		MicroDbModelEntry idEntry=(MicroDbModelEntry) modelEntryMap.get("id");
		
		String id=(String) requestParamMap.get("id");
		if(id==null || "".equals(id)){
			if(idEntry!=null && idEntry.colType.equals(String.class)){
				id=UUID.randomUUID().toString();
				requestParamMap.put("id", id);	
			}else{
				autoFlag=true;
			}
		
		}

		String cols=createInsertBeforeStr4ModelEntry(requestParamMap,modelEntryMap);

		List placeList=new ArrayList();
		String values=createInsertValueStr4ModelEntry(requestParamMap,modelEntryMap,placeList);
		String ncols=Cutil.jn(",", cols,cusCol);
		String nvalues=Cutil.jn(",", values,cusValue);
		Integer retStatus=(MicroMetaDao.getInstance(dbName)).insertObj(tableName, ncols, nvalues,placeList.toArray());
		if(autoFlag==true){
			String sql="SELECT LAST_INSERT_ID() as last_insert_id";
			List dataList=(MicroMetaDao.getInstance(dbName)).queryObjJoinByCondition(sql);
			Map oneMap=(Map) dataList.get(0);
			Object retId=oneMap.get("last_insert_id");
			requestParamMap.put("id", String.valueOf(retId));
		}

		return retStatus;
	}
	
	public Integer createInfoService(Map requestParamMap,String tableName) throws Exception{

		return createInfoServiceInner(requestParamMap,tableName,null,null,null);

	}	
	public Integer createInfoService(Map requestParamMap,String tableName,String cusCol,String cusValue) throws Exception{

		return createInfoServiceInner(requestParamMap,tableName,cusCol,cusValue,null);

	}	
	
	public Integer createInfoService(String id,Map requestParamMap,String tableName) throws Exception{
		requestParamMap.put("id", id);
		return createInfoServiceInner(requestParamMap,tableName,null,null,null);

	}
	public Integer createInfoService(String id,Map requestParamMap,String tableName,String cusCol,String cusValue) throws Exception{
		requestParamMap.put("id", id);
		return createInfoServiceInner(requestParamMap,tableName,cusCol,cusValue,null);

	}
	/**
	 * ������ݼ�¼
	 * @param requestParamMap �ύ����
	 * @param tableName �����
	 * @param cusCondition ���������ַ�
	 * @param cusSetStr ����set�ַ�
	 * @param modelName ģ����� 
	 * @return
	 * @throws Exception
	 */
	public Integer updateInfoServiceInner(String id,Map requestParamMap,String tableName,String cusCondition,String cusSetStr,String modelName) throws Exception{
		//String id=(String) requestParamMap.get("id");
		String condition="id=?";
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		Map modelEntryMap=getModelEntryMap(requestParamMap,tableName,modelName,dbName);
		
		List placeList=new ArrayList();
		String setStr=createUpdateInStr(requestParamMap,modelEntryMap,placeList);
		String nCondition=Cutil.jn(" and ", cusCondition,condition);
		String nSetStr=Cutil.jn(",", setStr,cusSetStr);
		placeList.add(id);
		Integer retStatus=(MicroMetaDao.getInstance(dbName)).updateObjByCondition(tableName, nCondition, nSetStr,placeList.toArray());
		return retStatus;
	}	
	
	//sql
	private Integer updateInfoServiceInnerBySql(String sql,List placeList) throws Exception{
		if(placeList==null){
			placeList=new ArrayList();
		}
		Integer retStatus=(MicroMetaDao.getInstance(dbName)).updateObjByCondition(sql,placeList.toArray());
		return retStatus;
	}
	public Integer updateInfoServiceBySql(String sql,List placeList) throws Exception{
		return updateInfoServiceInnerBySql(sql,placeList);
	}
	
	public Integer updateInfoService(Map requestParamMap,String tableName) throws Exception{
		String id=(String) requestParamMap.get("id");
		return updateInfoServiceInner(id,requestParamMap,tableName,null,null,null);
	}

	public Integer updateInfoService(Map requestParamMap,String tableName,String cusCondition,String cusSetStr) throws Exception{
		String id=(String) requestParamMap.get("id");
		return updateInfoServiceInner(id,requestParamMap,tableName,cusCondition,cusSetStr,null);
	}
	
	/**
	 * ���id������ݼ�¼
	 * @param id ��������	  
	 * @param requestParamMap �ύ����
	 * @param tableName �����
	 * @param cusCondition ���������ַ�
	 * @param cusSetStr ����set�ַ�
	 * @param modelName ģ����� 
	 * @return
	 * @throws Exception
	 */
	public Integer updateInfoByIdService(String id,Map requestParamMap,String tableName,String cusCondition,String cusSetStr) throws Exception{
		return updateInfoServiceInner(id,requestParamMap,tableName,cusCondition,cusSetStr,null);
	}	
	public Integer updateInfoByIdService(String id,Map requestParamMap,String tableName,String cusCondition,String cusSetStr,String modelName) throws Exception{
		return updateInfoServiceInner(id,requestParamMap,tableName,cusCondition,cusSetStr,modelName);
	}
	public Integer updateInfoByIdService(String id,String tableName,Map requestParamMap) throws Exception{
		return updateInfoServiceInner(id,requestParamMap,tableName,null,null,null);
	}
	public Integer updateInfoByIdService(String id,String tableName,Map requestParamMap,String modelName) throws Exception{
		return updateInfoServiceInner(id,requestParamMap,tableName,null,null,modelName);
	}

	
	/**
	 * ���ҵ��id������ݼ�¼
	 * @param bizid ҵ������	
	 * @param tableName �����
	 * @param bizCol ҵ������� 
	 * @param requestParamMap �ύ����
	 * @param cusCondition ���������ַ�
	 * @param cusSetStr ����set�ַ�
	 * @param modelName ģ����� 
	 * @return
	 * @throws Exception
	 */	
	public Integer updateInfoByBizIdServiceInner(String bizId,String tableName,String bizCol,Map requestParamMap,String cusCondition,String cusSetStr,String modelName) throws Exception{
		String condition=Cutil.rep(bizCol+"=?",bizId);
		
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		Map modelEntryMap=getModelEntryMap(requestParamMap,tableName,modelName,dbName);
		
		List placeList=new ArrayList();
		String setStr=createUpdateInStr(requestParamMap,modelEntryMap,placeList);
		
		String nCondition=Cutil.jn(" and ", cusCondition,condition);
		String nSetStr=Cutil.jn(",", setStr,cusSetStr);
		placeList.add(bizId);
		Integer retStatus=(MicroMetaDao.getInstance(dbName)).updateObjByCondition(tableName, nCondition, nSetStr,placeList.toArray());
		return retStatus;
	}	

	public Integer updateInfoByBizIdService(String bizId,String tableName,String bizCol,Map requestParamMap,String cusCondition,String cusSetStr) throws Exception{
		return  updateInfoByBizIdServiceInner(bizId, tableName, bizCol, requestParamMap, cusCondition,cusSetStr,null);
	}
	public Integer updateInfoByBizIdService(String bizId,String tableName,String bizCol,Map requestParamMap,String cusCondition,String cusSetStr,String modelName) throws Exception{
		return  updateInfoByBizIdServiceInner(bizId, tableName, bizCol, requestParamMap, cusCondition,cusSetStr,modelName);
	}
	public Integer updateInfoByBizIdService(String bizId,String tableName,String bizCol,Map requestParamMap) throws Exception{
		return  updateInfoByBizIdServiceInner(bizId, tableName, bizCol, requestParamMap, null,null,null);
	}
	public Integer updateInfoByBizIdService(String bizId,String tableName,String bizCol,Map requestParamMap,String modelName) throws Exception{
		return updateInfoByBizIdServiceInner(bizId,tableName,bizCol,requestParamMap,null,null,modelName);
	}
	
	
	/**
	 * ɾ����ݼ�¼
	 * @param requestParamMap �ύ����
	 * @param tableName ����� 
	 * @return
	 * @throws Exception
	 */		
	public Integer delInfoService(Map requestParamMap,String tableName){

		String id=(String) requestParamMap.get("id");
		Integer retStatus=(MicroMetaDao.getInstance(dbName)).delObjById(tableName,id);
		return retStatus;
	}

	public Integer delInfoByIdService(String id,String tableName){

		Integer retStatus=(MicroMetaDao.getInstance(dbName)).delObjById(tableName,id);
		return retStatus;
	}
	
	//���id�б�����ɾ��
	public Integer delInfoByIdListService(List<String> idList,String tableName){
		int status=0;
		for(String id:idList){

			int retStatus=(MicroMetaDao.getInstance(dbName)).delObjById(tableName,id);
			status=status+retStatus;
		}
		return status;
	}	
	
	/**
	 * ���ҵ��idɾ����ݼ�¼
	 * @param requestParamMap �ύ����
	 * @param tableName ����� 
	 * @return
	 * @throws Exception
	 */	
	public Integer delInfoByBizIdService(String bizId,String tableName,String bizCol){
		

		Integer retStatus=(MicroMetaDao.getInstance(dbName)).delObjByBizId(tableName,bizId,bizCol);
		return retStatus;
	}
	//���ҵ��id�б�����ɾ��
	public Integer delInfoByBizIdListService(List<String> bizIdList,String tableName,String bizCol){
		int status=0;
		for(String bizId:bizIdList){

			int retStatus=(MicroMetaDao.getInstance(dbName)).delObjByBizId(tableName,bizId,bizCol);
			status=status+retStatus;
		}
		return status;
	}	
	
	/**
	 * ���id��ȡ��ݼ�¼
	 * @param requestParamMap �ύ����
	 * @param tableName ����� 
	 * @return
	 * @throws Exception
	 */	
	public Map getInfoByIdService(Map requestParamMap,String tableName){

		String id=(String) requestParamMap.get("id");
		Map retMap=(MicroMetaDao.getInstance(dbName)).queryObjJoinById(tableName, id);
		if(retMap==null){
			return null;
		}
		CheckModelTypeUtil.addMetaCols(retMap);
		CheckModelTypeUtil.changeNoStrCols(retMap);
		return retMap;
	}

	public Map getInfoByIdService(String id,String tableName){

		Map retMap=(MicroMetaDao.getInstance(dbName)).queryObjJoinById(tableName, id);
		if(retMap==null){
			return null;
		}
		CheckModelTypeUtil.addMetaCols(retMap);
		CheckModelTypeUtil.changeNoStrCols(retMap);
		return retMap;
	}

	/**
	 * ���ҵ��id��ȡ��ݼ�¼
	 * @param bizId ҵ��id
	 * @param tableName ����� 
	 * @param bizCol ҵ�������
	 * @return
	 * @throws Exception
	 */	
	public Map getInfoByBizIdService(String bizId,String tableName,String bizCol){
		
		Map retMap=(MicroMetaDao.getInstance(dbName)).queryObjJoinByBizId(tableName, bizId,bizCol);
		if(retMap==null){
			return null;
		}
		CheckModelTypeUtil.addMetaCols(retMap);
		CheckModelTypeUtil.changeNoStrCols(retMap);
		return retMap;
	}
		

	/**
	 * ��ȡ�Ƿ�ҳ��ݼ�¼
	 * @param bizId ҵ��id
	 * @param tableName ����� 
	 * @param bizCol ҵ�������
	 * @return
	 * @throws Exception
	 */	
	private List getInfoListAllServiceInnerEx(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect,String modelName,int limit,List cusPlaceList) throws Exception{
		

		String sort=(String) sortMap.get("sort");
		String order=(String) sortMap.get("order");
		String cusSort=(String) sortMap.get("cusSort");
		
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		String id=(String) requestParamMap.get("id");

		String where="";
		
		if(cusWhere !=null && !"".equals(cusWhere)){
			where="where "+cusWhere;
		}
		
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		
		String whereCols="";		
	
		
		Map whereColsMap=requestParamMap;
		if(whereColsMap!=null && !whereColsMap.isEmpty()){
			StringBuilder sb=new StringBuilder("");
			Set<String> keySet=whereColsMap.keySet();
			int i=0;
			for(String key:keySet){
				if(!key.contains(".")){
					continue;
				}
				if(key.contains("dbcol_")){
					continue;
				}
				String colName=key;
				String realColName=colName.replace(".", CheckModelTypeUtil.DUMMY_SPLIT);
				String colsStr=colName+" as "+realColName;
				if(i==0){
					sb.append(colsStr);	
				}else{
					sb.append(",").append(colsStr);
				}
				i++;
			}
			whereCols=sb.toString();
		}		
		
		Map modelEntryMap=getModelEntryMap(requestParamMap,tableName,modelName,dbName,whereCols);
		List placeList=new ArrayList();
		String whereInStr=createWhereInStr(requestParamMap,modelEntryMap,placeList);
		if(cusPlaceList==null){
			cusPlaceList=new ArrayList();
		}
		List realPlaceList=new ArrayList();
		realPlaceList.addAll(placeList);
		realPlaceList.addAll(cusPlaceList);
		if(whereInStr!=null && !"".equals(whereInStr)){
			if(!where.equals("")){
				where=where+" and "+whereInStr;
			}else{
				where="where "+whereInStr;
			}
		}
	
		String select="";
		if(cusSelect!=null && !"".equals(cusSelect)){
			select="select "+cusSelect+" from "+tableName+" "+where;
		}else{
			select="select * from "+tableName+" "+where;
		}
		//String orderSql="order by ";
		String orderSql="";
		if(cusSort!=null && !"".equals(cusSort)){
			orderSql="order by "+cusSort;
		}else if(sort!=null && !sort.equals("")){
			orderSql="order by "+sort+" "+order;
		}
/*		String orderSql="";
		if(sort!=null && !sort.equals("")){
			orderSql="order by "+sort+" "+order;
		}*/
/*		else{
			orderSql=orderSql+"id desc";
		}*/
		String sql=select+" "+orderSql;
		if(limit>=0){
			sql=sql+" limit "+limit;
		}

		
		List infoList=(MicroMetaDao.getInstance(dbName)).queryObjJoinByCondition(sql,realPlaceList.toArray());
		if(infoList==null){
			return null;
		}
		CheckModelTypeUtil.addMetaCols(infoList);
		//CheckModelTypeUtil.changeDateCols(infoList);
		CheckModelTypeUtil.changeNoStrCols(infoList);
		return infoList;
	}

	//sql
	public List getInfoListAllServiceBySql(String sql) throws Exception{
		return getInfoListAllServiceInnerExBySql(sql, null);
	}
	public List getInfoListAllServiceBySql(String sql,int limit) throws Exception{
		String realSql=sql+" limit "+limit;
		return getInfoListAllServiceInnerExBySql(realSql, null);
	}	
	public List getInfoListAllServiceBySql(String sql,List placeList) throws Exception{
		return getInfoListAllServiceInnerExBySql(sql, placeList);
	}
	public List getInfoListAllServiceBySql(String sql,List placeList,int limit) throws Exception{
		String realSql=sql+" limit "+limit;
		return getInfoListAllServiceInnerExBySql(realSql, placeList);
	}
	
	private List getInfoListAllServiceInnerExBySql(String sql,List placeList) throws Exception{
		if(placeList==null){
			placeList=new ArrayList();
		}
		List infoList=(MicroMetaDao.getInstance(dbName)).queryObjJoinByCondition(sql,placeList.toArray());
		if(infoList==null){
			return null;
		}
		CheckModelTypeUtil.addMetaCols(infoList);
		CheckModelTypeUtil.changeNoStrCols(infoList);
		return infoList;
	}
	
	
	//inner
	public List getInfoListAllServiceInner(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect,String modelName,int limit) throws Exception{
		return getInfoListAllServiceInnerEx(requestParamMap,tableName,sortMap,cusWhere,cusSelect,modelName,limit,null);
	}
	public List getInfoListAllServiceInner(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect,String modelName,int limit,List placeList) throws Exception{
		return getInfoListAllServiceInnerEx(requestParamMap,tableName,sortMap,cusWhere,cusSelect,modelName,limit,placeList);
	}
	
	
	
	//
	public List getInfoListAllServiceInner(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect,String modelName) throws Exception{
		return getInfoListAllServiceInner(requestParamMap,tableName,sortMap,cusWhere,cusSelect,modelName,-1);
	}	
	
	public List getInfoListAllService(Map requestParamMap,String tableName,Map sortMap) throws Exception{
		
		return getInfoListAllServiceInner(requestParamMap,tableName,sortMap,"","","");
	}
	public List getInfoListAllService(Map requestParamMap,String tableName,Map sortMap,String cusWhere) throws Exception{
		
		return getInfoListAllServiceInner(requestParamMap,tableName,sortMap,cusWhere,"","");
	}
	public List getInfoListAllService(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect) throws Exception{

		return getInfoListAllServiceInner(requestParamMap,tableName,sortMap,cusWhere,cusSelect,"");
	}

	//placeList
	public List getInfoListAllServiceInner(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect,String modelName,List placeList) throws Exception{
		return getInfoListAllServiceInner(requestParamMap,tableName,sortMap,cusWhere,cusSelect,modelName,-1,placeList);
	}	
	
	public List getInfoListAllService(Map requestParamMap,String tableName,Map sortMap,List placeList) throws Exception{
		
		return getInfoListAllServiceInner(requestParamMap,tableName,sortMap,"","","",placeList);
	}
	public List getInfoListAllService(Map requestParamMap,String tableName,Map sortMap,String cusWhere,List placeList) throws Exception{
		
		return getInfoListAllServiceInner(requestParamMap,tableName,sortMap,cusWhere,"","",placeList);
	}
	public List getInfoListAllService(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect,List placeList) throws Exception{

		return getInfoListAllServiceInner(requestParamMap,tableName,sortMap,cusWhere,cusSelect,"",placeList);
	}	
	
	//single
	public Map getSingleInfoService(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect,String modelName) throws Exception{
		List retList= getInfoListAllServiceInner(requestParamMap,tableName,sortMap,cusWhere,cusSelect,modelName,1);
		if(retList==null || retList.size()<=0){
			return null;
		}
		return (Map) retList.get(0);
	}

	public Map getSingleInfoService(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect,String modelName,List placeList) throws Exception{
		List retList= getInfoListAllServiceInner(requestParamMap,tableName,sortMap,cusWhere,cusSelect,modelName,1,placeList);
		if(retList==null || retList.size()<=0){
			return null;
		}
		return (Map) retList.get(0);
	}
	//sql
	public Map getSingleInfoService(String sql) throws Exception{
		
		String realSql=sql+" limit 1";
		List retList= getInfoListAllServiceInnerExBySql(realSql, null);
		if(retList==null || retList.size()<=0){
			return null;
		}
		return (Map) retList.get(0);
	}
	public Map getSingleInfoService(String sql,List placeList) throws Exception{
		String realSql=sql+" limit 1";
		List retList= getInfoListAllServiceInnerExBySql(realSql, placeList);
		if(retList==null || retList.size()<=0){
			return null;
		}
		return (Map) retList.get(0);
	}
	
	@Deprecated
	public String sqlTemplateService(String template,Map paramMap,List placeList){
		return MicroServiceTemplateUtil.sqlTemplateService(template, paramMap, placeList);
	}
	@Deprecated
	public String sqlTemplateService(String template,Map paramMap){
		return MicroServiceTemplateUtil.sqlTemplateService(template,paramMap,new ArrayList());
	}
	
	public Object execGroovyRetObjByDbTran(String groovyName, String methodName,
			Object... paramArray) throws Exception{
/*		MicroMetaDao microDao=MicroMetaDao.getInstance(dbName);
		DataSource dataSource=microDao.getMicroDataSource();
		PlatformTransactionManager  transactionManager=new DataSourceTransactionManager(dataSource);*/
		PlatformTransactionManager  transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
	    DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	    TransactionStatus status=transactionManager.getTransaction(def);
	    try
	    {
	    	return GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray);
	    }
	    catch(Exception ex)
	    {
	    	transactionManager.rollback(status);
	        throw ex;
	    }
	    finally
	    {
	         transactionManager.commit(status);
	    }		
		
	}
	
	public Integer getSeqByMysql(String seqKey){

		PlatformTransactionManager  transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
	    DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	    TransactionStatus status=transactionManager.getTransaction(def);
	    try
	    {
			String sql="select get_micro_seq('"+seqKey+"') as seq";
			List retList=(MicroMetaDao.getInstance(dbName)).queryObjJoinByCondition(sql);
			if(retList==null){
				transactionManager.commit(status);
				return null;
			}
			Map retMap=(Map) retList.get(0);
			Integer seq=(Integer) retMap.get("seq");
			transactionManager.commit(status);
			return seq;	    	
	    }
	    catch(Exception ex)
	    {
	    	transactionManager.rollback(status);
	        throw new RuntimeException("getseq error",ex);
	    }


	}

	public Integer saveOrUpdateInfoByIdService(String id,String tableName,Map requestParamMap,String cusSetStr4Update,String cusCol4Insert,String cusValue4Insert) throws Exception{
		return saveOrUpdateInfoByIdServiceInner(id,tableName,requestParamMap,cusSetStr4Update,cusCol4Insert,cusValue4Insert);
	}
	public Integer saveOrUpdateInfoByIdService(String id,String tableName,Map requestParamMap) throws Exception{
		return saveOrUpdateInfoByIdServiceInner(id,tableName,requestParamMap,null,null,null);
	}	
	
	private Integer saveOrUpdateInfoByIdServiceInner(String id,String tableName,Map requestParamMap,String cusSetStr4Update,String cusCol4Insert,String cusValue4Insert) throws Exception{
		requestParamMap.put("id", id);
		Map retMap=getInfoByIdService(id,tableName);
		Integer status=0;
		if(retMap==null){
			status=createInfoServiceInner(requestParamMap,tableName,cusCol4Insert,cusValue4Insert,null);
		}else{
			status=updateInfoServiceInner(id,requestParamMap,tableName,null,cusSetStr4Update,null);
		}
		return status;
	}

	public Integer saveOrUpdateInfoByBizIdService(String bizId,String tableName,String bizCol,Map requestParamMap) throws Exception{
		return saveOrUpdateInfoByBizIdServiceInner( bizId, tableName, bizCol, requestParamMap, null, null, null) ;
	}
	public Integer saveOrUpdateInfoByBizIdService(String bizId,String tableName,String bizCol,Map requestParamMap,String cusSetStr4Update,String cusCol4Insert,String cusValue4Insert) throws Exception{
		return saveOrUpdateInfoByBizIdServiceInner( bizId, tableName, bizCol, requestParamMap, cusSetStr4Update, cusCol4Insert, cusValue4Insert) ;
	}

	private Integer saveOrUpdateInfoByBizIdServiceInner(String bizId,String tableName,String bizCol,Map requestParamMap,String cusSetStr4Update,String cusCol4Insert,String cusValue4Insert) throws Exception{

		Map retMap=getInfoByBizIdService(bizId,tableName,bizCol);
		Integer status=0;
		if(retMap==null){
			status=createInfoServiceInner(requestParamMap,tableName,cusCol4Insert,cusValue4Insert,null);
		}else{
			status=updateInfoByBizIdServiceInner(bizId, tableName, bizCol, requestParamMap, null,cusSetStr4Update,null);			
		}
		return status;		
	}

	public List<Map> getDicItemsByDicKey4List(String dicKey) throws Exception{
		String sql="select * from nh_micro_dict_items where meta_type='"+dicKey+"'";
		List infoList=getInfoListAllServiceBySql(sql);
		return infoList;
	}
	
	public Map<String,Map> getDicItemsByDicKey4Map(String dicKey) throws Exception{
		Map retMap=new HashMap();		
		List<Map> infoList=getDicItemsByDicKey4List(dicKey);
		if(infoList!=null){
			for(Map rowMap:infoList){
				String key=(String) rowMap.get("meta_key");
				retMap.put(key, rowMap);
			}
			return retMap;
		}				
		return null;		
	}
	
	public Map<String,String> getDicItemByItemKey(String dicKey,String itemKey) throws Exception{

		if(dicKey==null || itemKey==null){
			return null;
		}
		String sql="select * from nh_micro_dict_items where meta_type='"+dicKey+"' and meta_key='"+itemKey+"'";
		Map retMap=getSingleInfoService(sql);
		return retMap;
	}
	
	
}
