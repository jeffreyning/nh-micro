<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/common.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/zTree/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript">
function submitModify(){
	var reInput=$('#reInput').val();
	var newPass=$('#newPass').val();
	if(newPass==null || newPass=="" || newPass!=reInput){
		alert('新密码输入不正确！');
		return;
	}
	var url='<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_user&groovySubName=modifyPassByOld';
	var data=new Object();
	data.oldPass=$("#oldPass").val();
	data.newPass=$('#newPass').val();
	$.ajax({
		url:url,
		data:data,
		type:'post',
		dataType:'json',
		success:function(data,status){
			if(JSON.parse(data.resultData).resultObj>0){
				alert('修改完成');
			}else{
				alert('修改失败！');
			}
		}
	});
}
</script>
</head>
<body class="easyui-layout">


	<div id="roleList" region="center">
	<table>
	<tr><td>原密码</td><td><input type="password" id="oldPass"></input></td></tr>
	<tr><td>新密码</td><td><input type="password" id="newPass"></input></td></tr>
	<tr><td>新密码重复输入</td><td><input type="password" id="reInput"></input></td></tr>
	</table>
	<button onclick="submitModify()">提交修改</button>
	</div>



</body>
</html>