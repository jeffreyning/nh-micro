package com.nh.micro.controller;



import java.io.IOException;
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
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MicroControllerServlet() {
        super();
    }


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url=request.getRequestURI();
		String context=request.getServletContext().getContextPath();
		int prelen=context.length();
		String busUrl=url.substring(prelen);
		String version=request.getParameter("micro_api_version");
		String[] config=MicroControllerMap.mappingGroovyName(busUrl,version);
		if(config==null){
			throw new RuntimeException("can not check "+url+" config is null");
		}
		
		String groovyName=config[0];
		String methodName=config[1];
		GroovyExecUtil.execGroovyRetObj(groovyName, methodName, request, response);
	
	}
	

}
