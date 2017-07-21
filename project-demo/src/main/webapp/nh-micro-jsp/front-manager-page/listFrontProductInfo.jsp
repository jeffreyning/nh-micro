<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>理财产品管理</title>
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
		url:"<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_product_list&groovySubName=getInfoList4Page",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 50

					},
					{
						field : 'product_code',
						title : '产品编号',
						width : 50
					},
					{
						field : 'product_type_name',
						title : '产品分类',
						width : 50
					},
					{
						field : 'periods',
						title : '期限',
						width : 50
					},
					{
						field : 'years_income',
						title : '年化收益率',
						width : 50
					},
					{
						field : 'create_name',
						title : '创建人',
						width : 50
					},	
					{
						field : 'create_time',
						title : '创建时间',
						width : 50
					},	
					{
						field : 'product_state',
						title : '状态',
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
			id : "add",
			text : "添加",
			iconCls:"icon-addOne",
			handler : function() {
				add();
			}
		},{
			id : "update",
			text : "修改",
			iconCls : "icon-edit",
			handler : function() {
				update();
			}
		},{
			id : "delete",
			text : "删除",
			iconCls : "icon-edit",
			handler : function() {
				remove();
			}

		},{
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

	/*添加*/
	function add(){

	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_product_list&groovySubName=toEditProductGo&operFlag=create";
	window.location.href=url;

	}
	/*修改*/
	function update(){
		var sels = $('#infoList').datagrid("getSelected");
		if(sels == ''|| sels==null){
			alert('请选择行');
		}else{		
			var id = sels.id;
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_product_list&groovySubName=toEditProductGo&operFlag=update&id="+id;
		window.location.href=url;
		}
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
				var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=front_product_list&groovySubName=delInfo";
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
					<td>理财产品编码：</td>
					<td><input type="text" id="product_code" name="product_code" /></td>					
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