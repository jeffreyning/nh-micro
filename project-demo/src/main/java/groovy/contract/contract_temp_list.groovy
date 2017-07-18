package groovy.contract;

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.awt.event.ItemEvent;
import java.io.File;
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
import com.project.util.Word2Html;
import com.project.util.Html2Pdf;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

class ContractTemplate extends MicroMvcTemplate{
public String pageName="listDictionaryInfo";
public String tableName="contract_temp_list";

	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

	public void compileWordTemplate(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		Map infoMap=getInfoByIdService(id,"contract_temp_list");
		String fileId=infoMap.get("dbcol_ext_fileId"); 
		String rootPath=getRootPath();
		String filePath=rootPath+File.separator+fileId;
		String tempPath=getTempPath();
		String targetFile=tempPath+File.separator+fileId+"_html";

		Word2Html.convert2Html(filePath, targetFile);
		String htmlText=readFile(targetFile);
		Map updateMap=new HashMap();
		updateMap.put("id", id);
		updateMap.put("html_text", htmlText);
		updateInfoService(updateMap,"contract_temp_list");
		
		return;

	}
	
	public void createPdf(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		Map infoMap=getInfoByIdService(id,"contract_temp_list");
		String htmlText=infoMap.get("html_text");
		String paramStr=httpRequest.getParameter("param");
		Map paramMap=new JsonSlurper().parseText(paramStr);
		//Map paramMap=getRequestParamMap(httpRequest);
		String repedText=replaceRowByVelocity(htmlText,paramMap);
		
		try
		{
			String fileName="contract.pdf";
			httpResponse.setContentType("APPLICATION/OCTET-STREAM");
			/*要显示到客户端的文件名转码是必需的，特别是中文名， 否则可能出现文件名乱码甚至是浏览器显示无法下载的问题*/
			fileName=httpResponse.encodeURL(new String(fileName.getBytes(),"ISO8859_1"));//转码
			httpResponse.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
		   
			OutputStream out = httpResponse.getOutputStream();
			Html2Pdf.expPdf(repedText,out);
			httpRequest.setAttribute("forwardFlag", "true");
		}catch (Exception e)
		{
			logger.error("download error",e);
		}
		return;

	}

	
	public void createPdf4TempId(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse=gContextParam.getContextMap().get("httpResponse");
		String id=httpRequest.getParameter("id");
		Map infoMap=getInfoByBizIdService(id,"contract_temp_list","meta_key");
		String htmlText=infoMap.get("html_text");
		String paramStr=httpRequest.getParameter("param");
		Map paramMap=new JsonSlurper().parseText(paramStr);
		//Map paramMap=getRequestParamMap(httpRequest);
		String repedText=replaceRowByVelocity(htmlText,paramMap);
		
		try
		{
			String fileName="contract.pdf";
			httpResponse.setContentType("APPLICATION/OCTET-STREAM");
			/*要显示到客户端的文件名转码是必需的，特别是中文名， 否则可能出现文件名乱码甚至是浏览器显示无法下载的问题*/
			fileName=httpResponse.encodeURL(new String(fileName.getBytes(),"ISO8859_1"));//转码
			httpResponse.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
		   
			OutputStream out = httpResponse.getOutputStream();
			Html2Pdf.expPdf(repedText,out);
			httpRequest.setAttribute("forwardFlag", "true");
		}catch (Exception e)
		{
			logger.error("download error",e);
		}
		return;

	}
		
	
	private String readFile(String filePath){
		File file=new File(filePath);
		int size=(int)file.length();
		byte[] temp=new byte[size];
		FileInputStream fis=new FileInputStream(file);
		fis.read(temp);
		String fileStr=new String(temp,"UTF-8");
		return fileStr;
	}
	private String getRootPath(){
		Map infoMap=getDicItemByItemKey("sys","uploadRootPath");
		if(infoMap==null){
			return System.getProperty("java.io.tmpdir");;
		}
		String url=infoMap.get("meta_name");
		return url;
	}	
	
	private String getTempPath(){
		Map infoMap=getDicItemByItemKey("sys","tempRootPath");
		if(infoMap==null){
			return System.getProperty("java.io.tmpdir");;
		}
		String url=infoMap.get("meta_name");
		return url;
	}
	
	public static String replaceRowByVelocity(String template,Map rowMap){
		String ret="";
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		VelocityContext context = new VelocityContext();
		Set entrySet=rowMap.entrySet();
		Iterator it=entrySet.iterator();
		while(it.hasNext()){
			Map.Entry entry=(Map.Entry) it.next();
			String key=(String) entry.getKey();
			String value=(String) entry.getValue();
			context.put(key, value);
		}
		context.put("param", rowMap.get("root"));
		StringWriter writer = new StringWriter();
		ve.evaluate(context, writer, "", template);
		ret=writer.toString();
		return ret;
	}
				
}
