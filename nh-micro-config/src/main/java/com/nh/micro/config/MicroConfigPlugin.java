package com.nh.micro.config;

import groovy.lang.GroovyObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

import com.nh.micro.rule.engine.core.IGroovyLoadPlugin;

/**
 * 
 * @author ninghao
 * 
 */
public class MicroConfigPlugin implements IGroovyLoadPlugin {
	public String rootPath = "";

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public MicroConfigHandler microConfigHandler = null;

	public MicroConfigHandler getMicroConfigHandler() {
		return microConfigHandler;
	}

	public void setMicroConfigHandler(MicroConfigHandler microConfigHandler) {
		this.microConfigHandler = microConfigHandler;
	}
	
	public List fileArray=new ArrayList();
	


	public List getFileArray() {
		return fileArray;
	}

	public void setFileArray(List fileArray) {
		this.fileArray = fileArray;
	}

	private String queryMap(Map map, String attrName) {
		String[] names = attrName.split("\\.");
		int size = names.length;
		for (int i = 0; i < size; i++) {
			String key = names[i];
			if (i < size - 1) {
				Map temp = (Map) map.get(key);
				map = temp;
			} else {
				String value = (String) map.get(key);
				return value;
			}
		}
		return null;
	}

	public String getValue4file(String fileName,String attrName) throws IOException{
		if (fileName.endsWith(".yml")) {
			Yaml yaml = new Yaml();
			Map map = null;
			if (rootPath == null || "".equals(rootPath)) {
				URL url = MicroConfigPlugin.class.getClassLoader()
						.getResource(fileName);
				if (url != null) {
					// 也可以将值转换为Map
					map = (Map) yaml.load(new FileInputStream(url
							.getFile()));
				}
			} else {
				String realName = rootPath + fileName;
				InputStream in = new FileInputStream(realName);
				map = (Map) yaml.load(in);
			}
			String value = queryMap(map, attrName);
			//field.set(groovyObject, value);
			return value;

		} else {
			Properties pps = new Properties();
			if (rootPath == null || "".equals(rootPath)) {
				pps.load(MicroConfigPlugin.class
						.getResourceAsStream(fileName));
			} else {
				String realName = rootPath + fileName;
				InputStream in = new FileInputStream(realName);
				pps.load(in);
				in.close();
			}
			String value = pps.getProperty(attrName);

			//field.set(groovyObject, value);
			return value;
		}		
	}
	
	
	
	@Override
	public GroovyObject execPlugIn(String name, GroovyObject groovyObject,
			GroovyObject proxyObject) throws Exception {
		Field[] fields = groovyObject.getClass().getDeclaredFields();
		int size = fields.length;
		for (int i = 0; i < size; i++) {
			Field field = fields[i];
			MicroConfig anno = field.getAnnotation(MicroConfig.class);
			if (anno == null) {
				continue;
			}
			String fileName = null;
			String attrName = null;
			fileName = anno.configFile();
			attrName = anno.name();
			List checkList=new ArrayList();
			if(fileName!=null && !"".equals(fileName)){
				checkList.add(fileName);
			}else{
				checkList.addAll(fileArray);
			}
			int fileNum=checkList.size();
			for(int j=0;j<fileNum;j++){
				String rowFileName=(String) checkList.get(j);
				if (microConfigHandler != null) {
	
					String value = microConfigHandler.queryConfig(rowFileName,
							attrName);
					if(value!=null){
						field.set(groovyObject, value);
						break;
					}
				} else {
					String value=getValue4file(rowFileName,attrName);
					if(value!=null){
						field.set(groovyObject, value);
						break;
					}
				}
			}

		}
		return proxyObject;
	}
}
