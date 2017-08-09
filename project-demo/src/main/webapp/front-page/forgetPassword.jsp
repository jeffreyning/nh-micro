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
    <title>找回密码</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/forgetPassword.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>    
<script type="text/javascript" >
function resetPass(){
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_api&groovySubName=resetPassword";
	var data=new Object();
	data.phoneNo=$("#forgetPhone").val();
	data.msgCode=$("#J_msgCode").val();
	data.newPassword=$("#newPassword").val();
	data.confirmPassword=$("#confirmPassword").val();
	$.ajax({
		url:url,
		data:data,
		type:'post',
		dataType:'json',
		success:function(data,status){
			var go="<%=path%>/front-page/fundLogin.jsp";
			window.location.href=go;			
		}
	});
}
</script>	
</head>
<body> 

<div class="passwordBox">
	<div class="forgetPassword">
<!-- 		<ul class="headUl">
			<li class="forgetPasswordOn">
				<span>验证手机号</span>
				<i></i>
			</li>
			<li>
				<span>设置新密码</span>
				<i></i>
			</li>
			<li>
				<span>找回成功</span>
				<i></i>
			</li>
		</ul> -->
		<!-- 验证手机号 -->
		<div class="Verification" style="display: block;">
			<div class="phoneNumber boxWidth">
				<p>注册手机号</p>
				<div data-type="icon" class="boxWidthDiv width414">
					<!-- <i class="me-ion-person"></i> -->
					<i class="me-ion-o-telephone" data-pack="ios"></i>
					<input  type="text" id="forgetPhone" placeholder="请输入手机号" maxlength='11'>
				</div>
			</div>

			<div class="boxWidth">
				<p>短信验证码</p>
				<div data-type="icon" class="boxWidthDiv windth308">
					<!-- <i class="me-ion-person"></i> -->
					<i class="me-ion-o-email" data-pack="ios"></i>
					<input type="text" placeholder="请输入验证码" id="J_msgCode" >
				</div>
				<button class="send" id='J_sendMsgCode'>获取验证码</button>
			</div>


			<div class="phoneNumber boxWidth">
				<p>新密码</p>
				<div data-type="norm" class="boxWidthDiv width414">
					<img src="images/newpasswordIcon.png" alt="">
					<input type="password" placeholder="新密码" id="newPassword">
				</div>
			</div>
			<div class="phoneNumber boxWidth">
				<p>再次输入新密码</p>
				<div data-type="norm" class="boxWidthDiv width414">
					<img src="images/newpasswordIcon.png" alt="">
					<input type="password" placeholder="再次输入新密码" id="confirmPassword">
				</div>
			</div>
			<div class="boxWidth">
				<p></p>
				<div class="boxWidthDiv width414">
					<button class="next" id="next2" onclick="resetPass()">提交</button>
				</div>
			</div>
		</div>

	</div>
</div>
<footer>
<div class="footer_inner wrap">
    <div data-wrap="layout" class="bd">
      <div class="footer_inner_left" data-item="col-12">
        <ul style="padding-bottom:15px;border-bottom:1px solid #525252;overflow:hidden;">
          <li><a href="aboutCompany.html?categoryId=67">公司介绍</a></li>
          <li><a href="WholeHelpCenter.html">帮助中心</a></li>
          <li><a href="aboutInformation.html?categoryId=70">行业资讯</a></li>
          <li><a href="WholeHelpCenter.html">常见问题</a></li>
        </ul>
        <div class="footer_inner_bottom" style="clear:both" data-wrap="layout">
          <p style="font-size:12px;">
        版权所有  ©  2012-2017 北京信息技术股份有限公司    保留所有权利      京ICP备150号-2
        </p>
          <p data-item="col-12" class="f-link">
          友情链接：
          <a href="http://www.wdzj.com/" target="_blank" rel="nofollow">网贷之家</a>
          <a href="http://finance.sina.com.cn/" target="_blank" rel="nofollow">新浪财经</a>
          <a href="http://money.qq.com/" target="_blank" rel="nofollow">腾讯理财</a>
          <a href="http://business.sohu.com/" target="_blank" rel="nofollow">搜狐财经</a>
          <a href="http://finance.ifeng.com/" target="_blank" rel="nofollow">凤凰财经</a>
          <a href="http://www.hexun.com/" target="_blank" rel="nofollow">和讯网</a>
          <a href="http://money.163.com/" target="_blank" rel="nofollow">网易财经</a>
        </p>
      </div> 
      </div>
    </div>
    <div class="safe_info">
          <a target="_blank" href="http://www.itrust.org.cn/home/index/wx_certifi/wm/WX2017123456.html" style=""><img src="<%=path%>/front-page/images/index/foot5.jpg"></a>
          <a target="_blank" href="https://credit.szfw.org/CX2017.html"><img src="<%=path%>/front-page/images/index/cert.png" height="42"></a>
          <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=110102"><img src="<%=path%>/front-page/images/index/police.png" style="margin-right:10px;font-size:12px">京公网安备 110102号</a>
        </div>
  </div>
  <p class="none">
       版权所有 © 2012-2017   北京信息技术股份有限公司   京ICP备150号-2
    </p>  
</footer>
</body>

</html>