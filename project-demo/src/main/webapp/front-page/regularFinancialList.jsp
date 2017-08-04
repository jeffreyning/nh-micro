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
    <title>定期理财</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/financialList.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>   
<script type="text/javascript" src="<%=path%>/front-page/js/template-web.js"></script> 
	
<script id="product_tpl" type="text/html">
    {{each rowsData as value i}}
    <tr>
    <td><p class="xieyiLV">{{value.years_income}}<b>%</b></p></td>
    <td><p class="qixianData">{{value.periods}}</p></td>
    <td>{{value.product_name}}</td>
    <td>{{value.start_invest_money}}</td>
    <td>{{value.shengyu}}</td>
    <td class="red">
    <a class="gary" href="javascript:productDetail('{{value.product_code}}');" data-color="red" >立即抢购</a>
    </td>
	</tr>
    {{/each}}
</script> 

<script type="text/javascript">
function productDetail(productCode){
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_product_api&groovySubName=productDetailGo";
	url=url+"&productCode="+productCode;
	window.location.href=url;
}

function loadTable(){
	
	$.ajax({
		url:"<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_product_api&groovySubName=getInfoListAll",
		type:"post",
		dataType:"json",
		success:function(data,status){
			var templateData=new Object();
			templateData.rowsData=data;		
			var html = template('product_tpl', templateData);
			$("#J_listCentent").html(html);	
		  }
	});

}

$(function(){
	loadTable();
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
                    <img src="<%=path%>/front-page/images/logo.png" class="img-responsive">
                </a>
            </h1>
            <ul class="nav" data-item="col-10" data-flag="1" id="J_headerNav">

                <li><a href="<%=path%>/front-page/regularFinancialList.jsp">定期理财</a></li>
                
                <c:if test="${sessionScope.tokenId!=null}">
                <li><a href="<%=path%>/front-page/account.jsp">个人中心</a></li>
                </c:if>
                
            </ul>
        </div>
    </div>
</header>
    <div class="listBanner">
        <div class="bannerCenter regularBanner">
        </div>
    </div>
    
    <div class="listContent">
        <div class="financialChoice" id="J_financialChoice">
            
        </div>
        <div class="listSelect">
            <div class="listSelectCentent">
                <ul class="listSelectUl" id="J_xiangmuType">
                    <li>项目类型：</li>
				    <li >全部</li>
                </ul>
                <ul class="listSelectUl" id="J_xiangmuDate">
                    <li>项目期限：</li>
            		<li >全部</li>
                </ul>
                <ul class="listSelectUl" id="J_xiangmuRate">
                    <li>年化收益：</li>
                    <li>全部</li>
                    
                </ul>
            </div>
        </div>
        <table class="listTable regularListTable">
            <thead class="listTitle regularList" id="J_listSelect">
                <th><strong>预期年化收益率</strong> <span class="listSearch"><i datasort="1"></i><i datasort="0"></i></span></th>
                <th><strong>期限/锁定期</strong><span class="listSearch"><i datasort="1"></i><i datasort="0"></i></span></th>
                <th>项目名称</th>
                <th>起投金额</th>
                <th>剩余可投金额</th>
                <th>操作</th>
            </thead>
            <tbody class="listTitle listCentent" id="J_listCentent">

            </tbody>
           
        </table>
        <ul class="phoneList" id="J_phoneList">
              
        </ul>
        <ul class="m_fanye">
            
        </ul>
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