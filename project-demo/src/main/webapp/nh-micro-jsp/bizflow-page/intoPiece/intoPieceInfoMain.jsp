<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String id=request.getParameter("id");
	String sourceFlag=request.getParameter("sourceFlag");
	String formOperFlag="view";
	String qaApprovalOperFlag="view";
	if(sourceFlag!=null && "qaApproval".equals(sourceFlag)){
		qaApprovalOperFlag="update";
	}
	if(sourceFlag!=null && "updateForm".equals(sourceFlag)){
		formOperFlag="update";
	}

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
<style>
li.dLi{display:inline-block;vertical-align:top;_zoom:1;_display:inline;margin:0 3px 3px 3px;background:#fff;}
</style>

<script type="text/javascript">

$(function(){

});
	


</script>
</head>
<body >
	<div id="testtab">
		<ul id="xinshen_nrID" style="display:block;margin-left:0px; border-top:1px solid #e2e2e2;">
			<li id="XinShenJiaGou_left" class="dLi" style="width:100%;">
				<div>
					<p >进件信息查看和结果填写</p>
				</div>
				<div id="testtab0" class="easyui-tabs" >
					<div title="申请信息" >
<iframe marginwidth="0" 
src="<%=path %>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=viewTaskDataGo&id=<%=id %>&operFlag=<%=formOperFlag %>"
 marginheight="0" id="infoReviewFrame" frameborder="0" style="border:0;width:100%;height:500px;"></iframe>					
					</div>
					<div title="资料上传" >
<iframe marginwidth="0" 
src="<%=path %>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=viewTaskUploadGo&id=<%=id %>&operFlag=<%=formOperFlag %>"
 marginheight="0" id="infoReviewFrame" frameborder="0" style="border:0;width:100%;height:500px;"></iframe>					
					</div>					
					<div title="营业部质检" >
<iframe marginwidth="0" 
src="<%=path %>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=viewQaApprovalGo&id=<%=id %>&operFlag=<%=qaApprovalOperFlag %>"
 marginheight="0" id="infoReviewFrame" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>					
					</div>
				</div>
			</li>
		</ul>
	</div>


</body>
</html>