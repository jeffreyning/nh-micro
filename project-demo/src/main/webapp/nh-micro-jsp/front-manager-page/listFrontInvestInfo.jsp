<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户投资管理</title>
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
		url:"<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_invest_list&groovySubName=getInfoList4Page",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 50

					},
					{
						field : 'order_number',
						title : '订单号',
						width : 50

					},					
					{
						field : 'user_code',
						title : '用户编码',
						width : 50
					},
					{
						field : 'user_name',
						title : '用户姓名',
						width : 50
					},
					{
						field : 'bid_name',
						title : '标的名称',
						width : 50
					},
					{
						field : 'bid_code',
						title : '标的code',
						width : 50
					},
					{
						field : 'invest_amout',
						title : '投资金额',
						width : 50
					},	
					{
						field : 'bank_pay',
						title : '卡支付金额',
						width : 50
					},					
					{
						field : 'order_rate',
						title : '订单利率',
						width : 50
					},	
					{
						field : 'periods',
						title : '期限',
						width : 50
					},
				
					{
						field : 'invest_type',
						title : '投资类型',
						width : 50,
						formatter:function(val,row){
							if(val=='0'){
								return "首次";
							}else if(val=='1'){
								return "追加";
							}else{
								return "续投";
							}
						}						
					},
					{
						field : 'trade_status',
						title : '交易状态',
						width : 50,
						formatter:function(val,row){
							if(val=='0'){
								return "购买失败";
							}else if(val=='1'){
								return "购买成功";
							}else if(val=='3'){
								return "支付中";
							}else if(val=='4'){
								return "待支付";
							}else if(val=='5'){
								return "持有中";
							}else if(val=='6'){
								return "回款成功";
							}
						}						
					},	
					{
						field : 'create_time',
						title : '创建时间',
						width : 50
					},		
					{
						field : 'success_date',
						title : '支付成功时间',
						width : 50
					},						
					{
						field : 'approve_time',
						title : '审核时间',
						width : 50
					},
	
		
					{
						field : 'oper',
						title : '操作',
						width : 100,
						formatter:function(val,row){
								return "";
						}
					}
				]],
        toolbar : [ {
			id : "refresh",
			text : "刷新",
			iconCls : "icon-reload",
			handler : function() {
				refresh();
			}
		
		}],
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
		style="height: 55px">
		<form id="searchForm">
			<table id="searchTable">
				<tr>
					<td>用户编码：</td>
					<td><input type="text" id="user_code" name="user_code" /></td>					
					<td><a href="#" class="easyui-linkbutton "
						iconCls="icon-search" onclick="ReQuery()">查询</a><a href="#"
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