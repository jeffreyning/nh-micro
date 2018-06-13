package com.nh.micro.config;

import groovy.lang.GroovyObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
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
			if (microConfigHandler != null) {

				String value = microConfigHandler.queryConfig(fileName,
						attrName);

				field.set(groovyObject, value);
			} else {

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
					field.set(groovyObject, value);

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

					field.set(groovyObject, value);
				}
			}

		}
		return proxyObject;
	}
}
