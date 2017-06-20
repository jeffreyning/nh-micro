package com.nh.micro.db;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author ninghao
 *
 */
public abstract class IMicroDbModel  {
	public static List<MicroDbModelEntry> getEntryList(Class cls) throws Exception {
		List entryList=new ArrayList();
		
		Field[] fields=cls.getDeclaredFields();
		for(Field field:fields){
			if(field.getType().equals(MicroDbModelEntry.class)){
				entryList.add(field.get(cls));
			}
		}
		Class superCls=cls.getSuperclass();
		if(superCls==null || superCls.equals(Object.class)){
			return entryList;
		}
		List superEntryList=getEntryList(superCls);
		if(superEntryList!=null){
			entryList.addAll(superEntryList);
		}
		return entryList;
	}
	
	
	public static List<MicroDbModelEntry> getEntryList(Class cls,Boolean flag) throws Exception {
		List entryList=new ArrayList();
		
		Field[] fields=cls.getDeclaredFields();
		for(Field field:fields){
			if(field.getType().equals(MicroDbModelEntry.class)){
				entryList.add(field.get(cls));
			}
		}
		if(flag){
			Class superCls=cls.getSuperclass();
			if(superCls==null || superCls.equals(Object.class)){
				return entryList;
			}
			List superEntryList=getEntryList(superCls);
			if(superEntryList!=null){
				entryList.addAll(superEntryList);
			}
		}
		return entryList;
	}
	
	
	public static String allMetaAsStr(Class cls) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls);
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getMetaAs()).append(",");
		}
		int length=sb.length();
		sb.deleteCharAt(length-1);
		return sb.toString();
	}

	public static String allMetaStr(Class cls) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls);		
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getColId()).append(",");
		}
		int length=sb.length()-1;
		sb.deleteCharAt(length-1);
		return sb.toString();
	}	
	
	
	public static String allMetaAsStr(Class cls,String prefix,String split) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls);
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getMetaAs(prefix,split)).append(",");
		}
		int length=sb.length();
		sb.deleteCharAt(length-1);
		return sb.toString();
	}
	
	public static String allMetaAsStr(Class cls,String prefix) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls);
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getMetaAs(prefix)).append(",");
		}
		int length=sb.length();
		sb.deleteCharAt(length-1);
		return sb.toString();
	}

	public static String allMetaStr(Class cls,String prefix) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls);		
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getColId(prefix)).append(",");
		}
		int length=sb.length()-1;
		sb.deleteCharAt(length-1);
		return sb.toString();
	}

	
	
	public static String allMetaAsStr(Class cls,Boolean flag) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls,flag);
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getMetaAs()).append(",");
		}
		int length=sb.length();
		sb.deleteCharAt(length-1);
		return sb.toString();
	}

	public static String allMetaStr(Class cls,Boolean flag) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls,flag);		
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getColId()).append(",");
		}
		int length=sb.length()-1;
		sb.deleteCharAt(length-1);
		return sb.toString();
	}	
	
	
	public static String allMetaAsStr(Class cls,String prefix,String split,Boolean flag) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls,flag);
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getMetaAs(prefix,split)).append(",");
		}
		int length=sb.length();
		sb.deleteCharAt(length-1);
		return sb.toString();
	}
	
	public static String allMetaAsStr(Class cls,String prefix,Boolean flag) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls,flag);
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getMetaAs(prefix)).append(",");
		}
		int length=sb.length();
		sb.deleteCharAt(length-1);
		return sb.toString();
	}

	public static String allMetaStr(Class cls,String prefix,Boolean flag) throws Exception{
		List<MicroDbModelEntry> modelEntrys=getEntryList(cls,flag);		
		StringBuilder sb=new StringBuilder();
		for(MicroDbModelEntry modelEntry:modelEntrys){
			sb.append(modelEntry.getColId(prefix)).append(",");
		}
		int length=sb.length()-1;
		sb.deleteCharAt(length-1);
		return sb.toString();
	}
	
	
	
	
}
