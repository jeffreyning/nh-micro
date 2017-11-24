package com.nh.micro.template;

import java.lang.reflect.Method;


import groovy.lang.GroovyObject;


import com.nh.micro.db.GroovyDbSwitcher;
import com.nh.micro.db.GroovyDataSource;

import com.nh.micro.rule.engine.core.GroovyAopInter;

/**
 * 
 * @author ninghao
 *
 */
public class GroovyDbAopImpl extends GroovyAopInter {

	public static ThreadLocal<Integer> countCall=new ThreadLocal();
	public static void addCountCall(){
		Integer count=countCall.get();
		if(count==null){
			count=new Integer(0);
		}
		count=count+1;
		countCall.set(count);
	}
	
	public static void subAndRemoveCountCall(){
		Integer count=countCall.get();
		if(count==null){
			return;
		}
		count=count-1;
		countCall.set(count);
		if(count<=0){
			GroovyDbSwitcher.removeLocal();
		}
	}
	@Override
	public Object invokeMethod(GroovyObject groovyObject, String GroovyName,
			String methodName, Object... param) {
		Class[] paramTypeArray=null;
		if(param!=null){
			int size=param.length;
			paramTypeArray=new Class[size];
			for(int i=0;i<size;i++){
				paramTypeArray[i]=param[i].getClass();
			}

		}
		Method method=null;
		try {
			method = groovyObject.getClass().getDeclaredMethod(methodName,paramTypeArray);
		} catch (Exception e) {

		}
		
		//change info
		String changeName="";
		String oldName="";		
		boolean changeFlag=false;
		GroovyDataSource changeDb=null;
		if(method!=null){
			changeDb=method.getAnnotation(GroovyDataSource.class);
			if(changeDb!=null){
				changeFlag=true;
				changeName=changeDb.name();
				oldName=changeDb.oldName();				
			}
		}		

		
		//support info
		String supportName="";
		if(groovyObject instanceof MicroServiceTemplateSupport){
			supportName=((MicroServiceTemplateSupport)groovyObject).getDbName();
		}		
		boolean pushFlag=false;
	
		if(oldName==null || "".equals(oldName)){
			oldName=supportName;
		}
		if(changeFlag==true){
		    try
		    {
		    	addCountCall();
		    	if(oldName!=null && !"".equals(oldName)){
		    		GroovyDbSwitcher.pushCurrentDataSource(oldName, changeName);
		    	}
		    	Object retObj= execNextHandler(groovyObject, GroovyName, methodName, param);
		    	if(oldName!=null && !"".equals(oldName)){
		    		GroovyDbSwitcher.popCurrentDataSource(oldName);
		    	}
		    	subAndRemoveCountCall();
		    	return retObj;
		    }
		    catch(Exception ex)
		    {
		    	if(oldName!=null && !"".equals(oldName)){
		    		GroovyDbSwitcher.popCurrentDataSource(oldName);
		    	}
		    	subAndRemoveCountCall();
		        throw new RuntimeException(ex);
		    }			
		}else{
			Object retObj= execNextHandler(groovyObject, GroovyName, methodName, param);
			return retObj;			
		}		
		
		
	}

}
