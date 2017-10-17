package com.project.util.xa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosSQLException;
import com.nh.micro.db.MicroMetaDao;
import com.project.util.DESUtil;

public class XADataSourceHolder {

	public static Map holderMap = new HashMap();
	public static AtomikosDataSourceBean getDataSource(String key){
		return (AtomikosDataSourceBean) holderMap.get(key);
	}
    public void init() throws Exception{  
        System.out.println("**************init"); 
        MicroMetaDao dao=MicroMetaDao.getInstance();
        String sql="select * from nh_micro_xadbconf";
        List<Map<String,Object>> infoList=dao.queryObjJoinByCondition(sql);
        for(Map<String,Object> row:infoList){
        	//String metaKey=(String) row.get("meta_key");
        	String uniqueResourceName=(String) row.get("unique_resource_name");
        	String xaDataSourceClassName=(String) row.get("xa_datasource_classname");
        	String dbUser=(String) row.get("db_user");
        	String dbPassWord=(String) row.get("db_password");
        	if(dbPassWord.startsWith("nhjm-")){
        		String temp=dbPassWord.substring(5);
        		dbPassWord=DESUtil.decrypt(temp);
        	}
        	String dbUrl=(String) row.get("db_url");
	        AtomikosDataSourceBean ads=new AtomikosDataSourceBean();
	        ads.setUniqueResourceName(uniqueResourceName);
	        ads.setXaDataSourceClassName(xaDataSourceClassName);
	        ads.setPoolSize(3);
	        ads.setReapTimeout(10);
	        Properties xaProperties=new Properties();
	        xaProperties.setProperty("user", dbUser);
	        xaProperties.setProperty("password", dbPassWord);
	        xaProperties.setProperty("url", dbUrl);
	        ads.setXaProperties(xaProperties);
	        ads.init();
	        holderMap.put(uniqueResourceName, ads);
        }
    }
	public void setHolderMap(Map initMap) {
		holderMap = initMap;
	}
}
