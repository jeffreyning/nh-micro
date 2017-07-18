<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String tableName="contract_temp_list";
	String pageName="listDictionaryInfo";	
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
var contractId="";
$(function(){
	$('#infoList').datagrid({
		nowrap:true,
		striped:true,
		pagination : true,
		fitColumns: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"<%=path%>/NhEsbServiceServlet?tableName=<%=tableName%>&pageName=<%=pageName%>&cmdName=Groovy&subName=micro_mvc_dictionary_list&groovySubName=getInfoList4Page",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 100

					}
					,
					{
						field : 'meta_key',
						title : '合同模板标识',
						width : 100
					}
					,
					{
						field : 'meta_name',
						title : '合同模板名称',
						width : 100
					}
					,
					{
						field : 'oper',
						title : '操作',
						width : 100,
						formatter: function(value, row, index){
							var temp="'"+row.id+"'";
							var upload_url='<a href="javascript:void(0)" onclick="contractId='+temp+';upload();">上传</a>';
							var compile_url='<a href="javascript:void(0)" onclick="compile('+temp+');">编译</a>';
							var pdf_url='<a href="javascript:void(0)" onclick="createPdf('+temp+');">下载pdf</a>';
						    return upload_url+'&nbsp;'+compile_url+'&nbsp;'+pdf_url;
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
				updateInfo();
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

	/* 增加 */
	function add(){
			$("#addOne").form("clear");
			$("#addOne").dialog('open').dialog('setTitle', '信息添加');
	}	
	
	function addOne(){
		var dataO = $("#addForm").serialize();
		var temp = $("#addForm #addShowForm_temp").val();
	
		var url="<%=path%>/NhEsbServiceServlet?tableName=<%=tableName%>&pageName=<%=pageName%>&cmdName=Groovy&subName=micro_mvc_dictionary_list&groovySubName=createInfo";
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
		var url="<%=path%>/NhEsbServiceServlet?tableName=<%=tableName%>&pageName=<%=pageName%>&cmdName=Groovy&subName=micro_mvc_dictionary_list&groovySubName=updateInfo";
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
				var url="<%=path%>/NhEsbServiceServlet?tableName=<%=tableName%>&pageName=<%=pageName%>&cmdName=Groovy&subName=micro_mvc_dictionary_list&groovySubName=delInfo";
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
	
	
	function upload(){
			$("#uploadOne").form("clear");
			$("#uploadOne").dialog('open').dialog('setTitle', '模板上传');
			$("#uploadFrame").attr("src","<%=path%>/nh-micro-jsp/template-page/jspUpload.jsp");
	}	
function uploadOne(){
	var fileId=$("#uploadFrame").contents().find("#retFileId").val();
	var dataO = new Object();
	dataO.id=contractId;
	dataO.dbcol_ext_fileId=fileId;

 	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=contract_temp_list&groovySubName=updateInfo";
	$.post(url,dataO,function(data,stats){
		if(stats=="success" ){
			$.messager.show({
				msg : "操作成功",
				title : "消息"
			});
			$("#uploadForm").form("clear");
			$("#uploadOne").hide();
		}
	}); 
	
}

	function compile(id){


		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=contract_temp_list&groovySubName=compileWordTemplate";
		url=url+"&id="+id;
		$.post(url,function(data,stats){
				if(stats=="success" ){
					$.messager.show({
						msg : data.msg,
						title : "消息"
					});
				}
			}
		);


	}
	
	function createPdf(id){
		$.messager.prompt('pdf', '请输入替换参数json串', function(value){
            if(value==""){
                value="{}";           	
            }
			var pdf_open="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=contract_temp_list&groovySubName=createPdf&id="+id+"&param="+value;
            location.href = pdf_open;
		});
	}
</script>
</head>
<body class="easyui-layout">
	<div id="infoQuery" class="dQueryMod" region="north"
		style="height: 55px">
		<form id="searchForm">
			<table id="searchTable">
				<tr>
					<td>id：</td>
					<td><input type="text" id="id" name="id" /></td>
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
					<td align="right">合同模板标识:</td>
					<td><input type="text" id="meta_key" name="meta_key" value="" /></td>
				</tr>
				<tr>
					<td align="right">合同模板名称:</td>
					<td><input type="text" id="meta_name" name="meta_name" value="" /></td>
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
					<td align="right">合同模板标识:</td>
					<td><input type="text" id="meta_key" name="meta_key" value="" /></td>
				</tr>
				<tr>
					<td align="right">合同模板名称:</td>
					<td><input type="text" id="meta_name" name="meta_name" value="" /></td>
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
	
	<div id="uploadOne" class="easyui-dialog" modal="true" align="center"  closed="true" resizable="true" inline="false"
		style="height: 100px">
		<div id="buttons"
			style="margin-top: 5px; margin-left: 5px; padding-bottom: 5px;">
			<a class="easyui-linkbutton" href="javascript:uploadOne();">确认</a>
			<a class="easyui-linkbutton" href="javascript:uploadCancel();">取消</a>
		</div>		
		<form id="uploadForm" invalidate action="" method="post" enctype="multipart/form-data" >
		<%-- <input type="hidden" id="id" name="id" value="<%=data.get("id") %>"></input> --%>
		<iframe id="uploadFrame" marginwidth="0" marginheight="0" frameborder="0" style="border:0;width:100%;height:100%;" 
		src="<%=path%>/nh-micro-jsp/template-page/jspUpload.jsp">
		</iframe>

		</form>
	</div> 

</body>
</html>