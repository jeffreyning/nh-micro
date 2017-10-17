package com.project.util;

import java.util.List;
import java.util.Map;



import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.nh.micro.db.MicroDbHolder;
import com.nh.micro.db.MicroMetaDao;

public class MicroDSInit {
    public void init() throws Exception{  
        System.out.println("**************init"); 
        MicroMetaDao dao=MicroMetaDao.getInstance();
        String sql="select * from nh_micro_dbconf";
        List<Map<String,Object>> infoList=dao.queryObjJoinByCondition(sql);
        for(Map<String,Object> row:infoList){
        	String metaKey=(String) row.get("meta_key");
        	String dataSourceClassName=(String) row.get("datasource_classname");
        	String dbUser=(String) row.get("db_user");
        	String dbPassWord=(String) row.get("db_password");
        	if(dbPassWord.startsWith("nhjm-")){
        		String temp=dbPassWord.substring(5);
        		dbPassWord=DESUtil.decrypt(temp);
        	}
        	String dbUrl=(String) row.get("db_url");
        	DruidDataSource ds=new DruidDataSource();
        	ds.setUsername(dbUser);
        	ds.setPassword(dbPassWord);
        	ds.setUrl(dbUrl);
        	ds.setFilters("stat");
        	ds.setMaxActive(10);
        	ds.setInitialSize(1);
        	ds.setMaxWait(60000);
        	ds.setMinIdle(1);
        	ds.setTimeBetweenEvictionRunsMillis(60000);
        	ds.setMinEvictableIdleTimeMillis(60000);
        	ds.setValidationQuery("SELECT 'x'");
        	ds.setTestWhileIdle(true);
        	ds.setTestOnBorrow(false);
        	ds.setTestOnReturn(false);
        	ds.setPoolPreparedStatements(true);
        	ds.setMaxPoolPreparedStatementPerConnectionSize(10);
        	ds.setDriverClassName(dataSourceClassName);
        	ds.init();
        	JdbcTemplate jdbcTemplate=new JdbcTemplate();
        	jdbcTemplate.setDataSource(ds);
        	MicroDbHolder.getDbHolder().put(metaKey, jdbcTemplate);
        }
    }
}
