package com.nh.micro.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportParamBean {
private String tableName;
private String bizId;
private String bizCol;
private String page;
private String rows;
private String sort;
private String order;
private String cusSort;
private String cusWhere;
private String cusSelect;
private String id;
private Map requestParamMap;
private String modelName;
private List cusPlaceList;
private String sql;
private String countSql;
private List placeList;
private String cusCol;
private String cusValue;
private String cusCondition;
private String cusSetStr;
private Integer limit;
private List countPlaceList;
public List getCountPlaceList() {
	return countPlaceList;
}

public void setCountPlaceList(List countPlaceList) {
	this.countPlaceList = countPlaceList;
}
private Map sortMap=new HashMap();

public Map getSortMap(){
	return sortMap;
}

public String getTableName() {
	return tableName;
}
public void setTableName(String tableName) {
	this.tableName = tableName;
}
public String getBizId() {
	return bizId;
}
public void setBizId(String bizId) {
	this.bizId = bizId;
}
public String getBizCol() {
	return bizCol;
}
public void setBizCol(String bizCol) {
	this.bizCol = bizCol;
}
public String getPage() {
	return (String) sortMap.get("page");
}
public void setPage(String page) {
	sortMap.put("page", page);
}
public String getRows() {
	return (String) sortMap.get("rows");
}
public void setRows(String rows) {
	sortMap.put("rows", rows);
}
public String getSort() {
	return (String) sortMap.get("sort");
}
public void setSort(String sort) {
	sortMap.put("sort", sort);
}
public String getOrder() {
	return (String) sortMap.get("order");
}
public void setOrder(String order) {
	sortMap.put("order", order);
}
public String getCusSort() {
	return (String)sortMap.get("cusSort");
}
public void setCusSort(String cusSort) {
	sortMap.put("cusSort", cusSort);
}
public String getCusWhere() {
	return cusWhere;
}
public void setCusWhere(String cusWhere) {
	this.cusWhere = cusWhere;
}
public String getCusSelect() {
	return cusSelect;
}
public void setCusSelect(String cusSelect) {
	this.cusSelect = cusSelect;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public Map getRequestParamMap() {
	return requestParamMap;
}
public void setRequestParamMap(Map requestParamMap) {
	this.requestParamMap = requestParamMap;
}
public String getModelName() {
	return modelName;
}
public void setModelName(String modelName) {
	this.modelName = modelName;
}
public List getCusPlaceList() {
	return cusPlaceList;
}
public void setCusPlaceList(List cusPlaceList) {
	this.cusPlaceList = cusPlaceList;
}
public String getSql() {
	return sql;
}
public void setSql(String sql) {
	this.sql = sql;
}
public String getCountSql() {
	return countSql;
}
public void setCountSql(String countSql) {
	this.countSql = countSql;
}
public List getPlaceList() {
	return placeList;
}
public void setPlaceList(List placeList) {
	this.placeList = placeList;
}
public String getCusCol() {
	return cusCol;
}
public void setCusCol(String cusCol) {
	this.cusCol = cusCol;
}
public String getCusValue() {
	return cusValue;
}
public void setCusValue(String cusValue) {
	this.cusValue = cusValue;
}
public String getCusCondition() {
	return cusCondition;
}
public void setCusCondition(String cusCondition) {
	this.cusCondition = cusCondition;
}
public String getCusSetStr() {
	return cusSetStr;
}
public void setCusSetStr(String cusSetStr) {
	this.cusSetStr = cusSetStr;
}
public Integer getLimit() {
	return limit;
}
public void setLimit(Integer limit) {
	this.limit = limit;
}



}
