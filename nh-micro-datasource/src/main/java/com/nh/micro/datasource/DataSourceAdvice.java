package com.nh.micro.datasource;



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * 
 * @author ninghao
 *
 */
public class DataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {
	private static Logger log=Logger.getLogger(DataSourceAdvice.class);
	public String changeName="slave";
	public List<String> readMethodList=new ArrayList();
	
    public List getReadMethodList() {
		return readMethodList;
	}

	public void setReadMethodList(List readMethodList) {
		this.readMethodList = readMethodList;
	}

	public boolean checkName(String methodName){
		for(String filter:readMethodList){
			if(filter.contains("*")){
				String prefix=filter.replace("*", "");
				int index=methodName.indexOf(prefix);
				if(index==0){
					return true;
				}
			}else{
				int index=filter.indexOf(methodName);
				if(index==0){
					return true;
				}				
			}
		}
		return false;
	}

	@Override
    public void before(Method method, Object[] args, Object target) throws Throwable {  

        if(method.isAnnotationPresent(ChangeDataSource.class)){
	        ChangeDataSource annotation=(ChangeDataSource)method.getAnnotation(ChangeDataSource.class);
	        String cname=annotation.name();
	        DataSourceSwitcher.setDataSource(cname);  
	        log.debug("aop by anno methodname="+method.getName()+"change datasource ="+cname);
        }else{
	        String methodName=method.getName();
	        if(checkName(methodName)){ 
	            DataSourceSwitcher.setDataSource(changeName);   
	            log.debug("aop by checklist methodname="+method.getName()+"change datasource ="+changeName);
	        } else  {  
	            DataSourceSwitcher.setDefault();
	        }
        }
    }  
  

    @Override
    public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {  
    	DataSourceSwitcher.clearDataSource();
    }  
  

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {  
        DataSourceSwitcher.clearDataSource();
    }

 
  
}  