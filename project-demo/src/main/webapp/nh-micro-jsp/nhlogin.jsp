<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>nh-micro系统登录</title>
<style type="text/css">
<!--
*{overflow:hidden; font-size:9pt;}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-image: url("<%=path%>/nh-micro-jsp/images/bg.gif");
	background-repeat: repeat-x;
}
-->
</style>
<script type="text/javascript">
function login(){
var nhUserName=$('#nhUserName').val();
var nhPassWord=$('#nhPassWord').val();
var cmdDataObj={};
cmdDataObj.nhUserName=nhUserName;
cmdDataObj.nhPassWord=nhPassWord;
cmdDataObj.groovySubName="login";
//var cmdData=JSON.stringify(cmdDataObj);
var loginUrl="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhlogin";

$.ajax({
url:loginUrl,
data:cmdDataObj,
type:"post",
dataType:"json",
success:function(data,status){
	if(data.resultStatus!=0){
		alert("登录异常");
		return;
	}
	var resultData=data.resultData;
	var resultDataObj=JSON.parse(resultData);
	if(resultDataObj.resultStatus!=0){
		alert("用户名或密码不正确");
		return;		
	}
	window.location.href="<%=path%>/nh-micro-jsp/main.jsp";

},
error:function(data){
	alert("登录异常");
}
});

}
</script>

</head>

<body>
<table width="100%"  height="100%" border="0" cellspacing="0" cellpadding="0">
<form id="myform" name="myform" method="post">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="561" style="background:url(<%=path%>/nh-micro-jsp/images/lbg.gif)"><table width="940" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td height="238" style="background:url(<%=path%>/nh-micro-jsp/images/login01.jpg)">&nbsp;</td>
          </tr>
          <tr>
            <td height="190"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="208" height="190" style="background:url(<%=path%>/nh-micro-jsp/images/login02.jpg)">&nbsp;</td>
                <td width="518" style="background:url(<%=path%>/nh-micro-jsp/images/login03.jpg)"><table width="320" border="0" align="center" cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="40" height="50"><img src="<%=path%>/nh-micro-jsp/images/user.gif" width="30" height="30"></td>
                    <td width="38" height="50">用户</td>
                    <td width="242" height="50"><input type="text" name="userName" id="nhUserName" style="width:164px; height:32px; line-height:34px; background:url(<%=path%>/nh-micro-jsp/images/inputbg.gif) repeat-x; border:solid 1px #d1d1d1; font-size:9pt; font-family:Verdana, Geneva, sans-serif;"></td>
                  </tr>
                  <tr>
                    <td height="50"><img src="<%=path%>/nh-micro-jsp/images/password.gif" width="28" height="32"></td>
                    <td height="50">密码</td>
                    <td height="50"><input type="password" name="passWord" id="nhPassWord" style="width:164px; height:32px; line-height:34px; background:url(<%=path%>/nh-micro-jsp/images/inputbg.gif) repeat-x; border:solid 1px #d1d1d1; font-size:9pt; "></td>
                  </tr>                
                  <tr>
                    <td height="40">&nbsp;</td>
                    <td height="40">&nbsp;</td>
                    <td height="60"><img src="<%=path%>/nh-micro-jsp/images/login.gif" width="95" height="34" onclick="login()"></td>
                  </tr>
                </table></td>
                <td width="214" style="background:url(<%=path%>/nh-micro-jsp/images/login04.jpg)" >&nbsp;</td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td height="133" style="background:url(<%=path%>/nh-micro-jsp/images/login05.jpg)">&nbsp;</td>
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
