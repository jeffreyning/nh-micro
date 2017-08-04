<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Map,java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>录入互联网产品信息</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/common.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/zTree/css/zTreeStyle/zTreeStyle.css">
<style type="text/css">
.ntable {border-collapse:collapse;border-color:black;width:100%;border-style:solid;border-width:1px;}
.ntable td {border-style:solid;border-width:1px;}
</style>


<script type="text/javascript">

function selectProduct(){
	var productCode=$('#product_code').val();
	var data=new Object();
	data.productCode=productCode;
 	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_product_list&groovySubName=getInfoByCode";
	$.ajax({
		url:url,
		type:'post',
		data:data,
		dataType:'json',
		success:function(data,status){
			$('#start_invest_money').val(data.start_invest_money);
			$('#per_investment').val(data.per_investment);
			$('#stepping').val(data.stepping);
			$('#periods').val(data.periods);
			$('#years_income').val(data.years_income);
			$('#pay_type').val(data.pay_type);
			$('#repayment_mode').val(data.repayment_mode);
			
			
		}
	});
}

function updateTaskData(){
	var dataO = $('#taskDataForm').serialize();

 	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_bid_list&groovySubName=updateInfo&id=${formdata.id}";
	$.post(url,dataO,function(data,stats){
		if(stats=="success" ){
			$.messager.show({
				msg : "操作成功",
				title : "消息"
			});
		}
	}); 
} 
function insertTaskData(){
	var dataO = $('#taskDataForm').serialize();

 	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_bid_list&groovySubName=createInfo";
	$.post(url,dataO,function(data,stats){
		if(stats=="success" ){
			$.messager.show({
				msg : "操作成功",
				title : "消息"
			});
		}
	}); 
}
$(function(){

});
	


</script>
</head>
<body class="easyui-layout">
<c:if test="${operFlag=='create'}">
<button onclick="insertTaskData();">保存数据</button>
</c:if>
<c:if test="${operFlag=='update'}">
<button onclick="updateTaskData();">更新数据</button>
</c:if>
<c:if test="${operFlag=='view'}">

</c:if>
<form id="taskDataForm" >

<table class="ntable" cellspacing=0 cellpadding=0>
<tr>
<td>标的编号</td><td><input type="text" id="bid_code" name="bid_code" value="${formdata.bid_code}"></input></td>
</tr>
<tr>
<td>标的名称</td><td><input type="text" id="bid_name" name="bid_name" value="${formdata.bid_name}"></input></td>
</tr>
<tr>
<td>产品编号</td>
<td>
<select id="product_code" name="product_code" onchange="selectProduct()">
<option value=""></option>
<%
List productList=(List)request.getAttribute("productList");
Map formdata=(Map)request.getAttribute("formdata");
int size=productList.size();
for(int i=0;i<size;i++){
	Map rowdata=(Map)productList.get(i);
%>
 <option value="<%=rowdata.get("product_code")%>" 
 <%if(rowdata.get("product_code").equals(formdata.get("product_code"))) {%>>selected="selected"<%} %>
 ><%=rowdata.get("product_name")%></option>
<%
}
%>
</select>
</td>
</tr>
<tr>
<td>起投金额(元)</td><td><input type="text" id="start_invest_money" name="start_invest_money" value="${formdata.start_invest_money}"></input></td>
<td>单笔最高金额(元)</td><td><input type="text" id="per_investment" name="per_investment" value="${formdata.per_investment}"></input></td>
</tr>
<tr>
<td>步进金额(元)</td><td><input type="text" id="stepping" name="stepping" value="${formdata.stepping}"></input></td>
<td>支付方式</td><td>
<select id="pay_type" name="pay_type"  >
	<option value="">--请选择--</option>
	<option value="OPAY" <c:if test="${ formdata.pay_type=='OPAY'}"> selected = selected </c:if>>在线支付</option>
	<option value="OAPP" <c:if test="${ formdata.pay_type=='OAPP'}"> selected = selected </c:if>>在线预约</option>
</select><span style="color:red;">*</span>
</td>
</tr>
<tr>
<td>期数</td><td><input type="text" id="periods" name="periods" value="${formdata.periods}"></input></td>
<td>年化收益率</td><td><input type="text" id="years_income" name="years_income" value="${formdata.years_income}"></input></td>
</tr>
<tr>
<td>起息规则</td><td><input type="text" id="interrest_mode" name="interrest_mode" value="${formdata.interrest_mode}"></input></td>
<td>还款方式</td><td>
	<select id="repayment_mode" name="repayment_mode">
		<option value="">--请选择--</option>
		<option value="DQBX" <c:if test="${ formdata.repayment_mode=='DQBX'}"> selected = selected </c:if>>到期本息</option>
		<option value="XXHB" <c:if test="${ formdata.repayment_mode=='XXHB'}"> selected = selected </c:if>>先息后本</option>
	</select><span style="color:red;">*</span>
</td>
</tr>
<tr>
<td>支付主体</td><td><input type="text" id="pay_bus_id" name="pay_bus_id" value="${formdata.pay_bus_id}"></input></td>
</tr>
</table>
</form>


</body>
</html>