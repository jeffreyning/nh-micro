package com.nh.micro.template;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.nh.micro.db.MicroDbHolder;
import com.nh.micro.db.MicroMetaDao;

public class MicroTranManagerHolder {
	public static Boolean singleFlag=false;
	
	public static Boolean getSingleFlag() {
		return singleFlag;
	}

	public void setSingleFlag(Boolean singleFlag) {
		MicroTranManagerHolder.singleFlag = singleFlag;
	}

	public static Map transManagerHolderMap=new HashMap();

	public static Map getTransManagerHolderMap() {
		return transManagerHolderMap;
	}

	public void setTransManagerHolderMap(Map transManagerHolderMap) {
		MicroTranManagerHolder.transManagerHolderMap = transManagerHolderMap;
	}
	
	public static PlatformTransactionManager getTransactionManager(String dbName){
		//MicroMetaDao microDao=MicroMetaDao.getInstance(dbName);
		//DataSource dataSource=microDao.getMicroDataSource();
		JdbcTemplate retTemplate=(JdbcTemplate) MicroDbHolder.getDbSource(dbName);
		DataSource dataSource=retTemplate.getDataSource();
		PlatformTransactionManager  transactionManager=null;
		if(singleFlag==true){
			transactionManager=(PlatformTransactionManager) transManagerHolderMap.get(dataSource);
			if(transactionManager==null){
				transactionManager=new DataSourceTransactionManager(dataSource);
				transManagerHolderMap.put(dataSource, transactionManager);
			}
		}else{
			transactionManager=new DataSourceTransactionManager(dataSource);
		}
	    return transactionManager;
	}
}
