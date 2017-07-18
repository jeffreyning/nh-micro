<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path=request.getContextPath();
%>

[
{
	"id":1,
	"text":"系统管理",
	"iconCls":"icon-channels",
	"children":[{
		"id":12,
		"text":"用户列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/user-manager/listUserInfo.jsp"
		  }
	},{
		"id":16,
		"text":"角色列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/user-manager/listRoleInfo.jsp"
		  }
	},{
		"id":17,
		"text":"部门列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/user-manager/listDeptInfo.jsp"
		  }
	},
	{
		"id":14,
		"text":"字典列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/dictionary-page/listDictionaryInfo.jsp"
		  }
	},
	{
		"id":15,
		"text":"菜单管理",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/user-manager/listMenuInfo.jsp"
		  }
	}	  
	]
}
,
{
	"id":2,
	"text":"产品中心管理",
	"iconCls":"icon-channels",
	"children":[{
		"id":21,
		"text":"产品列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/product-center-page/listProductCenterInfo.jsp"
		  }
	},
	  {
		"id":22,
		"text":"产品合同模板列表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/nh-micro-jsp/contract-page/listContractTempInfo.jsp"
		  }
	}
	]
}

]