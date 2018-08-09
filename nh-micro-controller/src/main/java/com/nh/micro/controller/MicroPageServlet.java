package com.nh.micro.controller;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 * @author ninghao
 *
 */
public class MicroPageServlet extends HttpServlet  {

	private static final long serialVersionUID = 1L;
	private String prepath=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MicroPageServlet() {
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
		String subPath="/WEB-INF"+busUrl;
		request.getRequestDispatcher(subPath).forward(request, response);  
	
	}
	

}
