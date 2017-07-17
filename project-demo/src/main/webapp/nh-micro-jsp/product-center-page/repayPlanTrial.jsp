<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String productId=request.getParameter("productId");

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>试算产品还款计划</title>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/common.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/zTree/css/zTreeStyle/zTreeStyle.css">
<script type="text/javascript">




$(function(){
	$('#infoList').datagrid({
		nowrap:true,
		striped:true,
		pagination : true,
		fitColumns: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=micro_product_center_list&groovySubName=repayPlanTrial",
		columns:[[
					{
						field : 'period',
						title : '期数',
						width : 50

					},
					{
						field : 'interest',
						title : '利息',
						width : 50
					},
					{
						field : 'capital',
						title : '本金',
						width : 50
					},
					{
						field : 'serviceFee',
						title : '服务费',
						width : 50
					}
					,
					{
						field : 'dueDate',
						title : '还款日',
						width : 50
					}					

				]],
        toolbar : [],
        rownumbers:false,
        singleSelect:true
		
	});
});


	//条件查询
	function ReQuery(){
		var data = $('#searchForm').serializeObject();
		$('#infoList').datagrid('reload',data);
	}
	
	//重置查询条件
	function clearForm(){
		$('#searchForm').form('clear');
	}
	
	//刷新
	function refresh(){
		var querydata = $('#searchForm').serializeObject();
		$('#infoList').datagrid('reload',querydata);
	}

	
</script>
</head>
<body class="easyui-layout">
	<div id="infoQuery" class="dQueryMod" region="north"
		style="height: 85px">
		<form id="searchForm">
			<table id="searchTable">
				<tr>
					<td>产品id：</td>
					<td><input type="text" name="productId" value="<%=productId %>"></input></td>
					<td>放款日期：</td>
					<td><input type="text" class="easyui-datebox" id="payDate" name="payDate" /></td>					
					<td>还款截止日期：</td>
					<td><input type="text" class="easyui-datebox" id="dueDate" name="dueDate" /></td>
				</tr>
				<tr>	
					<td>还款日：</td>
					<td><input type="text" id="repayDay" name="repayDay" /></td>
					<td>合同金额：</td>
					<td><input type="text" id="contractAmt" name="contractAmt" /></td>
					<td>合同期数：</td>
					<td><input type="text" id="contractPeriods" name="contractPeriods" /></td>	
				</tr>
				<tr>				
					<td><a href="#" class="easyui-linkbutton "
						iconCls="icon-search" onclick="ReQuery()">试算</a><a href="#"
						class="easyui-linkbutton" iconCls="icon-redo"
						onclick="clearForm()">清空</a></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="roleList" region="center">
		<div class="easyui-tabs l_listwid" id="accountTab">
			<table id="infoList"></table>
		</div>
	</div>



</body>
</html>