package groovy.dictionary;

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import groovy.json.*;
import com.nh.micro.db.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.nh.micro.cache.base.*;
import com.nh.micro.db.Cutil;
import com.nh.micro.db.Cobj;
import com.nh.micro.db.MicroDbHolder;


import org.springframework.jdbc.support.rowset.*;
import groovy.template.MicroMvcTemplate;

 
class nrule1 extends MicroMvcTemplate{
public String pageName="listDictItemsInfo";
public String tableName="nh_micro_dict_items";
/**
 * 跳转页面
 * @param gInputParam
 * @param gOutputParam
 * @param gContextParam
 */
	public void toList(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpRequest.setAttribute("meta_type",httpRequest.getParameter("meta_type"));
		httpRequest.getRequestDispatcher("/WEB-INF/views/dictionary-page/listDictItemsInfo.jsp").forward(httpRequest, httpResponse);
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
		
}
