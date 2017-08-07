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
    <link rel="stylesheet" href="<%=path%>/front-page/css/product_details.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">

    
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>   
<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>   
<script type="text/javascript" >
function toPay(){
	var investAmount=$('#inputMoney').val();
	var url="/project-demo/NhEsbServiceServlet?cmdName=Groovy&subName=front_product_api&groovySubName=productPayGo";
	url=url+'&productCode=${productInfo.product_code }&investAmount='+investAmount;
	window.location.href=url;	
}
$(function(){
	var par=${productInfo.have_money}/${productInfo.product_amount}*100;
	$("#progressbar").progressbar({ value: par });
	$("#progressbar_val").html(parseInt(par)+"%");
});
</script>	
</head>
<body>
<style>
    header .header li.header_none {
        display: none;
    }
    
.ui-progressbar {
  height: 0.5em;
  text-align: left;
  overflow: hidden;
    background-color: #666;
}
.ui-progressbar .ui-progressbar-value {
  margin: -1px;
  height: 100%;
  background-color: #ccc;
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
<!--详情banner-->
<div class="p-wrap">
    <div class="p-cont pr">
       <div class="banner-left">
            <p class="btH1">${productInfo.product_name }</p>
        </div>
        <div class="banner">
            <ul class="banner_list clearfix">
                <li>
                <p class="red"><span class="font60 rate">${productInfo.years_income }</span><b class="font24">%</b></p>
                <label class="c_66">
                   预期年化收益率
                </label>
                </li>
                <li class="two">
                    <p class="c_33 gdqx_show"><span class="font32 periods">${productInfo.periods }</span><b class="font22">个月</b></p> 
                    <label class="c_66 gdqx">
                        项目期限
                    </label>
                </li>
                <li class="three">
                    <p class="c_33">
                        <span class="font32 J_surplus_money">${productInfo.surplus_invest_money }</span><b class="font22">元</b>
                    </p> 
                    <label class="c_66">
                        剩余可投金额
                    </label>
                </li>
            </ul>
            <div class="progress">
                <div class="progressBar clearfix">
                    <span class="fl"><div id="progressbar" class="J_progressBar"></div></span>
                    <label id="progressbar_val" class="red fr J_progressBar_val">%</label>
                </div>
                <p class="clearfix">
                    <label class="fl">项目金额：</label>
                    <span class="fl"><b class="red J_product_amount">${productInfo.product_amount}</b> 元
                </span></p>
            </div>    
        </div>
        <div class="banner_wap">          
            <ul class="clearfix">
                <li class="clearfix">          
                    <label class="c_66">预期年化收益率：</label>
                    <p class="red">
                        <span class="font60 rate" >${productInfo.years_income }</span><b class="font24">%</b>
                    </p>
                </li>
                <li class="clearfix">                   
                    <label class="c_66 gdqx">项目期限：</label>
                    <p class="c_33 gdqx_show">
                        <span class="font32 periods">${productInfo.periods }</span><b class="font22">个月</b>
                    </p>           
                </li>

            </ul>

        </div>    
        <div class="banner-right">
            <ul class="banner_infor">
                <li class="clearfix">
                    <label class="fl">账户余额：</label>
                    <div id="sign_In" class="fl">
                        <span><b class="red" id="balanceMoney" data-money="balanceMoney">${accountInfo.available_balance }</b>元</span>
                        <strong class="eyesIcon hide"></strong>
                    </div>
                </li>
                <li>
                    <div data-type="norm">
                        <input type="tel" id="inputMoney" maxlength="13" autocomplete="off" data-step="1,000.00" data-maxAmount="1,000,000.00" data-least="50,000.00" placeholder="100元起投" data-result="false">
                    </div>
                    <em>元</em> 
                    <p class="error"></p>                 
                </li>
               <!--  <li class="lineH clearfix"><label>预期到期收益：</label><span><b class="red J_dailyReturn">0.00</b>元</span></li> -->
                <li>
                <button class="me-u-btn" data-color="red" data-size="block" id="J_submit" onclick="toPay()">立即抢购</button> 
                </li>
                <li class="clearfix last">
                <div data-wrap="checkbox" data-orange="" id="checkbox" class="fl">
                    <input type="checkbox" id="mr0" > 
                    <label for="mr0">
                    </label>
                </div>
                <span class="fl">
                    我已阅读并同意<a href="<%=path%>/front-page/protocol_risk.html" target="_blank">《风险提示书》</a><br>且同意
                        <a href="javascript:void(0);"  id="agreement">《认购协议》</a>
                </span>
                </li>    
            </ul>

     
        </div>

    </div>
</div>
<!--详情介绍-->
<div class="p-cont">
    <ul class="p_details_tab clearfix">
        <li class="fl active">项目介绍</li>
        <li class="fl">参与记录</li>
    </ul>
    <div class="p_details">
        <!--项目介绍-->
        <div class="p_details_con" style="display:block">
            <div class="p_details_list clearfix">
                <span class="p_icon fl"></span>          
                <dl class="fl">
                    <dd>项目名称</dd>
                    <dt>${productInfo.bid_name}</dt>
                </dl>
            </div>
            <div class="p_details_list clearfix" data-show="2">
                <span class="p_icon pic02 fl"></span>          
                <dl class="fl" >
                    <dd>合作机构介绍</dd>
                    <dt>旨在为各类金融资产、大宗金融衍生产品、企业产权债权提供公开、公正、公平的交易。
                    </dt>
                </dl>
            </div>
            <div class="p_details_list clearfix" data-show="0">
                <span class="p_icon pic03 fl"></span>          
                <dl class="fl" >
                    <dd>适合人群</dd>
                    <dt>稳定性、进取型用户</dt>
                </dl>
            </div>
            <div class="p_details_list clearfix" data-show="2">
                <span class="p_icon pic14 fl"></span>          
                <dl class="fl" >
                    <dd>备案机构</dd>
                    <dt>交易所有限公司</dt>
                </dl>
            </div>
            <div class="p_details_list clearfix">
                <span class="p_icon pic05 fl"></span>          
                <dl class="fl">
                    <dd>募集金额</dd>
                    <dt>1,500,000.00元</dt>
                </dl>
            </div>
            <div class="p_details_list clearfix">
                <span class="p_icon pic06 fl"></span>          
                <dl class="fl">
                    <dd>募集周期</dd>
                    <dt></dt>
                </dl>
            </div>
            <div class="p_details_list clearfix">
                <span class="p_icon pic07 fl"></span>          
                <dl class="fl">
                    <dd>收益起算日</dd>
                    <dt>投资成功计息 T+1</dt>
                </dl>
            </div>
            <div class="p_details_list clearfix" data-show="0">
                <span class="p_icon pic09 fl"></span>          
                <dl class="fl" >
                    <dd>资金回收方式</dd>
                    <dt>到期一次性回收本金和收益</dt>
                </dl>
            </div>
            <div class="p_details_list clearfix" data-show="0">
                <span class="p_icon pic10 fl"></span>          
                <dl class="fl">
                    <dd>风控措施</dd>
                    <dt>交易所对产品发行方资质要求严格，所有交易产品均经过交易所成熟风控体系过滤</dt>
                </dl>
            </div>
            <div class="p_details_list clearfix" data-show="2">
                <span class="p_icon pic11 fl"></span>          
                <dl class="fl">
                    <dd>交易所挂牌信息</dd>
                    <dt><p><a href="" target="_blank" title="挂牌信息">挂牌信息</a></p></dt>
                </dl>
            </div>
            <div class="p_details_list clearfix" data-show="0">
                <span class="p_icon pic12 fl"></span>          
                <dl class="fl">
                    <dd>其它费用</dd>
                    <dt>无</dt>
                </dl>
            </div>
            <div class="p_details_list clearfix" data-show="0">
                <span class="p_icon pic13 fl"></span>          
                <dl class="fl">
                    <dd>资产类型</dd>
                    <dt>收益权转让</dt>
                </dl>
            </div>
        </div>
        <!--参与记录-->
        <div class="p_details_con">
            <div class="p_pic_list">
                <table class="p_details_tabel">
                        <tr>
                            <th width="15%">序号</th>
                            <th width="20%">参与人</th>
                            <th width="45%">
                                <span>参与金额(元)</span>
                            </th>
                            <th width="20%">参与时间</th>
                        </tr>
                        <tbody id="recordlist">

                        </tbody>    
                </table>
            </div>
            <!--page-->
            <ul class="m_fanye">
            </ul>              
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
	
