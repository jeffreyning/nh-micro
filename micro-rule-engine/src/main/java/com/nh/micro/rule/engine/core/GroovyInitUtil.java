package com.nh.micro.rule.engine.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author ninghao
 * 
 */
public class GroovyInitUtil {
	private static Logger logger=Logger.getLogger(GroovyInitUtil.class);
	public static List<GFileBean> fileList = new ArrayList<GFileBean>();

	public static List<GFileBean> getFileList() {
		return fileList;
	}

	public void setFileList(List<GFileBean> fileList) {
		GroovyInitUtil.fileList = fileList;
	}

	public static void loadDir(String dirName,Boolean flag,String stamp) throws Exception{
		File temp=null;
		if(flag==true){
			temp=new File(GroovyInitUtil.class.getResource(dirName).toURI());
		  }else{
			temp=new File(dirName);
		  }
		File[] fs=null;  
		fs=temp.listFiles();
		  for(int i=0; i<fs.length; i++){
			  File file=fs[i];
		   //System.out.println(file.getAbsolutePath());
		   if(file.isFile()){
			   if(file.getName().endsWith(".groovy")){
				   String fullName=file.getAbsolutePath();
				   String ruleName=file.getName();
				   ruleName=ruleName.replace(".groovy", "");
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
				fileTime=fileTime(new File(GroovyInitUtil.class.getResource(fullName).toURI()).getAbsolutePath());
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
			is = GroovyInitUtil.class.getResourceAsStream(fullName);
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
		if (stamp != null && stamp.equals("true")) {
	
			GroovyLoadUtil.loadGroovyStampCheck(ruleName, content, fileTime);
		} else {
			GroovyLoadUtil.loadGroovy(ruleName, content);
		}		
		
	}
	public static void initGroovy() throws Exception {
		for (GFileBean fileBean : fileList) {
			Boolean dirFlag=fileBean.getDirFlag();
			if(dirFlag==false){
				Boolean flag = fileBean.getJarFileFlag();
				String fullName = fileBean.getRulePath() + fileBean.getRuleName()
						+ ".groovy";
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

	public static String fileTime(String fullName){
		File tempFile=new File(fullName);
		long time=tempFile.lastModified();
		Date tempDate=new Date(time);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ret=sdf.format(tempDate);
		return ret;
	}
}
