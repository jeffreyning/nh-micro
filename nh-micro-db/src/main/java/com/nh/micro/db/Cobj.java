package com.nh.micro.db;


/**
 * 
 * @author ninghao
 *
 */
public class Cobj {
	public Cobj(){};
	public Cobj(String defaultSplit){
		this.defaultSplit=defaultSplit;
	}
	public int getIsNullFlag() {
		return isNullFlag;
	}
	public void setIsNullFlag(int isNullFlag) {
		this.isNullFlag = isNullFlag;
	}
	public int getSkipFlag() {
		return skipFlag;
	}
	public void setSkipFlag(int skipFlag) {
		this.skipFlag = skipFlag;
	}
	public String defaultSplit=",";
	public String defaultNull="null";
	public int isNullFlag=1;
	public int skipFlag=0;
	public String getDefaultSplit() {
		return defaultSplit;
	}
	public void setDefaultSplit(String defaultSplit) {
		this.defaultSplit = defaultSplit;
	}
	public String getDefaultNull() {
		return defaultNull;
	}
	public void setDefaultNull(String defaultNull) {
		this.defaultNull = defaultNull;
	}
	public StringBuilder sb = new StringBuilder("");
	public boolean haseStr(){
		if(sb.length()>0){
			return true;
		}
		return false;
	}
	public String getStr() {
		return sb.toString();
	}

	private void appendStr(String str,String split,boolean splitFlag){
		boolean nullFlag=false;
		if(isNullFlag==0){
			if(str==null || str.length()==0){
				nullFlag=true;
			}
		}else if(isNullFlag==1){
			if(str==null){
				nullFlag=true;
			}			
		}
		if(nullFlag){
			if(skipFlag==0){
				return;
			}else if(skipFlag==1){
				if(splitFlag){
					sb.append(split);
				}
				sb.append(defaultNull);
			}
		}else{
			if(splitFlag){
				sb.append(split);
			}
			sb.append(str);
		}
		
	}

	public Cobj append(String str, boolean flag,String split, boolean splitFlag ) {
		if (flag) {
			appendStr(str,split,splitFlag);
		}
		return this;
	}
	public Cobj append(String str, boolean flag,String split) {
		if (flag) {
			boolean splitFlag=haseStr();
			appendStr(str,split,splitFlag);
		}
		return this;
	}
	public Cobj append(String str, boolean flag) {
		if (flag) {
			boolean splitFlag=haseStr();
			appendStr(str,defaultSplit,splitFlag);
		}
		return this;
	}
	public Cobj append(String str) {
		boolean splitFlag=haseStr();
		appendStr(str,defaultSplit,splitFlag);
		return this;
	}
}

