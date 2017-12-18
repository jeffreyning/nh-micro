package com.nh.micro.nhs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

import com.nh.micro.rule.engine.core.GFileBean;
import com.nh.micro.rule.engine.core.GroovyLoadUtil;


/**
 * 
 * @author ninghao
 * 
 */
public class NhsInitUtil {
	private static Logger logger=Logger.getLogger(NhsInitUtil.class);
	public static ILoadHandler loadHandler=new NhsParseHandler();
	public ILoadHandler getLoadHandler() {
		return loadHandler;
	}
	public void setLoadHandler(ILoadHandler loadHandler) {
		this.loadHandler = loadHandler;
	}	
	public static Boolean startThread=false;
	

	public static Boolean getStartThread() {
		return startThread;
	}
	public static void setStartThread(Boolean startThread) {
		NhsInitUtil.startThread = startThread;
	}
	public static List<GFileBean> fileList = new ArrayList<GFileBean>();

	public static List<GFileBean> getFileList() {
		return fileList;
	}

	public void setFileList(List<GFileBean> fileList) {
		NhsInitUtil.fileList = fileList;
	}

	public static void loadDir(String dirName,Boolean flag,String stamp) throws Exception{

		File temp=null;
		if(flag==true){
			URL url=NhsInitUtil.class.getResource(dirName);
			if(url==null){
				logger.warn("skip load cause cannot find path="+dirName);
				return;				
			}
			URI uri=NhsInitUtil.class.getResource(dirName).toURI();
			if(uri==null || uri.getPath()==null || uri.getPath().contains(".jar!")){
				logger.warn("skip load cause cannot this is jar="+uri.getPath());
				return;				
			}
			temp=new File(NhsInitUtil.class.getResource(dirName).toURI());
		  }else{
			temp=new File(dirName);
		  }
		File[] fs=null;  
		fs=temp.listFiles();
		  for(int i=0; i<fs.length; i++){
			  File file=fs[i];
		   //System.out.println(file.getAbsolutePath());
		   if(file.isFile()){
			   if(file.getName().endsWith(".nhs")){
				   String fullName=file.getAbsolutePath();
				   String ruleName=file.getName();
				   ruleName=ruleName.replace(".nhs", "");
				   initOneFile(fullName,false,ruleName,stamp);
			   }
		   }else{
			   String subDirName=file.getAbsolutePath();
			   loadDir(subDirName,false,stamp); 
		   }
		  }
	}
	public static void initOneFile(String fullName,Boolean flag,String ruleName,String stamp)  throws Exception {
		
		String oldFullName=GroovyLoadUtil.getFilePath(ruleName);
		if(oldFullName!=null && !oldFullName.equals(fullName)){
			logger.warn("skip load cause exist same filename in diff path ruleName="+ruleName+" fullName="+fullName+" oldFullName="+oldFullName);
			return ;
		}
		if(oldFullName==null){
			GroovyLoadUtil.setFilePath(ruleName, fullName);
		}
		
		
		String fileTime="";
		if (stamp != null && stamp.equals("true")) {
			if (flag == true) {
				fileTime=fileTime(new File(NhsInitUtil.class.getResource(fullName).toURI()).getAbsolutePath());
			}else{
				fileTime=fileTime(fullName);
			}
			
			boolean sameFlag=GroovyLoadUtil.checkStamp(ruleName, fileTime);
			if(sameFlag){
				return;
			}
		}
		
		GroovyLoadUtil.setTypeFlag(ruleName, GroovyLoadUtil.LOADTYPE_FILE);
		
		
		InputStream is = null;
		if (flag == true) {
			is = NhsInitUtil.class.getResourceAsStream(fullName);
		} else {
			is = new FileInputStream(fullName);
		}
		StringBuilder strb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
		String s = null;
		while ((s = br.readLine()) != null) {
			strb.append(s).append("\n");
		}
		String content = strb.toString();
		is.close();
		is = null;
		
		String realContent=content;
		if(loadHandler!=null){
			boolean checkFlag=loadHandler.check(ruleName, content);
			if(checkFlag==true){
				realContent=loadHandler.parse(ruleName, content);
				logger.debug("groovy realContent=" + realContent);
			}
		}
		
		if (stamp != null && stamp.equals("true")) {
	
			GroovyLoadUtil.loadGroovyStampCheck(ruleName, realContent, fileTime);
		} else {
			GroovyLoadUtil.loadGroovy(ruleName, realContent);
		}		
		
	}
	public static void initGroovy() throws Exception {
		for (GFileBean fileBean : fileList) {
			Boolean dirFlag=fileBean.getDirFlag();
			if(dirFlag==false){
				Boolean flag = fileBean.getJarFileFlag();
				String fullName = fileBean.getRulePath() + fileBean.getRuleName()
						+ ".nhs";
				String ruleName = fileBean.getRuleName();
				String stamp = fileBean.getRuleStamp();
				initOneFile(fullName,flag,ruleName,stamp);
			}else{
				Boolean flag = fileBean.getJarFileFlag();
				String fullName = fileBean.getRulePath();
				String stamp = fileBean.getRuleStamp();
				loadDir(fullName,flag,stamp);
			}
		}
	}

	public static void initGroovyAndThread() throws Exception {
		initGroovy();
		initThread();
	}
	public static String fileTime(String fullName){
		File tempFile=new File(fullName);
		long time=tempFile.lastModified();
		Date tempDate=new Date(time);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ret=sdf.format(tempDate);
		return ret;
	}
	
	public static void initThread(){
        Runnable daemonTask = new Runnable() {
            public void run() {
                daemonCheck();
            }
        };
        Thread daemonThread=null;
        daemonThread = new Thread(daemonTask);
        daemonThread.setDaemon(true);
        daemonThread.setName("LoadNhsDaemon");
        daemonThread.start();
	}
	public static void daemonCheck(){
        logger.info("nhs daemon started.");

        for (;;) {
            try {
            	Thread.sleep(1000*3);
				initGroovy();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                break;
            }
        }

        logger.info("nhs daemon stopped.");
	}
}
