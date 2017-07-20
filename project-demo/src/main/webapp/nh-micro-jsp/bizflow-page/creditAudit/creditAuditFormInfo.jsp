<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();

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



function updateTaskData(){
	var dataO = $('#taskDataForm').serialize();

 	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=updateInfo";
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
<button onclick="updateTaskData();">更新数据</button>
</c:if>
<c:if test="${operFlag=='view'}">

</c:if>
<form id="taskDataForm" >

<table class="ntable" cellspacing=0 cellpadding=0>
<tr>
<td>表单标题</td><td><input type="text" id="remark" name="remark" value="${formdata.remark}"></input></td>
<td>表单ID</td><td><input readonly="true" type="text" id="meta_key" name="meta_key" value="${formdata.meta_key}"></input></td>
</tr>
<tr>
<td>客户姓名</td><td><input type="text" id="dbcol_ext_name" name="dbcol_ext_customer_name" value="${formdata.dbcol_ext_customer_name}"></input></td>
<td>客户身份证</td><td><input type="text" id="dbcol_ext_age" name="dbcol_ext_customer_card_id" value="${formdata.dbcol_ext_customer_card_id}"></input></td>
</tr>
<tr>
<td>申请额度</td><td><input type="text" id="dbcol_ext_jiguan" name="dbcol_ext_application_credit_amount" value="${formdata.dbcol_ext_application_credit_amount}"></input></td>
<td>批贷额度</td><td><input type="text" id="dbcol_ext_work" name="dbcol_ext_approval_credit_amount" value="${formdata.dbcol_ext_approval_credit_amount}"></input></td>
</tr>
</table>
</form>


</body>
</html>