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
    <title>个人登录密码修改</title>
    <link rel="stylesheet" href="<%=path%>/front-page/css/base.min.css">
    <link rel="stylesheet" href="<%=path%>/front-page/css/personal_center.css">
    <link rel="stylesheet" type="text/css" href="<%=path%>/front-page/css/me.css">
<script type="text/javascript" src="<%=path%>/front-page/js/json2.js"></script> 
<script type="text/javascript" src="<%=path%>/front-page/js/jquery-1.7.2.min.js"></script>   
<script type="text/javascript" src="<%=path%>/front-page/js/template-web.js"></script> 
	
<script type="text/javascript">
function modifyPass(){
	var data=$("#u_l_form").serialize();
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_api&groovySubName=modifyPassword";
	$.ajax({
		url:url,
		type:'post',
		data:data,
		dataType:'json',
		success:function(data,status){
			var result=JSON.parse(data.resultData);

			if(result.resultStatus!=0){
				alert("修改失败！");
				return;
			}
			var go="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_api&groovySubName=queryInfoByCodeGo";
			window.location.href=go;			
		}
	});	
}
</script>
</head>
  <body>
    <div class="accountMain">
      <div id="J_accountMenu"></div>
      <div class="accountRight">
        <div class="acTitle"> 
          <h2>修改登录密码</h2>
        </div>
        <form id="u_l_form" class="u_l_form p_c_style" onsubmit="return false;">
          <div data-wrap="layout">
            <div data-item="col-2" class="u_l_label">
              <label>原密码</label>
            </div>
            <div data-item="col-5">
              <div data-type="icon"><i class="me-ion-locked"></i>
                <input id="oldpwd" type="password" name="oldpwd" data-validate="/(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,16}$/" placeholder="原密码">
              </div>
            </div>
          </div>
          <div data-wrap="layout">
            <div data-item="col-2" class="u_l_label">
              <label>新密码</label>
            </div>
            <div data-item="col-5">
              <div data-type="icon"><i class="me-ion-locked"></i>
                <input id="newpwd" type="password" name="newpwd" data-validate="/(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,16}$/" placeholder="新密码">
              </div>
            </div>
          </div>
          <div data-wrap="layout">
            <div data-item="col-2" class="u_l_label">
              <label>确认密码</label>
            </div>
            <div data-item="col-5">
              <div data-type="icon"><i class="me-ion-locked"></i>
                <input id="confirmnewpwd" type="password" name="confirmnewpwd" data-validate="/\w{6,}/" placeholder="确认新密码">
              </div>
            </div>
          </div>
          <div data-wrap="layout">
            <div data-offset="2">
              <button id="refer" type="submit" name="refer" data-color="red" class="me-u-btn" onclick="modifyPass()">确认修改</button>
            </div>
          </div>
          <div id="backset"><a href="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_user_api&groovySubName=queryInfoByCodeGo">返回个人设置&gt;&gt;</a></div>
          <div data-wrap="layout" class="remindbox">
            <div class="linear"></div>
            <div class="remindertxt">
              <h5> <span class="spanheart"><i class="me-ion-heart"></i></span>温馨提示：</h5>
              <ul>
                <li>请牢记您设置的新密码，登录密码可通过密码找回功能找回。</li>
              </ul>
            </div>
          </div>
        </form>
      </div>
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