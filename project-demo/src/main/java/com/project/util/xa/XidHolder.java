package com.project.util.xa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.xa.XAResource;

public class XidHolder {
	public static Map xidHolder = new HashMap();

	public static void appendXa(String uuid, MyXid myXid,XAResource xaResource) {
		List xaList=(List) xidHolder.get(uuid);
		if(xaList==null){
			xaList=new ArrayList();
			xidHolder.put(uuid, xaList);
		}
		Map xaMap=new HashMap();
		xaMap.put("myXid", myXid);
		xaMap.put("xaResource", xaResource);
		xaList.add(xaMap);

	}

	public static List getXaList(String uuid) {
		List xaList=(List) xidHolder.get(uuid);
		return xaList;
	}
	
	public static void removeXaList(String uuid){
		if(xidHolder.containsKey(uuid)){
			xidHolder.remove(uuid);
		}
	}
}
