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
    <title>注册</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/login.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>    
<script type="text/javascript" >
function regUser(){
	var regTel=$('#register_tel').val();
	var password=$('#register_pwd').val();
	var smsCode=$('#sms_code').val();
	var data=new Object();
	data.regTel=regTel;
	data.password=password;
	data.smsCode=smsCode;
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_login&groovySubName=regUser";
	$.ajax({
		url:url,
		data:data,
		type:'post',
		dataType:'json',
		success:function(data,status){
			var resultData=JSON.parse(data.resultData);
			if(resultData.resultStatus!=0){
				alert("注册失败-"+resultData.resultMsg);
			}else{
				var gourl="<%=path%>/front-page/fundLogin.jsp";
				window.location.href=gourl;
			}
		}
	});
}

 $(function(){
	var _form = $('.userForm');
    _form.css({'left':'48%'}).find('.form_title').text('用户注册');    
    document.title = '用户注册';
	_form.children('div.register').removeClass('none');	

}); 
</script>	
</head>

<body id="J_noLoadHeanAndFooter">
    <div class="contentBox">
            <!--注册提示语-->
            <div class="loginBtn tabBtn">
                <div class="tagline">
                    <h3><img src="images/login/register_icon.png">注册</h3>
                    <p>让金融贴近生活</p>
                </div>
                <a href="#login">已有帐号,立即登录</a>
            </div>
            <!--登录提示语-->
            <div class="regBtn tabBtn">
                <div class="tagline">
 
                </div>
                <a href="#register">没有账号,立即注册</a>
            </div>
            <div class="userForm">                
                <!--注册 form-->
                <div class="register"  id="register">

                    <input type="tel" placeholder="请输入手机号" id="register_tel" maxlength="11" autocomplete="off"/>
                    <input type="password" placeholder="请设置登录密码" id="register_pwd"max-length = "16"/>
                    <p class="relation_tip">6-16个字符,只能包含字母、数字、以及标点符号（除空格）字母，数字和标点符号至少包含2种</p>

                    <div class="code_wrap clearfix">
                        <input type="text" placeholder="请输入手机验证码" id="sms_code" maxlength="6">
                        <button id="get_code">获取验证码</button>
                    </div>

                    <div class="form_submit_wrap clearfix">
                        <div data-wrap="checkbox" data-blue class="register_checkbox">
                            <input type="checkbox" id="mr1" checked> 
                            <label for="mr1">我已阅读并同意 </label>
                            <a href="protocol_service.html" style="color:#ff4c4c" target="_blank">《网站服务协议》</a>
                        </div>
                         <input type="button" id="register_submit" value="提交注册" onclick="regUser()">
                    </div>
                   
                </div>
            </div>
    </div>
    <div class="about_intro">
        版权所有 © 2012-2017 北京信息技术股份有限公司 京 ICP备 150号 - 2
    </div>

</body>