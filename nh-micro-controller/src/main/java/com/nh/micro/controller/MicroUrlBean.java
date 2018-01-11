package com.nh.micro.controller;

import java.util.TreeMap;

/**
 * 
 * @author ninghao
 *
 */
public class MicroUrlBean {
public String[] defaultInfo=null;
public TreeMap treeMap=new TreeMap();
public String[] getDefaultInfo() {
	return defaultInfo;
}
public void setDefaultInfo(String[] defaultInfo) {
	this.defaultInfo = defaultInfo;
}
public TreeMap getTreeMap() {
	return treeMap;
}
public void setTreeMap(TreeMap treeMap) {
	this.treeMap = treeMap;
}
}
