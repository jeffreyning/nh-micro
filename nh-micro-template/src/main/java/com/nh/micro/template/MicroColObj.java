package com.nh.micro.template;

import java.util.ArrayList;
import java.util.List;

public class MicroColObj {
	public String colInfo = "";
	public List colData = new ArrayList();

	public String getColInfo() {
		return colInfo;
	}

	public void setColInfo(String colInfo) {
		this.colInfo = colInfo;
	}

	public List getColData() {
		return colData;
	}

	public void setColData(List colData) {
		this.colData = colData;
	}
}
