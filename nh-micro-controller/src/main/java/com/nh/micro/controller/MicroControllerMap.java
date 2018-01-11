package com.nh.micro.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import java.util.Map;

/**
 * 
 * @author ninghao
 *
 */
public class MicroControllerMap {
	public static Map urlMap = new HashMap();
	//public static Map urlMethodMap = new HashMap();
	public static String[] mappingGroovyName(String url,String version) {
		Iterator it = urlMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();

			String key = (String) entry.getKey();
			if(key.equals(url)){
				MicroUrlBean urlBean=(MicroUrlBean) entry.getValue();
				if(version==null || "".equals(version)){
					return urlBean.getDefaultInfo();
				}else{
					version=versionFormat(version);
				}
				Entry fEntry=urlBean.getTreeMap().floorEntry(version);
				String[] ret=null;
				if(fEntry!=null){
					ret=(String[]) fEntry.getValue();
				}
				return ret;
			}
		}
		return null;
	}


	public static String versionFormat(String version){
		version=version.replace("\\.", "/");
		String[] temp=version.split("/");
		int size=temp.length;

		for(int i=0;i<3;i++){
			int rowInt=0;
			if(i<size){
				String row=temp[i];
				rowInt=Integer.valueOf(row);
				if(rowInt>999){
					rowInt=999;
				}
				if(rowInt<0){
					rowInt=0;
				}
			}
			String rowTemp=String.format("%1$03d", rowInt);
			if(i==0){
				version=rowTemp;
			}else{
				version=version+"/"+rowTemp;
			}
		}	
		return version;
	}
	public static void setUrl(String url, String groovyName, String methodName, String version){
		String[] array={groovyName, methodName};
		MicroUrlBean urlBean=(MicroUrlBean) urlMap.get(url);
		if(urlBean==null){
			urlBean=new MicroUrlBean();
			urlMap.put(url, urlBean);
		}
		if(version==null || "".equals(version)){
			urlBean.setDefaultInfo(array);
		}else{
			version=versionFormat(version);
			urlBean.getTreeMap().put(version, array);
		}

	}
	

}
