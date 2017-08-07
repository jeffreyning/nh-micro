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
    <title>金宝宝</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/pay.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>  
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>  
<script type="text/javascript">
function X(){
	return false;
}
function payNow(){
	if($("#mr0").prop("checked") && !$("#mr1").prop("checked") && !$("#mr2").prop("checked")){
		var investAmount=$('#J_investAmount').html();
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_invest_api&groovySubName=investProductGo";
		url=url+"&orderNumber=${investInfo.order_number}";
		window.location.href=url;			
	}else if($("#mr1").prop("checked")){
		var investAmount=$('#J_investAmount').html();
		var accountPay=$('#J_balance').html();
		var bankPay=$('#J_kuaijieMoney').html();

		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_invest_api&groovySubName=investProductBank";
		var data=new Object();
		data.orderNumber='${investInfo.order_number}';
		data.investAmount=investAmount;
		data.accountPay=accountPay;
		data.bankPay=bankPay;
		$.ajax({
			url:url,
			type:'post',
			dataType:'json',
			data:data,
			success:function(data,status){
				$("#RechargeSendPaySmsDialog").dialog("open");
			}
		});
	}else if($("#mr2").prop("checked")){
		
	}else{
		alert("请选择正确的支付方式");
	}


}
$(function() {
    $("#RechargeSendPaySmsDialog").dialog({
      autoOpen: false,
    });
});

function submitQuickConfirm(){
	var investAmount=$('#J_investAmount').val();
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_invest_api&groovySubName=confirmQuickPayGo";
	url=url+"&orderNumber=${investInfo.order_number}";
	window.location.href=url;
}
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
                    <a href="<%=path%>/front-page/login.jsp">注册</a>/<a href="<%=path%>/front-page/fundLogin.jsp">登录</a>
                    <a href="javascript:;" id="login_out">退出</a>
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
    <div class="wrapper con">
        <div id="J_title">

                <div class="title">
                    <h2>支付(<i id="J_bidName">${investInfo.product_name}</i>)</h2>
                </div>
                <div data-wrap="layout" class="money-show clearfix">
                    <div data-item="col-5" class="fr"><i>购买金额：</i><b id="buyMoneyShow">${investInfo.invest_amount}</b><b id="buyMoney" class="none">${investInfo.invest_amount}</b>元</div>
                </div>
  
        </div> 
        <div class="pay-con">
            <div class="pay-title clearfix">
                <div data-wrap="checkbox" data-blue="" class="J_zpay">
                    <input type="checkbox" id="mr0" checked> 
                    <label for="mr0">使用账户余额支付</label>
                </div> 
            </div>
            <div data-wrap="layout" class="zh-pay grey-shadow" data-checked="false">
                <span data-item="col-5" class="fl">账户余额：<b class="J_balance" id="J_balanceYShow">${accountInfo.available_balance}</b><b class="J_balance none" id="J_balanceY">${accountInfo.available_balance}</b><i>元</i></span>
                <span data-item="col-5" class="fr">支付<em></em>：<b class="J_balance" id="J_balance">${investInfo.invest_amount+0.0>accountInfo.available_balance?accountInfo.available_balance:investInfo.invest_amount}</b>元</span>
            </div>
            <div class="pay-title special clearfix">
                <div data-wrap="radio" data-blue="" class="J_kpay" producttype="1">
                    <input type="radio" id="mr1" name="my-radio"> 
                    <label for="mr1">快捷支付</label>
                </div>
                <span class="pay-show J_pay-show">支付:<b class="J_kuaijieMoney" id="J_kuaijieMoney">${investInfo.invest_amount+0.0>accountInfo.available_balance?investInfo.invest_amount-accountInfo.available_balance:0}</b>元</span>
                <span class="tip">(未绑定银行卡？<a href="javascript:void(0);" class="J_bindCard">立即绑定银行卡)</a></span>
            </div>
            <!--快捷支付未绑卡-->
            <div class="not-card none">
                <div data-wrap="layout">
                    <div data-item="col-2">
                        <label>银行卡号</label>
                    </div>
                    <div data-item="col-5">
                        <div data-type="norm">
                            <input type="tel" id="bankCard" placeholder="" autocomplete="off" data-result="false">  
                        </div>
                        <p class="error"></p>
                    </div>
                </div>
                <p class="watchBankList"><a href="<%=path%>/front-page/bankQuota.html" target="_blank">查看支持银行限额</a></p>
                <!--<div data-wrap="layout">
                    <div data-item="col-2">
                        <label>所属银行</label>
                    </div>
                    <div data-item="col-5">
                        <div id="bankList" data-type="norm" data-result = "false">
                            <select data-not="true" data-h="42">
                                <option value="0">请选择所属银行</option>
                            </select>   
                        </div>
                        <div data-type="norm">
                            <input type="text" id="bankLong" placeholder="请输入所属银行" autocomplete="off" data-result = "false">
                        </div>
                        <p class="error"></p>
                    </div>
                    <p data-item="col-4">卡支付限额：单笔1万／单日10万</p>
                </div>-->
                <div data-wrap="layout">
                    <div data-item="col-2">
                        <label>预留手机</label>
                    </div>
                    <div data-item="col-5">
                        <div data-type="norm">
                            <input type="tel" id="mobile" placeholder="请输入银行预留手机号" maxlength="11" autocomplete="off" data-result="false">
                        </div>
                        <p class="error"></p>
                    </div>
                </div>
                <div class="card-btn">
                    <button type="submit" data-color="red" class="me-u-btn" id="refer" data-size="lg">确认绑定</button>
                    <button type="reset" class="me-u-btn J_reset" data-type="text">重置</button>
                </div>
                <div class="card-btn">
                    <div data-wrap="checkbox" data-orange="" id="checkbox" class="fl">
                        <input type="checkbox" checked> 
                        <label for="mr0">
                        </label>
                    </div>
                    <p class="protocol">
                        我已阅读并同意<a href="<%=path%>/front-page/protocol_card.html" target="_block">《快捷支付服务协议》</a>
                    </p>
                </div>
            </div>
            <!--已绑卡-->
             <div data-wrap="layout" class="kj-pay grey-bd none">
                <!--<div data-item="col-7" class="fl">
                    <label><img src="images/banklogo/js.png" alt="" title="" class="img-responsive"/></label>
                    <span>建设银行</span>
                    <span>＊＊3306</span>
                    <i>(卡支付限额：单笔1万／单日10万／单月20万)</i>
                </div>
                <span data-item="col-3" class="fr">支付:<b>9,500.00 </b> 元</span>-->
            </div>
            <div class="pay-title clearfix">
                <div data-wrap="radio" data-blue="" class="J_wpay" producttype="2">
                    <input type="radio" id="mr2" name="my-radio"> 
                    <label for="mr2">网银支付</label>
                </div>
                <span class="pay-show">支付:<b id="J_wangyinMoney">${investInfo.invest_amount+0.0>accountInfo.available_balance?investInfo.invest_amount-accountInfo.available_balance:0}</b> 元</span>
            </div>
            <ul class="wy-pay netbankList clearfix">
                <!--<li>
                    <label><img src="../../images/banks/CMB.jpg" alt="招商银行">
                    <input type="radio" name="bank" value="CMB"><span></span><i class="me-ion-checkmark"></i>
                    </label>
                </li>-->
            </ul>
            <div class="bank-form none">
                <h3>银行限额</h3>
                <!--<table>
                    <thead>
                        <tr>
                            <th width="25%">每笔限额(元)<span>|</span></th>
                            <th width="25%">每日限额(元)<span>|</span></th>
                            <th width="25%">每月限额(元)<span>|</span></th>
                            <th width="25%" class="wap-none">满足条件</th>
                        </tr>
                    </thead>
                    <tbody id="myTable">
                        
                    </tbody>
                </table>-->
            </div>
        </div> 
    </div>
    <div class="tipShow">
        <div class="wrapper">
            <span class="paymentTotle">应付总额：<b id="J_investAmount">${investInfo.invest_amount}</b> 元</span>
            <button type="submit" id="J_pay" data-color="red" class="me-u-btn" data-size="lg" onclick="payNow()">立即支付</button>
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