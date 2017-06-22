<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录成功</title>
<script type="text/javascript" language="javascript"> 

</script> 



</head>
<body>
<a href="<%=basePath %>inner-jsp/nhlogin.jsp">跳转至登录页</a>
<iframe id="iframepage" src="${nhloginProxyFirstUrl }"  marginwidth="0"  marginheight="0"  frameborder="0" style="border:0;width:100%;height:1000px;"  >
</iframe>
</body>
</html>