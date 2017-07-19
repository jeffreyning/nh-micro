

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



function createTaskData(){
	var dataO = $('#taskDataForm').serialize();

 	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=createInfo";
	$.post(url,dataO,function(data,stats){
		if(stats=="success" ){
			$.messager.show({
				msg : "操作成功",
				title : "消息"
			});
		}
	}); 
} 


function updateTaskData(){
	if(!$('#taskDataForm').form('validate')){
      alert("表单填写有误");
		return;
	}  
	var dataO = $('#taskDataForm').serialize();

 	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=updateInfo&id=${formdata.id}";
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
<c:if test="${ operFlag=='create'}">
<button onclick="createTaskData();">提交数据</button>
</c:if>
<c:if test="${ operFlag=='update'}">
<button onclick="updateTaskData();">更新数据</button>
</c:if>
<c:if test="${ operFlag=='view'}">

</c:if>
<form id="taskDataForm" >
<table class="ntable" cellspacing=0 cellpadding=0>
<tr>
<td>表单标题</td><td><input type="text" id="remark" name="remark" value="${formdata.remark}"></input></td>
<td>表单ID</td><td><input readonly="true" type="text" id="meta_key" name="meta_key" value="${formdata.meta_key}"></input></td>
</tr>  
<tr>
<td>申请产品</td><td colspan="">
<input type="text" class="easyui-validatebox" id="dbcol_ext_application_credit_product" name="dbcol_ext_application_credit_product"  value="${ formdata.dbcol_ext_application_credit_product}"></input>
</td>
<td>申请额度</td><td colspan="">
<input type="text" class="easyui-validatebox" id="dbcol_ext_application_credit_amount" name="dbcol_ext_application_credit_amount"  value="${ formdata.dbcol_ext_application_credit_amount}"></input>
</td>
</tr>
<tr>
<td>客户姓名</td><td colspan="">
    <textArea id="dbcol_ext_name" name="dbcol_ext_name" >${ formdata.dbcol_ext_name}</textArea>

  </td>
<td>客户生日</td><td colspan="">
  
<input id="dbcol_ext_customer_birthday" type="text" class="easyui-datebox" name="dbcol_ext_customer_birthday"></input>  </td>
</tr>
<tr>
<td>客户身份证</td><td colspan="">
<input type="text" class="easyui-validatebox" id="dbcol_ext_customer_card_id" name="dbcol_ext_customer_card_id"  value="${ formdata.dbcol_ext_customer_card_id}"></input>
</td>
<td>客户手机</td><td colspan="">
<input type="text" class="easyui-validatebox" id="dbcol_ext_customer_mobile" name="dbcol_ext_customer_mobile"  value="${ formdata.dbcol_ext_customer_mobile}"></input>
</td>
</tr>
<tr>
<td>客户学历</td><td colspan="">
  <input type="radio" name="dbcol_ext_customer_xueli" value="1" <c:if test="${ formdata.dbcol_ext_customer_xueli=='1'}">checked="checked"</c:if>>大专</input>
  <input type="radio" name="dbcol_ext_customer_xueli" value="2" <c:if test="${ formdata.dbcol_ext_customer_xueli=='2'}">checked="checked"</c:if>>大本</input>
  <input type="radio" name="dbcol_ext_customer_xueli" value="3" <c:if test="${ formdata.dbcol_ext_customer_xueli=='3'}">checked="checked"</c:if>>硕士</input>  
</td>
<td>客户籍贯</td><td colspan="">
    <select id="dbcol_ext_customer_jiguan" name="dbcol_ext_customer_jiguan" >
    <option value="beijing" <c:if test="${ formdata.dbcol_ext_customer_jiguan=='beijing'}"> selected = selected </c:if>>北京</option>
    <option value="shanghai" <c:if test="${ formdata.dbcol_ext_customer_jiguan=='shanghai'}"> selected = selected </c:if>>上海</option>
  </select>
</td>
</tr>
<tr>
<td></td><td colspan="">
</td>
<td>客户住址</td><td colspan="">
<input type="text" class="easyui-validatebox" id="dbcol_ext_customer_address" name="dbcol_ext_customer_address"  required  value="${ formdata.dbcol_ext_customer_address}"></input>
</td>
</tr>
<tr>
<td>客户性别</td><td colspan="">
<input type="text" class="easyui-validatebox" id="dbcol_ext_customer_sex" name="dbcol_ext_customer_sex"  value="${ formdata.dbcol_ext_customer_sex}"></input>
</td>
<td>营业员</td><td colspan="">
<input type="text" class="easyui-validatebox" id="dbcol_ext_employee" name="dbcol_ext_employee"  required  value="${ formdata.dbcol_ext_employee}"></input>
</td>
</tr>
<tr>
<td>客户年龄</td><td colspan="">
<input type="text" class="easyui-validatebox" id="dbcol_ext_customer_age" name="dbcol_ext_customer_age"  value="${ formdata.dbcol_ext_customer_age}"></input>
</td>
</tr>
</table>
</form>


</body>
</html>









