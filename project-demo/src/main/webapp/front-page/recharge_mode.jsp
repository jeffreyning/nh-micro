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
    <title>充值</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/recharge_mode.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>   
<script type="text/javascript" src="<%=path%>/front-page/js/template-web.js"></script> 
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>  	
<script type="text/javascript">
var rechargeNumber="";
function payNow(){

		var bankPay=$('#money').val();

		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_recharge_api&groovySubName=rechargeBank";
		var data=new Object();
		data.bankPay=bankPay;
		$.ajax({
			url:url,
			type:'post',
			dataType:'json',
			data:data,
			success:function(data,status){
				var resultData=JSON.parse(data.resultData);
				rechargeNumber=resultData.resultObj;
				$("#RechargeSendPaySmsDialog").dialog("open");
			}
		});

}

function submitQuickConfirm(){

	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_recharge_api&groovySubName=confirmQuickPayGo";
	url=url+"&rechargeNumber="+rechargeNumber;
	window.location.href=url;
}

$(function(){
    $("#RechargeSendPaySmsDialog").dialog({
        autoOpen: false
      });
});
</script>
</head>
  <body>
    <style>
    header .header li.header_none {
       /*  display: true; */
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
    <li class="choosen">
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
        <a href="<%=path%>/front-page/myregular_finance.jsp">
                        定期理财
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>
    <li>
        <a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_api&groovySubName=queryInfoByCodeGo">
                        个人设置
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>    
</ul>
</div>
      <div class="accountRight">
        <div class="acTitle">
          <h2>充 值</h2>
        </div>
        <div id="r_m_module" class="r_m_module">
          <h3>充值方式</h3>
          <div class="r_m_pay_type">
            <div data-wrap="radio">
              <input id="quickpay" type="radio" name="payradio" checked>
              <label for="quickpay">快捷支付</label>
            </div>
<!--             <div data-wrap="radio">
              <input id="netbankpay" type="radio" name="payradio">
              <label for="netbankpay">网银支付</label>
            </div> -->
          </div>        
        </div>


          <div class="r_m_pay_cont">
            <div id="r_m_pay_tabcont" class="r_m_pay_tabcont">
		          <div id="b_s_form_view">
		          <div id="r_m_quick_form" class="r_m_quick_form p_c_style" >
		            <div data-wrap="layout">
		              <div class="card_box"><img src="images/withdrawBanks/${cardInfo.bank_name}.png" alt="${cardInfo.bank_name}"><span>**** <em>${cardInfo.card_last}</em></span></div>
		            </div>
		            <div data-wrap="layout">
		              <div data-item="col-2">
		                <label>可用余额</label>
		              </div>
		              <div data-item="col-5" class="label_in_txt"><strong>${accountInfo.available_balance}</strong>元</div>
		            </div>
		            <div data-wrap="layout">
		              <div data-item="col-2">
		                <label>充值金额</label>
		              </div>
		              <div data-item="col-5">
		                <div data-type="norm">
		                  <input id="money" type="text" name="money"><b>元</b>
		                </div>
		              </div>
		            </div>
		            <div class="warndiv"><i class="me-ion-a-alert"></i>
		            <div class="btnboxcenter">
		              <button id="refer" type="submit" data-color="red" name="refer" class="me-u-btn" onclick="payNow()">确认充值</button>
		            </div>
		            <div data-wrap="layout">
		              <div class="linear"></div>
		              <div class="remindertxt">
		                <h5> <span class="spanheart"><i class="me-ion-heart"></i></span>温馨提示：</h5>
		                <ul>
		                  <li>1、充值免费，不收取任何手续费；</li>
		                  <li>2、严禁信用卡充值、套现等违法行为；</li>
		                  <li>3、请注意您的银行卡充值限制，以免造成不便；</li>
		                </ul>
		              </div>
		            </div>
		          </div>
				</div>            
            </div>
          </div>

      </div>
    </div>
<!--快捷充值弹窗-->
<div id="RechargeSendPaySmsDialog">
  <div id="r_m_send_form" class="r_m_state_form" >
    <a class="closed" href="javascript:;"><i class="me-ion-close-round"></i></a>
    <h3>系统已发送手机验证码到您的银行预留手机</h3>
    <strong id="bankPhone">&nbsp;</strong>
    <div class="r_m_send_group">
      <div data-type="norm">
        <input type="text" id="code" name="code" placeholder="请输入手机验证码" />
      </div>
      <button type="button" id="sendcode" class="me-u-btn" name="sendcode" data-type="minor">60秒后重试</button>
    </div>
    <button id="quickTopUpBtn1" class="me-u-btn" type="submit" data-color="red" onclick="submitQuickConfirm()">确定</button>
  </div>
</div>
<!---->     
    
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