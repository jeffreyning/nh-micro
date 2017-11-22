package com.nh.micro.rule.engine.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import org.apache.log4j.Logger;



/**
 * 
 * @author ninghao
 *
 */
public class GroovyInitJarRunUtil {
	private static Logger logger=Logger.getLogger(GroovyInitJarRunUtil.class);
	public static List<GFileBean> fileList = new ArrayList<GFileBean>();

	public static List<GFileBean> getFileList() {
		return fileList;
	}

	public void setFileList(List<GFileBean> fileList) {
		GroovyInitJarRunUtil.fileList = fileList;
	}

	public static boolean checkRegFileName(String curFileName,String regFileName){
		String[] chaifen=regFileName.split("\\*");
	    String first=chaifen[0];
	    boolean firstFlag=curFileName.startsWith(first);
	    boolean lastFlag=true;
	    if(chaifen.length>1){
	    	String last=chaifen[1];
	    	lastFlag=curFileName.endsWith(last);
	    }	
	    if(firstFlag==true && lastFlag==true){
	    	return true;
	    }
	    return false;
	}
	public static String getRealFileName(String jarFileName) throws Exception{
		if(!jarFileName.contains("*")){
			return jarFileName;
		}
    	String temp[] = jarFileName.replaceAll("\\\\","/").split("/");
    	String ruleName = "";
    	if(temp.length > 1){
    		ruleName = temp[temp.length - 1];
    	}
    	String parentPath=jarFileName.replace(ruleName, "");
		String clsPath=GroovyInitJarRunUtil.class.getResource("/").toURI().getPath();
		String loadPath=clsPath+parentPath;
		File rootDir=new File(loadPath);
		String[] fileNames=rootDir.list();
		if(fileNames==null){
			return jarFileName;
		}
		for(String one:fileNames){
			  boolean flag= checkRegFileName(one,ruleName);
			  if(flag==true){
				  return parentPath+"/"+one;
			  }
		}
		
		return jarFileName;
	}
	final static void loadDir(String dirName,String jarFileName,String stamp) throws Exception{
		File file =null;
		JarFile jarFile=null;
		logger.info("begin load from runnable jar jarFileName="+jarFileName+" dirName="+dirName);
		
		java.net.URL url=GroovyInitJarRunUtil.class.getResource("/");
		String tempPath=url.getPath();
		logger.info("jarFullpath="+tempPath);
		String[] pathArray=tempPath.split("!");
		String path=pathArray[0];
		path=path.substring(5);
		logger.info("jarRootpath="+path);
		if(!path.endsWith(".jar")){
			logger.warn("skip load groovy from runnable jar cause jarRootpath is not jar");
			return;
		}
		file=new File(path);
		jarFile = new JarFile(file); 		

		List<JarEntry> groovyList=new ArrayList();
        Enumeration<JarEntry> entrys = jarFile.entries();
        while(entrys.hasMoreElements()){
        	JarEntry entry=entrys.nextElement();
            String resname = entry.getName();
            String checkName=resname.replaceAll("\\\\","/");
            checkName="/"+checkName;
            String[] checkArray=checkName.split("/");
            int size=checkArray.length;
            String readName=checkArray[size-1];
            if(checkRegFileName(readName,jarFileName)){
            	JarInputStream jis=new JarInputStream(jarFile.getInputStream(entry)); 
            	loadInnerJar(jis,dirName);
            }
            int contentIndex=checkName.indexOf(dirName);
            if(contentIndex>=0 && contentIndex<3){
	            if (resname.lastIndexOf(".groovy") != -1){
	            	if(jarFileName==null || "".equals(jarFileName)){
	            		groovyList.add(entry);  
	            	}
	            }
            }
        }
        for (JarEntry ruleEntry : groovyList) {
        	String fullName=ruleEntry.getName();
        	
        	String temp[] = fullName.replaceAll("\\\\","/").split("/");
        	String ruleName = "";
        	if(temp.length > 1){
        		ruleName = temp[temp.length - 1];
        	}
        	ruleName=ruleName.replace(".groovy", "");
        	Object gobj=GroovyExecUtil.getGroovyObj(ruleName);
        	if(gobj==null){
        		initOneFile(ruleName,ruleEntry,jarFile);
        	}
        }
        jarFile.close();
        logger.info("end load runnable jar file "+jarFileName);

	}
	
	private static void loadInnerJar(JarInputStream jis,String dirName) throws Exception{
		List<JarEntry> groovyList=new ArrayList();
		JarEntry entry=null;
		while((entry=jis.getNextJarEntry()) !=null){
            String resname = entry.getName();
            String checkName=resname.replaceAll("\\\\","/");
            checkName="/"+checkName;
            int contentIndex=checkName.indexOf(dirName);
            if(contentIndex>=0 && contentIndex<3){
	            if (resname.lastIndexOf(".groovy") != -1){
	            		groovyList.add(entry);  
	            }
            }
		}
        for (JarEntry ruleEntry : groovyList) {
        	String fullName=ruleEntry.getName();
        	
        	String temp[] = fullName.replaceAll("\\\\","/").split("/");
        	String ruleName = "";
        	if(temp.length > 1){
        		ruleName = temp[temp.length - 1];
        	}
        	ruleName=ruleName.replace(".groovy", "");
        	Object gobj=GroovyExecUtil.getGroovyObj(ruleName);
        
        	if(gobj==null){
        		initInnerOneFile(ruleName,ruleEntry);
        	}
        }
	}

	public static void initOneFile(String ruleName,Object ruleEntry,JarFile jarFile)  throws Exception {
		
		GroovyLoadUtil.setTypeFlag(ruleName, GroovyLoadUtil.LOADTYPE_JAR);
		InputStream is = null;
		is = jarFile.getInputStream((ZipEntry) ruleEntry);

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
		GroovyLoadUtil.loadGroovy(ruleName, content);
		
		
	}
	public static void initInnerOneFile(String ruleName,JarEntry ruleEntry)  throws Exception {
		
		GroovyLoadUtil.setTypeFlag(ruleName, GroovyLoadUtil.LOADTYPE_JAR);
		InputStream is = null;
		is=new JarInputStream(new ByteArrayInputStream(ruleEntry.getExtra()));
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
		GroovyLoadUtil.loadGroovy(ruleName, content);
		
		
	}	
	public static void initGroovy() throws Exception {
		for (GFileBean fileBean : fileList) {
			Boolean dirFlag=fileBean.getDirFlag();

			String jarFileName=fileBean.getJarFileName();
			String rulePath = fileBean.getRulePath();
			String stamp = fileBean.getRuleStamp();
			loadDir(rulePath,jarFileName,stamp);

		}
	}


}
