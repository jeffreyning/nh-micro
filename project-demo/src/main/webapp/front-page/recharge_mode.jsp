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
</script>
</head>
  <body>
  
    <div class="accountMain">
      <div id="J_accountMenu">
      </div>
      <div class="accountRight">
        <div class="acTitle">
          <h2>充 值</h2>
        </div>
        <div id="r_m_module" class="r_m_module"></div>

          <h3>充值方式</h3>
          <div class="r_m_pay_type">
            <div data-wrap="radio">
              <input id="quickpay" type="radio" name="payradio" checked>
              <label for="quickpay">快捷支付</label>
            </div>
            <div data-wrap="radio">
              <input id="netbankpay" type="radio" name="payradio">
              <label for="netbankpay">网银支付</label>
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