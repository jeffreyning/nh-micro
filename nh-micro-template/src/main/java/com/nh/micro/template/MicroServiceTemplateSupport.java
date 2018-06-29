package com.nh.micro.template;

import groovy.json.JsonSlurper;

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
import com.nh.micro.db.MicroMetaDao;

import com.nh.micro.db.MicroDbModelEntry;




import com.nh.micro.db.CheckModelTypeUtil;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import java.util.LinkedHashMap;

import javax.sql.DataSource;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.Gson;

/**
 * service��ģ���װ֧��
 * @author ninghao ���
 *
 */
public class MicroServiceTemplateSupport {
	public static final String TYPE_DEL_ID="del_id";
	public static final String TYPE_UPDATE_ID="update_id";
	public static final String TYPE_DEL_BIZID="del_bizid";
	public static final String TYPE_UPDATE_BIZID="update_bizid";	
	public static final String TYPE_INSERT="insert";
	public static final String TYPE_SELECT_ID="insert_id";
	public static final String TYPE_SELECT_ID_LOCK="insert_id_lock";
	public static final String TYPE_SELECT_BIZID="insert_bizid";
	
	public static final String ORCL_DATE_FORMAT="yyyy-mm-dd hh24:mi:ss";
	public MicroServiceTemplateSupport(){
		
	}
	public MicroServiceTemplateSupport(String dbName){
		this.dbName=dbName;
	}
	public MicroServiceTemplateSupport(String dbName,String dbType){
		this.dbName=dbName;
		this.dbType=dbType;
	}	
	public static Map supportHolder=new HashMap();
	
	public static Map getSupportHolder() {
		return supportHolder;
	}

	public void setSupportHolder(Map supportHolder) {
		MicroServiceTemplateSupport.supportHolder = supportHolder;
	}
	
	public void filterParam(String tableName,Map paramMap){
			
	}

	public Integer filterView(String tableName,Map paramMap,String bizId,String bizCol,String type){
		return 0;
	}
	
	public Map filterView4Select(String tableName,Map paramMap,String bizId,String bizCol,String type){
		return null;
	}
	private MicroMetaDao microDao=null;
	public MicroMetaDao getInnerDao(){
		if(microDao!=null){
			return microDao;
		}
		microDao=new MicroMetaDao(dbName,dbType);
		microDao.setDefaultId(defaultId);
		return microDao;
		
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
	
	public static MicroServiceTemplateSupport getInstance(String dbName,String dbType){
		if(dbName==null || "".equals(dbName)){
			dbName="default";
		}
		if(dbType==null || "".equals(dbType)){
			dbType="mysql";
		}		
		MicroServiceTemplateSupport instance=(MicroServiceTemplateSupport) getSupportHolder().get(dbName);
		if(instance==null){
			instance=new MicroServiceTemplateSupport(dbName,dbType);
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
	
	public String dbType="default";
	public String getDbType(){
		return dbType;
	}
	public void setDbType(String dbType){
		this.dbType=dbType;
	}
	
	public String defaultId="default";

	public String getDefaultId() {
		return defaultId;
	}
	public void setDefaultId(String defaultId) {
		this.defaultId = defaultId;
	}
	
	public String calcuDbType(){
		if(dbType!=null && !dbType.equals("default")){
			return dbType;
		}
		MicroMetaDao microDao=getInnerDao();
		String tempDbType=microDao.calcuDbType();
		
		return tempDbType;
	}
	
	public String calcuIdKey(){
		if(defaultId!=null && !"default".equals(defaultId)){
			return defaultId;
		}
		MicroMetaDao microDao=getInnerDao();
		String tempIdKey=microDao.calcuIdKey();
		return tempIdKey;		
	}
	
	/**
	 * ��ȡ���ģ��
	 * @param requestParamMap �ύ�������
	 * @param tableName �����
	 * @param modelName ģ�����
	 * @return
	 * @throws Exception
	 */
	public Map getModelEntryMap(Map requestParamMap,String tableName,String modelName,String dbName) throws Exception{
		return getModelEntryMap( requestParamMap, tableName, modelName, dbName,"");
	}	
	public Map getModelEntryMap(Map requestParamMap,String tableName,String modelName,String dbName,String cusSelect) throws Exception{
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
	public List getColInfoFromMeta(String tableName,String dbName){
		return getColInfoFromMeta(tableName,dbName,"");
	}
	public List getColInfoFromMeta(String tableName,String dbName,String cusSelect){
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
	public String createWhereInStr(Map requestParamMap,Map modelEntryMap){
		if(modelEntryMap==null){
			return null;
		}
		Cobj cobjValues=Cutil.createCobj(" and ");
		String tempDbType=calcuDbType();
		Iterator it=modelEntryMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			MicroDbModelEntry modelEntry=(MicroDbModelEntry) modelEntryMap.get(key);
			Object value= requestParamMap.get(key);
			String whereValue="";
			if(CheckModelTypeUtil.isNumber(modelEntry)){
				whereValue=Cutil.rep("<REPLACE>",(String) value);
			}else if(CheckModelTypeUtil.isDate(modelEntry) ){
				//20171103 for oracle
				String temp="str_to_date('<REPLACE>','%Y-%m-%d %H:%i:%s')";
				if(tempDbType!=null && "oracle".equals(tempDbType)){
					temp="to_date('<REPLACE>','"+ORCL_DATE_FORMAT+"')";
				}
				whereValue=Cutil.rep(temp,(String) value);

			}else{
				whereValue=Cutil.rep("'<REPLACE>'",(String) value);
			}
			if(CheckModelTypeUtil.isRealCol(modelEntry)==false){
				String metaName=modelEntry.getMetaContentId();
				String realKey=CheckModelTypeUtil.getRealColName(key);
				cobjValues.append(Cutil.rep(metaName+"->>'$.<REPLACE>'=", realKey)+whereValue,value!=null);
			}else if(CheckModelTypeUtil.isRealCol(modelEntry)){
				//add 20171115 by ninghao

				if(value instanceof MicroColObj){
					String colInfoStr=((MicroColObj)value).getColInfo();
					List colData=((MicroColObj)value).getColData();
					cobjValues.append(key+" "+colInfoStr);

				}else{
					//end				
				cobjValues.append(Cutil.rep("<REPLACE>=", key)+whereValue,value!=null);
				}
			}
		}

		return cobjValues.getStr();
	}

	public String createWhereInStr(Map requestParamMap,Map modelEntryMap,List placeList){
		if(modelEntryMap==null){
			return null;
		}
		Cobj cobjValues=Cutil.createCobj(" and ");
		Iterator it=modelEntryMap.keySet().iterator();
		String tempDbType=calcuDbType();
		while(it.hasNext()){
			String key=(String) it.next();
			MicroDbModelEntry modelEntry=(MicroDbModelEntry) modelEntryMap.get(key);
			Object value= requestParamMap.get(key);
			String whereValue="";
			if(CheckModelTypeUtil.isNumber(modelEntry)){
				whereValue=Cutil.rep("<REPLACE>",(String) value);

			}else if(CheckModelTypeUtil.isDate(modelEntry) ){
				//for oracle
/*				String temp="str_to_date('<REPLACE>','%Y-%m-%d %H:%i:%s')";
				if(tempDbType!=null && "oracle".equals(tempDbType)){
					temp="to_date('<REPLACE>','"+ORCL_DATE_FORMAT+"')";
				}
				whereValue=Cutil.rep(temp,(String) value);*/
				whereValue=Cutil.rep("<REPLACE>",(String) value);

			}else{
				whereValue=Cutil.rep("<REPLACE>",(String) value);

			}
			
			
			if(CheckModelTypeUtil.isRealCol(modelEntry)==false){
				String metaName=modelEntry.getMetaContentId();
				String realKey=CheckModelTypeUtil.getRealColName(key);
				cobjValues.append(Cutil.rep(metaName+"->>'$.<REPLACE>'=?", realKey),value!=null);
				if(value!=null){
					placeList.add(whereValue);
				}
			}else if(CheckModelTypeUtil.isRealCol(modelEntry)){
				//add 20171115 by ninghao

				if(value instanceof MicroColObj){
					String colInfoStr=((MicroColObj)value).getColInfo();
					List colData=((MicroColObj)value).getColData();
					cobjValues.append(key+" "+colInfoStr);
					if(value!=null){
						placeList.addAll(colData);
					}

				}else{
					//end
					//add 201806 ning
					if(CheckModelTypeUtil.isDate(modelEntry)){
						String temp="str_to_date(?,'%Y-%m-%d %H:%i:%s')";
						if(tempDbType!=null && "oracle".equals(tempDbType)){
							temp="to_date(?,'"+ORCL_DATE_FORMAT+"')";
						}
						cobjValues.append(Cutil.rep("<REPLACE>="+temp, key),value!=null);
						if(value!=null){
							placeList.add(whereValue);
						}	
					}else{//end
						cobjValues.append(Cutil.rep("<REPLACE>=?", key),value!=null);
						if(value!=null){
							placeList.add(whereValue);
						}	
					}
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
	public String createUpdateInStr(Map dbColMap,Map modelEntryMap){
		if(dbColMap==null){
			return null;
		}
		List realValueList=new ArrayList();
		List extValueList=new ArrayList();
		Boolean metaFlag=false;
		Map<String,Cobj> metaFlagMap=new LinkedHashMap();
		Cobj crealValues=Cutil.createCobj();

		String tempDbType=calcuDbType();
		Iterator it=modelEntryMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			MicroDbModelEntry modelEntry=(MicroDbModelEntry) modelEntryMap.get(key);
			

			if(CheckModelTypeUtil.isDynamicCol(modelEntry)){
				String value=(String) dbColMap.get(key);
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
							//for oracle
							String tmpValue="now()";
							if(tempDbType!=null && "oracle".equals(tempDbType)){
								tmpValue="SYSDATE";
							}
							cobjValues.append(Cutil.rep("<REPLACE>",tmpValue),value!=null);
						}else{
							cobjValues.append(Cutil.rep("'<REPLACE>'",value),value!=null);
						}
					}
					
				}else if(realKey.endsWith("_json")){
					cobjValues.append(Cutil.rep("<REPLACE>",value),value!=null && !"".equals(value));
				}else{
					cobjValues.append(Cutil.rep("'<REPLACE>'",value),value!=null);
				}
				if(value!=null){
					extValueList.add(value);
				}
			}else if(CheckModelTypeUtil.isRealCol(modelEntry)){
				
				//add 20170830 by ninghao
				Object vobj=dbColMap.get(key);
				if(vobj instanceof MicroColObj){
					String colInfoStr=((MicroColObj)vobj).getColInfo();
					crealValues.append(key+"="+colInfoStr);
					continue;

				}
				//end
				
				
				String value=(String) dbColMap.get(key);
				String whereValue="";
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					whereValue=Cutil.rep("<REPLACE>",value);
				}else if(CheckModelTypeUtil.isDate(modelEntry)){
					if(value!=null ){
						if(value.toLowerCase().equals("now()")){
							//for oracle
							String tmpValue="now()";
							if(tempDbType!=null && "oracle".equals(tempDbType)){
								tmpValue="SYSDATE";
							}							
							whereValue=tmpValue;
						}else{
							//for oracle
							String temp="str_to_date('<REPLACE>','%Y-%m-%d %H:%i:%s')";
							if(tempDbType!=null && "oracle".equals(tempDbType)){
								temp="to_date('<REPLACE>','"+ORCL_DATE_FORMAT+"')";
							}
							whereValue=Cutil.rep(temp,value);	
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
	public String createUpdateInStr(Map dbColMap,Map modelEntryMap,List placeList){
		if(dbColMap==null){
			return null;
		}
		List realValueList=new ArrayList();
		List extValueList=new ArrayList();
		Boolean metaFlag=false;
		Map<String,Cobj> metaFlagMap=new LinkedHashMap();
		Cobj crealValues=Cutil.createCobj();

		String tempKeyId=calcuIdKey();
		String tempDbType=calcuDbType();
		Iterator it=modelEntryMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			//������idֵ
			if(tempKeyId.equals(key)){
				continue;
			}
			MicroDbModelEntry modelEntry=(MicroDbModelEntry) modelEntryMap.get(key);
			

			if(CheckModelTypeUtil.isDynamicCol(modelEntry)){
				String value=(String) dbColMap.get(key);
				String metaName=modelEntry.getMetaContentId();
				Cobj cobjValues=metaFlagMap.get(metaName);
				if(cobjValues==null){
					cobjValues=Cutil.createCobj();
					metaFlagMap.put(metaName,cobjValues);
				}
				String realKey=CheckModelTypeUtil.getRealColName(key);
				if((value!=null && !value.equals("")) || (value!=null && !realKey.endsWith("_json"))){
					cobjValues.append(Cutil.rep("'$.<REPLACE>'", realKey),value!=null);
					if(CheckModelTypeUtil.isNumber(modelEntry)){
						cobjValues.append(Cutil.rep("<REPLACE>","?"),value!=null);
						
					}else if(CheckModelTypeUtil.isDate(modelEntry) ){
						if(value!=null ){
							if(value.toLowerCase().equals("now()")){
								//for oracle
								String tmpValue="now()";
								if(tempDbType!=null && "oracle".equals(tempDbType)){
									tmpValue="SYSDATE";
								}								
								cobjValues.append(Cutil.rep("<REPLACE>",tmpValue),value!=null);
							}else{
								cobjValues.append(Cutil.rep("<REPLACE>","?"),value!=null);
							}
						}					
	
					}else if(realKey.endsWith("_json")){
						cobjValues.append(Cutil.rep("<REPLACE>","cast(? as json)"),value!=null && !"".equals(value));
						
					}else{
						cobjValues.append(Cutil.rep("<REPLACE>","?"),value!=null);
						
					}
					if(value!=null ){
						if(value.toLowerCase().equals("now()")==false){
						extValueList.add(value);
						}
					}
				}
			}else if(CheckModelTypeUtil.isRealCol(modelEntry)){
				//add 20170830 by ninghao
				Object vobj=dbColMap.get(key);
				if(vobj instanceof MicroColObj){
					String colInfoStr=((MicroColObj)vobj).getColInfo();
					List colDataList=((MicroColObj)vobj).getColData();
					crealValues.append(key+"="+colInfoStr);
					if(colDataList!=null){
						realValueList.addAll(colDataList);
					}
					continue;

				}
				//end
				String value=(String) dbColMap.get(key);
				if(value!=null && "".equals(value)){//add 201805 ning
					if(CheckModelTypeUtil.isDate(modelEntry) || CheckModelTypeUtil.isNumber(modelEntry)){
						realValueList.add(null);
						crealValues.append(key+"=?",value!=null);	
					}else{
						realValueList.add("");
						crealValues.append(key+"=?",value!=null);
					}
				}
				else if(value!=null ){
					if(CheckModelTypeUtil.isDate(modelEntry) ){
						if(value.toLowerCase().equals("now()")){
							//for oracle
							String tmpValue="now()";
							if(tempDbType!=null && "oracle".equals(tempDbType)){
								tmpValue="SYSDATE";
							}
							crealValues.append(Cutil.rep("<REPLACE>=", key)+tmpValue,value!=null);
						}else{
							realValueList.add(value);
							//add for oracle
							String temp="=str_to_date(?,'%Y-%m-%d %H:%i:%s')";
							if(tempDbType!=null && "oracle".equals(tempDbType)){
								temp="=to_date(?,'"+ORCL_DATE_FORMAT+"')";
							}
							crealValues.append(key+temp,value!=null);
						}
					}else{
						realValueList.add(value);
						crealValues.append(key+"=?",value!=null);						
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
	public String createInsertBeforeStr4ModelEntry(Map dbColMap,Map<String,MicroDbModelEntry> modelEntryMap){
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
	public String createInsertValueStr4ModelEntry(Map dbColMap,Map<String,MicroDbModelEntry> modelEntryMap){
		if(dbColMap==null){
			return null;
		}
		Cobj crealValues=Cutil.createCobj();
		Map metaFlagMap=new TreeMap();
		
		String tempDbType=calcuDbType();
		Iterator it=dbColMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			
			
			MicroDbModelEntry modelEntry=modelEntryMap.get(key);
			if(modelEntry==null){
				continue;
			}
			
			if(CheckModelTypeUtil.isRealCol(modelEntry)==false){

				String value=(String) dbColMap.get(key);
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
				//add 20170830 by ninghao
				Object vobj=dbColMap.get(key);
				if(vobj instanceof MicroColObj){
					String colInfoStr=((MicroColObj)vobj).getColInfo();
					crealValues.append(key+"="+colInfoStr);
					continue;
				}
				//end				
				String value=(String) dbColMap.get(key);
				String whereValue="";
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					whereValue=Cutil.rep("<REPLACE>",value);
				}else if(CheckModelTypeUtil.isDate(modelEntry)){
					//add for oracle
					String temp="str_to_date('<REPLACE>','%Y-%m-%d %H:%i:%s')";
					if(tempDbType!=null && "oracle".equals(tempDbType)){
						temp="to_date('<REPLACE>','"+ORCL_DATE_FORMAT+"')";
					}
					whereValue=Cutil.rep(temp,value);
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

	public String createInsertValueStr4ModelEntry(Map dbColMap,Map<String,MicroDbModelEntry> modelEntryMap,List placeList){
		if(dbColMap==null){
			return null;
		}
		Cobj crealValues=Cutil.createCobj();
		Map metaFlagMap=new TreeMap();
		
		String tempDbType=calcuDbType();
		Iterator it=dbColMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			
			
			MicroDbModelEntry modelEntry=modelEntryMap.get(key);
			if(modelEntry==null){
				continue;
			}
			
			if(CheckModelTypeUtil.isRealCol(modelEntry)==false){
				String value=(String) dbColMap.get(key);
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
				//add 20170830 by ninghao
				Object vobj=dbColMap.get(key);
				if(vobj instanceof MicroColObj){
					String colInfoStr=((MicroColObj)vobj).getColInfo();
					List colDataList=((MicroColObj)vobj).getColData();
					crealValues.append(key+"="+colInfoStr);
					if(colDataList!=null){
						placeList.addAll(colDataList);
					}
					continue;

				}
				//end
				String value=(String) dbColMap.get(key);
				String whereValue="";
				if(CheckModelTypeUtil.isNumber(modelEntry)){
					
					whereValue=Cutil.rep("?",value);
				}else if(CheckModelTypeUtil.isDate(modelEntry)){
					
					if(value!=null && "".equals(value)){//add 201805 ning
						whereValue="?";
					}else if(value!=null){
						if(value.toLowerCase().equals("now()")){
							//for oracle
							String tmpValue="now()";
							if(tempDbType!=null && "oracle".equals(tempDbType)){
								tmpValue="SYSDATE";
							}
							whereValue=tmpValue;
						}else{
							//add for oracle
							String temp="str_to_date(?,'%Y-%m-%d %H:%i:%s')";
							if(tempDbType!=null && "oracle".equals(tempDbType)){
								temp="to_date(?,'"+ORCL_DATE_FORMAT+"')";
							}
							whereValue=Cutil.rep(temp,value);
						}
					}						
					
					//whereValue=Cutil.rep("str_to_date(?,'%Y-%m-%d %H:%i:%s')",value);
				}else{
					whereValue=Cutil.rep("?",value);
				}
				crealValues.append(whereValue,value!=null);
				if(value!=null && "".equals(value)){//add 201805 ning
					if(CheckModelTypeUtil.isDate(modelEntry) || CheckModelTypeUtil.isNumber(modelEntry)){
						placeList.add(null);
					}else{
						placeList.add("");
					}
				}else if(value!=null){
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
					if(skey.endsWith("_json")){
						if(svalue.equals("")){
							continue;
						}
						realv="cast(? as json)";
					}
					if(svalue.toString().equalsIgnoreCase("now()")){
						//for oracle
						String tmpValue="now()";
						if(tempDbType!=null && "oracle".equals(tempDbType)){
							tmpValue="SYSDATE";
						}
						realv=tmpValue;
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

	
	private Map getInfoList4PageService(SupportParamBean supportParamBean) throws Exception{
		Map requestParamMap=supportParamBean.getRequestParamMap();
		
		String tableName=supportParamBean.getTableName();
		Map pageMap=supportParamBean.getSortMap();
		String cusWhere=supportParamBean.getCusWhere();
		String cusSelect=supportParamBean.getCusSelect();
		String modelName=supportParamBean.getModelName();
		List cusPlaceList=supportParamBean.getCusPlaceList();
		return getInfoList4PageServiceInnerEx(requestParamMap,tableName,pageMap,cusWhere,cusSelect,modelName,cusPlaceList);
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
		
		String tempDbType=calcuDbType();
		
		Integer pageNum=Integer.valueOf(page);
		Integer rowsNum=Integer.valueOf(rows);
		
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		
		String tempKeyId=calcuIdKey();
		String id=(String) requestParamMap.get(tempKeyId);
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
		//realPlaceList.addAll(placeList);
		if(cusPlaceList==null){
			cusPlaceList=new ArrayList();
		}
		realPlaceList.addAll(cusPlaceList);
		
		//add 20170927
		realPlaceList.addAll(placeList);
		String selectCount="select count(1) from "+tableName+" "+where;
		Integer total=getInnerDao().queryObjJoinCountByCondition(selectCount,realPlaceList.toArray());
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
		int startNum=getInnerDao().calcuStartIndex(pageNum-1, rowsNum);
		int endNum=startNum+rowsNum;

		List infoList=getInnerDao().queryObjJoinDataByPageCondition(sql, startNum, endNum,realPlaceList.toArray());
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
	
	
	public Map getInfoList4PageServiceBySql(SupportParamBean supportParamBean) throws Exception{
		String countSql=supportParamBean.getCountSql();
		List countPlaceList=supportParamBean.getCountPlaceList();
		String sql=supportParamBean.getSql();
		List placeList=supportParamBean.getPlaceList();
		Map sortMap=supportParamBean.getSortMap();
		return getInfoList4PageServiceInnerExBySql(countSql,countPlaceList,sql,placeList,sortMap);
	}
	
	private Map getInfoList4PageServiceInnerExBySql(String countSql,List countPlaceList,String sql,List placeList,Map pageMap) throws Exception{

		if(countPlaceList==null){
			countPlaceList=new ArrayList();
		}
		if(placeList==null){
			placeList=new ArrayList();
		}
		String tempDbType=calcuDbType();
		String page=(String) pageMap.get("page");
		String rows=(String) pageMap.get("rows");
		String sort=(String) pageMap.get("sort");
		String order=(String) pageMap.get("order");
		String cusSort=(String) pageMap.get("cusSort");
		
		Integer pageNum=Integer.valueOf(page);
		Integer rowsNum=Integer.valueOf(rows);
		
		Integer total=getInnerDao().queryObjJoinCountByCondition(countSql,countPlaceList.toArray());

		String orderSql="";
		if(cusSort!=null && !"".equals(cusSort)){
			orderSql="order by "+cusSort;
		}else if(sort!=null && !sort.equals("")){
			orderSql="order by "+sort+" "+order;
		}		
		String realSql=sql+" "+orderSql;
		int startNum=getInnerDao().calcuStartIndex(pageNum-1, rowsNum);
		int endNum=startNum+rowsNum;
		//int endNum=rowsNum;
		List infoList=getInnerDao().queryObjJoinDataByPageCondition(realSql, startNum, endNum,placeList.toArray());
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
		
		String tempDbType=calcuDbType();

		String orderSql="";
		if(cusSort!=null && !"".equals(cusSort)){
			orderSql="order by "+cusSort;
		}else if(sort!=null && !sort.equals("")){
			orderSql="order by "+sort+" "+order;
		}		
		sql="select SQL_CALC_FOUND_ROWS "+sql.substring(6) ;
		String realSql=sql+" "+orderSql;
		int startNum=getInnerDao().calcuStartIndex(pageNum-1, rowsNum);
		int endNum=startNum+rowsNum;
		//int endNum=rowsNum;
		List infoList=getInnerDao().queryObjJoinDataByPageCondition(realSql, startNum, endNum,placeList.toArray());
		if(infoList==null){
			infoList=new ArrayList();
		}
		CheckModelTypeUtil.addMetaCols(infoList);
		CheckModelTypeUtil.changeNoStrCols(infoList);	
		
		String countSql="SELECT FOUND_ROWS() as total";
		List tempList=getInnerDao().queryObjJoinByCondition(countSql);
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
	
	public Integer createInfoSerivce(SupportParamBean supportParamBean) throws Exception{
		Map requestParamMap=supportParamBean.getRequestParamMap();
		String tableName=supportParamBean.getTableName();
		String cusCol=supportParamBean.getCusCol();
		String cusValue=supportParamBean.getCusValue();
		String modelName=supportParamBean.getModelName();
		return createInfoServiceInner(requestParamMap, tableName, cusCol, cusValue, modelName);
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
		//add 20170829 ninghao
		Integer filterViewRet=filterView(tableName,requestParamMap,"","",TYPE_INSERT);
		if(filterViewRet!=null && filterViewRet>0){
			return filterViewRet;
		}
		String tempDbType=calcuDbType();
		//add 20170627 ninghao
		filterParam(tableName,requestParamMap);
		
		boolean autoFlag=false;
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		String tempKeyId=calcuIdKey();
		Map modelEntryMap=getModelEntryMap(requestParamMap,tableName,modelName,dbName);
		MicroDbModelEntry idEntry=(MicroDbModelEntry) modelEntryMap.get(tempKeyId);
		
		String id=(String) requestParamMap.get(tempKeyId);
		
		if(id==null || "".equals(id)){
			requestParamMap.put(tempKeyId, null);
			
			if(idEntry!=null && idEntry.colType.equals(String.class)){
				id=UUID.randomUUID().toString();
				requestParamMap.put(tempKeyId, id);	
			}else if(idEntry==null){//add 201805 ning support nonid
				autoFlag=false;
			}else{
				autoFlag=true;
			}
		
		}

		String cols=createInsertBeforeStr4ModelEntry(requestParamMap,modelEntryMap);

		List placeList=new ArrayList();
		String values=createInsertValueStr4ModelEntry(requestParamMap,modelEntryMap,placeList);
		String ncols=Cutil.jn(",", cols,cusCol);
		String nvalues=Cutil.jn(",", values,cusValue);
		Integer retStatus=0;
		if(autoFlag==false){
			retStatus=getInnerDao().insertObj(tableName, ncols, nvalues,placeList.toArray());
		}else{
			KeyHolder keyHolder=new GeneratedKeyHolder(); 
			retStatus=getInnerDao().insertObj(tableName, ncols, nvalues,placeList.toArray(),keyHolder,tempKeyId);
			if(keyHolder!=null){
/*				List keyList=keyHolder.getKeyList();
				if(keyList!=null && keyList.size()>0){
					Map keyMap=(Map) keyList.get(0);
					Object retId=keyMap.get(defaultId);
					if(retId!=null){
						requestParamMap.put(defaultId, retId.toString());
					}
				}*/
				requestParamMap.put(tempKeyId, keyHolder.getKey().toString());
			}
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
		String tempKeyId=calcuIdKey();
		requestParamMap.put(tempKeyId, id);
		return createInfoServiceInner(requestParamMap,tableName,null,null,null);

	}
	public Integer createInfoService(String id,Map requestParamMap,String tableName,String cusCol,String cusValue) throws Exception{
		String tempKeyId=calcuIdKey();
		requestParamMap.put(tempKeyId, id);
		return createInfoServiceInner(requestParamMap,tableName,cusCol,cusValue,null);

	}
	
	
	public Integer updateInfoService(SupportParamBean supportParamBean) throws Exception{
		String id=supportParamBean.getId();
		Map requestParamMap=supportParamBean.getRequestParamMap();
		String tableName=supportParamBean.getTableName();
		String cusCondition=supportParamBean.getCusCondition();
		String cusSetStr=supportParamBean.getCusSetStr();
		String modelName=supportParamBean.getModelName();
		return updateInfoServiceInner(id,requestParamMap,tableName,cusCondition,cusSetStr,modelName);
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
		String tempKeyId=calcuIdKey();
		//add 20170829 ninghao
		Integer filterViewRet=filterView(tableName,requestParamMap,id,tempKeyId,TYPE_UPDATE_ID);
		if(filterViewRet!=null && filterViewRet>0){
			return filterViewRet;
		}
		String tempDbType=calcuDbType();
		//add 20170627 ninghao
		filterParam(tableName,requestParamMap);
		
		//String id=(String) requestParamMap.get(defaultId);
		String condition=tempKeyId+"=?";
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		Map modelEntryMap=getModelEntryMap(requestParamMap,tableName,modelName,dbName);
		
		List placeList=new ArrayList();
		String setStr=createUpdateInStr(requestParamMap,modelEntryMap,placeList);
		String nCondition=cusCondition;
		String nSetStr=setStr;
		
		//add 201806 ninghao
		if(condition!=null && !"".equals(condition)){
			nCondition=Cutil.jn(" and ", condition,cusCondition);
		}
		if(cusSetStr!=null && !"".equals(cusSetStr)){
			nSetStr=Cutil.jn(",", setStr,cusSetStr);
		}		
		
		
		placeList.add(id);
		Integer retStatus=getInnerDao().updateObjByCondition(tableName, nCondition, nSetStr,placeList.toArray());
		return retStatus;
	}	

	//sql
	private Integer updateInfoServiceInnerBySql(String sql,List placeList) throws Exception{
		if(placeList==null){
			placeList=new ArrayList();
		}
		String tempDbType=calcuDbType();
		Integer retStatus=getInnerDao().updateObjByCondition(sql,placeList.toArray());
		return retStatus;
	}
	public Integer updateInfoServiceBySql(String sql,List placeList) throws Exception{
		return updateInfoServiceInnerBySql(sql,placeList);
	}
	
	public Integer updateInfoService(Map requestParamMap,String tableName) throws Exception{
		String tempKeyId=calcuIdKey();
		String id=(String) requestParamMap.get(tempKeyId);
		return updateInfoServiceInner(id,requestParamMap,tableName,null,null,null);
	}

	public Integer updateInfoService(Map requestParamMap,String tableName,String cusCondition,String cusSetStr) throws Exception{
		String tempKeyId=calcuIdKey();
		String id=(String) requestParamMap.get(tempKeyId);
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

	public Integer updateInfoByBizIdSerivce(SupportParamBean supportParamBean) throws Exception{
		String bizId=supportParamBean.getBizId();
		String tableName=supportParamBean.getTableName();
		String bizCol=supportParamBean.getBizCol();
		Map requestParamMap=supportParamBean.getRequestParamMap();
		String cusCondition=supportParamBean.getCusCondition();
		String cusSetStr=supportParamBean.getCusSetStr();
		String modelName=supportParamBean.getModelName();
		return updateInfoByBizIdServiceInner(bizId, tableName, bizCol, requestParamMap, cusCondition, cusSetStr, modelName);
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
		//add 20170829 ninghao
		Integer filterViewRet=filterView(tableName,requestParamMap,bizId,bizCol,TYPE_UPDATE_BIZID);
		if(filterViewRet!=null && filterViewRet>0){
			return filterViewRet;
		}
		//add 20170627 ninghao
		filterParam(tableName,requestParamMap);
		String tempDbType=calcuDbType();
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
		Integer retStatus=getInnerDao().updateObjByCondition(tableName, nCondition, nSetStr,placeList.toArray());
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
		String tempKeyId=calcuIdKey();
		String id=(String) requestParamMap.get(tempKeyId);
		//add 20170829 ninghao
		Integer filterViewRet=filterView(tableName,requestParamMap,id,tempKeyId,TYPE_DEL_ID);
		if(filterViewRet!=null && filterViewRet>0){
			return filterViewRet;
		}
		String tempDbType=calcuDbType();
		Integer retStatus=getInnerDao().delObjByBizId(tableName, id, tempKeyId);
		return retStatus;
	}

	public Integer delInfoByIdService(String id,String tableName){
		String tempKeyId=calcuIdKey();
		//add 20170829 ninghao
		Map requestParamMap=new HashMap();
		requestParamMap.put(tempKeyId, id);
		Integer filterViewRet=filterView(tableName,requestParamMap,id,tempKeyId,TYPE_DEL_ID);
		if(filterViewRet!=null && filterViewRet>0){
			return filterViewRet;
		}
		String tempDbType=calcuDbType();
		Integer retStatus=getInnerDao().delObjByBizId(tableName, id, tempKeyId);
		return retStatus;
	}
	
	//���id�б�����ɾ��
	public Integer delInfoByIdListService(List<String> idList,String tableName){
		int status=0;
		String tempDbType=calcuDbType();
		String tempKeyId=calcuIdKey();
		for(String id:idList){

			int retStatus=getInnerDao().delObjByBizId(tableName, id, tempKeyId);
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
		
		Integer filterViewRet=filterView(tableName,new HashMap(),bizId,bizCol,TYPE_DEL_BIZID);
		if(filterViewRet!=null && filterViewRet>0){
			return filterViewRet;
		}
		
		Integer retStatus=getInnerDao().delObjByBizId(tableName,bizId,bizCol);
		return retStatus;
	}
	//���ҵ��id�б�����ɾ��
	public Integer delInfoByBizIdListService(List<String> bizIdList,String tableName,String bizCol){
		int status=0;
		for(String bizId:bizIdList){

			int retStatus=getInnerDao().delObjByBizId(tableName,bizId,bizCol);
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
		String tempKeyId=calcuIdKey();
		String id=(String) requestParamMap.get(tempKeyId);
		//add 20170831 ninghao
		Map filterViewRet=filterView4Select(tableName,requestParamMap,id,tempKeyId,TYPE_SELECT_ID);
		if(filterViewRet!=null ){
			return filterViewRet;
		}
		
		Map retMap=getInnerDao().queryObjJoinByBizId(tableName, id, tempKeyId);
		if(retMap==null){
			return null;
		}
		CheckModelTypeUtil.addMetaCols(retMap);
		CheckModelTypeUtil.changeNoStrCols(retMap);
		return retMap;
	}

	public Map getInfoByIdService(String id,String tableName){
		String tempKeyId=calcuIdKey();
		//add 20170831 ninghao
		Map filterViewRet=filterView4Select(tableName,new HashMap(),id,tempKeyId,TYPE_SELECT_ID);
		if(filterViewRet!=null ){
			return filterViewRet;
		}
		
		Map retMap=getInnerDao().queryObjJoinByBizId(tableName, id, tempKeyId);
		if(retMap==null){
			return null;
		}
		CheckModelTypeUtil.addMetaCols(retMap);
		CheckModelTypeUtil.changeNoStrCols(retMap);
		return retMap;
	}

	public Map getInfoByIdForLockService(String id,String tableName){
		String tempKeyId=calcuIdKey();
		//add 20170831 ninghao
		Map filterViewRet=filterView4Select(tableName,new HashMap(),id,tempKeyId,TYPE_SELECT_ID_LOCK);
		if(filterViewRet!=null ){
			return filterViewRet;
		}
		
		String sql="select * from "+tableName+" where id=? FOR UPDATE";
		List placeList=new ArrayList();
		placeList.add(id);
		List retList=getInnerDao().queryObjJoinByCondition(sql,placeList.toArray());
		if(retList==null){
			return null;
		}
		Map retMap=(Map) retList.get(0);
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
		//add 20170831 ninghao
		Map filterViewRet=filterView4Select(tableName,new HashMap(),bizId,bizCol,TYPE_SELECT_BIZID);
		if(filterViewRet!=null ){
			return filterViewRet;
		}
		
		Map retMap=getInnerDao().queryObjJoinByBizId(tableName, bizId,bizCol);
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
		String tempKeyId=calcuIdKey();

		String sort=(String) sortMap.get("sort");
		String order=(String) sortMap.get("order");
		String cusSort=(String) sortMap.get("cusSort");
		
		if(modelName==null || "".equals(modelName)){
			modelName=tableName;
		}
		String id=(String) requestParamMap.get(tempKeyId);

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
		List infoList=null;
		String sql=select+" "+orderSql;
		if(limit>=0){

			infoList=getInnerDao().queryLimitObjJoinByCondition(sql,realPlaceList.toArray(),limit);
		}else{
			infoList=getInnerDao().queryObjJoinByCondition(sql,realPlaceList.toArray());			
		}

		

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
		String realSql=sql;
		return getInfoListLimitServiceInnerExBySql(realSql, null,limit);
	}	
	public List getInfoListAllServiceBySql(String sql,List placeList) throws Exception{
		return getInfoListAllServiceInnerExBySql(sql, placeList);
	}
	public List getInfoListAllServiceBySql(String sql,List placeList,int limit) throws Exception{
		String realSql=sql;
		return getInfoListLimitServiceInnerExBySql(realSql, placeList, limit);
	}
	
	private List getInfoListAllServiceInnerExBySql(String sql,List placeList) throws Exception{
		if(placeList==null){
			placeList=new ArrayList();
		}
		List infoList=getInnerDao().queryObjJoinByCondition(sql,placeList.toArray());
		if(infoList==null){
			return null;
		}
		CheckModelTypeUtil.addMetaCols(infoList);
		CheckModelTypeUtil.changeNoStrCols(infoList);
		return infoList;
	}
	private List getInfoListLimitServiceInnerExBySql(String sql,List placeList,int limit) throws Exception{
		if(placeList==null){
			placeList=new ArrayList();
		}
		List infoList=getInnerDao().queryLimitObjJoinByCondition(sql,placeList.toArray(),limit);
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
		
/*		String realSql=sql+" limit 1";
		List retList= getInfoListAllServiceInnerExBySql(realSql, null);
		if(retList==null || retList.size()<=0){
			return null;
		}		
		return (Map) retList.get(0);*/
		//for oracle
		String realSql=sql;
		Map retMap= getInnerDao().querySingleObjJoinByCondition(realSql);
		CheckModelTypeUtil.addMetaCols(retMap);
		CheckModelTypeUtil.changeNoStrCols(retMap);			
		return retMap;

		
	}
	public Map getSingleInfoService(String sql,List placeList) throws Exception{
/*		String realSql=sql+" limit 1";
		List retList= getInfoListAllServiceInnerExBySql(realSql, placeList);
		if(retList==null || retList.size()<=0){
			return null;
		}
		return (Map) retList.get(0);*/
		String realSql=sql;
		Map retMap= getInnerDao().querySingleObjJoinByCondition(realSql,placeList.toArray());
		CheckModelTypeUtil.addMetaCols(retMap);
		CheckModelTypeUtil.changeNoStrCols(retMap);	
		return retMap;		
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
/*		MicroMetaDao microDao=MicroMetaDao.getInstance(dbName,dbType);
		DataSource dataSource=microDao.getMicroDataSource();
		PlatformTransactionManager  transactionManager=new DataSourceTransactionManager(dataSource);*/
		PlatformTransactionManager  transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
	    DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	    def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	    TransactionStatus status=transactionManager.getTransaction(def);
	    try
	    {
	    	Object retObj= GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray);
	    	transactionManager.commit(status);
	    	return retObj;
	    }
	    catch(Exception ex)
	    {
	    	transactionManager.rollback(status);
	        throw ex;
	    }
		
	}

	
	public Object execGroovyRetObjByDbTranNest(String groovyName, String methodName, Integer nestDef,
			Object... paramArray) throws Exception{
/*		MicroMetaDao microDao=MicroMetaDao.getInstance(dbName,dbType);
		DataSource dataSource=microDao.getMicroDataSource();
		PlatformTransactionManager  transactionManager=new DataSourceTransactionManager(dataSource);*/
		PlatformTransactionManager  transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
	    DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	    def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
	    if(nestDef==null){
	    	nestDef=TransactionDefinition.PROPAGATION_REQUIRED;
	    }
	    def.setPropagationBehavior(nestDef);
	    TransactionStatus status=transactionManager.getTransaction(def);
	    try
	    {
	    	Object retObj= GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray);
	    	transactionManager.commit(status);
	    	return retObj;
	    }
	    catch(Exception ex)
	    {
	    	transactionManager.rollback(status);
	        throw ex;
	    }
		
	}	

	public void dbTranNestRollback() throws Exception{
		PlatformTransactionManager  transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
	    DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	    def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	    TransactionStatus status=transactionManager.getTransaction(def);
	    transactionManager.rollback(status);

	}	
	public void dbTranNestRollbackAndThrow() throws Exception{
		PlatformTransactionManager  transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
	    DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	    def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	    TransactionStatus status=transactionManager.getTransaction(def);
	    transactionManager.rollback(status);
	    throw new RuntimeException("dbTranNestRollbackAndThrow");

	}
	public void checkRollbackAndThrow() throws Exception{
		PlatformTransactionManager  transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
		boolean flag=((AbstractPlatformTransactionManager) transactionManager).isFailEarlyOnGlobalRollbackOnly();
		if(flag){
			throw new RuntimeException("dbTranNestRollbackAndThrow");
		}

	}
/*	public void dbTranNestRollbackAndReStart() throws Exception{
		PlatformTransactionManager  transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
	    DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	    def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	    TransactionStatus status=transactionManager.getTransaction(def);
	    transactionManager.rollback(status);
	    ((AbstractPlatformTransactionManager) transactionManager).setFailEarlyOnGlobalRollbackOnly(false);

	}	*/	
	
	public Integer getSeqByMysql(String seqKey){

		PlatformTransactionManager  transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
	    DefaultTransactionDefinition def =new DefaultTransactionDefinition();
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	    TransactionStatus status=transactionManager.getTransaction(def);
	    try
	    {
			String sql="select get_micro_seq('"+seqKey+"') as seq";
			List retList=getInnerDao().queryObjJoinByCondition(sql);
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
		String tempKeyId=calcuIdKey();
		requestParamMap.put(tempKeyId, id);
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
	
	public String getDicItemStrByItemKey(String dicKey,String itemKey) throws Exception{

		if(dicKey==null || itemKey==null){
			return null;
		}
		String sql="select * from nh_micro_dict_items where meta_type='"+dicKey+"' and meta_key='"+itemKey+"'";
		Map retMap=getSingleInfoService(sql);
		if(retMap!=null){
			return (String)retMap.get("meta_name");
		}
		return null;
	}	
	
	public void changeJsonMap(Map infoMap){
		Set<String> keySet=infoMap.keySet();
		for(String key:keySet){
			if(key.endsWith("_json")){
				String val=(String) infoMap.get(key);
				Object obj=new JsonSlurper().parseText(val);
				infoMap.put(key, obj);
			}
		}
	}
	
	//页面获取指定节点数据(data)
	public String getNodeData(String nodePath,String formId,String tableName,String dataColName,String idColName){

		MicroMetaDao microDao=getInnerDao();
		String select=dataColName+"->>'$."+dianNode(nodePath)+"' as dyna_data";
		String sql="select "+select+" from "+tableName+" where "+idColName+"=?";
		Object[] paramArray=new Object[1];
		paramArray[0]=formId;
		Map retMap=microDao.querySingleObjJoinByCondition(sql,paramArray);
		//返回的一定是个map
		if(retMap!=null){
			return (String) retMap.get("dyna_data");
		}
		
		return null;
	}

	public void updateNodeData(String paramData,String nodePath,String formId,String tableName,String dataColName,String idColName){

		Gson gson = new Gson();
		Map paramMap=gson.fromJson(paramData,Map.class);
		List placeList=new ArrayList();
		Set<String> paramSet=paramMap.keySet();
		String setStr="";
		for(String key:paramSet){
			setStr=setStr+",?,?";
			String oneKey="$."+dianNode(nodePath)+"."+key;
			placeList.add(oneKey);
			placeList.add(paramMap.get(key));
		}
		checkAndCreateNodePathService(nodePath,formId,tableName,dataColName,idColName);
		MicroMetaDao microDao=getInnerDao();
		String sql="update nh_micro_dyna_form_list set dyna_content=JSON_SET(dyna_content"+setStr+") where "+idColName+"=?";
		microDao.updateObjByCondition(sql, placeList.toArray());

		return ;
	}
	
	//设置指定位置数据(data)
	public void putNodeData(String paramData,String nodePath,String formId,String tableName,String dataColName,String idColName,String dataType){

		checkAndCreateNodePathService(nodePath,formId,tableName,dataColName,idColName);
		
		String sql="update "+tableName+" set "+dataColName+"=JSON_SET("+dataColName+",?,";
		String filter="$."+dianNode(nodePath);
		List placeList=new ArrayList();
		placeList.add(filter);
		if(dataType==null || "".equals(dataType) || "string".equalsIgnoreCase(dataType)){
			sql=sql+"?) where "+idColName+"=?";
			placeList.add(paramData);
			placeList.add(formId);
		}else if("json".equals(dataType)){
			sql=sql+"convert(?,JSON)) where "+idColName+"=?";
			placeList.add(paramData);
			placeList.add(formId);
		}else{
			sql=sql+paramData+") where "+idColName+"=?";
			placeList.add(formId);
		}
		
		MicroMetaDao microDao=getInnerDao();
		microDao.updateObjByCondition(sql, placeList.toArray());
		return ;
	}

	public void removeNodeData(String nodePath,String formId,String tableName,String dataColName,String idColName){

		checkAndCreateNodePathService(nodePath,formId,tableName,dataColName,idColName);
		MicroMetaDao microDao=getInnerDao();
		String sql="update "+tableName+" set "+dataColName+"=JSON_REMOVE("+dataColName+",?) where "+idColName+"=?";
		Object[] paramArray=new Object[2];
		String filter="$."+dianNode(nodePath);
		paramArray[0]=filter;
		paramArray[1]=formId;
		microDao.updateObjByCondition(sql, paramArray);
		return ;
	}
	
	public void appendNodeData4ListService(String paramData,String nodePath,String formId,String tableName,String dataColName,String idColName,String dataType){
		String checkNodePath=nodePath;
		if(!nodePath.endsWith("]")){
			checkNodePath=nodePath+"[]";
		}
		checkAndCreateNodePathService(checkNodePath,formId,tableName,dataColName,idColName);
		MicroMetaDao microDao=getInnerDao();
		String sql="update "+tableName+" set "+dataColName+"=JSON_ARRAY_APPEND("+dataColName+",?,";
		String filter="$."+dianNode(nodePath);
		List placeList=new ArrayList();
		placeList.add(filter);
		if(dataType==null || "".equals(dataType) || "string".equalsIgnoreCase(dataType)){
			sql=sql+"?) where "+idColName+"=?";
			placeList.add(paramData);
			placeList.add(formId);
		}else if("json".equals(dataType)){
			sql=sql+"convert(?,JSON)) where "+idColName+"=?";
			placeList.add(paramData);
			placeList.add(formId);
		}else{
			sql=sql+paramData+") where "+idColName+"=?";
			placeList.add(formId);
		}
		microDao.updateObjByCondition(sql, placeList.toArray());
		return ;
	}

	public void insertNodeData4ListService(String paramData,String nodePath,String formId,String tableName,String dataColName,String idColName,String dataType){
		if(!nodePath.endsWith("]")){
			nodePath=nodePath+"[]";
		}
		checkAndCreateNodePathService(nodePath,formId,tableName,dataColName,idColName);
		MicroMetaDao microDao=getInnerDao();
		String sql="update "+tableName+" set "+dataColName+"=JSON_ARRAY_INSERT("+dataColName+",?,";
		String filter="$."+dianNode(nodePath);
		List placeList=new ArrayList();
		placeList.add(filter);
		if(dataType==null || "".equals(dataType) || "string".equalsIgnoreCase(dataType)){
			sql=sql+"?) where "+idColName+"=?";
			placeList.add(paramData);
			placeList.add(formId);
		}else if("json".equals(dataType)){
			sql=sql+"convert(?,JSON)) where "+idColName+"=?";
			placeList.add(paramData);
			placeList.add(formId);
		}else{
			sql=sql+"'"+paramData+"') where "+idColName+"=?";
			placeList.add(formId);
		}
		microDao.updateObjByCondition(sql, placeList.toArray());
		return ;
	}
	
	private void checkAndCreateNodePathService(String node_path,String formId,String tableName,String dataColName,String idColName){
		Integer flag=checkNodePathService(node_path,formId,tableName,dataColName,idColName);
		if(flag==1){
			return ;
		}
		String[] pathArray=node_path.split("\\.");
		int size=pathArray.length;
		for(int i=0;i<size;i++){
			String oneNodePath=calNodePathService(pathArray,i);
			Integer oneFlag=checkNodePathService(oneNodePath,formId,tableName,dataColName,idColName);
			if(oneFlag==1){
				continue;
			}
			String parentPath=calNodePathService(pathArray,i-1);
			creatNodePathService(parentPath,pathArray[i],formId,tableName,dataColName,idColName);
		}
	}
	
	private Integer checkNodePathService(String node_path,String formId,String tableName,String dataColName,String idColName){
		node_path=node_path.replace("[]", "");
		String sql="select JSON_CONTAINS_PATH("+dataColName+",'all',?) as data_flag from "+tableName+" where "+idColName+"=?";
		MicroMetaDao microDao=getInnerDao();
		Object[] paramArray=new Object[2];
		String filter="$."+dianNode(node_path);
		paramArray[0]=filter;
		paramArray[1]=formId;
		Map retMap=microDao.querySingleObjJoinByCondition(sql, paramArray);
		Integer retInt=((Long)retMap.get("data_flag")).intValue();
		return retInt;
	}
	
	private String calNodePathService(String[] pathArray,int pathDept){
		String retStr="";
		for(int i=0;i<=pathDept;i++){
			if(retStr.length()==0){
				retStr=pathArray[i];
			}else{
				retStr=retStr+"."+pathArray[i];
			}
		}
		return retStr;
	}
	private void creatNodePathService(String parentPath,String curPath,String formId,String tableName,String dataColName,String idColName){
		boolean listFlag=false;
		if(curPath.indexOf("[")>0){
			listFlag=true;
		}

		if(listFlag==false){
			creatNodePath4MapService(parentPath+"."+curPath,formId,tableName,dataColName,idColName);
			return;
		}
		

		int index=curPath.indexOf("[");
		int endIndex=curPath.indexOf("]");
		String noneIndexStr=curPath.substring(0,index);
		String indexStr=curPath.substring(index+1,endIndex);
		if(indexStr==null ||"".equals(indexStr)){
			indexStr="-1";
		}
		creatNodePath4ListService(parentPath+"."+noneIndexStr,Integer.valueOf(indexStr),formId,tableName,dataColName,idColName);
	}
	
	private void creatNodePath4MapService(String nodePath,String formId,String tableName,String dataColName,String idColName){
		MicroMetaDao microDao=getInnerDao();
		String sql="update "+tableName+" set "+dataColName+"=JSON_SET("+dataColName+",?,convert(null,JSON)) where "+idColName+"=?";

		List paramList=new ArrayList();
		String filter="$."+dianNode(nodePath);
		paramList.add(filter);
		paramList.add(formId);
		microDao.updateObjByCondition(sql, paramList.toArray());
	}

	private void creatNodePath4ListService(String noneIndexPath,int index,String formId,String tableName,String dataColName,String idColName){
		
		Integer flag=checkNodePathService(noneIndexPath,formId,tableName,dataColName,idColName);
		if(flag==0){
			creatNodePath4EmptyListService(noneIndexPath,formId,tableName,dataColName,idColName);
		}
		String nodeType="";
		nodeType=checkNodeTypeService(noneIndexPath,formId,tableName,dataColName,idColName);
		if(!"array".equalsIgnoreCase(nodeType)){
			creatNodePath4EmptyListService(noneIndexPath,formId,tableName,dataColName,idColName);
		}
		if(index<0){
			return ;
		}
		Integer nodeLength=0;
		nodeLength=calNodeLengthService(noneIndexPath,formId,tableName,dataColName,idColName);
		if(index<nodeLength){
			return;
		}
		int size=index-nodeLength;
		for(int i=0;i<=size;i++){
			appendNodePath4EmptyListService(noneIndexPath,formId,tableName,dataColName,idColName);
		}
		
	}
	
	private Integer calNodeLengthService(String nodePath,String formId,String tableName,String dataColName,String idColName){
		String sql="select JSON_LENGTH("+dataColName+",?) as data_length from "+tableName+" where "+idColName+"=?";
		Object[] paramArray=new Object[2];
		String filter="$."+dianNode(nodePath);
		paramArray[0]=filter;
		paramArray[1]=formId;
		Map retMap=getInnerDao().querySingleObjJoinByCondition(sql, paramArray);
		if(retMap==null){
			return null;
		}
		return (Integer) retMap.get("data_length");
	}
	
	private void creatNodePath4EmptyListService(String nodePath,String formId,String tableName,String dataColName,String idColName){
		String sql="update "+tableName+" set "+dataColName+"=JSON_SET("+dataColName+",?,convert(?,JSON)) where "+idColName+"=?";
		Object[] paramArray=new Object[3];
		String filter="$."+dianNode(nodePath);
		paramArray[0]=filter;
		paramArray[1]="[]";
		paramArray[2]=formId;
		getInnerDao().updateObjByCondition(sql, paramArray);
	}
	private void appendNodePath4EmptyListService(String nodePath,String formId,String tableName,String dataColName,String idColName){
		String sql="update "+tableName+" set "+dataColName+"=JSON_ARRAY_APPEND("+dataColName+",?,convert(?,JSON)) where "+idColName+"=?";
		Object[] paramArray=new Object[3];
		String filter="$."+dianNode(nodePath);
		paramArray[0]=filter;
		paramArray[1]="null";
		paramArray[2]=formId;
		getInnerDao().updateObjByCondition(sql, paramArray);
	}
	
	private String checkNodeTypeService(String nodePath,String formId,String tableName,String dataColName,String idColName){
		String sql="select JSON_TYPE(JSON_EXTRACT("+dataColName+",?)) as data_type from "+tableName+" where "+idColName+"=?";
		Object[] paramArray=new Object[2];
		String filter="$."+dianNode(nodePath);
		paramArray[0]=filter;
		paramArray[1]=formId;
		Map retMap=getInnerDao().querySingleObjJoinByCondition(sql, paramArray);
		if(retMap==null){
			return null;
		}
		return (String) retMap.get("data_type");
	}
	private String dianNode(String node){
		if(node==null || "".equals(node)){
			return node;
		}
		if(node.startsWith(".")){
			return node.substring(1);
		}
		return node;
	}	
	
	
	
	public Object execGroovyRetObjByDbReadOnly(String groovyName, String methodName,
			Object... paramArray) throws Exception{
		Boolean temp=MicroMetaDao.getIsThreadReadOnly();
	    try
	    {
	    	MicroMetaDao.setIsThreadReadOnly(true);
	    	return GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray);
	    }
	    finally
	    {
	    	MicroMetaDao.setIsThreadReadOnly(temp);
	    }		
		
	}	
	
	public int[] batchUpdateInfoServiceBySql(String[] sql) throws Exception{

		int[] retStatus=getInnerDao().updateObjBatch(sql);
		return retStatus;
	}
	public int[] batchUpdateInfoServiceBySql(String sql,List<Object[]> paramList) throws Exception{

		int[] retStatus=getInnerDao().updateObjBatch(sql,paramList);
		return retStatus;
	}	
	
	public Integer createInfoServiceBySql(String sql, List placeList, IdHolder idHolder) throws Exception{
		String tempKeyId=calcuIdKey();
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		Integer retStatus=getInnerDao().insertObj(sql,placeList.toArray(),keyHolder,tempKeyId);
		if(idHolder!=null){
			idHolder.setIdVal(keyHolder.getKey());
		}
		return retStatus;
	}	
	
	

	//20180605 ning
	
	public Map getMapByIdService4text(String id, String tableName, String colName){
		Map data=getInfoByIdService(id,tableName);
		if(data==null){
			return null;
		}
		String dataStr=(String) data.get(colName);
		if(dataStr==null || "".equals(dataStr)){
			dataStr="{}";
		}
		Gson gson = new Gson();
		Map dataMap=gson.fromJson(dataStr,Map.class);
		return dataMap;
	}
	
	public int saveMapByIdService4text(String id, String tableName, String colName, Map paramMap) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(paramMap); 
		Map saveMap=new HashMap();
		saveMap.put(colName, jsonString);
		int status=0;
		try {
			status = updateInfoByIdService(id,tableName,saveMap);
		} catch (Exception e) {

		}
		return status;

	}

	//根据fullpath获取指定位置数据(data)
	public static Object getCurDataByFull4text(Map dataMap,String dept_fullpath){
		String[] paths=dept_fullpath.split("/");
		int size=paths.length;
		String dept_id="";
		if(size>0){
			dept_id=paths[size-1];
		}
		String dept_path="";
		int dept_id_size=dept_id.length();
		dept_path=(String) dept_fullpath.subSequence(0, dept_fullpath.length()-dept_id_size);
		Object deptData=null;
		Map parentMap=(Map) getParentObjByPath4text(dataMap,dept_path);
		if(parentMap!=null){
			int haveIndex=dept_id.indexOf("[");
			if(haveIndex<=0){
				deptData=parentMap.get(dept_id);
			}else{
				String real_id=dept_id.substring(0,haveIndex);
				int lastIndex=dept_id.indexOf("]");
				String real_index=dept_id.substring(haveIndex+1,lastIndex);
				Integer index_i=Integer.valueOf(real_index);
				List tempList=(List) parentMap.get(real_id);
				deptData=tempList.get(index_i);
			}
		}
		return deptData;
	}
	
	//根据path获取指定位置数据(data)
	private static Object getParentObjByPath4text(Map dataMap,String dept_path){
		String[] pathNode=dept_path.split("/");
		int size=pathNode.length;
		if(size<0){
			return dataMap;
		}
		Object subObj=null;
		Map rowObj=dataMap;
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
				List tempList=(List) rowObj.get(realKey);
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
			if(i+1<size){
				rowObj=(Map) subObj;
			}
		}
		return subObj;
	}	
	
	//根据fullpath设置指定位置数据(data)
	public static int setCurDataByFull4text(Map dataMap,String dept_fullpath,Object paramObj){
		String[] paths=dept_fullpath.split("/");
		int size=paths.length;
		String dept_id="";
		if(size>0){
			dept_id=paths[size-1];
		}
		String dept_path="";
		int dept_id_size=dept_id.length();
		dept_path=(String) dept_fullpath.subSequence(0, dept_fullpath.length()-dept_id_size);

		Map pMap=(Map) getParentObjByPath4text(dataMap,dept_path);
		int index=getDeptIdIndex(dept_id);
		if(index<0){
			pMap.put(dept_id,paramObj);
		}else{
			String realDeptId=getRealDeptId(dept_id);
			List tempList=(List) pMap.get(realDeptId);
			Map tempMap=(Map) getDataFromList(tempList,index);
			tempList.set(index, paramObj);
		}
		return 1;

	}	
	
	
	//设置指定位置数据(data)
	public static int appendParentMap4text(String dept_fullpath,Map dataMap,Map paramObj){

		String[] paths=dept_fullpath.split("/");
		int size=paths.length;
		String dept_id="";
		if(size>0){
			dept_id=paths[size-1];
		}
		String dept_path="";
		int dept_id_size=dept_id.length();
		dept_path=(String) dept_fullpath.subSequence(0, dept_fullpath.length()-dept_id_size);
		
		Map pMap=(Map) getParentObjByPath4text(dataMap,dept_path);
		List targetList=(List) pMap.get(dept_id);
		if(targetList==null){
			targetList=new ArrayList();
			pMap.put(dept_id, targetList);
		}
		targetList.add(paramObj);
		return 1;
	}
	
	//获取index值时自动填充之前值
	private static Object getDataFromList(List dataList,int index){
		int size=dataList.size();
		while(size<index+1){
			dataList.add(new HashMap());
			size=dataList.size();
		}
		Object retObj=dataList.get(index);
		return retObj;
	}
	
	
	//获取非index的路径
	private static String getRealDeptId(String deptId){
		int start=deptId.indexOf("[");
		String realKey=deptId;
		if(start>0){
			realKey=deptId.substring(0,start);
		}
		return realKey;
	}	
	
	//获取最后的index
	private static int getDeptIdIndex(String deptId){
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
	
	public static int removeParentMap(Map dataMap,String dept_fullpath){
		String[] paths=dept_fullpath.split("/");
		int size=paths.length;
		String dept_id="";
		if(size>0){
			dept_id=paths[size-1];
		}
		String dept_path="";
		int dept_id_size=dept_id.length();
		dept_path=(String) dept_fullpath.subSequence(0, dept_fullpath.length()-dept_id_size);

		Map pMap=(Map) getParentObjByPath4text(dataMap,dept_path);
		int index=getDeptIdIndex(dept_id);
		if(index<0){
			pMap.remove(dept_id);
		}else{
			String realDeptId=getRealDeptId(dept_id);
			List tempList=(List) pMap.get(realDeptId);
			if(index+1<tempList.size()){
				tempList.remove(index);
			}else{
				return 0;
			}
		}

		return 1;
	}	
	
}
