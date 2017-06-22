package groovy.template.upload;

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import groovy.json.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


import org.springframework.jdbc.support.rowset.*;
import com.nh.micro.rule.engine.core.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


class MicroMvcFileUpload {
	public String uploadPath="";
	public String getUploadPath(HttpServletRequest httpRequest){
		return uploadPath;
	}
	public void execUploadFile(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		HttpServletRequest httpRequest=	gContextParam.getContextMap().get("httpRequest");	
        httpRequest.setCharacterEncoding("utf-8");  //设置编码  
          
        //获得磁盘文件条目工厂  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //获取文件需要上传到的路径  
		String rootPath=getUploadPath(httpRequest);
		String servletPath=httpRequest.getSession().getServletContext().getRealPath("/");
		rootPath=servletPath+rootPath;
        factory.setSizeThreshold(1024*1024) ;  
        ServletFileUpload upload = new ServletFileUpload(factory);  
 
        try {  
			String filePath=rootPath;
            //可以上传多个文件  
            List<FileItem> list = (List<FileItem>)upload.parseRequest(httpRequest);  
            for(FileItem item : list)  
            {  
                String name = item.getFieldName(); 
				if(name.equals("packagePath")){
					filePath=rootPath+"/"+item.getString();
					File file =new File(filePath);
					//如果文件夹不存在则创建
					if(!file.exists()  && !file.isDirectory())
					{
						file .mkdirs();
					}
				} 
                if(item.isFormField())  
                {                     
                    //获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的  
                    String value = item.getString() ;  
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
                    int start = value.lastIndexOf("\\");  
                    String filename = value.substring(start+1);  
					
					OutputStream out =null;
					InputStream ins=null;
					try{
	                    out = new FileOutputStream(new File(filePath,filename));  
	                    ins = item.getInputStream() ;  
	                    int length = 0 ;  
	                    byte [] buf = new byte[1024] ;   
	                    while( (length = ins.read(buf) ) != -1)  
	                    {  
	                        out.write(buf, 0, length);  
	                    }  
					}finally{  
						if(ins!=null){
							ins.close();  
						}
						if(out!=null){
							out.close();
						}
					}  
					httpRequest.setAttribute("microInfoMsg", "文件上传成功");
                }  
            }  
              
        } catch (Exception e) {  
            e.printStackTrace();  
			httpRequest.setAttribute("microInfoMsg", "文件上传异常");
        }  
        httpRequest.getRequestDispatcher("nh-micro-jsp/template-page/info.jsp").forward(httpRequest, httpResponse);  
	}

}
