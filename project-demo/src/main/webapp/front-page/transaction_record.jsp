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
    <title>交易记录</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/myregular_finance.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>   
<script type="text/javascript" src="<%=path%>/front-page/js/template-web.js"></script> 
        <script id="t_r_table_tpl" type="text/html">
{{each rowsData as value i}}
          <tr>
            <td>{{value.create_time}}</td>
            <td>
{{if(value.recharge_type==1)}}充值{{/if}}
{{if(value.recharge_type==2)}}提现{{/if}}
{{if(value.recharge_type==3)}}投资{{/if}}
{{if(value.recharge_type==4)}}回款{{/if}}
{{if(value.recharge_type==5)}}手续费{{/if}}
{{if(value.recharge_type==6)}}红包{{/if}}

</td>
            <td>
		{{if(value.recharge_status==1)}}处理中{{/if}}
		{{if(value.recharge_status==2)}}成功{{/if}}
		{{if(value.recharge_status==3)}}失败{{/if}}
			</td>
            <td>{{value.recharge_money}}</td>
           <!-- <td>{{value.account_balance}}</td> -->
          </tr>
{{/each}}

        </script> 
<script type="text/javascript" >
function renderRechargeTable(){
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_recharge_api&groovySubName=getMyRechargeListAll";

	$.ajax({
		url:url,
		type:'post',
		dataType:'json',
		success:function(data,status){
			var templateData=new Object();
			templateData.rowsData=data;
			var html = template('t_r_table_tpl', templateData);
			$("#t_r_tbody").html(html);			
		}
	});

}
$(function(){
	renderRechargeTable();
});
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
                <a href="index.html">
                    <img src="images/logo.png" class="img-responsive">
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
        <img src="images/headImg.png">
    </div>
    <p class="loginTel" id="J_loginTel">
        ----***----
    </p>
    <p class="saftyLevel">
        <span>安全等级</span>
        <span class="levelPercent">
                        <span></span>
        </span>
        <a id="J_goUp" href="personal_center.html">
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
    <li class="choosen">
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
          <h2>交易记录</h2>
        </div>
        <div class="t_r_cont">
          <div class="selecttool">
            <div data-wrap="layout">
              <div data-item="col-1" class="selectname">交易日期：</div>
              <div data-item="col-11">
                <label for="alltime">
                  <input id="alltime" type="radio" name="transactiontime" value="" num="all" checked><span>全部</span><i></i>
                </label>
<!--                 <label for="oneweek">
                  <input id="oneweek" type="radio" name="transactiontime" value="1" num="7"><span>最近一周</span><i></i>
                </label>
                <label for="onemonth">
                  <input id="onemonth" type="radio" name="transactiontime" value="2" num="30"><span>最近一个月</span><i></i>
                </label>
                <label for="threemonth">
                  <input id="threemonth" type="radio" name="transactiontime" value="3" num="90"><span>最近三个月</span><i></i>
                </label>
                <label for="sixmonth">
                  <input id="sixmonth" type="radio" name="transactiontime" value="6" num="180"><span>最近六个月</span><i></i>
                </label>
                <label for="oneyearover">
                  <input id="oneyearover" type="radio" name="transactiontime" value="12" num="360"><span>最近一年</span><i></i>
                </label> -->
              </div>
            </div>
            <div data-wrap="layout">
              <div data-item="col-1" class="selectname">交易类型：</div>
              <div data-item="col-11">
                <label for="alltype">
                  <input id="alltype" type="radio" name="transactiontype" value="" checked><span>全部</span><i></i>
                </label>
<!--                 <label for="rechargemode">
                  <input id="rechargemode" type="radio" name="transactiontype" value="1"><span>充值</span><i></i>
                </label>
                <label for="withdrawcash">
                  <input id="withdrawcash" type="radio" name="transactiontype" value="2"><span>提现</span><i></i>
                </label>
                <label for="investment">
                  <input id="investment" type="radio" name="transactiontype" value="3"><span>投资</span><i></i>
                </label>
                <label for="payment">
                  <input id="payment" type="radio" name="transactiontype" value="4"><span>回款</span><i></i>
                </label>
                <label for="fee">
                  <input id="fee" type="radio" name="transactiontype" value="5"><span>手续费</span><i></i>
                </label>
				<label for="redenv">
                  <input id="redenv" type="radio" name="transactiontype" value="6"><span>红包</span><i></i>
                </label>         -->      
			   </div>
            </div>
          </div>
          <div class="recordList">
            <table class="listTable">
              <thead class="listTitle">
                <tr>
                  <th>时间</th>
                  <th>交易类型</th>
                  <th>交易详情</th>
                  <th>交易金额（元）</th>
                  <!-- <th>账户余额（元）</th> -->
                </tr>
              </thead>
              <tbody id="t_r_tbody" class="listTitle listCentent2"></tbody>
            </table>

            <ul class="m_fanye"></ul>
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