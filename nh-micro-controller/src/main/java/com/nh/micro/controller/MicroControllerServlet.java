package com.nh.micro.controller;



import groovy.lang.GroovyObject;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.nh.micro.rule.engine.core.GroovyExecUtil;


/**
 * 
 * @author ninghao
 *
 */
public class MicroControllerServlet extends HttpServlet  {

	private static final long serialVersionUID = 1L;

	private String prepath=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MicroControllerServlet() {
        super();
    }


	public String getPrepath() {
		return prepath;
	}
	public void setPrepath(String prepath) {
		this.prepath = prepath;
	}
	public void init(ServletConfig config) throws ServletException { 
		if(prepath==null){
			this.prepath=config.getInitParameter("prepath");
		}
	
    } 

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url=request.getRequestURI();
		String context=request.getServletContext().getContextPath();
		int prelen=context.length();
		String busUrl=url.substring(prelen);
		if(prepath!=null && !"".equals(prepath)){
			if(busUrl.startsWith("/"+prepath)){
				busUrl=busUrl.substring(prepath.length()+1);
			}
		}
		String version=request.getParameter("micro_api_version");
		String[] config=MicroControllerMap.mappingGroovyName(busUrl,version);
		if(config==null){
			throw new RuntimeException("can not check from "+url+" to "+busUrl+" config is null");
		}
		
		String groovyName=config[0];
		String methodName=config[1];
		
		Object[] typeArray=new Object[3];
		typeArray[0]=String.class;
		typeArray[1]=String.class;
		typeArray[2]=HttpServletRequest.class;
		//boolean methodFlag=false;
		
		//add 201808 ning
/*		if(checkflag!=null && checkflag.equals("yes")){
			GroovyObject authObj=GroovyExecUtil.getGroovyObj(groovyName);
			authObj.getClass().getAnnotations()
			Method[] methods=authObj.getClass().getDeclaredMethods();
			if(methods!=null){
				int size=methods.length;
				for(int i=0;i<size;i++){
					Method md=methods[i];
					if(md.getName().equals("checkExecAuth")){
						methodFlag=true;
					}
				}
			}
		}
		
		if(methodFlag && checkflag!=null && checkflag.equals("yes")){
			Boolean authFlag=(Boolean) GroovyExecUtil.execGroovyRetObj(groovyName, "checkExecAuth",groovyName, methodName,request);
			if(authFlag==null || authFlag==false){
				throw new RuntimeException("no auth for access url "+url);
			}
		}*/
		
		
		GroovyExecUtil.execGroovyRetObj(groovyName, methodName, request, response);
	
	}
	

}
