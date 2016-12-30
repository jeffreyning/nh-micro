package com.nh.micro.rule.engine.core;

public class GOutputParam {
	public Integer getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(Integer resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}

	public Integer resultStatus = 0;
	public String resultCode = "";
	public String resultMsg = "";
	public Object resultObj = null;

	public GOutputParam() {
	};

	public GOutputParam(Object resultObj) {
		this.resultObj = resultObj;
	};
}
