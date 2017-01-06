<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript" charset="UTF-8">
	var centerTabs;
	var $centerFrame;


	$(function() {
		$centerFrame=$("#jn_Frame");
		$centerFrame.attr("src","layout/home.jsp");

	});
</script>

<div id="centerTabs" style="border:0;width:100%;height:99.2%;"><iframe id="jn_Frame"   frameborder="0" style="border:0;width:100%;height:99.2%;"></iframe></div>
