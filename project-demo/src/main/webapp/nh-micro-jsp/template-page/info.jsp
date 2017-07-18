<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>micro-info</title>
<script type="text/javascript">

</script>
</head>
<body>
提示信息：<br>
<%=request.getAttribute("microInfoMsg") %>
<input type="hidden" id="retFileId" value="<%=request.getAttribute("fileId") %>"></input>
</body>
</html>