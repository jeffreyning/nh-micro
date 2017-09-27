package com.project.util;



import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.esb.core.INhCmdConst;
import com.nh.esb.core.INhCmdHandler;
import com.nh.esb.core.INhCmdService;
import com.nh.esb.core.NhCmdRequest;
import com.nh.esb.core.NhCmdResult;

/**
 * Servlet implementation class NhEsbServiceServlet
 */
public class MicroDynaServiceServlet extends HttpServlet  {
	private static Logger logger=Logger.getLogger(MicroDynaServiceServlet.class);
	private static final long serialVersionUID = 1L;
	
	public ApplicationContext getContext(){
		return WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MicroDynaServiceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		GroovyExecUtil.execGroovy("mock_dispatch", "dispatch",request,response);

	
	}
	

}
