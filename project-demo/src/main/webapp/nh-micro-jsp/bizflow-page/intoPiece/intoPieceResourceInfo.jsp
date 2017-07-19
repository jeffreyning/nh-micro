<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String id=request.getParameter("id");
	Map data=(Map)request.getAttribute("formdata");
	String fileId=(String)data.get("dbcol_ext_fileid");
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>录入信息管理</title>

<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/locale/easyui-lang-zh_CN.js"></script>
 <script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/common.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/zTree/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/icon.css">


<script type="text/javascript">
function add(){
		$("#addOne").form("clear");
		$("#addOne").show();
		$("#uploadFrame").attr("src","<%=path%>/nh-micro-jsp/template-page/jspUpload.jsp");
}	

function addOne(){
	var fileId=$("#uploadFrame").contents().find("#retFileId").val();
	var dataO = new Object();
	dataO.id="<%=data.get("id") %>";
	dataO.fileId=fileId;

 	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=appendResource";
	$.post(url,dataO,function(data,stats){
		if(stats=="success" ){
			$.messager.show({
				msg : "操作成功",
				title : "消息"
			});
			$("#addForm").form("clear");
			$("#addOne").hide();
			window.location.href="<%=path %>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=viewTaskUploadGo&id=<%=data.get("id") %>";
		}
	}); 
	
}
function addCancel() {
	$("#addForm").form("clear");
	$("#addOne").hide();
}
$(function(){
	$("#addOne").hide();
});
</script>
</head>
<body class="easyui-layout" width="100%" height="450px">
	<div id="addOne" class="" region="north" 
		style="height: 100px">
		<div id="buttons"
			style="margin-top: 5px; margin-left: 5px; padding-bottom: 5px;">
			<a class="easyui-linkbutton" href="javascript:addOne();">确认</a>
			<a class="easyui-linkbutton" href="javascript:addCancel();">取消</a>
		</div>		
		<form id="addForm" invalidate action="" method="post" enctype="multipart/form-data" >
		<%-- <input type="hidden" id="id" name="id" value="<%=data.get("id") %>"></input> --%>
		<iframe id="uploadFrame" marginwidth="0" marginheight="0" frameborder="0" style="border:0;width:100%;height:100%;" 
		src="<%=path%>/nh-micro-jsp/template-page/jspUpload.jsp">
		</iframe>

		</form>
	</div> 
	<div id="infoQuery" class="" region="center"
		style="height: 300px">
		<button onclick="add()">上传资料</button><br>
		图片预览<br>
		<%
		List picList=(List)data.get("dbcol_ext_fileid_json");
		if(picList!=null){
		for(int i=0;i<picList.size();i++){
			String theFileId=(String)picList.get(i);
		
		%>
	<img heigth="300px" src="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=micro_attachment_list&groovySubName=execDownLoadFile&fileId=<%=theFileId %>"></img><br>
		<%
		}}
		%>
	</div> 
	



</body>
</html>