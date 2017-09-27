package com.project.util;
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  
import net.sf.json.JSONObject;  
  
import org.apache.log4j.Logger;  
import org.quartz.DisallowConcurrentExecution;  
import org.quartz.Job;  
import org.quartz.JobExecutionContext;  
import org.quartz.JobExecutionException;  
import org.springframework.context.ApplicationContext;  

import com.nh.micro.rule.engine.core.GroovyExecUtil;

public class QuartzJobFactory  implements Job {
    private static final Logger log = Logger.getLogger("");  
    @Override  
    public void execute(JobExecutionContext context) throws JobExecutionException {  
          
        log.info("任务运行开始-------- start --------");   
        try {  

            String groovyName =(String) context.getMergedJobDataMap().get("groovyName");  
            String groovyMethod =(String) context.getMergedJobDataMap().get("groovyMethod");
            String paramStr=(String) context.getMergedJobDataMap().get("paramStr");
            GroovyExecUtil.execGroovy(groovyName, groovyMethod, paramStr);
        }catch (Exception e) {  
            log.info("捕获异常==="+e);  
        }  
        log.info("任务运行结束-------- end --------");   
    }  
}
