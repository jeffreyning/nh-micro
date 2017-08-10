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
    <title>个人设置</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/personal_center.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>   
<script type="text/javascript" src="<%=path%>/front-page/js/template-web.js"></script> 
	
<script type="text/javascript">

</script>
</head>
  <body>
    <style>
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

</header>  
    <div class="accountMain">
        <div id="J_accountMenu">
            <!--账户中心通用菜单-->
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
    <li >
        <a href="<%=path%>/front-page/account.jsp">
                        账户总览
                        <i class="me-ion-chevron-right"></i>
                    </a>

    </li>
    <li>
        <a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_recharge_api&groovySubName=openRechargeGo" id="J_goCharge">
                        账户充值
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>
    <li>
        <a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_withdraw_api&groovySubName=withdrawPageGo" id="J_goWithdraw">
                        账户提现
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>
    <li>
        <a href="<%=path%>/front-page/transaction_record.jsp">
                        交易记录
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>
    <li>
        <a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_invest_api&groovySubName=myRegularGo">
                        定期理财
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>
    <li class="choosen">
        <a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_api&groovySubName=queryInfoByCodeGo">
                        个人设置
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>    
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

</script>
        </div>
      <div class="accountRight">
        <div class="acTitle">
          <h2>个人设置</h2>
        </div>
        <div id="p_c_cont" class="p_c_cont">

          <div data-wrap="layout">
            <div data-item="col-2"><i class="me-ion-checkmark-circled"></i><strong>手机认证</strong></div>
            <div data-item="col-8"><span>${userInfo.user_phone}</span></div>
            <div data-item="col-2"><span class="certified">已认证</span></div>
          </div>
          <div data-wrap="layout">
            <div data-item="col-2"><i class="me-ion-checkmark-circled"></i><strong>登录密码</strong></div>
            <div data-offset="10"><a href="<%=path%>/front-page/update_loginpwd.jsp" class="update">修改</a></div>
          </div>
          <div data-wrap="layout">
            <div data-item="col-2"><c:if test="${userInfo.is_authentication==1}"><i class="me-ion-checkmark-circled"></i></c:if>
              <c:if test="${userInfo.is_authentication!=1}"><i class="me-ion-a-alert"></i></c:if><strong>实名认证</strong>
            </div>
            <div data-item="col-8"><span><c:if test="${userInfo.is_authentication==1}">${userInfo.real_name}</c:if></span></div>
            <div data-item="col-2"><c:if test="${userInfo.is_authentication==1}"><span class="certified">已认证</span></c:if>
              <c:if test="${userInfo.is_authentication!=1}"><a href="<%=path%>/front-page/realname_certification.jsp" class="update">认证</a></c:if>
            </div>
          </div>

          <div data-wrap="layout">
            <div data-item="col-2"><c:if test="${userInfo.is_bindcard==1 }"><i class="me-ion-checkmark-circled"></i></c:if>
              <c:if test="${userInfo.is_bindcard!=1 }"><i class="me-ion-a-alert"></i></c:if><strong>银行卡设置</strong>
            </div>
            <div data-item="col-8"><span><c:if test="${userInfo.is_bindcard==1 }">**** ${cardInfo.card_last}</c:if></span></div>
            <div data-item="col-2"><c:if test="${userInfo.is_bindcard==1}"><span class="certified">已绑卡</span></c:if>
              <c:if test="${userInfo.is_bindcard!=1}"><a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_bank_api&groovySubName=bankCardSetGo" class="update">绑卡</a></c:if>
            </div>           
          </div>
          <div class="reminder">
            <h5><span class="spanheart"><i class="me-ion-heart"></i></span><em>温馨提示：</em></h5>
            <p>银行卡解绑请致电客服：<span class="certifi">400-111-110</span></p>
          </div>
		</div>
      </div>
    </div>
    <footer>
  <div class="footer_inner wrap">
    <div data-wrap="layout" class="bd">
      <div class="footer_inner_left" data-item="col-12">
        <ul style="padding-bottom:15px;border-bottom:1px solid #525252;overflow:hidden;">
          <li><a href="javascript:;">关于我们</a></li>
          <li><a href="javascript:;">公司介绍</a></li>
          <li><a href="javascript:;">新手帮助</a></li>
          <li><a href="javascript:;">帮助中心</a></li>
          <li><a href="javascript:;">行业资讯</a></li>
          <li><a href="javascript:;">常见问题</a></li>
          <li>客服热线(服务时间：9:00-18:00)</li>
        </ul>
        <div class="footer_inner_bottom" style="clear:both" data-wrap="layout">
          <p style="font-size:12px;">
          版权所有 © 2012-2017 北京信息技术股份有限公司 &nbsp;&nbsp; 保留所有权利 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;京ICP备12345678号-2
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
      版权所有 © 2012-2017 北京信息技术股份有限公司   京ICP备12345678号-2
    </p>
</footer>
  </body>
</html>