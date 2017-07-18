package groovy.attachment;

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import groovy.json.*;
import groovy.template.MicroMvcTemplate;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


import org.springframework.jdbc.support.rowset.*;
import com.nh.micro.rule.engine.core.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;


class MicroAttachmentManager extends MicroMvcTemplate {
	
	private static Logger logger=Logger.getLogger(MicroAttachmentManager.class);
	public String pageName="";
	public String tableName="nh_micro_attachment_list";

	
	public String getPageName(HttpServletRequest httpRequest){
		return pageName;
	}
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}
	
	public String uploadPath="";
	public String getUploadPath(HttpServletRequest httpRequest){
		String uploadRootPath=getRootPath();
		if(uploadRootPath!=null && !"".equals(uploadRootPath)){
			return uploadRootPath;
		}
		String servletPath=httpRequest.getSession().getServletContext().getRealPath("/");
		return servletPath;
	}
	
	private String getRootPath(){
		Map infoMap=getDicItemByItemKey("sys","uploadRootPath");
		if(infoMap==null){
			return null;
		}
		String url=infoMap.get("meta_name");
		return url;
	}
	
	public void execUploadFile(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		HttpServletRequest httpRequest=	gContextParam.getContextMap().get("httpRequest");	
        httpRequest.setCharacterEncoding("utf-8");  //设置编码  
          
        //获得磁盘文件条目工厂  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //获取文件需要上传到的路径  
		String rootPath=getUploadPath(httpRequest);
        factory.setSizeThreshold(1024*1024) ;  
        ServletFileUpload upload = new ServletFileUpload(factory);  
 
        try {  
			String filePath=rootPath;
			//如果文件夹不存在则创建
			File file =new File(filePath);
			if(!file.exists()  && !file.isDirectory())
			{
				file .mkdirs();
			}
            //可以上传多个文件  
            List<FileItem> list = (List<FileItem>)upload.parseRequest(httpRequest);  
            for(FileItem item : list)  
            {  
                String name = item.getFieldName(); 
                if(item.isFormField())  
                {                     
                    //获取用户具体输入的字符串
                    String value = item.getString();  
                    httpRequest.setAttribute(name, value);  
                }  
                //对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些  
                else  
                {  
                    /** 
                     * 以下三步，主要获取 上传文件的名字 
                     */  
                    //获取路径名  
                    String value = item.getName() ; 
					value=value.replace("\\", "/"); 
                    int start = value.lastIndexOf("/");  
                    String filename = value.substring(start+1);  
					String uuid=UUID.randomUUID().toString();
					OutputStream out =null;
					InputStream ins=null;
					try{
	                    out = new FileOutputStream(new File(filePath,uuid));  
	                    ins = item.getInputStream() ;  
	                    int length = 0 ;  
	                    byte [] buf = new byte[1024] ;   
	                    while( (length = ins.read(buf) ) != -1)  
	                    {  
	                        out.write(buf, 0, length);  
	                    }  
						Map paramMap=new HashMap();
						paramMap.put("meta_key", uuid);
						paramMap.put("meta_name", filename);
						createInfoService(paramMap,"nh_micro_attachment_list");
					}finally{  
						if(ins!=null){
							ins.close();  
						}
						if(out!=null){
							out.close();
						}
					}  
					httpRequest.setAttribute("microInfoMsg", "文件上传成功");
					httpRequest.setAttribute("fileId", uuid);
                }  
            }  
              
        } catch (Exception e) {  
            logger.error("file upload error", e);
			httpRequest.setAttribute("microInfoMsg", "文件上传失败");
        }  
		httpRequest.setAttribute("forwardFlag", "true");
		httpRequest.getRequestDispatcher("nh-micro-jsp/template-page/info.jsp").forward(httpRequest, httpResponse);
	}

	
	
	public void execDownLoadFile(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		HttpServletRequest httpRequest=	gContextParam.getContextMap().get("httpRequest");
		httpRequest.setCharacterEncoding("utf-8");  //设置编码
		String rootPath=getUploadPath(httpRequest);
		String fileId=httpRequest.getParameter("fileId");
		String fileName="aaa.txt";//保存窗口中显示的文件名
		try
		{

		httpResponse.setContentType("APPLICATION/OCTET-STREAM");
		/*要显示到客户端的文件名转码是必需的，特别是中文名， 否则可能出现文件名乱码甚至是浏览器显示无法下载的问题*/
		fileName=httpResponse.encodeURL(new String(fileName.getBytes(),"ISO8859_1"));//转码
		httpResponse.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
	   
		OutputStream out = httpResponse.getOutputStream();
		InputStream inStream=new FileInputStream(rootPath+"/"+fileId);
		 //循环取出流中的数据
		byte[] b = new byte[1024];
		int len;
		while((len=inStream.read(b)) >0)
		out.write(b,0,len);
			out.close();
			inStream.close();
		}
		catch (Exception e)
		{
			logger.error("download error",e);
		}
	 }
	
	
}
