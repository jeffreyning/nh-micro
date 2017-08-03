package com.project.util;

import java.util.HashMap;
import java.util.Map;

public class CacheUtils {
	public static Map cacheMap=new HashMap();
	
	public static Object get(String key){
		return cacheMap.get(key);
	}
	
	public static void set(String key,Object value){
		cacheMap.put(key,value);
	}

	public static void set(String key,Object value,Long time){
		cacheMap.put(key,value);
	}
	public static boolean exists(String key){
		return cacheMap.containsKey(key);
	}
	
	public static void del(String key){
		cacheMap.remove(key);
	}
}
