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
    <title>金宝宝</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/myregular_finance.min.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>  
  <script type="text/javascript" src="<%=path%>/front-page/js/template-web.js"></script> 
        <script id="mr_f_view_tpl" type="text/html">
          <table class="listTable">
            <thead class="listTitle">
              <tr>
                <th>项目名称</th>
                <th>投资金额(元)</th>
                <th>预期收益率</th>
                <th>项目期限</th>
                <th>预期收益(元)</th>
                <th>起息日/到期日</th>
                <th>状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody class="listTitle listCentent2">
				{{each rowsData as value i}}

              <tr>
                <td>{{value.product_name}}</td>
                <td>{{value.invest_amount}}</td>
                <td>{{value.order_rate}}%</td>
                <td>{{value.periods}}</td>
                <td>{{value.expire_profit}}</td>

              </tr>
				{{/each}}


            </tbody>
          </table>

        </script>  
<script type="text/javascript" >
function renderProductTable(){
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_invest_api&groovySubName=getMyInvestListAll";

	$.ajax({
		url:url,
		type:'post',
		dataType:'json',
		success:function(data,status){
			var templateData=new Object();
			templateData.rowsData=data;
			var html = template('mr_f_view_tpl', templateData);
			console.log(html);
			$("#mr_f_view").html(html);			
		}
	});

}
$(function(){
	renderProductTable();
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
    <li class="choosen">
        <a href="<%=path%>/front-page/account.jsp">
                        账户总览
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>
    <li>
        <a href="<%=path%>/front-page/recharge_mode.jsp" id="J_goCharge">
                        账户充值
                        <i class="me-ion-chevron-right"></i>
                    </a>
    </li>
    <li>
        <a href="<%=path%>/front-page/withdraw.jsp" id="J_goWithdraw">
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
          <h2>定期理财</h2>
        </div>
        <div class="mr_f_cont">
          <div id="mr_f_finance" data-wrap="layout" class="mr_f_finance">
            <div data-item="col-6">
              <h3>定期总资产 (元)</h3>
              <p><strong>0.00</strong></p>
              <p>待收本金： 0.00</p>
              <p>待收收益： 0.00</p>
            </div>
            <div data-item="col-6">
              <h3>定期总收益 (元)</h3>
              <p><strong>0.00</strong></p>
              <p>待收收益： 0.00</p>
              <p>已收收益： 0.00</p>
            </div>
          </div>
          <div class="selecttool">
            <div data-wrap="layout">
              <div data-item="col-1" class="selectname">项目类型：</div>
              <div data-item="col-11">
                <label for="alltype">
                  <input id="alltype" type="radio" name="projecttype" value="" checked><span>全部</span><i></i>
                </label>

                <div style="width:60%" class="choosedate"><span>投资时间：</span>
                  <div data-type="norm">
                    <input id="startDate" type="text" placeholder="选择开始日期" class="laydate-icon">
                  </div><span>-</span>
                  <div data-type="norm">
                    <input id="endDate" type="text" placeholder="选择结束日期" class="laydate-icon">
                  </div>
                  <button id="datebtn" type="button" data-color="red" class="me-u-btn">查询</button>
                </div>
              </div>
            </div>
          </div>
          <div class="recordList">
            <div id="mr_f_view">
                              
            </div>
            <ul class="m_fanye"></ul>
          </div>
        </div>
        <script id="mr_f_finance_tpl" type="text/tpl">
          <div data-item="col-6">
            <h3>定期总资产 (元)</h3>
            <p><strong>{{totalAmount}}</strong></p>
            <p>待收本金： {{principal_received}}</p>
            <p>待收收益： {{interest_received}}</p>
          </div>
          <div data-item="col-6">
            <h3>定期总收益 (元)</h3>
            <p><strong>{{accumulated_income}}</strong></p>
            <p>待收收益： {{interest_received}}</p>
            <p>已收收益： {{incomeReceived}}</p>
          </div>
        </script>


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