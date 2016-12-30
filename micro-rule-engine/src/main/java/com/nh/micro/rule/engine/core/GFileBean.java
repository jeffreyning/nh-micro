package com.nh.micro.rule.engine.core;

/**
 * 
 * @author ninghao
 * 
 */
public class GFileBean {
	public String ruleName;
	public String rulePath = "";
	public String ruleStamp = "";

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRulePath() {
		return rulePath;
	}

	public void setRulePath(String rulePath) {
		this.rulePath = rulePath;
	}

	public String getRuleStamp() {
		return ruleStamp;
	}

	public void setRuleStamp(String ruleStamp) {
		this.ruleStamp = ruleStamp;
	}

	public Boolean getJarFileFlag() {
		return jarFileFlag;
	}

	public void setJarFileFlag(Boolean jarFileFlag) {
		this.jarFileFlag = jarFileFlag;
	}

	public Boolean jarFileFlag = false;
	public Boolean dirFlag=false;

	public Boolean getDirFlag() {
		return dirFlag;
	}

	public void setDirFlag(Boolean dirFlag) {
		this.dirFlag = dirFlag;
	}
}
