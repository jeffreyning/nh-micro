<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <meta http-equiv="cleartype" content="on">
    <meta name="keywords" content="">
	<meta name="description" content="">
    <title>登陆</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/fundLogin.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>    
<script type="text/javascript" >
function login(){
	var userCode=$('#login_tel').val();
	var password=$('#login_pwd').val();
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_login&groovySubName=login";
	var data=new Object();
	data.userCode=userCode;
	data.password=password;
	$.ajax({
		url:url,
		type:'post',
		dataType:'json',
		data:data,
		success:function(data,status){
			var resultData=JSON.parse(data.resultData);
			if(resultData.resultStatus==0){
			var gourl="<%=path%>/front-page/regularFinancialList.jsp";
			window.location.href=gourl;
			}else{
				alert("登陆失败 "+resultData.resultMsg);
			}
		}
	});
	
}
</script>	
</head>

<body>

    <div class="loginContent">
        <div class="formBox">
            <p class="goRegister">
                <i class="me-ion-edit"></i> 登录没有账号？
                <a class="redTxt" href="<%=path%>/front-page/login.jsp">立即注册</a>
            </p>
            <div data-wrap="layout" class="username">
                <div data-item="col-2">登录账号</div>
                <div data-item="col-10">
                    <div data-type="norm">
                        <input type="text" placeholder="请输入登录账号" id="login_tel" maxlength="11">
                    </div>
                </div>
            </div>
            <div data-wrap="layout">
                <div data-item="col-2">登录密码</div>
                <div data-item="col-10">
                    <div data-type="norm">
                        <input type="password" placeholder="请输入登录密码" id="login_pwd">
                    </div>
                </div>
            </div>
            <p class="findPassword">
                <a href="<%=path%>/front-page/forgetPassword.jsp">忘记密码?</a>
            </p>
            <button class="me-u-btn" data-size=block id="login_submit" onclick="login()">马上登录</button>
        </div>
    </div>

</body>

</html>