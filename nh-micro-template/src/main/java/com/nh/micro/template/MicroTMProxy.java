package com.nh.micro.template;



import java.lang.reflect.Method;


import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.nh.micro.db.GroovyDataSource;
import com.nh.micro.rule.engine.context.MicroContextHolder;
import com.nh.micro.rule.engine.core.plugin.IMicroProxy;
import com.nh.micro.template.MicroServiceTemplateSupport;
import com.nh.micro.template.MicroTranManagerHolder;

/**
 * 
 * @author ninghao
 *
 */
public class MicroTMProxy implements IMicroProxy {

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
			if(proxy instanceof MicroServiceTemplateSupport){
				supportName=((MicroServiceTemplateSupport)proxy).getDbName();
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
		    	
		    	Object retObj= method0.invoke(groovyObj, args);
		    	transactionManager.commit(status);
		    	return retObj;
		    }
		    catch(Exception ex)
		    {
		    	transactionManager.rollback(status);
		        throw new RuntimeException(ex);
		    }			
		}else{
			Object retObj= method0.invoke(groovyObj, args);
			return retObj;			
		}

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

	@Override
	public void setOldGroovyObj(Object oldGroovyObj) {
		this.oldGroovyObj=oldGroovyObj;
		
	}

}
