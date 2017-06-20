package com.nh.micro.db;


/**
 * 
 * @author ninghao
 *
 */
public class MicroDbModelEntry {
	public MicroDbModelEntry(){
		
	}
	public String colId;
	public String metaId;
	public String metaContentId;

	public String colName;
	public Class colType;
	public String remark;
	public String functionReplace;
	public boolean isMetaTable=false;
	
	public String getFunctionReplace() {
		return functionReplace;
	}
	public void setFunctionReplace(String functionReplace) {
		this.functionReplace = functionReplace;
	}

	public boolean isMetaTable() {
		return isMetaTable;
	}
	public void setMetaTable(boolean isMetaTable) {
		this.isMetaTable = isMetaTable;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getColId() {
		return colId;
	}
	public void setColId(String colId) {
		this.colId = colId;
	}
	public String getMetaId() {
		return metaId;
	}
	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public Class getColType() {
		return colType;
	}
	public void setColType(Class colType) {
		this.colType = colType;
	}
	public String getMetaContentId() {
		return metaContentId;
	}
	public void setMetaContentId(String metaContentId) {
		this.metaContentId = metaContentId;
	}
	@Deprecated
	public MicroDbModelEntry(String colId,String metaId,String colName,Class colType,String remark,boolean isMetaTable){
		this.colId=colId;
		if(isMetaTable==false){
			this.metaId=metaId;
		}else{
			if(metaId.contains("->")){
				this.metaId=metaId;
				int si=metaId.indexOf("->");
				this.metaContentId=metaId.substring(0,si);			
			}else{
				this.metaId=metaId;
				this.metaContentId="meta_content";
			}
		}
		this.colName=colName;
		this.colType=colType;
		this.remark=remark;
		this.isMetaTable=isMetaTable;
	}
	
	@Deprecated
	public MicroDbModelEntry(String colId,String metaId,String colName,Class colType,String remark){
		this.colId=colId;
		if(metaId.contains("->")){
			this.metaId=metaId;
			int si=metaId.indexOf("->");
			this.metaContentId=metaId.substring(0,si);
			this.isMetaTable=true;
		}else{
			this.metaId=metaId;
			this.isMetaTable=false;
		}
		this.colName=colName;
		this.colType=colType;
		this.remark=remark;
	}
	@Deprecated
	public MicroDbModelEntry(String colId,String colName,Class colType,String remark){
		this.colId=colId;
		//this.metaId="meta_content->>'$."+colId+"'";
		this.metaId=colId;
		//this.metaContentId="meta_content";
		this.colName=colName;
		this.colType=colType;
		this.remark=remark;
	}	
	
	public MicroDbModelEntry(String colId,String colName,String metaContentId,String remark,Class colType,boolean isMetaTable){
		this.colId=colId;
		if(isMetaTable==false){
			this.metaId=colId;
		}else{
			this.metaContentId=metaContentId;
			this.metaId=metaContentId+"->>'$."+colId+"'";
		}
		this.isMetaTable=isMetaTable;
		this.colName=colName;
		this.colType=colType;
		this.remark=remark;
	}	
	@Deprecated
	public MicroDbModelEntry(String colId,String colName,Class colType,String remark,boolean isMetaTable){
		this.colId=colId;
		if(isMetaTable==true){
			this.metaId="meta_content->>'$."+colId+"'";
			this.metaContentId="meta_content";
		}else{
			this.metaId=colId;
		}

		this.isMetaTable=isMetaTable;
		this.colName=colName;
		this.colType=colType;
		this.remark=remark;
	}
	
	@Deprecated
	public MicroDbModelEntry(String colId,String metaId,String colName,Class colType,String remark,boolean isMetaTable,String functionReplace){
		this.colId=colId;
		if(isMetaTable==false){
			this.metaId=metaId;
		}else{
			if(metaId.contains("->")){
				this.metaId=metaId;
				int si=metaId.indexOf("->");
				this.metaContentId=metaId.substring(0,si);			
			}else{
				this.metaId=metaId;
				this.metaContentId="meta_content";
			}
		}

		if(metaId!=null && metaId.contains("<REPLACE>")){
			this.metaId=metaId.replace("<REPLACE>", colId);
		}
		this.colName=colName;
		this.colType=colType;
		this.remark=remark;
		this.isMetaTable=isMetaTable;
		this.functionReplace=functionReplace;
	}
	
	@Deprecated
	public MicroDbModelEntry(String colId,String metaId,String colName,Class colType,String remark,String functionReplace){
		this.colId=colId;
		if(metaId.contains("->")){
			this.metaId=metaId;
			int si=metaId.indexOf("->");
			this.metaContentId=metaId.substring(0,si);
			this.isMetaTable=true;
		}else{
			this.metaId=metaId;
			this.isMetaTable=false;
		}
		this.colName=colName;
		this.colType=colType;
		this.remark=remark;
		this.functionReplace=functionReplace;
	}
	
	@Deprecated
	public MicroDbModelEntry(String colId,String colName,Class colType,String remark,String functionReplace){
		this.colId=colId;
		this.metaId=colId;
		//this.metaId="meta_content->>'$."+colId+"'";
		//this.metaContentId="meta_content";
		this.colName=colName;
		this.colType=colType;
		this.remark=remark;
		this.functionReplace=functionReplace;
	}	
	
	@Deprecated
	public String replaceMetaId(String prefix){
		String tempMetaId=metaId;
		if(prefix!=null && !"".equals(prefix)){
			tempMetaId=prefix+"."+tempMetaId;
		}
		if(functionReplace!=null && functionReplace.contains("<REPLACE>")){
			String temp=functionReplace.replace("<REPLACE>", tempMetaId);
			return temp;
		}else{
			return tempMetaId;
		}
	}
	
	@Deprecated
	public String getMetaAs(){
		
		return replaceMetaId("")+" as "+colId;
	}
	
	@Deprecated
	public String getMeta(){
		if(isMetaTable){
			return replaceMetaId("");
		}
		return colId;
	}
	
	@Deprecated
	public String getMetaAs(String prefix,String split){
		if(split==null){
			split="_";
		}
		return replaceMetaId(prefix)+" as "+prefix+split+colId;
	}
	
	@Deprecated
	public String getMetaAs(String prefix){

		return replaceMetaId(prefix)+" as "+colId;
	}
	
	@Deprecated
	public String getMeta(String prefix){
		if(isMetaTable){
			return replaceMetaId(prefix);
		}
		return prefix+"."+colId;
	}
	public String getColId(String prefix,String split){
		if(split==null){
			split="_";
		}
		return prefix+split+colId;
	}
	public String getColId(String prefix) {
		return prefix+"."+colId;
	}
}
