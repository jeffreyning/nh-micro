package com.nh.micro.rule.engine.core.plugin;

import groovy.lang.GroovyObject;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.nh.micro.rule.engine.core.GroovyExecUtil;

public class MicroAuthProxy implements IMicroProxy  {

	private static Logger logger=Logger.getLogger(MicroDefaultLogProxy.class);
	public Object groovyObj=null;
	public Object oldGroovyObj=null;
	public String groovyName=null;
	public String property="";	
	
	
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
		String checkName="";
		String checkMethod="";
		String[] checkValue=null;
		String logical="and";
		String exclude="false";
		
		boolean checkFlag=false;
		MicroAuth microAuth=null;
		if(method!=null){
			microAuth=method.getAnnotation(MicroAuth.class);
			if(microAuth!=null){
				checkFlag=true;
				checkName=microAuth.checkName();
				checkMethod=microAuth.checkMethod();
				checkValue=microAuth.value();
				logical=microAuth.logical();
				exclude=microAuth.exclude();
				if(exclude!=null && "true".equals(exclude)){
					checkFlag=false;
				}
			}
		}			
		if(checkFlag==true){
			boolean authFlag=false;

			Object[] checkParamArray=new Object[5];
			checkParamArray[0]=checkValue;
			checkParamArray[1]=logical;
			checkParamArray[2]=groovyName;
			checkParamArray[3]=methodName;
			checkParamArray[4]=paramArray;

			
			if(checkName==null || "".equals(checkName)){
				authFlag=(Boolean) GroovyExecUtil.execGroovyRetObj((GroovyObject)oldGroovyObj, checkMethod, checkParamArray);
			}else{
				authFlag=(Boolean) GroovyExecUtil.execGroovyRetObj(checkName, checkMethod, checkParamArray);
			}
			if(authFlag==true){
				Object retObj= method0.invoke(groovyObj, args);
				return retObj;					
			}else{
				throw new RuntimeException("no auth to exec groovy="+groovyName+" method="+methodName);
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
		this.property=property;
		
	}
	@Override
	public void setGroovyName(String groovyName) {
		this.groovyName=groovyName;
		
	}

}
