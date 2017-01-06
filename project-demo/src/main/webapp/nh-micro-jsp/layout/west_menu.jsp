<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path=request.getContextPath();
%>

[
{
	"id":1,
	"text":"管理",
	"iconCls":"icon-channels",
	"children":[{
		"id":12,
		"text":"日表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/jsp/test.jsp"
		  }
	},
	{
		"id":13,
		"text":"月表",
		"iconCls":"icon-nav",
		"attributes":{
		  "url":"/<%=path %>/jsp/test.jsp"
		  }
	}
	]
}
 
]