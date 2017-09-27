package com.project.util;
import java.util.HashMap;  
import java.util.Iterator;
import java.util.List;  
import java.util.Map;  
  
import net.sf.json.JSONObject;  
  
import org.apache.log4j.Logger;  
import org.springframework.jdbc.core.JdbcTemplate;

public class LoadTask {
    private static final Logger log = Logger.getLogger("");  
    public static Map timerMap=new HashMap();
	public JdbcTemplate jdbcTemplate=null;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	} 
	
	
    /** 
     * @param sendTime 发送时间  
     * @return 
     * @author qgw  
     * 
     */ 
    public  void doJob(){
    	String sql="select * from nh_micro_timer";
    	List<Map<String,Object>> retList= jdbcTemplate.queryForList(sql);
    	Map retMap=new HashMap();
    	for(Map rowMap:retList){
    		String jobName=(String) rowMap.get("meta_key");
    		retMap.put(jobName, rowMap);
    	}
    	Iterator retit=retMap.keySet().iterator();
    	while(retit.hasNext()){
    		String key=(String) retit.next();
    		Map vMap=(Map) retMap.get(key);
    		if(!timerMap.containsKey(key)){
    			String cron=(String) vMap.get("time_cron");
    			String groovyName=(String) vMap.get("groovy_name");
    			String groovyMethod=(String) vMap.get("groovy_method");
    			String paramStr=(String) vMap.get("groovy_param");
    			log.info("add micro timer key="+key+" cron="+cron+ " groovyName="+
    					groovyName+" groovyMethod="+groovyMethod+ " paramStr="+paramStr);
    			boolean status=addTask(key,cron,groovyName,groovyMethod,paramStr);
    			if(status){
    				timerMap.put(key, vMap);
    			}
    		}
    	}
    	
    	Iterator tit=timerMap.keySet().iterator();
    	while(tit.hasNext()){
    		String key=(String)tit.next();
    		Map vMap=(Map) timerMap.get(key);
    		if(!retMap.containsKey(key)){
    			log.info("remove micro timer key="+key);
    			removeTask(key);
    			timerMap.remove(key);
    		}else{
    			String oldCron=(String) vMap.get("time_cron");
    			Map nMap=(Map) retMap.get(key);
    			String newCron=(String) nMap.get("time_cron");
    			if(!oldCron.equalsIgnoreCase(newCron)){
    				log.info("update micro timer key="+key+" cron="+newCron);
    				vMap.put("time_cron", newCron);
    				modifyTask(key,newCron);
    			}
    		}
    	}
    }
    
    public static boolean addTask(String jobName,String cron,String groovyName, String groovyMethod, String paramStr) { 


        try {  
        	if(cron==null || "".equals(cron)){
        		log.info("加载定时器错误：cron is null"); 
        		return false;
        	}
            //删除已有的定时任务  
            MicroQuartzManager.removeJob(jobName);  
            //添加定时任务  
            MicroQuartzManager.addJob(jobName, QuartzJobFactory.class, cron, groovyName, groovyMethod, paramStr);  
            return true;  
        } catch (Exception e) {  
            log.info("加载定时器错误："+e);  
            return false;  
        }  
    }  

    public static boolean removeTask(String jobName) { 


        try {  
            //删除已有的定时任务  
            MicroQuartzManager.removeJob(jobName);  
            return true;  
        } catch (Exception e) {  
            log.info("删除定时器错误："+e);  
            return false;  
        }  
    }      
    
    public static boolean modifyTask(String jobName,String cron){
        try {      	
	    	MicroQuartzManager.modifyJobTime(jobName,cron);
	        return true;  
	    } catch (Exception e) {  
	        log.info("更新定时器错误："+e);  
	        return false;  
	    } 
    }
    
    public static void main(String[] args){
    	LoadTask.addTask("testjob", "0/5 * * * * ?", "test", "exec", "");
    }
}
