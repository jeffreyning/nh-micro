<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path=request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>index</title>
<script type="text/javascript">

</script>
</head>
<body>
<form action="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=micro_attachment_list&groovySubName=execUploadFile" method="post" enctype="multipart/form-data">
    <input type="text" name="packagePath"/>
    <input type="file" name="file"/>
    <input type="submit" name="upload"value="上传"/>
</form>
</body>
</html>