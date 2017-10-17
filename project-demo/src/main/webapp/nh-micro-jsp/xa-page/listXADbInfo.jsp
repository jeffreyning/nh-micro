<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息管理</title>
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
		url:"<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=MicroXADbList&groovySubName=getInfoList4Page&sort=create_time&order=desc",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 100

					},
					{
						field : 'unique_resource_name',
						title : '数据源标识',
						width : 100
					
					},
					{
						field : 'xa_datasource_classname',
						title : '数据源驱动类',
						width : 100
					
					},
					{
						field : 'db_url',
						title : '数据库连接地址',
						width : 100
					
					},
					{
						field : 'db_user',
						title : '数据库连接用户',
						width : 100
					
					}					
					,
					{
						field : 'remark',
						title : '备注',
						width : 100,
						sortable:true
					}
					,
					{
						field : 'create_time',
						title : '创建时间',
						width : 100,
						sortable:true
					}
				]],
        toolbar : [ {
			id : "add",
			text : "创建",
			iconCls:"icon-addOne",
			handler : function() {
				add();
			}
		}
        ,{
			id : "update",
			text : "修改",
			iconCls : "icon-edit",
			handler : function() {
				updateInfo();
			}
		}
        ,{
			id : "updatePass",
			text : "修改密码",
			iconCls : "icon-edit",
			handler : function() {
				updatePass();
			}
		}        
        ,{
			id : "delete",
			text : "删除",
			iconCls : "icon-edit",
			handler : function() {
				remove();
			}

		},
        {
			id : "disconn",
			text : "暂停连接",
			iconCls : "icon-edit",
			handler : function() {
				disconn();
			}

		},		
        {
			id : "reconn",
			text : "重新连接",
			iconCls : "icon-edit",
			handler : function() {
				reconn();
			}

		},		
		
		{
			id : "refresh",
			text : "刷新",
			iconCls : "icon-reload",
			handler : function() {
				refresh();
			}
		}
		],
        rownumbers:false,
        singleSelect:true
		
	});
});

function disconn(){
	var sels = $("#infoList").datagrid("getSelected");
    if(sels==""||sels==null){
	    alert("请选择行");
    }else{
    	var sels = $("#infoList").datagrid("getSelected");
    	var param=new Object();
    	param.id=sels.id;
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=MicroXADbList&groovySubName=closeDb";
		$.post(url,param,function(data,stats){
			if(stats=="success" ){
				$.messager.show({
					msg : "操作成功",
					title : "消息"
				});

			}
		});    	
    }
}

function reconn(){
	var sels = $("#infoList").datagrid("getSelected");
    if(sels==""||sels==null){
	    alert("请选择行");
    }else{
    	var sels = $("#infoList").datagrid("getSelected");
    	var param=new Object();
    	param.id=sels.id;
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=MicroXADbList&groovySubName=restartDb";
		$.post(url,param,function(data,stats){
			if(stats=="success" ){
				$.messager.show({
					msg : "操作成功",
					title : "消息"
				});

			}
		});    	
    }
}
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
		//var querydata = $('#searchForm').serializeObject();
		//$('#infoList').datagrid('reload',querydata);
		$('#infoList').datagrid('reload');
	}

	/* 增加 */
	function add(){
			$("#addOne").form("clear");
			$("#addOne").dialog('open').dialog('setTitle', '信息添加');
	}	
	
	function addOne(){
		var dataO = $("#addForm").serializeObject();
	
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=MicroXADbList&groovySubName=createInfo";
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

	/* 修改密码 */
	function updatePass(){  
		var sels = $("#infoList").datagrid("getSelected");
	    if(sels==""||sels==null){
		    alert("请选择行");
	    }else{
	    	var sels = $("#infoList").datagrid("getSelected");
	    	$.messager.prompt('提示框', '输入密码:', function(r) {
	    	    if( r ) {
	    	    	var param=new Object();
	    	    	param.id=sels.id;
	    	    	param.db_password=r;
	    			var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=MicroXADbList&groovySubName=modifyPass";
	    			$.post(url, param, function(data, stats) {
	    				if (stats == "success" ) {
	    					$.messager.show({
	    						msg : "操作成功",
	    						title : "消息"
	    					});
	    				}
	    			});	    	    	
	    	    };
	    	});

	    }
		
	}	
	
	
	/* 修改 */
	function updateInfo(){  
		var sels = $("#infoList").datagrid("getSelected");
	    if(sels==""||sels==null){
		    alert("请选择行");
	    }else{
	    	var sels = $("#infoList").datagrid("getSelected");
	    	$("#updateOne").dialog('open').dialog('setTitle', '信息修改');
	    	$("#updateOne").form("load", sels);
	    }
		
	}	
	
	function updateOne(){
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=MicroXADbList&groovySubName=updateInfo";
		$.post(url, $("#updateOneForm").serialize(), function(data, stats) {
			if (stats == "success" ) {
				$.messager.show({
					msg : "操作成功",
					title : "消息"
				});
				refresh();
				updateCancel();

			}
		});
	}
	
	function updateCancel() {
		$("#updateOneForm").form("clear");
		$("#updateOne").dialog('close');
	}

	/*删除*/
	function remove(){
		var sels = $('#infoList').datagrid("getSelected");
		if(sels == ''|| sels==null){
			alert('请选择行');
		}else{
			var result = confirm("确定要删除吗？");
			if(result == true){
				var querydata = $('#searchForm').serializeObject();
				var id = sels.id;
				var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=MicroXADbList&groovySubName=delInfo";
				url=url+"&id="+id;
				$.post(url,function(data,stats){
							if(stats=="success" ){
								$.messager.show({
									msg : data.msg,
									title : "消息"
								});
								$('#infoList').datagrid('reload',querydata);
							}
						}
					);
			        
			}
		}
	}
</script>
</head>
<body class="easyui-layout">
	<div id="infoQuery" class="dQueryMod" region="north"
		style="height: 55px">
		<form id="searchForm">
			<table id="searchTable">
				<tr>
					<td>备注：</td>
					<td><input type="text" id="remark" name="remark" /></td>
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



	<!-- 修改 -->
	<div id="updateOne" class="easyui-dialog" modal="true" align="center"
		style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="updateOneForm" novalidate method="post" action="">
			<input type="hidden" id="showForm_temp" value="" />
			<table id="updateTable"
				style="margin-top: 10px; margin-left: -40px;">
				<tr>
					<!-- <td>Id：</td> -->
					<td><input type="hidden" id="id" name="id" value="" /></td>
				</tr>
				<tr>
					<td align="right">数据源标识:</td>
					<td><input type="text" id="unique_resource_name" name="unique_resource_name" value="" /></td>
				</tr>
				
				<tr>
					<td align="right">数据源驱动类:</td>
					<td><input type="text" id="xa_datasource_classname" name="xa_datasource_classname" value="" /></td>
				</tr>	
				<tr>
					<td align="right">数据库连接地址:</td>
					<td><input type="text" id="db_url" name="db_url" value="" /></td>
				</tr>	
				<tr>
					<td align="right">数据库连接用户:</td>
					<td><input type="text" id="db_user" name="db_user" value="" /></td>
				</tr>				
			</table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70"
					href="javascript:updateOne();">确认</a> <a
					class="easyui-linkbutton dPbtnLight70"
					href="javascript:updateCancel();">取消</a>
			</div>
		</form>
	</div>

	<div id="addOne" class="easyui-dialog" modal="true" align="center"
		style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="addForm" novalidate method="post" action="">
			<input type="hidden" id="addShowForm_temp" value="" />
			<table id="addTable" style="margin-top: 10px; margin-left: -40px;">

				<tr>
					<td align="right">数据源标识:</td>
					<td><input type="text" id="unique_resource_name" name="unique_resource_name" value="" /></td>
				</tr>
				
				<tr>
					<td align="right">数据源驱动类:</td>
					<td><input type="text" id="xa_datasource_classname" name="xa_datasource_classname" value="" /></td>
				</tr>	
				<tr>
					<td align="right">数据库连接地址:</td>
					<td><input type="text" id="db_url" name="db_url" value="" /></td>
				</tr>	
				<tr>
					<td align="right">数据库连接用户:</td>
					<td><input type="text" id="db_user" name="db_user" value="" /></td>
				</tr>	
				<tr>
					<td align="right">数据库连接密码:</td>
					<td><input type="text" id="db_password" name="db_password" value="" /></td>
				</tr>	
			</table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:addOne();">确认</a>
				<a class="easyui-linkbutton dPbtnLight70"
					href="javascript:addCancel();">取消</a>
			</div>
		</form>
	</div>


</body>
</html>