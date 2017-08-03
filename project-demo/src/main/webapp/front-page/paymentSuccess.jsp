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
    <title>支付结果</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/paymentSuccess.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>    
<script type="text/javascript" >

</script>	
</head>
<body>

    <div class="paySuccessCentent" id="">
        <div id="J_payResult">

        </div>
        <div class="paySuccessBtn">
            <a class="me-u-btn" href="<%=path%>/front-page/account.jsp" data-type="minor">查看账户</a>
            <a class="me-u-btn" href="<%=path%>/front-page/regularFinancialList.jsp" data-color="red">继续投资</a>
        </div>
    </div>

        <div class="paySuccessTitle">
            <img src="<%=path%>/front-page/images/paySuccess.png" />
        </div>
        <p class="payText">恭喜您，订单支付成功。</p>
        <table class="firstTable">
            <tr>
                <td>项目名称：<span>{{bid_name}}</span></td>
            </tr>
            <tr>
                <td>到期待收总金额：<span>{{back_amount}}元</span></td>
            </tr>
            <tr>
                <td>投资到期日：<span>{{trade_end_date}}</span></td>
            </tr>     
        </table>
        <table>
            <tr>
                <td>投资金额：<span>{{invest_amount}}元</span></td>
            </tr>
            <tr>
                <td>到期收益：<span>{{expire_profit}}元</span></td>
            </tr>
        </table>

  
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