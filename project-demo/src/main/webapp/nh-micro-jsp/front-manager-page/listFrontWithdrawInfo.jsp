<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户提现管理</title>
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

/* 审批 */
function add(id){
		$("#addOne").form("clear");
		$("#addShowForm_temp").val(id);
		$("#addOne").dialog('open').dialog('setTitle', '审批');
}	

function addOne(approvalStatus){
	var dataO = $("#addForm").serializeObject();
	dataO.approve_status=approvalStatus;


	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_withdraw_list&groovySubName=withdrawApproval";
	$.post(url,dataO,function(data,stats){
		if(stats=="success" ){
			$.messager.show({
				msg : "操作成功",
				title : "消息"
			});
			refresh();
			addCancel();
		}
	});

}
function addCancel() {
	$("#addForm").form("clear");
	$("#addOne").dialog('close');
}


$(function(){
	$('#infoList').datagrid({
		nowrap:true,
		striped:true,
		pagination : true,
		fitColumns: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_withdraw_list&groovySubName=getInfoList4Page",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 50

					},
					{
						field : 'withdraw_number',
						title : '提现流水号',
						width : 50

					},					
					{
						field : 'apply_user_code',
						title : '用户编码',
						width : 50
					},
					{
						field : 'approve_user_name',
						title : '用户姓名',
						width : 50
					},
					{
						field : 'withdraw_money',
						title : '提现金额',
						width : 50
					},
					{
						field : 'withdraw_toaccount_money',
						title : '提现到账金额',
						width : 50
					},
					{
						field : 'withdraw_fee',
						title : '提现手续费',
						width : 50
					},					
					{
						field : 'withdraw_status',
						title : '提现状态',
						width : 50,
						formatter:function(val,row){
							if(val=='1'){
								return "处理中";
							}else if(val=='2'){
								return "提现成功";
							}else{
								return "提现失败";
							}
						}						
					},
					{
						field : 'approve_status',
						title : '审核状态',
						width : 50,
						formatter:function(val,row){
							if(val=='0'){
								return "待审";
							}else if(val=='1'){
								return "通过";
							}else{
								return "拒绝";
							}
						}						
					},	
					{
						field : 'create_time',
						title : '创建时间',
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
							if(row.approve_status==0){
								return "<a href='javascript:add(\""+row.id+"\")'>提现审批</a>";
							}else{
								return "";
							}
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

	<div id="addOne" class="easyui-dialog" modal="true" align="center"
		style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="addForm" novalidate method="post" action="">
			<input type="hidden" id="addShowForm_temp" name="id" value="" />

			<table id="addTable" style="margin-top: 10px; margin-left: -40px;">

				<tr>
					<td align="right">提现审批意见:</td>
					<td><input type="text" id="refuse_reason" name="refuse_reason" value="" /></td>
				</tr>
	
			</table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:addOne('1');">同意</a>
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:addOne('2');">拒绝</a>
			</div>
		</form>
	</div>
	
</body>
</html>