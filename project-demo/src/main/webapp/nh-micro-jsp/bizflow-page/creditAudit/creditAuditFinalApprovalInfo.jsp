<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String id=request.getParameter("id");
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>录入信息管理</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/common.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/zTree/css/zTreeStyle/zTreeStyle.css">
<style type="text/css">
.ntable {border-collapse:collapse;border-color:black;width:100%;border-style:solid;border-width:1px;}
.ntable td {border-style:solid;border-width:1px;}
</style>


<script type="text/javascript">



function submit_approval(){
	var dataO = $('#approval_form').serializeObject();
	dataO.id="<%=id%>";

 	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_creditaudit_final_approval&groovySubName=submitApproval";
	$.post(url,dataO,function(data,stats){
		if(stats=="success" ){
			$.messager.show({
				msg : "操作成功",
				title : "消息"
			});
		}
	}); 
} 


$(function(){

});
	


</script>
</head>
<body class="easyui-layout">
<c:if test="${operFlag=='update'}">
<button onclick="submit_approval()">提交信审审批终审意见</button>
</c:if>


<form id="approval_form">
<table class="ntable" cellspacing=0 cellpadding=0>
<tr>
<td>批贷额度<input type="text" name="dbcol_ext_approval_credit_amount" value="${formdata.dbcol_ext_approval_credit_amount}"></input></td>
</tr>
<tr>
<td>信审审批终审意见</td>
</tr>
<tr>
<td>
<input type="radio" name="approval_status" value="yes" 
<c:if test="${formdata.dbcol_ext_bizflow_creditaudit_final_approval_status=='yes'}">checked="true"</c:if>
>同意</input>
&nbsp;
<input type="radio" name="approval_status" value="no" 
<c:if test="${formdata.dbcol_ext_bizflow_creditaudit_final_approval_status=='no'}">checked="true"</c:if>
>不同意</input>
&nbsp;
<input type="radio" name="approval_status" value="back" 
<c:if test="${formdata.dbcol_ext_bizflow_creditaudit_final_approval_status=='back'}">checked="true"</c:if>
>打回</input>
</td>
</tr>
<tr>
<td><textArea name="approval_text">${formdata.dbcol_ext_bizflow_creditaudit_final_approval_text}</textArea></td>
</tr>
</table>
</form>



</body>
</html>