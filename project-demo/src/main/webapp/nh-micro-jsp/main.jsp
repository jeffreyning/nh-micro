<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
    <title>nh-micro管理系统</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
		var serverName="<%=path%>";
	</script>
</head>
<body class="easyui-layout">
	<div region="north" href="layout/north.jsp" split="false" border="false" style="overflow: hidden; height: 30px;background: 	#D1E9E9">
    </div>
	<div region="west" href="layout/west2.jsp" title="导航" split="false" style="width:200px;overflow: hidden;"></div>
	<div region="center" href="layout/center.jsp" title="欢迎访问nh-micro系统" style="overflow: hidden;"></div>
    <div region="south" split="false" style="height: 30px; background: #D2E0F2; ">
        <div class="footer">By NH-MICRO</div>
    </div>
</body>
</html>
