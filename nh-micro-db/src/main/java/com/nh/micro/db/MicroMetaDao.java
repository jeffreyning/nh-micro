package com.nh.micro.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.sql.DataSource;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.KeyHolder;

import com.nh.micro.rule.engine.core.GroovyLoadUtil;

/**
 * 
 * @author ninghao
 *
 */
public class MicroMetaDao {
	public static Boolean orclEndFlag=false;
	public static Boolean getOrclEndFlag() {
		return orclEndFlag;
	}
	public void setOrclEndFlag(Boolean orclEndFlag) {
		MicroMetaDao.orclEndFlag = orclEndFlag;
	}

	private static Logger logger=Logger.getLogger(MicroMetaDao.class);
	public static Map<String,MicroMetaDao> microDaoMap=new HashMap();
	public static Map getMicroDaoMap() {
		return microDaoMap;
	}
	public void setMicroDaoMap(Map microDaoMap) {
		MicroMetaDao.microDaoMap = microDaoMap;
	}
	public static MicroMetaDao getInstance(String dbName){
		if(dbName==null || "".equals(dbName)){
			dbName="default";
		}
		MicroMetaDao instance=(MicroMetaDao) microDaoMap.get(dbName);
		if(instance==null){
			if("default".equals(dbName)){
				instance=new MicroMetaDao();
				microDaoMap.put("default", instance);
			}else{
				instance=new MicroMetaDao(dbName);
				microDaoMap.put(dbName, instance);				
			}
		}
		return instance;
	}
	
	public static MicroMetaDao getInstance(){
		String dbName="default";
		MicroMetaDao instance=(MicroMetaDao) microDaoMap.get(dbName);
		if(instance==null){
			if("default".equals(dbName)){
				instance=new MicroMetaDao();
				microDaoMap.put("default", instance);
			}
		}
		return instance;
	}
	public static MicroMetaDao getInstance(String dbName,String dbType){
		if(dbName==null || "".equals(dbName)){
			dbName="default";
		}
		MicroMetaDao instance=(MicroMetaDao) microDaoMap.get(dbName);
		if(instance==null){
			if("default".equals(dbName)){
				instance=new MicroMetaDao();
				instance.setDbType(dbType);
				microDaoMap.put("default", instance);
			}else{
				instance=new MicroMetaDao(dbName);
				instance.setDbType(dbType);
				microDaoMap.put(dbName, instance);				
			}
		}
		return instance;
	}	
	
	public MicroMetaDao(){}
	public MicroMetaDao(String dbName){
		this.dbName=dbName;
	}
	public MicroMetaDao(String dbName,String dbType){
		this.dbName=dbName;
		this.dbType=dbType;
	}
	public MicroMetaDao(String dbName,String dbType,Boolean autoOperTime){
		this.dbName=dbName;
		this.dbType=dbType;
		this.autoOperTime=autoOperTime;
	}
	
	public MicroMetaDao setPropDbName(String dbName){
		this.dbName=dbName;
		return this;
	}
	public MicroMetaDao setPropDbType(String dbType){
		this.dbType=dbType;
		return this;
	}
	public MicroMetaDao setPropAutoOperTime(Boolean autoOperTime){
		this.autoOperTime=autoOperTime;
		return this;
	}

	public String dbName = "default";
	public Boolean autoOperTime=false;

	public Boolean getAutoOperTime() {
		return autoOperTime;
	}

	public void setAutoOperTime(Boolean autoOperTime) {
		this.autoOperTime = autoOperTime;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String dbType="default";
	
	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	public String defaultId="default";
	
	public String getDefaultId() {
		return defaultId;
	}
	public void setDefaultId(String defaultId) {
		this.defaultId = defaultId;
	}
	
	public JdbcTemplate getMicroJdbcTemplate(){
		return getMicroJdbcTemplate(dbName);
		//JdbcTemplate retTemplate=(JdbcTemplate) MicroDbHolder.getDbSource(dbName);
		//if(isReadOnly==true || getIsThreadReadOnly()==true){
		//	retTemplate=(JdbcTemplate) MicroDbHolder.getDbSource(dbName+"_readonly");	
/*			List<String> readOnlyList=MicroDbHolder.getReadOnlyList(dbName);
			int size=readOnlyList.size();
			int index=createRandom(size);
			String name=readOnlyList.get(index);
			retTemplate=(JdbcTemplate) MicroDbHolder.getDbSource(name);		*/		
		//}
		
		//return retTemplate;
	}
	
	public DataSource getMicroDataSource(){
		//JdbcTemplate retTemplate=(JdbcTemplate) MicroDbHolder.getDbSource(dbName);
		JdbcTemplate retTemplate=(JdbcTemplate) getMicroJdbcTemplate(dbName);
		return retTemplate.getDataSource();
	}

	private static int createRandom(int size){
        int max=size;
        int min=0;
        Random random = new Random();
        int randomNum = random.nextInt(max)%(max-min+1) + min;
        return randomNum;
	}
	
	public JdbcTemplate getMicroJdbcTemplate(String name){
		//return (JdbcTemplate) MicroDbHolder.getDbSource(name);
		JdbcTemplate retTemplate=(JdbcTemplate) MicroDbHolder.getDbSource(name);
		if(isReadOnly==true || getIsThreadReadOnly()==true){
			retTemplate=(JdbcTemplate) MicroDbHolder.getDbSource(name+"_readonly");	
		}
		return retTemplate;		
	}
	
	public String calcuDbType(){
		if(dbType!=null && !"default".equals(dbType)){
			return dbType;
		}
		String retType=(String) MicroDbHolder.getDbTypeMap().get(dbName);
		if(retType==null){
			retType="mysql";
		}
		return retType;
	}	
	
	public String calcuIdKey(){
		if(defaultId!=null && !"default".equals(defaultId)){
			return defaultId;
		}
		String retId=null;
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
			retId="id";
		}else{
			retId="ID";
		}

		return retId;
	}	
	
	
	public static ThreadLocal<Boolean> isThreadReadOnly=new ThreadLocal();
	
	public static Boolean getIsThreadReadOnly() {
		if(isThreadReadOnly.get()==null)
		{
			return false;
		}
		return isThreadReadOnly.get();
	}
	public static void setIsThreadReadOnly(Boolean isReadOnly) {
		isThreadReadOnly.set(isReadOnly);
	}
	
	public Boolean isReadOnly=false;

	public Boolean getIsReadOnly() {
		return isReadOnly;
	}
	public void setIsReadOnly(Boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	/*
	 * 锟斤拷锟絠d锟斤拷询锟斤拷准锟斤拷锟絙ean
	 */
	public MicroMetaBean getMetaBeanById(String tableName, String id) {
/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "select * from " + tableName + " where id=?";
		logger.debug(sql);
		logger.debug("["+id+"]");
		Map retMap = jdbcTemplate.queryForMap(sql, id);
		MicroMetaBean metaBean = new MicroMetaBean();
		metaBean.setId((String) retMap.get("id"));
		metaBean.setMeta_content((String) retMap.get("meta_content"));
		metaBean.setMeta_key((String) retMap.get("meta_key"));
		metaBean.setMeta_name((String) retMap.get("meta_name"));
		metaBean.setMeta_type((String) retMap.get("meta_type"));
		metaBean.setRemark((String) retMap.get("remark"));
		metaBean.setCreate_time((Date) retMap.get("create_time"));
		metaBean.setUpdate_time((Date) retMap.get("update_time"));
		return metaBean;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷id删锟斤拷锟阶硷拷锟斤拷bean
	 */
	public int delMetaBeanById(String tableName, String id) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		String sql = "delete from " + tableName + " where id=?";
		String[] paramArray=new String[1];
		paramArray[0]=id;
		logger.debug(sql);
		logger.debug(paramArray);
		Integer retStatus=jdbcTemplate.update(sql,paramArray);
		return retStatus;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷id锟斤拷锟铰憋拷准锟斤拷锟絙ean
	 */
	public int updateMetaBeanById(String tableName, String id,MicroMetaBean microMetaBean) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		String setMetaContent="";
		String setMetaKey="";
		String setMetaName="";
		String setMetaType="";
		String setRemark="";
		String setCreate_time="";
		String setUpdate_time="";
		String setStr="";

		List paramList=new ArrayList();
		if(microMetaBean.getMeta_content()!=null){
			setMetaContent="meta_content=?";
			setStr=setStr+","+setMetaContent;
			paramList.add(microMetaBean.getMeta_content());
		}
		
		if(microMetaBean.getMeta_key()!=null){
			setMetaKey="meta_key=?";
			setStr=setStr+","+setMetaKey;
			paramList.add(microMetaBean.getMeta_key());
		}

		if(microMetaBean.getMeta_name()!=null){
			setMetaName="meta_name=?";
			setStr=setStr+","+setMetaName;
			paramList.add(microMetaBean.getMeta_name());
		}
		
		if(microMetaBean.getMeta_type()!=null){
			setMetaType="meta_type=?";
			setStr=setStr+","+setMetaType;
			paramList.add(microMetaBean.getMeta_type());
		}
		
		if(microMetaBean.getRemark()!=null){
			setRemark="remark=?";
			setStr=setStr+","+setRemark;
			paramList.add(microMetaBean.getRemark());
		}


		final MicroMetaBean insertBean=microMetaBean;
		String timeName=getTimeName();
		String sql = "update " + tableName +" set update_time="+timeName+" "+setStr+ " where id=?";
		paramList.add(id);
		logger.debug(sql);
		logger.debug(paramList.toArray());
		Integer retStatus=jdbcTemplate.update(sql,paramList.toArray());

		return retStatus;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷id锟斤拷锟斤拷锟阶硷拷锟斤拷bean
	 */
	public int insertMetaBeanById(String tableName, MicroMetaBean microMetaBean) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		final MicroMetaBean insertBean=microMetaBean;
		String timeName=getTimeName();
		String sql = "insert into " + tableName +"(id,meta_content,meta_key,meta_name,meta_type,remark,create_time,update_time) values(?,?,?,?,?,?,"+timeName+","+timeName+") ";
		List paramList=new ArrayList();
		paramList.add(insertBean.getId());
		paramList.add(insertBean.getMeta_content());
		paramList.add(insertBean.getMeta_key());
		paramList.add(insertBean.getMeta_name());
		paramList.add(insertBean.getMeta_type());
		paramList.add(insertBean.getRemark());
		logger.debug(sql);
		logger.debug(paramList.toArray());
		Integer retStatus=jdbcTemplate.update(sql,paramList.toArray());

		return retStatus;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷锟斤拷锟斤拷锟窖拷锟阶硷拷锟斤拷bean
	 */
	public List<MicroMetaBean> queryMetaBeanByCondition(String tableName, String condition) {
		List<MicroMetaBean> retBeanList=new ArrayList();
		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "select * from " + tableName + " where "+condition;
		logger.debug(sql);
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql);
		if(retList==null){
			return retBeanList;
		}
		
		for(Map<String,Object> rowMap:retList){
			MicroMetaBean metaBean = new MicroMetaBean();
			metaBean.setId((String) rowMap.get("id"));
			metaBean.setMeta_content((String) rowMap.get("meta_content"));
			metaBean.setMeta_key((String) rowMap.get("meta_key"));
			metaBean.setMeta_name((String) rowMap.get("meta_name"));
			metaBean.setMeta_type((String) rowMap.get("meta_type"));
			metaBean.setRemark((String) rowMap.get("remark"));
			metaBean.setCreate_time((Date) rowMap.get("create_time"));
			metaBean.setUpdate_time((Date) rowMap.get("update_time"));
			retBeanList.add(metaBean);
		}
		return retBeanList;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷锟斤拷锟斤拷锟窖拷锟阶硷拷锟斤拷bean
	 */
	public List<MicroMetaBean> queryMetaBeanByCondition(String tableName, String condition,Object[] paramArray,int[] typeArray) {
		List<MicroMetaBean> retBeanList=new ArrayList();
		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "select * from " + tableName + " where "+condition;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql,paramArray,typeArray);
		if(retList==null){
			return retBeanList;
		}
		
		for(Map<String,Object> rowMap:retList){
			MicroMetaBean metaBean = new MicroMetaBean();
			metaBean.setId((String) rowMap.get("id"));
			metaBean.setMeta_content((String) rowMap.get("meta_content"));
			metaBean.setMeta_key((String) rowMap.get("meta_key"));
			metaBean.setMeta_name((String) rowMap.get("meta_name"));
			metaBean.setMeta_type((String) rowMap.get("meta_type"));
			metaBean.setRemark((String) rowMap.get("remark"));
			metaBean.setCreate_time((Date) rowMap.get("create_time"));
			metaBean.setUpdate_time((Date) rowMap.get("update_time"));
			retBeanList.add(metaBean);
		}
		return retBeanList;
	}	
	

	
	private String getTimeName(){
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
			return "now()";
		}else{
			return "sysdate";
		}
	}
	
	/*
	 * 锟斤拷锟絪ql锟斤拷询
	 */
	public List<Map<String, Object>> queryObjJoinByCondition(String sql) {

		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		logger.debug(sql);
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql);
		return retList;
	}

	public Map<String, Object> querySingleObjJoinByCondition(String sql) {

		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
		//if(dbType!=null && dbType.equals("mysql")){
			sql=sql+" limit 1";
		}else{
			sql="select * from ("+sql+") where rownum=1";
		}	
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		logger.debug(sql);
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql);
		Map retMap=null;
		if(retList.size()>=1){
			retMap=retList.get(0);
		}
		return retMap;
	}

	public List<Map<String, Object>> queryLimitObjJoinByCondition(String sql,int limit) {

		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
		//if(dbType!=null && dbType.equals("mysql")){
			sql=sql+" limit "+limit;
		}else{
			sql="select * from ("+sql+") where rownum <="+limit;
		}	
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		logger.debug(sql);
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql);
		return retList;
	}	
	
	
	
	/*
	 * 锟斤拷锟絠d锟斤拷询
	 */
	public Map<String, Object> queryObjJoinById(String tableName,Object id) {
		String tempIdKey=calcuIdKey();
		String where="where "+tempIdKey+"=?";
		String limitStr="";
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){		
		//if(dbType!=null && dbType.equals("mysql")){
			limitStr="limit 1";
		}else{
			limitStr="and rownum=1";
		}			
		String select="select * from "+tableName+" "+where+" "+limitStr;
		String sql=select+" ";
		Object[] paramArray=new Object[1];
		logger.debug(sql);
		logger.debug(paramArray);
		
		paramArray[0]=id;
		List infoList=queryObjJoinByCondition(sql,paramArray);
		// init by null
		Map retMap=null;
		if(infoList!=null && infoList.size()>0){
			retMap=(Map) infoList.get(0);
		}		
		return retMap;
	}	


	
	/*
	 * 锟斤拷锟揭碉拷锟絠d锟斤拷询
	 */
	public Map<String, Object> queryObjJoinByBizId(String tableName,Object bizId,String condition) {

		String where="where "+condition+"=?";
		String limitStr="";
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){		
		//if(dbType!=null && dbType.equals("mysql")){
			limitStr="limit 1";
		}else{
			limitStr="and rownum=1";
		}		
		

		String select="select * from "+tableName+" "+where+" "+limitStr;
		String sql=select+" ";
		logger.debug(sql);
		Object[] paramArray=new Object[1];
		paramArray[0]=bizId;
		List infoList=queryObjJoinByCondition(sql,paramArray);
		//init by null
		Map retMap=null;
		if(infoList!=null && infoList.size()>0){
			retMap=(Map) infoList.get(0);
		}		
		return retMap;
	}	
	/*
	 * 锟斤拷锟絪ql锟斤拷询
	 */
	public List<Map<String, Object>> queryObjJoinByCondition(String sql,Object[] paramArray,int[] typeArray) {

		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql,paramArray,typeArray);
		return retList;
	}

	
	/*
	 * 锟斤拷锟絪ql锟斤拷询
	 */
	public List<Map<String, Object>> queryObjJoinByCondition(String sql,Object[] paramArray) {

		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql,paramArray);
		return retList;
	}	

	
	public Map<String, Object> querySingleObjJoinByCondition(String sql,Object[] paramArray) {
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
		//if(dbType!=null && dbType.equals("mysql")){
			sql=sql+" limit 1";
		}else{
			sql="select * from ("+sql+") where rownum=1";
		}	
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> infoList = jdbcTemplate.queryForList(sql,paramArray);
		Map retMap=null;
		if(infoList!=null && infoList.size()>0){
			retMap=(Map) infoList.get(0);
		}
		return retMap;
	}	
	
	
	public List<Map<String, Object>> queryLimitObjJoinByCondition(String sql,Object[] paramArray,int limit) {
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
		//if(dbType!=null && dbType.equals("mysql")){
			sql=sql+" limit "+limit;
		}else{
			sql="select * from ("+sql+") where rownum <= "+limit;
		}	
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> infoList = jdbcTemplate.queryForList(sql,paramArray);
		return infoList;
	}		
	/*
	 * 锟斤拷锟絪ql锟斤拷页锟斤拷询
	 */
	public List<Map<String, Object>> queryObjJoinDataByPageCondition(String innerSql, int start,int end) {

		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "";
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){		
		//if(dbType!=null && dbType.equals("mysql")){
			int pageRows=end-start;
			String limit=start+","+pageRows;
			sql=innerSql+ " limit "+limit;
		}else{
			String startLimit=" WHERE NHPAGE_RN >= "+start;
			String endLimit=" WHERE ROWNUM < "+end;
/*			if(orclEndFlag==false){
				endLimit=" WHERE ROWNUM < "+end;
			}*/
			sql="SELECT * FROM ( SELECT NHPAGE_TEMP.*, ROWNUM NHPAGE_RN FROM ("+ innerSql +" ) NHPAGE_TEMP "+endLimit+" ) "+ startLimit;
		}
		logger.debug(sql);
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql);
		return retList;
	}	

	/*
	 * 锟斤拷锟絪ql锟斤拷页锟斤拷询
	 */
	public List<Map<String, Object>> queryObjJoinDataByPageCondition(String innerSql, int start,int end, Object[] paramArray,int[] typeArray) {

		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "";
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){	
		//if(dbType!=null && dbType.equals("mysql")){
			int pageRows=end-start;
			String limit=start+","+pageRows;
			sql=innerSql+ " limit "+limit;
		}else{
			String startLimit=" WHERE NHPAGE_RN >= "+start;
			String endLimit=" WHERE ROWNUM < "+end;
			sql="SELECT * FROM ( SELECT NHPAGE_TEMP.*, ROWNUM NHPAGE_RN FROM ("+ innerSql +" ) NHPAGE_TEMP "+endLimit+" ) "+ startLimit;
		}
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql,paramArray,typeArray);
		return retList;
	}		

	/*
	 * 锟斤拷锟絪ql锟斤拷页锟斤拷询
	 */
	public List<Map<String, Object>> queryObjJoinDataByPageCondition(String innerSql, int start,int end, Object[] paramArray) {

		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "";
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
		//if(dbType!=null && dbType.equals("mysql")){
			int pageRows=end-start;
			String limit=start+","+pageRows;
			sql=innerSql+ " limit "+limit;
		}else{
			String startLimit=" WHERE NHPAGE_RN >= "+start;
			String endLimit=" WHERE ROWNUM < "+end;
			sql="SELECT * FROM ( SELECT NHPAGE_TEMP.*, ROWNUM NHPAGE_RN FROM ("+ innerSql +" ) NHPAGE_TEMP "+endLimit+" ) "+ startLimit;
		}
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql,paramArray);
		return retList;
	}	
	
	/*
	 * 锟斤拷锟絪ql锟斤拷询锟斤拷录锟斤拷
	 */
	public int queryObjJoinCountByCondition(String sql){
		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		logger.debug(sql);
		Integer total=jdbcTemplate.queryForObject(sql,Integer.class);
		return total;
	}

	/*
	 * 锟斤拷锟絪ql锟斤拷询锟斤拷录锟斤拷
	 */
	public int queryObjJoinCountByCondition(String sql,Object[] paramArray){
		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer total=jdbcTemplate.queryForObject(sql,Integer.class,paramArray);
		return total;
	}	
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷询
	 */
	public List<Map<String, Object>> queryObjByCondition(String tableName, String condition,String cols, String orders) {

		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "select "+cols+" from " + tableName + " where "+condition+" order by "+orders;
		logger.debug(sql);
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql);
		return retList;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷询
	 */
	public List<Map<String, Object>> queryObjByCondition(String tableName, String condition,String cols, String orders,Object[] paramArray,int[] typeArray) {

		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "select "+cols+" from " + tableName + " where "+condition+" order by "+orders;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql,paramArray,typeArray);
		return retList;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷询
	 */
	public List<Map<String, Object>> queryObjByCondition(String tableName, String condition,String cols, String orders,Object[] paramArray) {

		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "select "+cols+" from " + tableName + " where "+condition+" order by "+orders;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql,paramArray);
		return retList;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷页锟斤拷询
	 */
	public List<Map<String, Object>> queryObjDataPageByCondition(String tableName, String condition,String cols,String orders, int start,int end ) {
		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "";
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
		//if(dbType!=null && dbType.equals("mysql")){
			int pageRows=end-start;
			String limit=start+","+pageRows;
			sql="select "+cols+" from " + tableName + " where "+condition+" order by "+orders+ " limit "+limit;
		}else{
			String startLimit=" WHERE NHPAGE_RN >= "+start;
			String endLimit=" WHERE ROWNUM < "+end;
			String innerSql="select "+cols+" from " + tableName + " where "+condition+" order by "+orders;
			sql="SELECT * FROM ( SELECT NHPAGE_TEMP.*, ROWNUM NHPAGE_RN FROM ("+ innerSql +" ) NHPAGE_TEMP "+endLimit+" ) "+ startLimit;
		}
		logger.debug(sql);
		
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql);
		return retList;
	}	
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷页锟斤拷询
	 */
	public List<Map<String, Object>> queryObjDataPageByCondition(String tableName, String condition,String cols,String orders, int start,int end,Object[] paramArray,int[] typeArray ) {
		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "";
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
		//if(dbType!=null && dbType.equals("mysql")){
			int pageRows=end-start;
			String limit=start+","+pageRows;
			sql="select "+cols+" from " + tableName + " where "+condition+" order by "+orders+ " limit "+limit;
		}else{
			String startLimit=" WHERE NHPAGE_RN >= "+start;
			String endLimit=" WHERE ROWNUM < "+end;
			String innerSql="select "+cols+" from " + tableName + " where "+condition+" order by "+orders;
			sql="SELECT * FROM ( SELECT NHPAGE_TEMP.*, ROWNUM NHPAGE_RN FROM ("+ innerSql +" ) NHPAGE_TEMP "+endLimit+" ) "+ startLimit;
		}
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql,paramArray,typeArray);
		return retList;
	}	
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷页锟斤拷询
	 */
	public List<Map<String, Object>> queryObjDataPageByCondition(String tableName, String condition,String cols,String orders, int start,int end,Object[] paramArray ) {
		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql = "";
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
		//if(dbType!=null && dbType.equals("mysql")){
			int pageRows=end-start;
			String limit=start+","+pageRows;
			sql="select "+cols+" from " + tableName + " where "+condition+" order by "+orders+ " limit "+limit;
		}else{
			String startLimit=" WHERE NHPAGE_RN >= "+start;
			String endLimit=" WHERE ROWNUM < "+end;
			String innerSql="select "+cols+" from " + tableName + " where "+condition+" order by "+orders;
			sql="SELECT * FROM ( SELECT NHPAGE_TEMP.*, ROWNUM NHPAGE_RN FROM ("+ innerSql +" ) NHPAGE_TEMP "+endLimit+" ) "+ startLimit;
		}
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		List<Map<String, Object>> retList = jdbcTemplate.queryForList(sql,paramArray);
		return retList;
	}		
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷询锟斤拷录锟斤拷
	 */
	public int queryObjCountByCondition(String tableName, String condition){
		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql="";
		sql="select count(1) from " + tableName + " where "+condition;
		logger.debug(sql);
		Integer total=jdbcTemplate.queryForObject(sql,Integer.class);
		return total;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷询锟斤拷录锟斤拷
	 */
	public int queryObjCountByCondition(String tableName, String condition,Object[] paramArray){
		/*		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
		.getDbSource(dbName);*/
		JdbcTemplate jdbcTemplate = getMicroJdbcTemplate();
		String sql="";
		sql="select count(1) from " + tableName + " where "+condition;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer total=jdbcTemplate.queryForObject(sql,Integer.class,paramArray);
		return total;
	}	
	
	/*
	 * 锟斤拷锟斤拷锟揭筹拷锟绞硷拷锟铰嘉伙拷锟�
	 */
	public int calcuStartIndex(int pageNum,int onePageCount){
		int startIndex=0;
		
		String tempType=calcuDbType();
		if(tempType!=null && tempType.equals("mysql")){
		//if(dbType!=null && dbType.equals("mysql")){
			startIndex=pageNum*onePageCount;
		}else{
			startIndex=pageNum*onePageCount+1;
		}
		return startIndex;
	}
	
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷锟斤拷
	 */
	public int updateObjByCondition(String tableName, String condition,String setStr) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		String timeName=getTimeName();
		if(autoOperTime){
			setStr="update_time="+timeName+","+setStr;
		}
		
		String sql = "update " + tableName +" set "+setStr+ " where "+condition;
		logger.debug(sql);
		Integer retStatus=jdbcTemplate.update(sql);
		return retStatus;
	}	
	/*
	 * 锟斤拷锟絪ql锟斤拷锟斤拷
	 */
	public int updateObjByCondition(String sql) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		logger.debug(sql);
		Integer retStatus=jdbcTemplate.update(sql);
		return retStatus;
	}	
	
	public int updateObjByCondition(String sql,Object[] paramArray) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray);
		return retStatus;
	}	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷锟斤拷
	 */
	public int updateObjByCondition(String tableName, String condition,String setStr,Object[] paramArray,int[] typeArray) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		String timeName=getTimeName();
		if(autoOperTime){
			setStr="update_time="+timeName+","+setStr;
		}
		String sql = "update " + tableName +" set "+setStr+ " where "+condition;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray,typeArray);
		return retStatus;
	}		
	
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷锟斤拷
	 */
	public int updateObjByCondition(String tableName, String condition,String setStr,Object[] paramArray) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		String timeName=getTimeName();
		if(autoOperTime){
			setStr="update_time="+timeName+","+setStr;
		}
		String sql = "update " + tableName +" set "+setStr+ " where "+condition;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray);
		return retStatus;
	}		

	public int updateObjById(String tableName, Object id,String setStr,List paramList) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		String timeName=getTimeName();
		if(autoOperTime){
			setStr="update_time="+timeName+","+setStr;
		}
		String tempIdKey=calcuIdKey();
		String sql = "update " + tableName +" set "+setStr+ " where "+tempIdKey+"=?";
		if(paramList==null){
			paramList=new ArrayList();
		}
		paramList.add(id);
		logger.debug(sql);
		logger.debug(paramList.toArray());
		Integer retStatus=jdbcTemplate.update(sql,paramList.toArray());
		return retStatus;
	}	

	public int updateObjByBizId(String tableName, Object bizId,String bizCol,String setStr,List paramList) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		String timeName=getTimeName();
		if(autoOperTime){
			setStr="update_time="+timeName+","+setStr;
		}
		String sql = "update " + tableName +" set "+setStr+ " where "+bizCol+"=?";
		if(paramList==null){
			paramList=new ArrayList();
		}
		paramList.add(bizId);
		logger.debug(sql);
		logger.debug(paramList.toArray());
		Integer retStatus=jdbcTemplate.update(sql,paramList.toArray());
		return retStatus;
	}
	
	public int updateObjById(String tableName, Object id,Map paramMap) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		List paramList=new ArrayList();
		String setStr="";
		String values="";
		Set<String> keySet=paramMap.keySet();
		for(String key:keySet){
			Object val=paramMap.get(key);
			if(setStr.equals("")){
				setStr=key+"=?";
			}else{
				setStr=setStr+","+key+"=?";
			}
	
			paramList.add(val);
		}		
		
		String timeName=getTimeName();
		if(autoOperTime){
			setStr="update_time="+timeName+","+setStr;
		}
		String tempIdKey=calcuIdKey();
		String sql = "update " + tableName +" set "+setStr+ " where "+tempIdKey+"=?";

		paramList.add(id);
		logger.debug(sql);
		logger.debug(paramList.toArray());
		Integer retStatus=jdbcTemplate.update(sql,paramList.toArray());
		return retStatus;
	}
	
	public int updateObjByBizId(String tableName, Object bizId,String bizCol,Map paramMap) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		List paramList=new ArrayList();
		String setStr="";
		String values="";
		Set<String> keySet=paramMap.keySet();
		for(String key:keySet){
			Object val=paramMap.get(key);
			if(setStr.equals("")){
				setStr=key+"=?";
			}else{
				setStr=setStr+","+key+"=?";
			}
	
			paramList.add(val);
		}			
		
		String timeName=getTimeName();
		if(autoOperTime){
			setStr="update_time="+timeName+","+setStr;
		}
		String sql = "update " + tableName +" set "+setStr+ " where "+bizCol+"=?";
		if(paramList==null){
			paramList=new ArrayList();
		}
		paramList.add(bizId);
		logger.debug(sql);
		logger.debug(paramList.toArray());
		Integer retStatus=jdbcTemplate.update(sql,paramList.toArray());
		return retStatus;
	}	
	
	
	/*
	 * 锟斤拷荼锟斤拷锟缴撅拷锟�
	 */
	public int delObjByCondition(String tableName, String condition) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		String sql = "delete from " + tableName + " where "+condition;
		logger.debug(sql);
		Integer retStatus=jdbcTemplate.update(sql);
		return retStatus;
	}	
	
	/*
	 * 锟斤拷荼锟斤拷锟缴撅拷锟�
	 */
	public int delObjByCondition(String tableName, String condition,Object[] paramArray,int[] typeArray) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		String sql = "delete from " + tableName + " where "+condition;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray,typeArray);
		return retStatus;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟缴撅拷锟�
	 */
	public int delObjByCondition(String tableName, String condition,Object[] paramArray) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		String sql = "delete from " + tableName + " where "+condition;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray);
		return retStatus;
	}	
	
	
	public int delObjById(String tableName, Object id) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		String tempIdKey=calcuIdKey();
		String sql = "delete from " + tableName + " where "+tempIdKey+"=?";
		Object[] paramArray=new Object[1];
		paramArray[0]=id;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray);
		return retStatus;
	}		

	public int delObjByBizId(String tableName, Object bizId,String bizCol) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		String sql = "delete from " + tableName + " where "+bizCol+"=?";
		Object[] paramArray=new Object[1];
		paramArray[0]=bizId;
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray);
		return retStatus;
	}
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷锟斤拷
	 */
	public int insertObj(String tableName, String cols,String values) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		String timeName=getTimeName();
		if(autoOperTime){
			cols="create_time,update_time,"+cols;
			values=timeName+","+timeName+","+values;
		}
		String sql = "insert into " + tableName + " ("+cols+") values ("+values+")";
		logger.debug(sql);
		Integer retStatus=jdbcTemplate.update(sql);
		return retStatus;
	}	

	
	/*
	 * 锟斤拷锟絪ql锟斤拷锟斤拷
	 */
	public int insertObj(String sql) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		logger.debug(sql);
		Integer retStatus=jdbcTemplate.update(sql);
		return retStatus;
	}
	public int insertObj(String sql,Object[] paramArray) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray);
		return retStatus;
	}	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷锟斤拷
	 */
	public int insertObj(String tableName, String cols,String values,Object[] paramArray,int[] typeArray) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		String timeName=getTimeName();
		if(autoOperTime){
			cols="create_time,update_time,"+cols;
			values=timeName+","+timeName+","+values;
		}
		String sql = "insert into " + tableName + " ("+cols+") values ("+values+")";
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray,typeArray);
		return retStatus;
	}		
	
	/*
	 * 锟斤拷荼锟斤拷锟斤拷锟斤拷
	 */
	public int insertObj(String tableName, String cols,String values,Object[] paramArray) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		String timeName=getTimeName();
		if(autoOperTime){
			cols="create_time,update_time,"+cols;
			values=timeName+","+timeName+","+values;
		}
		String sql = "insert into " + tableName + " ("+cols+") values ("+values+")";
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));
		Integer retStatus=jdbcTemplate.update(sql,paramArray);
		return retStatus;
	}

	
	public int insertObj(String tableName, String cols,String values,final Object[] paramArray, KeyHolder keyHolder, final String idCol) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);

		String timeName=getTimeName();
		if(autoOperTime){
			cols="create_time,update_time,"+cols;
			values=timeName+","+timeName+","+values;
		}
		final String sql = "insert into " + tableName + " ("+cols+") values ("+values+")";
		logger.debug(sql);
		logger.debug(Arrays.toString(paramArray));

		//Integer retStatus=jdbcTemplate.update(sql,paramArray);
		Integer retStatus=jdbcTemplate.update(new PreparedStatementCreator() { 

			public PreparedStatement createPreparedStatement(Connection con) 
			throws SQLException { 
					String[] keyColNames=new String[1];
					keyColNames[0]=idCol;
					PreparedStatement ps=con.prepareStatement(sql,keyColNames); 
					if(paramArray!=null){
						int size=paramArray.length;
						for(int i=0;i<size;i++){
							ps.setObject(i+1, paramArray[i]);
						}
					}
		
					return ps; 
				} 
			}, keyHolder);		
		
		return retStatus;
	}	
	
	public int insertObj(String tableName, Map paramMap) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		String cols="";
		String values="";
		List paramList=new ArrayList();
		Set<String> keySet=paramMap.keySet();
		for(String key:keySet){
			Object val=paramMap.get(key);
			if(cols.equals("")){
				cols=key;
			}else{
				cols=cols+","+key;
			}
			if(values.equals("")){
				values="?";
			}else{
				values=values+","+"?";
			}			
			paramList.add(val);
		}

		String timeName=getTimeName();
		if(autoOperTime){
			cols="create_time,update_time,"+cols;
			values=timeName+","+timeName+","+values;
		}
		String sql = "insert into " + tableName + " ("+cols+") values ("+values+")";
		logger.debug(sql);
		logger.debug(paramList.toArray());
		Integer retStatus=jdbcTemplate.update(sql,paramList.toArray());
		return retStatus;
	}
	
	
	public int[] updateObjBatch(String[] sql) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		logger.debug(sql);
		int[] retStatus=jdbcTemplate.batchUpdate(sql);
		return retStatus;
	}
	
	public int[] updateObjBatch(String sql,List<Object[]> paramList) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) MicroDbHolder
				.getDbSource(dbName);
		logger.debug(sql);
		logger.debug(paramList.toArray());
		int[] retStatus=jdbcTemplate.batchUpdate(sql,paramList);
		return retStatus;
	}
}
