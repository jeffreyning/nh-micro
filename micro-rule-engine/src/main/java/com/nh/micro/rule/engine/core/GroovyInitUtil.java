package com.nh.micro.rule.engine.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ninghao
 * 
 */
public class GroovyInitUtil {
	public static List<GFileBean> fileList = new ArrayList<GFileBean>();

	public static List<GFileBean> getFileList() {
		return fileList;
	}

	public void setFileList(List<GFileBean> fileList) {
		GroovyInitUtil.fileList = fileList;
	}

	final static void loadDir(String dirName,Boolean flag,String stamp) throws Exception{
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
		   System.out.println(file.getAbsolutePath());
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
		if (stamp != null) {
			GroovyLoadUtil.loadGroovyStampCheck(ruleName, content, stamp);
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

}
