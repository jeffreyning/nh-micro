package com.nh.micro.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GroovyDbSwitcher {
    @SuppressWarnings("rawtypes")  
    private static final ThreadLocal<Map> contextHolderMap = new ThreadLocal();   
 
    public static void removeLocal(){
    	contextHolderMap.remove();
    }
    
    public static String peekCurrentDataSource(String oldDbName) {  
    	Map map=contextHolderMap.get();
    	if(map==null){
    		return null;
    	}
    	LinkedList list=(LinkedList) map.get(oldDbName);
    	if(list!=null && list.size()>0){
    		return (String) list.peek();
    	}
        return null; 
    }    
 
    public static void popCurrentDataSource(String oldDbName) {  
    	Map map=contextHolderMap.get();
    	LinkedList list=(LinkedList) map.get(oldDbName);
    	if(list!=null && list.size()>0){
    		list.pop();
    	}
    	
    } 
    public static void pushCurrentDataSource(String oldDbName, String currentDbName) {  
    	Map map=contextHolderMap.get();
    	if(map==null){
    		map=new HashMap();
    		contextHolderMap.set(map);
    	}
    	LinkedList list=(LinkedList) map.get(oldDbName);
    	if(list==null){
    		list=new LinkedList();
    		map.put(oldDbName, list);
    	}
    	list.push(currentDbName);

    	
    }    
}
