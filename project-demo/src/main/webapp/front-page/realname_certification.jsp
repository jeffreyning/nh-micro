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
    <title>实名认证</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/personal_center.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>    
<script type="text/javascript" >
function submit_real(){
	var data0=$('#r_c_form').serialize();
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_realname_api&groovySubName=realName";

	$.ajax({
		url:url,
		type:'post',
		dataType:'json',
		data:data0,
		complete: function(msg) { 
			var go="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_api&groovySubName=queryInfoByCodeGo";
			alert(go);
			window.location.href=go;			
		}		
	});

}
</script>	
</head>
  <body><style>
    header .header li.header_none {
        display: none;
    }
</style>
<header>
    <div data-wrap="layout" class="bg-grey">
        <div class="wrapper">
            <ul class="header" data-item="col-12">
            <c:if test="${empty sessionScope.tokenId}">
                <a href="<%=path%>/front-page/login.jsp">注册</a>/<a href="<%=path%>/front-page/fundLogin.jsp">登录</a>
            </c:if>
                <c:if test="${sessionScope.tokenId!=null}">
                <a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_login&groovySubName=logoutGo" id="login_out">退出</a>
                 </c:if>
                <a href="<%=path%>/front-page/regularFinancialList.jsp">定期理财列表</a>
            </ul>
        </div>
    </div>
    <div class="wrap">
        <div data-wrap="layout" class="pos">
            <a class="key-btn me-ion-navicon"></a>
            <h1 class="logo" data-item="col-2">
                <a href="<%=path%>/front-page/index.html">
                    <img src="<%=path%>/front-page/images/logo.png" class="img-responsive">
                </a>
            </h1>
            <ul class="nav" data-item="col-10" data-flag="1" id="J_headerNav">
                
            </ul>
        </div>
    </div>
</header>
    <div class="accountMain">
      <div id="J_accountMenu"><!--账户中心通用菜单-->
<div class="userInfo">
    <div class="headImg">
        <img src="<%=path%>/front-page/images/headImg.png">
    </div>
    <p class="loginTel" id="J_loginTel">
        ----***----
    </p>
    <p class="saftyLevel">
        <span>安全等级</span>
        <span class="levelPercent">
                        <span></span>
        </span>
        <a id="J_goUp" href="<%=path%>/front-page/personal_center.html">
                        提升
                    </a>
    </p>
    <p class="investType">
        <span class="investTag">投资属性:</span>
        <span id="J_userType">
            <span class="pc">未评测
                <!--<a href="questionnaire.html?f=account"> 前去评测</a>-->
            </span>
            <span class="mobile">未评测
                <!--<a href="questionnaire.html?f=account"> 未评测</a>-->
            </span>
        </span>
    </p>
    <div class="imgInfo clearfix">
        <div class="mouseTips--bottom" data-label="实名认证"></div>
        <div class="mouseTips--bottom" data-label="绑卡认证"></div>
        <div class="mouseTips--bottom" data-label="邮箱认证"></div>
    </div>
</div>

<ul class="menuList">

</ul>


<div class="mobileIcon menuHide">
    <!--<img src="images/icon-l.svg" alt="">-->
    <i class="me-ion-chevron-right"></i>
</div>

<script>
    $(".mobileIcon").click(function () {
        if ($(this).hasClass("menuHide")) {
            $("#J_accountMenu").animate({
                left: 0
            })
            $('.mobileIcon').animate({
                left: '30%'
            })
            $(this).removeClass("menuHide");
        } else {
            $("#J_accountMenu").animate({
                left: '-30%'
            })
            $('.mobileIcon').animate({
                left: '0'
            })
            $(this).addClass("menuHide");
        }
    })

</script></div>
      <div class="accountRight">
        <div class="acTitle">
          <h2>实名认证</h2>
        </div>
        <div class="warndiv r_c_warndiv"><i class="me-ion-a-alert"></i>实名认证由公安局系统统一验证，请确保输入匹配的身份信息！</div>
        <form id="r_c_form" class="r_c_form p_c_style"  onsubmit="return false;">
          <div data-wrap="layout">
            <div data-item="col-2" class="r_c_label">
              <label>真实姓名</label>
            </div>
            <div data-item="col-5">
              <div data-type="icon"><i class="me-ion-person"></i>
                <input id="realname" type="text" name="realname" placeholder="请输入您的真实姓名" data-validate="chinese">
              </div>
            </div>
          </div>
          <div data-wrap="layout">
            <div data-item="col-2" class="r_c_label">
              <label>身份证号</label>
            </div>
            <div data-item="col-5">
              <div data-type="icon"><i class="me-ion-card"></i>
                <input id="idcard" type="text" name="idcard" placeholder="请输入您的身份证号" data-validate="IDCard">
              </div>
            </div>
          </div>
          <div data-wrap="layout">
            <div data-offset="2">
              <button id="refer" type="submit" name="refer" data-color="red" class="me-u-btn" onclick="submit_real()">提交</button>
              <button id="reset" type="reset" data-type="minor" class="me-u-btn">重置</button>
            </div>
          </div>
          <div id="backset"><a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_api&groovySubName=queryInfoByCodeGo">返回个人设置&gt;&gt;</a></div>
        </form>
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