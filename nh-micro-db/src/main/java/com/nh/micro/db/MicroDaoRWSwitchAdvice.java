package com.nh.micro.db;



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * 
 * @author ninghao
 *
 */
public class MicroDaoRWSwitchAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {  
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

    public void before(Method method, Object[] args, Object target) throws Throwable {  

        String methodName=method.getName();
        if(checkName(methodName)){ 
        	MicroMetaDao.setIsThreadReadOnly(true);
        }else{
        	MicroMetaDao.setIsThreadReadOnly(false);
        }

    }  
  
    public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {  
    	MicroMetaDao.setIsThreadReadOnly(false);
    }  
  
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {  
    	MicroMetaDao.setIsThreadReadOnly(false);
    }
 
}  