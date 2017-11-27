package com.nh.micro.orm;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author ninghao
 *
 */
public class MicroBeanMapUtil {

	public static Map getFieldMap(Class cls) {
		Map map = new HashMap();
		Field[] fields = cls.getDeclaredFields();
		int size = fields.length;
		for (int i = 0; i < size; i++) {
			Field field = fields[i];
			MicroMappingAnno anno = field.getAnnotation(MicroMappingAnno.class);
			if (anno == null) {
				continue;
			}
			String keyName = anno.name();
			String fieldName = field.getName();
			map.put(fieldName, keyName);
		}
		return map;
	}

	public static Map getKeyMap(Class cls) {
		Map map = new HashMap();
		Field[] fields = cls.getDeclaredFields();
		int size = fields.length;
		for (int i = 0; i < size; i++) {
			Field field = fields[i];
			MicroMappingAnno anno = field.getAnnotation(MicroMappingAnno.class);
			if (anno == null) {
				continue;
			}
			String keyName = anno.name();
			String fieldName = field.getName();
			map.put(keyName, fieldName);
		}
		return map;
	}

	/**
	 * @param beanObj
	 * 
	 * @param fieldName
	 * 
	 * @param value
	 */
	public static void setBeanProperty(Object beanObj, String fieldName,
			Object value) throws Exception {
		Class cls = beanObj.getClass();
		Field field = cls.getDeclaredField(fieldName);
		Class fieldCls = field.getType();
		field.setAccessible(true);
		Object fieldObj = str2Obj(fieldCls, value);
		field.set(beanObj, fieldObj);
	}
	
	public static Object getBeanProperty(Object beanObj, String fieldName) throws Exception {
		Class cls = beanObj.getClass();
		Field field = cls.getDeclaredField(fieldName);
		Class fieldCls = field.getType();
		field.setAccessible(true);
		Object retVal=field.get(beanObj);
		Object retObj=obj2Str(fieldCls,retVal);
		return retObj;
	}
	
	

	public static Object str2Obj(Class cls, Object val) throws Exception {
		if(val==null){
			return null;
		}
		Object retObj = null;
		if (cls.isAssignableFrom(String.class)) {
			retObj = new String((String) val);
		}
		if (cls.isAssignableFrom(Integer.class)) {
			retObj = Integer.valueOf((String) val);
		}
		if (cls.isAssignableFrom(Long.class)) {
			retObj = Long.valueOf((String) val);
		}
		if (cls.isAssignableFrom(BigDecimal.class)) {
			retObj = new BigDecimal((String) val);
		}
		if (cls.isAssignableFrom(Float.class)) {
			retObj = Float.valueOf((String) val);
		}
		if (cls.isAssignableFrom(Double.class)) {
			retObj = Double.valueOf((String) val);
		}
		if (cls.isAssignableFrom(Date.class)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String tempVal=(String) val;
			if(tempVal.length()==10){
				tempVal=tempVal+" 00:00:00";
			}
			retObj = (Date)sdf.parseObject(tempVal);
		}
		
		if (cls.isAssignableFrom(Boolean.class)) {
			retObj = Boolean.valueOf((String) val);
		}
		if (cls.isAssignableFrom(List.class)) {
			retObj = val;
		}
		if (cls.isAssignableFrom(Short.class)) {
			retObj = Short.valueOf((String) val);
		}
		if (cls.isAssignableFrom(Byte.class)) {
			retObj = Byte.valueOf((String) val);
		}		
		
		if (cls.isAssignableFrom(Map.class)) {
			retObj = val;
		}
		
		return retObj;
	}

	
	public static Object obj2Str(Class cls, Object val) throws Exception {
		if(val==null){
			return null;
		}
		Object retObj = null;
		if (cls.isAssignableFrom(String.class)) {
			retObj = new String((String) val);
		}
		if (cls.isAssignableFrom(Integer.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Long.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(BigDecimal.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Float.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Double.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Date.class)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			retObj=sdf.format(val);
		}
		
		if (cls.isAssignableFrom(Boolean.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(List.class)) {
			retObj = val;
		}
		if (cls.isAssignableFrom(Short.class)) {
			retObj = val.toString();
		}
		if (cls.isAssignableFrom(Byte.class)) {
			retObj = val.toString();
		}		
		
		if (cls.isAssignableFrom(Map.class)) {
			retObj = val;
		}
		
		return retObj;
	}	
	
	public static Object mapToBean(Map paramMap, Class cls) throws Exception {
		Object retObj = cls.newInstance();
		mapToBean(paramMap,retObj);
		return retObj;
	}
	
	
	public static void mapToBean(Map paramMap, Object beanObj) throws Exception {
		Class cls=beanObj.getClass();
		Map mappingInfo = getKeyMap(cls);
		Object retObj = beanObj;
		Set<String> keySet = paramMap.keySet();
		for (String key : keySet) {
			if (mappingInfo.get(key) == null) {
				continue;
			}
			Object val = paramMap.get(key);
			String fieldName = (String) mappingInfo.get(key);
			setBeanProperty(retObj, fieldName, val);
		}

	}	
	
	public static void mapToBean4NotNull(Map paramMap, Object beanObj) throws Exception {
		Class cls=beanObj.getClass();
		Map mappingInfo = getKeyMap(cls);
		Object retObj = beanObj;
		Set<String> keySet = paramMap.keySet();
		for (String key : keySet) {
			if (mappingInfo.get(key) == null) {
				continue;
			}
			Object val = paramMap.get(key);
			if(val==null){
				continue;
			}
			String fieldName = (String) mappingInfo.get(key);
			setBeanProperty(retObj, fieldName, val);
		}

	}	
	
	public static void mapToBean4FullField(Map paramMap, Object beanObj) throws Exception {
		Class cls=beanObj.getClass();
		Map mappingInfo = getKeyMap(cls);
		Object retObj = beanObj;
		Set<String> keySet = mappingInfo.keySet();
		for (String mapKey : keySet) {
			Object val = paramMap.get(mapKey);
			String fieldName = (String) mappingInfo.get(mapKey);
			setBeanProperty(retObj, fieldName, val);
		}
	}	
	
	public static void beanToMap(Object beanObj, Map paramMap) throws Exception {
		Class cls=beanObj.getClass();
		Map fieldMap = getFieldMap(cls);

		Set<String> keySet = fieldMap.keySet();
		for (String fieldKey : keySet) {

			String mapKey = (String) fieldMap.get(fieldKey);
			Object val=getBeanProperty(beanObj, fieldKey);
			paramMap.put(mapKey, val);
		}
	}	

	public static void beanToMap4NotNull(Object beanObj, Map paramMap) throws Exception {
		Class cls=beanObj.getClass();
		Map fieldMap = getFieldMap(cls);

		Set<String> keySet = fieldMap.keySet();
		for (String fieldKey : keySet) {

			String mapKey = (String) fieldMap.get(fieldKey);
			Object val=getBeanProperty(beanObj, fieldKey);
			if(val==null){
				continue;
			}
			paramMap.put(mapKey, val);
		}
	}	
	
	public static Map beanToMap(Object beanObj) throws Exception {
		Map retMap=new HashMap();
		beanToMap(beanObj,retMap);
		return retMap;

	}
	

	
}
