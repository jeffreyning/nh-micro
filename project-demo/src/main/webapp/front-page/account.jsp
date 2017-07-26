<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>账户信息</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/accountIndex.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>    
<script type="text/javascript">
function getMyAccount(){
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_account_api&groovySubName=queryMyAccount";

	$.ajax({
		url:url,
		type:'post',
		dataType:'json',
		success:function(data,status){
			$('#available_balance').html(data.available_balance);
			$('#regularFinancing').html(data.total_investment);
			$('#frozen_amount').html(data.frozen_amount);
			$('#J_balance').html(data.available_balance);
		}
	});
}
$(function(){
	getMyAccount();
	
});
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
    <li class="choosen">
        <a href="<%=path%>/front-page/account.jsp">
                        账户总览
                        <i class="me-ion-chevron-right"></i>
                    </a>

    </li>
    <li>
        <a href="<%=path%>/front-page/recharge_mode.html" id="J_goCharge">
                        账户充值
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>
    <li>
        <a href="<%=path%>/front-page/withdraw.html" id="J_goWithdraw">
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
        <a href="<%=path%>/front-page/myregular_finance.jsp">
                        定期理财
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>


    <li>
        <a href="<%=path%>/front-page/personal_center.html">
                        个人设置
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>
    <li>
        <a href="<%=path%>/front-page/systemMessage.html">
                        系统消息
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
            <div class="accountRightTop">
                <p class="privateOpration">
                    <span id="J_hideWeath"></span>
                    <a href="<%=path%>/front-page/systemMessage.html">
                        <span>
                        <b class="" id="J_unreadMeassage"></b>
                    </span>
                    </a>
                </p>
                <div class="allWeath" data-wrap="layout">
                    <div data-item="col-6">
                        <p class="weathTitle">我的总资产 (元)</p>
                        <p id="J_weath" class="weathAmount hideAmount" data-money="totalAmount">****</p>
                        <p class="allBalance">
                            <i class="me-ion-plus"></i>
                            <span class="icon"></span>
                            <span class="txt">
                            可用余额： <b id="available_balance" class="hideAmount">****</b>
                        </span>
                        </p>
                        <p class="allBalance allFixed">
                            <i class="me-ion-plus"></i>
                            <span class="icon"></span>
                            <span class="txt">
                            定期理财： <b id="regularFinancing" class="hideAmount">****</b>
                        </span>
                        </p>

                        <p class="allBalance allFrozen">
                            <i class="me-ion-plus"></i>
                            <span class="icon"></span>
                            <span class="txt">
                            冻结金额： <b id="frozen_amount" class="hideAmount">****</b>
                        </span>
                        </p>
                    </div>
                    <div data-item="col-6" class="col-6-right">
                        <p class="balanceTitle">可用余额 (元)</p>
                        <p id="J_balance" class="balanceAmount hideAmount" data-money="available_balance">****</p>
                        <p class="banlaceTxt">充值后可购买定期理财产品。</p>
                        <div class="charge wBtn"><a href="<%=path%>/front-page/recharge_mode.html" id="J_accountIndexCharge">充 值</a></div>
                        <div class="withdraw wBtn"><a href="<%=path%>/front-page/withdraw.html" id="J_accountIndexWithdraw">提 现</a></div>
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
          <!--<li><a href="javascript:;">关于我们</a></li>-->
          <li><a href="aboutCompany.html?categoryId=67">公司介绍</a></li>
          <!--<li><a href="WholeHelpCenter.html">新手帮助</a></li>-->
          <li><a href="WholeHelpCenter.html">帮助中心</a></li>
          <li><a href="aboutInformation.html?categoryId=70">行业资讯</a></li>
          <li><a href="WholeHelpCenter.html">常见问题</a></li>
          <!--<li>客服热线(服务时间：9:00-18:00)</li>-->
        </ul>
        <div class="footer_inner_bottom" style="clear:both" data-wrap="layout">
          <p style="font-size:12px;">
          版权所有 © 2012-2017 北京信息技术股份有限公司 &nbsp;&nbsp; 保留所有权利 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;京ICP备150号-2
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
      版权所有 © 2012-2017 北京信息技术股份有限公司   京ICP备150号-2
    </p>
</footer>

</body>

</html>