package com.nh.micro.template;

import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import groovy.lang.GroovyObject;
import groovy.lang.MetaMethod;


import com.nh.micro.db.GroovyDataSource;
import com.nh.micro.rule.engine.context.MicroContextHolder;
import com.nh.micro.rule.engine.core.GroovyAopInter;
import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.micro.template.MicroTranManagerHolder;

import java.lang.reflect.Method;

public class GroovyTMAopImpl extends GroovyAopInter {

	public Object invokeMethod(GroovyObject groovyObject, String GroovyName,
			String methodName, Object... param){
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
		boolean tranFlag=false;
		Transactional trans=null;
		if(method!=null){
			trans=method.getAnnotation(Transactional.class);
			if(trans!=null){
				tranFlag=true;
			}
		}
		
		if(tranFlag==true){
			//support info
			String supportName="";
			if(groovyObject instanceof MicroServiceTemplateSupport){
				supportName=((MicroServiceTemplateSupport)groovyObject).getDbName();
			}
			
			//change info
			GroovyDataSource changeDataSource=method.getAnnotation(GroovyDataSource.class);
			String changeName="";
			String oldName="";
			boolean changeFlag=false;
			boolean pushFlag=false;
			if(changeDataSource!=null){
				changeFlag=true;
				changeName=changeDataSource.name();
				oldName=changeDataSource.name();
				
			}
			
			if(oldName==null || "".equals(oldName)){
				oldName=supportName;
			}
			
			//tran info
			String dbName="";			
			String beanId=trans.value();
			
			PlatformTransactionManager  transactionManager=null;
			if(beanId!=null && !"".equals(beanId)){
				transactionManager= (PlatformTransactionManager) MicroContextHolder.getContext().getBean(beanId);
			}else{
				if(changeFlag==false){
					dbName=supportName;
				}else{
					dbName=changeName;
				}
				if(dbName==null || "".equals(dbName)){
					dbName="default";
				}
				transactionManager=MicroTranManagerHolder.getTransactionManager(dbName);
			}
		    DefaultTransactionDefinition def =new DefaultTransactionDefinition();
		    def.setIsolationLevel(trans.isolation().value());
		    def.setPropagationBehavior(trans.propagation().value());
		    TransactionStatus status=transactionManager.getTransaction(def);
		    try
		    {
		    	
		    	//Object retObj= groovyObject.invokeMethod(methodName, param);
		    	Object retObj= execNextHandler(groovyObject, GroovyName, methodName, param);
		    	transactionManager.commit(status);
		    	return retObj;
		    }
		    catch(Exception ex)
		    {
		    	transactionManager.rollback(status);
		        throw new RuntimeException(ex);
		    }			
		}else{
			//Object retObj=groovyObject.invokeMethod(methodName, param);
			Object retObj= execNextHandler(groovyObject, GroovyName, methodName, param);
			return retObj;			
		}

	}

}
