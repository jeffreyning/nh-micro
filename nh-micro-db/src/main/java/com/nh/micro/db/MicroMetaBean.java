package com.nh.micro.db;

import java.util.Date;

/**
 * 
 * @author ninghao
 *
 */
public class MicroMetaBean {
public String id;
public String meta_key;
public String meta_name;
public String meta_type;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getMeta_key() {
	return meta_key;
}
public void setMeta_key(String meta_key) {
	this.meta_key = meta_key;
}
public String getMeta_name() {
	return meta_name;
}
public void setMeta_name(String meta_name) {
	this.meta_name = meta_name;
}
public String getMeta_type() {
	return meta_type;
}
public void setMeta_type(String meta_type) {
	this.meta_type = meta_type;
}
public String getMeta_content() {
	return meta_content;
}
public void setMeta_content(String meta_content) {
	this.meta_content = meta_content;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public Date getCreate_time() {
	return create_time;
}
public void setCreate_time(Date create_time) {
	this.create_time = create_time;
}
public Date getUpdate_time() {
	return update_time;
}
public void setUpdate_time(Date update_time) {
	this.update_time = update_time;
}
public String meta_content;
public String remark;
public Date create_time;
public Date update_time;
}
