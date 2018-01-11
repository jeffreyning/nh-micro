package com.nh.micro.template;





import java.lang.reflect.Method;

import com.nh.micro.db.GroovyDataSource;
import com.nh.micro.db.GroovyDbSwitcher;
import com.nh.micro.rule.engine.core.plugin.IMicroProxy;

/**
 * 
 * @author ninghao
 *
 */
public class MicroDbProxy implements IMicroProxy {
	public Object groovyObj=null;
	public Object oldGroovyObj=null;
	
	public Method checkMethod(String methodName,Object[] paramArray, Method[] methods){
		int size=paramArray.length;
		
		Class[] paramTypeArray=new Class[size];
		for(int i=0;i<size;i++){
			paramTypeArray[i]=paramArray[i].getClass();
		}		
		int msize=methods.length;
		for(int i=0;i<msize;i++){
			Method method=methods[i];
			String mname=method.getName();
			if(!methodName.equals(mname)){
				continue;
			}
			Class[] types=method.getParameterTypes();
			if(types.length!=paramTypeArray.length){
				continue;
			}
			boolean flag=true;
			for(int j=0;j<size;j++){
				Class incls=paramTypeArray[j];
				Class cls=types[j];
				if(!cls.isAssignableFrom(incls)){
					flag=false;
				}
			}
			if(flag==true){
				return method;
			}
		}
		return null;
	}	
	
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
	public Object invoke(Object proxy, Method method0, Object[] args)
			throws Throwable {
		String methodName=(String) args[0];
		Object[] paramArray=(Object[]) args[1];		
		

		Method method=null;
		try {
			method = checkMethod(methodName,paramArray,oldGroovyObj.getClass().getDeclaredMethods());
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
		if(proxy instanceof MicroServiceTemplateSupport){
			supportName=((MicroServiceTemplateSupport)proxy).getDbName();
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
		    	Object retObj= method0.invoke(groovyObj, args);
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
			Object retObj= method0.invoke(groovyObj, args);
			return retObj;			
		}
	}

	@Override
	public void setOldGroovyObj(Object oldGroovyObj) {
		this.oldGroovyObj=oldGroovyObj;
		
	}

	@Override
	public void setGroovyObj(Object groovyObj) {
		this.groovyObj=groovyObj;
		
	}

	@Override
	public void setProperty(String property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGroovyName(String groovyName) {
		// TODO Auto-generated method stub
		
	}

}
