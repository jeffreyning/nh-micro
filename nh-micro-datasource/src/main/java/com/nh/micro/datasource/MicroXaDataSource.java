package com.nh.micro.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.nh.micro.datasource.delay.MicroConnDelayHandler;
import com.nh.micro.datasource.delay.MicroDelayHandler;
import com.nh.micro.datasource.delay.MicroExpireCache;


/**
 * 
 * @author ninghao
 *
 */
public class MicroXaDataSource implements DataSource {
	private static Logger log=Logger.getLogger(MicroXaDataSource.class);	
	public static ThreadLocal<Map> xidLocal=new ThreadLocal<Map>();
	
	private Map connHolderMap=new HashMap();    
	public Integer delayToRollTime=60;


	private Boolean delayToRollFlag=true;
	private String dataSourceId="default";

	private String dirverClassName = "com.mysql.jdbc.Driver"; 
    private String url = ""; 
    private String username = ""; 
    private String password = ""; 

    private AtomicInteger count=new AtomicInteger(0);

    private static MicroExpireCache microExpireCache=new MicroExpireCache();
	private String validationQuery="select 'x' from dual"; 
    private Integer level=Connection.TRANSACTION_READ_COMMITTED;
    private static ConcurrentLinkedQueue<Connection> pool = new ConcurrentLinkedQueue<Connection>();    
    private Integer maxSize=20;
    private Integer minSize=10;
    
    public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public Integer getMinSize() {
		return this.minSize;
	}

	public void setMinSize(Integer minSize) {
		this.minSize = minSize;
	}

	public MicroXaDataSource() { 
        try { 
            Class.forName(dirverClassName); 
	    } catch (ClassNotFoundException e) { 
	            log.error("not found dirverClass", e); 
	    } 

    }    
    
	public Map getConnHolderMap() {
		return connHolderMap;
	}

	public void setConnHolderMap(Map connHolderMap) {
		this.connHolderMap = connHolderMap;
	}
	
	public Boolean getDelayToRollFlag() {
		return delayToRollFlag;
	}

	public void setDelayToRollFlag(Boolean delayToRollFlag) {
		this.delayToRollFlag = delayToRollFlag;
	}
   
    public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getDirverClassName() {
		return dirverClassName;
	}

	public String getValidationQuery() {
		return validationQuery;
	}


	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}


	public void setDirverClassName(String dirverClassName) {
		this.dirverClassName = dirverClassName;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	

	public Integer getDelayToRollTime() {
		return this.delayToRollTime;
	}

	public void setDelayToRollTime(Integer delayToRollTime) {
		delayToRollTime = delayToRollTime;
	}	
	public void init() throws Exception{
		for(int i=0;i<minSize;i++){
			Connection connection=DriverManager.getConnection(url, username, password); 
			count.incrementAndGet();
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(level);	
			pool.add(connection);
		}
		if(delayToRollFlag){
			MicroConnDelayHandler microDelayHandler=new MicroConnDelayHandler();
			microDelayHandler.dataSourceId=dataSourceId;
			microExpireCache.setMicroDelayHandler(microDelayHandler);
			microExpireCache.init();
		}
	}
	
	
	public static void removeXid(){
		xidLocal.remove();
	}  
	
	public static void setXid(String xGroupId,String xBranchId){
		Map xMap=new HashMap();
		xMap.put("xGroupId", xGroupId);
		xMap.put("xBranchId", xBranchId);
		xidLocal.set(xMap);
	}
	
	public void commit(String xGroupId){
		if(xGroupId==null || "".equals(xGroupId)){
			log.debug("commit skip for xGroupId is null");
			return;
		}
		List branchList=getBranchListByGroupId(xGroupId);	
		if(branchList==null){
			log.debug("commit skip for branchList is null xGroupId="+xGroupId);
			return;
		}	
		int size=branchList.size();
		for(int i=0;i<size;i++){
			String xBranchId=(String) branchList.get(i);
			if(xBranchId==null){
				log.debug("commit skip for xBranchId is null in holder xGroupId="+xGroupId);
				continue;		
			}		
			try {
				log.debug("commit in holder xGroupId="+xGroupId+" xBranchId="+xBranchId);
				MicroPooledConnection conn=getConnByGbId(xGroupId,xBranchId);
				conn.commitReal();
				freeMicroConnection(conn);
				microExpireCache.remove(xGroupId);
			} catch (SQLException e) {
				log.error("commit error",e);
			}
			
		}
		log.debug("after commit remove conn from holder xGroupId="+xGroupId);
		getConnHolderMap().remove(xGroupId);		
	}
	

	
	public void rollback(String xGroupId){
		if(xGroupId==null || "".equals(xGroupId)){
			log.debug("rollback skip for xGroupId is null");
			return;
		}		
		List branchList=getBranchListByGroupId(xGroupId);
		if(branchList==null){
			log.debug("rollback skip for branchList is null xGroupId="+xGroupId);
			return;
		}
		int size=branchList.size();
		for(int i=0;i<size;i++){
			String xBranchId=(String) branchList.get(i);
			if(xBranchId==null){
				log.debug("rollback skip for xBranchId is null in holder xGroupId="+xGroupId);
				continue;		
			}		
			try {
				log.debug("rollback in holder xGroupId="+xGroupId+" xBranchId="+xBranchId);
				MicroPooledConnection conn=getConnByGbId(xGroupId,xBranchId);
				conn.rollbackReal();
				freeMicroConnection(conn);	
				microExpireCache.remove(xGroupId);
			} catch (SQLException e) {
				log.error("rollback error",e);
			}
			
		}
		log.debug("after rollback remove conn from holder xGroupId="+xGroupId);
		getConnHolderMap().remove(xGroupId);
	}	
	

    
    /** 
     * 获取一个数据库连接 
     * 
     * @return 一个数据库连接 
     * @throws SQLException 
     */ 
    public Connection getConnection() throws SQLException { 
    	MicroPooledConnection microConn=null;
		String xGroupId=getXGroupIdByLocal();
		String xBranchId=getXBranchIdByLocal();
    	if(xGroupId==null || "".equals(xGroupId)){
    		log.error("getConnection error xid is null");
    		throw new RuntimeException("getConnection error xid is null");
    	}
    	Connection conn=getConnByGbId(xGroupId,xBranchId);

    	
    	if(conn!=null){
    		log.debug("get conn from holdermap xid="+getStr4XidLocal());
        	if(checkConn(conn)==false){
        		Connection reconn=recreateConn(conn);
        		microConn=new MicroPooledConnection(reconn);
        	}else{
        		microConn=new MicroPooledConnection(conn);
        	}
        	putConnByGbId(xGroupId,xBranchId, microConn);
    		return microConn;
    	}
    	
    	int totalSize=getConnCount();
    	if(totalSize>=maxSize){
    		throw new RuntimeException("getConnection error totalsize > maxsize");
    	}
    	
       // synchronized (pool) { 

            if (pool.isEmpty()==false){
            	conn=pool.poll(); 
            	log.debug("get conn from pool xid="+getStr4XidLocal());
            	if(checkConn(conn)==false){
            		Connection reconn=recreateConn(conn);
            		microConn=new MicroPooledConnection(reconn);
            	}else{
            		microConn=new MicroPooledConnection(conn);
            	}
            	
            }else{
            	log.debug("create conn in pool xid="+getStr4XidLocal());
    			Connection nconn=DriverManager.getConnection(url, username, password); 
    			nconn.setAutoCommit(false);
    			nconn.setTransactionIsolation(level);	
    			microConn=new MicroPooledConnection(nconn);
            }
            putConnByGbId(xGroupId,xBranchId, microConn);
      //  } 
        return microConn;
    } 
    

    
    private Connection recreateConn(Connection conn) throws SQLException {

    	log.debug("recreating conn xid="+getStr4XidLocal());
    	if(conn!=null){
    		try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		conn=null;
    	}
		Connection connection=DriverManager.getConnection(url, username, password); 
		connection.setAutoCommit(false);
		connection.setTransactionIsolation(level);

		return connection;
    }
    
    private boolean checkConn(Connection conn){
    	Statement stat=null;
    	try {
			stat=conn.createStatement();
			stat.execute(validationQuery);
		} catch (SQLException e) {
			log.error(e.toString());
			return false;
		}finally{
			if(stat!=null){
				try {
					stat.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	return true;
    }
    private int getConnCount(){
    	int poolSize=pool.size();
    	int cacheSize=connHolderMap.keySet().size();
    	int totalSize=poolSize+cacheSize;
    	return totalSize;
    }
    /** 
     * 连接归池 
     * 
     * @param conn 
     */ 
    private void freeMicroConnection(MicroPooledConnection microConn) { 


		Connection tempConn=null;
		try {
			tempConn = microConn.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(tempConn!=null){
			int totalSize=getConnCount();
			if(totalSize<=getMinSize()){
				pool.add(tempConn); 
			}
		}
    } 

	private MicroPooledConnection getConnByGbId(String xGroupId,String xBranchId){
		Map subHolderMap=(Map) connHolderMap.get(xGroupId);
		if(subHolderMap==null){
			return null;
		}
		MicroPooledConnection conn=(MicroPooledConnection) subHolderMap.get(xBranchId);
		return conn;
	}
	
	private void putConnByGbId(String xGroupId,String xBranchId,Connection conn){
		Map subHolderMap=getSubHolderMap(xGroupId);
		subHolderMap.put(xBranchId, conn);
		microExpireCache.put(xGroupId, xBranchId, delayToRollTime, TimeUnit.SECONDS);
		
	}
	
	private List getBranchListByGroupId(String xGroupId){
		Map subHolderMap=(Map) connHolderMap.get(xGroupId);
		if(subHolderMap==null){
			return null;
		}
		List retList=new ArrayList();
		Iterator it=subHolderMap.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			retList.add(key);
		}
		return retList;
	}
	
	private String getStr4XidLocal(){
		Map xMap=xidLocal.get();
		if(xMap==null){
			return null;
		}
		String xGroupId=(String) xMap.get("xGroupId");
		String xBranchId=(String) xMap.get("xBranchId");
		String ret=""+xGroupId+","+xBranchId;
		return ret;
	}
	
    private String getXGroupIdByLocal(){
		Map xMap=xidLocal.get();
		if(xMap==null){
			return null;
		}
		String xGroupId=(String) xMap.get("xGroupId");
		return xGroupId;
    }
    
    private String getXBranchIdByLocal(){
		Map xMap=xidLocal.get();
		if(xMap==null){
			return null;
		}
		String xBranchId=(String) xMap.get("xBranchId");
		return xBranchId;   	
    }

	private void removeByGroupId(String xGroupId){
		getConnHolderMap().remove(xGroupId);
	}

	private void removeByGbId(String xGroupId,String xBranchId){
		Map subMap=(Map) getConnHolderMap().get(xGroupId);
		if(subMap==null){
			return;
		}
		subMap.remove(xBranchId);
	}
	
	private Map getSubHolderMap(String xGroupId){
		Map subMap=(Map) getConnHolderMap().get(xGroupId);
		if(subMap==null){
			subMap=new HashMap();
			getConnHolderMap().put(xGroupId,subMap);
		}
		return subMap;
	}
	
    public Connection getConnection(String username, String password) throws SQLException { 
            //return DriverManager.getConnection(url, username, password); 
    	return null;
    } 

    public PrintWriter getLogWriter() throws SQLException { 
            return null; 
    } 

    public void setLogWriter(PrintWriter out) throws SQLException { 

    } 

    public void setLoginTimeout(int seconds) throws SQLException { 

    } 

    public int getLoginTimeout() throws SQLException { 
            return 0; 
    } 

    public <T> T unwrap(Class<T> iface) throws SQLException { 
            return null; 
    } 

    public boolean isWrapperFor(Class<?> iface) throws SQLException { 
            return false; 
    }   	
}
