package com.nh.micro.rule.engine.core;
/**
 * 
 * @author ninghao
 *
 */
public class GInputParam {
	public GInputParam() {
	};

	public GInputParam(Object paramData) {
		this.paramData = paramData;
	}

	public Object paramData = null;

	public Object getParamData() {
		return paramData;
	}

	public void setParamData(Object paramData) {
		this.paramData = paramData;
	}
	public String subName=null;

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}



}
