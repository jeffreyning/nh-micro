package groovy.bizflow.creditaudit

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.nh.micro.db.*;

import com.nh.micro.rule.engine.core.*;


import groovy.json.*;
import groovy.template.MicroMvcTemplate;
import net.sf.json.JSONArray
import net.sf.json.JSONObject

import com.nh.micro.template.MicroTranAopInter;


class MicroManager extends MicroMvcTemplate{
	private static Logger logger=Logger.getLogger(MicroManager.class);
	public String pageName="listRoleInfo.jsp";
	public String tableName="bizflow_intopiece_list";

	
	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}
	
	public String execInit(String id,String nodeId,String approvalStatus,Map extParam){
		Map creditMap=getInfoByIdService(id,"bizflow_creditaudit_list");
		String creditAmount=creditMap.get("dbcol_ext_approval_credit_amount");
		String formId=creditMap.get("meta_name");
		Map pieceMap=new HashMap();
		pieceMap.put("dbcol_ext_approval_credit_amount", creditAmount);
		updateInfoByBizIdService(formId,"bizflow_intopiece_list","meta_key",pieceMap);
	}

	
}
