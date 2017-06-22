<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>微服务系统登录</title>
<style type="text/css">
<!--
*{overflow:hidden; font-size:9pt;}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-image: url(<%=path%>nh-micro-jsp/images/bg.gif);
	background-repeat: repeat-x;
}
-->
</style>
<script type="text/javascript">
function login(){
	document.getElementById("myform").submit();
}
</script>

</head>

<body>
<table width="100%"  height="100%" border="0" cellspacing="0" cellpadding="0">
<form id="myform" name="myform" action="<%=basePath%>NhEsbServiceServlet?cmdName=Groovy&subName=nhlogin&groovySubName=execLoginGo" method="post">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="561" style="background:url(<%=basePath%>nh-micro-jsp/images/lbg.gif)"><table width="940" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td height="238" style="background:url(<%=basePath%>nh-micro-jsp/images/login01.jpg)">&nbsp;</td>
          </tr>
          <tr>
            <td height="190"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="208" height="190" style="background:url(<%=basePath%>nh-micro-jsp/images/login02.jpg)">&nbsp;</td>
                <td width="518" style="background:url(<%=basePath%>nh-micro-jsp/images/login03.jpg)"><table width="320" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="40" height="50"><img src="<%=basePath%>nh-micro-jsp/images/user.gif" width="30" height="30"></td>
                    <td width="38" height="50">用户</td>
                    <td width="242" height="50"><input type="text" value="guest" name="nhUserName" id="textfield" style="width:164px; height:32px; line-height:34px; background:url(<%=basePath%>nh-micro-jsp/images/inputbg.gif) repeat-x; border:solid 1px #d1d1d1; font-size:9pt; font-family:Verdana, Geneva, sans-serif;"></td>
                  </tr>
                  <tr>
                    <td height="50"><img src="<%=basePath%>nh-micro-jsp/images/password.gif" width="28" height="32"></td>
                    <td height="50">密码</td>
                    <td height="50"><input type="password" value="guest" name="nhPassWord" id="textfield2" style="width:164px; height:32px; line-height:34px; background:url(<%=basePath%>nh-micro-jsp/images/inputbg.gif) repeat-x; border:solid 1px #d1d1d1; font-size:9pt; "></td>
                  </tr>
               
                  <tr>
                    <td height="40">&nbsp;</td>
                    <td height="40">&nbsp;</td>
                    <td height="60"><img src="<%=basePath%>nh-micro-jsp/images/login.gif" width="95" height="34" onclick="login()"></td>
                  </tr>
                </table></td>
                <td width="214" style="background:url(<%=basePath%>nh-micro-jsp/images/login04.jpg)" >&nbsp;</td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td height="133" style="background:url(<%=basePath%>nh-micro-jsp/images/login05.jpg)">&nbsp;</td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
  </form>
</table>
<div style="text-align:center;">
</div>
</body>
</html>
