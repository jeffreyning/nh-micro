package com.nh.micro.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author ninghao
 *
 */
public class MicroDbHolder {
public static Map dbHolder=new HashMap();
public static Map readOnlyMap=new HashMap();

public static Map getReadOnlyMap() {
	return readOnlyMap;
}

public static void setReadOnlyMap(Map readOnlyMap) {
	MicroDbHolder.readOnlyMap = readOnlyMap;
}

public static Map getDbHolder() {
	return dbHolder;
}

public void setDbHolder(Map dbHolder) {
	MicroDbHolder.dbHolder = dbHolder;
}

public static Object getDbSource(String sourceName){
	return dbHolder.get(sourceName);
}

public static List<String> getReadOnlyList(String readOnlyName){
	List retList=new ArrayList();
	String readOnlyStr= (String) readOnlyMap.get(readOnlyName);
	if(readOnlyStr!=null){
		String[] tempArray=readOnlyStr.split(",");
		retList=Arrays.asList(tempArray);
	}
	return retList;
}

}
