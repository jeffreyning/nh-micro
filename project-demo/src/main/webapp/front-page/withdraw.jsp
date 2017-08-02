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
    <title>提现</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/withdraw.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>  
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>  
<script type="text/javascript">
function submitWithdraw(){
	var withdrawAmount=$("#J_withdrawAmount").val();
	var data=new Object();
	data.withdraw_money=withdrawAmount;
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_withdraw_api&groovySubName=createWithdrawInfo";
	$.ajax({
		url:url,
		type:'post',
		data:data,
		dataType:'json',
		success:function(data,status){
			
		}
	});	
}
</script>	
</head>

<body>

    <div class="accountMain">
        <div id="J_accountMenu">

        </div>
        <div class="accountRight">
            <div class="acTitle">
                <h2>提现</h2>
            </div>

            <div class="r_m_state"><img src="<%=path%>/front-page/images/recharge/rechargesuccess.jpg">
                <h3>申请提现成功！</h3>
                <div><a data-color="red" class="me-u-btn" href="index.html">返回首页</a><a href="account.html" data-type="minor" class="me-u-btn">查看账户</a></div>
            </div>
            <div class="withdrawContent">

                <div id="J_withdraw" >
                    <div class="withdrawCard">
                        <img src="" alt="" class="img-responsive bankBelong">
                        <span class="cardNo">**** <em class="bankNoLast">----<em></span>
                    </div>
                    <p class="balance">
                        <span>可用余额</span>
                        <span><b class="balanceAmount">----</b>元</span>
                    </p>
                    <div class="withdrawAmount">
                        <span>提现金额</span>
                        <div data-type="norm">
                            <input type="text" placeholder="提现金额" maxlength="10" id="J_withdrawAmount">
                            <b>元</b>
                        </div>
                        <div data-type="norm" class="withdrawAmtD">
                            到账金额：<strong>----</strong> 元
                        </div>
                    </div>
                    <p class="balance fee">
                        <span>提现费用</span>
                        <span><b class="withdrawFee">----</b>元/笔</span>
                    </p>
                    <div class="sms">
                        <span>短信验证</span>
                        <div data-type="norm">
                            <input type="text" placeholder="短信验证码" maxlength="6" id="J_code">
                        </div>
                        <span class="getCode" id="J_getCode">获取验证码</span>
                    </div>
                </div>


                <button class="withdrawSubmit" id="" data-color="red" onclick="submitWithdraw()">提交</button>
                <a href="javascript:;" class="reset none">重置</a>
                <div data-wrap="layout" class="agreement none">
                    <div>
                        <div data-wrap="checkbox" data-red="">
                            <input type="checkbox" id="agree" name="agree" data-validate="/checked/" checked>
                            <label for="agree" class="readAgreement">我已阅读并同意<a href="protocol_card.html" target="_block">《快捷支付服务协议》</a></label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="withdrawTips">
                <div class="topLine"></div>
                <p class="tipsTitle">
                    <span></span> 温馨提示
                </p>
                <p class="content">
                    1.工作日每日16:00前申请提现，预期到账时间为次日到账。16:00后视为次日申请，逢节假日到账时间顺延。
                </p>
                <p class="content">
                    2.严禁非法提现、套现、洗钱等违法行为。
                </p>
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