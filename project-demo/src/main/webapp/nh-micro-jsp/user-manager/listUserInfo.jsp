<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户信息管理</title>
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

function getDivContent(){
	alert($("#checkOrgdialog").html());
}

$(function(){
	$('#userInfoList').datagrid({
		nowrap:true,
		striped:true,
		pagination : true,
		fitColumns: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_user&groovySubName=getInfoList4Page",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 100

					},
					{
						field : 'meta_key',
						title : '用户标识',
						width : 250,
						sortable:true
					},
					{
						field : 'meta_name',
						title : '用户名',
						width : 200,
						sortable:true
					},
					{
						field : 'dbcol_ext_user_status',
						title : '是否启用',
						width : 80,
						formatter: function(value, row, index){
							if(value == 0){
								return "启用";
							}else{
								return "禁用";
							}
						}
					},
					{
						field : 'dbcol_ext_roleid',
						title : '系统角色',
						width : 200
					},
					{
						field : 'dbcol_ext_orgid',
						title : '所属机构',
						width : 200

					},
					{
						field : 'dbcol_ext_user_mobile',
						title : '手机号',
						width : 200

					},
					{
						field : 'dbcol_ext_user_id_num',
						title : '身份证',
						width : 200

					},
					{
						field : 'dbcol_ext_user_email',
						title : 'email',
						width : 200

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
				updateUserInfo();
			}
		},{
			id : "delete",
			text : "删除",
			iconCls : "icon-edit",
			handler : function() {
				remove();
			}

		},{
			id : "changePwd",
			text : "密码重置",
			iconCls : "icon-reload",
			handler : function() {
				changePwd();
			}
		
		},{
			id : "setRole",
			text : "角色设置",
			iconCls : "icon-edit",
			handler : function() {
				SetRole();
			}
		
		},	
		{
			id : "setDept",
			text : "部门设置",
			iconCls : "icon-edit",
			handler : function() {
				clicke();
			}
		
		},
		
		
		
		{
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

function ReQuery(){
	var data = $('#searchForm').serializeObject();
	$('#userInfoList').datagrid('reload',data);
}

function clearForm(){
	$('#searchForm').form('clear');
}

var  setuser_type="add";
var  update_userid;
function add(){
	setuser_type = "add";
		$("#addOne").form("clear");
		$("#addForm #addShowForm_temp").val("add");
		$("#addOne").dialog('open').dialog('setTitle', '用户信息添加');
}
function updateUserInfo(){  
	setuser_type = "update";
	var sels = $("#userInfoList").datagrid("getSelected");
    if(sels==""||sels==null){
	    alert("请选择行");
    }else{
    	$("#updateOneForm #passWord").remove();
    	$("#updateOneForm p").remove();
    	var sels = $("#userInfoList").datagrid("getSelected");
    	$("#updateOne").dialog('open').dialog('setTitle', '用户信息修改');
    	$("#updateOne").form("load", sels);
    	$("#updateOneForm #showForm_temp").val("update");
    }
	
}

function remove(){
	var sels = $('#userInfoList').datagrid("getSelected");
	if(sels == ''|| sels==null){
		alert('请选择行');
	}else{
		var result = confirm("确定要删除吗？");
		if(result == true){
			var querydata = $('#searchForm').serializeObject();
			var id = sels.id;
			var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_user&groovySubName=delInfo";
			url=url+"&id="+id;
			$.post(url,function(data,stats){
						if(stats=="success" ){
							$.messager.show({
								msg : data.msg,
								title : "消息"
							});
							$('#userInfoList').datagrid('reload',querydata);
						}
					}
				);
		        
		}
	}
}


function refresh(){
	var querydata = $('#searchForm').serializeObject();
	$('#userInfoList').datagrid('reload',querydata);
}

function changeCancel(){
	$("#changePwdForm").form("clear");
	$("#changePassword").dialog('close');
}

function changePwd(){
	var sels = $('#userInfoList').datagrid("getSelected");
	if(sels == ''|| sels==null){
		alert('请选择行');
		return false;
	}else{
		$("#changePwdForm").form("clear");
		$("#changePwdForm").form("load",sels);
		$("#changePassword").dialog('open').dialog('setTitle', '密码重置');
	}
	
}

function changeSubmit(){
	var sels = $('#userInfoList').datagrid("getSelected");
	var newPwd = $("#changePassword #dbcol_ext_user_password").val();
	var confirmPwd = $("#changePassword #confirmPwd").val();
	if(newPwd==""||newPwd=="undifine"){
		$.messager.show({
			msg : "新密码不能为空！",
			title : "消息"
		});
		return false;
		}
	if(confirmPwd=="undifine"||confirmPwd==""){
		$.messager.show({
			msg : "确认密码不能为空!",
			title : "消息"
		});
		return false;
	}	
	if(newPwd!=confirmPwd){
		$.messager.show({
			msg : "新密码和确认密码不一致！",
			title : "消息"
		});
		return false;
	}
	var dataO = $("#changePwdForm").serializeObject();
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_user&groovySubName=modifyPass";

     $.post(url,dataO,function(data,stats){
			if(stats=="success"){
				$.messager.show({
					msg : data.msg,
					title : "消息"
				});
				$("#changePassword").dialog('close');
			}else{
				$("#changePassword").dialog('close');
				$.messager.show({
					msg : data.msg,
					title : "消息"
				});				
			}

	});
}
function addOrUpdate(){

	var dataO = $("#updateOneForm").serialize();

	if(temp=="add"){
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_user&groovySubName=createInfo";
		$.post(url,dataO,function(data,stats){
			if(stats=="success" && data.success == true){
				$.messager.show({
					msg : data.msg,
					title : "消息"
				});
				updatecancel();
				refresh();
			}
		});
	}else{
		updateSubmit();
	}
}
/* 增加 */
function addOne(){
	var dataO = $("#addForm").serialize();
	var temp = $("#addForm #addShowForm_temp").val();

	if(temp=="add"){
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_user&groovySubName=createInfo";
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
	}else{
		updateSubmit();
	}
}
function updateSubmit(){
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_user&groovySubName=updateInfo";
		$.post(url, $("#updateOneForm").serialize(), function(data, stats) {
			if (stats == "success" ) {
				$.messager.show({
					msg : "操作成功",
					title : "消息"
				});
				refresh();
				updatecancel();

			}
		});
	};
	function updatecancel() {
		$("#updateOneForm").form("clear");
		$("#updateOne").dialog('close');
	}

	function addCancel() {
		$("#addForm").form("clear");
		$("#addOne").dialog('close');
	}

	function checkOrg() {
		if (seleObj != null && seleObj != "") {
			if (clicktype == "1") {
				$("#searchForm #userOrgId").val(seleObj.nodeId);
			} else if (clicktype == "2") {

				$("#addForm #userOrg").val(seleObj.name);
				$("#addForm #userOrgId").val(seleObj.nodeId);
			} else if (clicktype == "3") {
				$("#updateOneForm #userOrg").val(seleObj.name);
				$("#updateOneForm #userOrgId").val(seleObj.nodeId);
			}

			$("#checkOrgdialog").dialog('close');
		} else {
			$.messager.alert('信息提示', '请选择所属机构', 'info');
		}
	}

	function Orgclose() {
		$("#checkOrgdialog").dialog('close');
	}
	var clicktype;
	//加载树
	function clicke() {
		var sels = $('#userInfoList').datagrid("getSelected");
		if(sels == ''|| sels==null){
			alert('请选择行');
			return false;
		}
		var userId=sels.meta_key;
		//clicktype = str;
		$("#checkOrgdialog").dialog('open').dialog('setTitle', '选择机构');
		initTree(userId);
	}

	var zTreeObj;
	var setting = {
	
        check: {
            enable: true,
            chkboxType: { "Y": "", "N": "" }
        },
		data : {
			key : {
				name : "meta_name"
			},
			simpleData : { //简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的                           
				enable : true,
				idKey : "meta_key", //id和pid，这里不用多说了吧，树的目录级别  
				pIdKey : "dbcol_ext_parent_id",
				rootPId : "root" //根节点  
			}
		},		
		callback : {
			onClick : zTreeOnClick
		}
	};

	var zNodes;


	function initTree(userId) {
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_dept&groovySubName=getInfoListAll";
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			dataType : "json",
			url:url,
			//url : webPath + 'theorganizationController/QueryZTree.do',
			success : function(data) {
				zNodes = data;
				zTreeObj = $.fn.zTree.init($("#tree"), setting, zNodes);
				checkSelectTreeNode(zTreeObj,userId);
			},
			error : function() {
				alert("请求失败");
			}
		});
		
		
	}

	function checkSelectTreeNode(treeObj,userId){
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_ref_user_dept&groovySubName=getInfoListAll";
		url=url+"&user_id="+userId;
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			dataType : "json",
			url:url,
			success : function(data) {
                for (var i = 0; i < data.length; i++) {
                	console.log(data[i].meta_key);
                    var node = treeObj.getNodeByParam("id", data[i].meta_key, null);
                    treeObj.checkNode(node);
                }
			},
			error : function() {
				alert("请求失败");
			}
		});		
	}
	var seleObj;
	function zTreeOnClick(event, treeId, treeNode) {
		//alert(treeNode.id + ", " + treeNode.name+","+treeNode.nodeType);
		var treeObj = $.fn.zTree.getZTreeObj("tree");
		var nodes = treeObj.getSelectedNodes();
		seleObj = nodes[0];
	};

	function SetRole() {
		var sels = $("#userInfoList").datagrid("getSelected");
		update_userid = sels.meta_key;
		
		$("#aur").dialog('open').dialog('setTitle', '角色配置');
		queryRole_Set(update_userid);
	}

	var hasAttrDataGrid;
	var noAttrDataGrid;
	function queryRole_Set(userid) {
		var ref_url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_ref_user_role&groovySubName=getInfoList4Ref&user_id="+userid;
		
		hasAttrDataGrid = $('#hasAttrDataGrid')
				.datagrid(
						{
							url : ref_url,
							nowrap : true,
							striped : true,
							rownumbers : false,
							singleSelect : true,
							columns : [ [ {
								width : '1',
								title : 'id',
								field : 'id',
								sortable : false,
								hidden : true
							}, {
								width : '1',
								title : '角色id',
								field : 'meta_key',
								sortable : false,
								hidden : true
							}, {
								width : '150',
								title : '角色名称',
								field : 'meta_name',
								sortable : false
							} ] ]
						});
		var role_url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_role&groovySubName=getInfoListAll";

		noAttrDataGrid = $('#noAttrDataGrid').datagrid({
			url : role_url,
			nowrap : true,
			striped : true,
			rownumbers : false,
			singleSelect : true,
			columns : [ [ {
				width : '1',
				title : 'id',
				field : 'id',
				sortable : true,
				hidden : true
			}, {
				width : '1',
				title : 'role_id',
				field : 'meta_key',
				sortable : true,
				hidden : true
			}, {
				width : '150',
				title : '角色名称',
				field : 'meta_name',
				sortable : false
			} ] ]
		});

	}

	function moveIn() {

		var obj = $('#noAttrDataGrid').datagrid('getSelected');
		if (obj == null) {
			alert("请选择要移入的属性");
			return;
		}
		var hrows = $('#hasAttrDataGrid').datagrid('getRows'); //这段代码是获取当前页的所有行。
		for (var i = 0; i < hrows.length; i++) {
			//获取每一行的数据
			if (obj.id == hrows[i].id) {
				alert("已经配置" + obj.role_name + "角色,请重新选择。");
				return;
			}

		}
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_ref_user_role&groovySubName=createInfo";

		//var url = webPath + 'generalUserContraller/addSysRoleUser.do';
		var dataO = {
			'meta_key' : update_userid,
			'meta_name' : obj.meta_key
		};
		$.post(url, dataO, function(data, stats) {
			if (stats == "success") {
				$('#hasAttrDataGrid').datagrid('reload');
				//$('#noAttrDataGrid').datagrid('reload');
				$.messager.show({
					msg : data.msg,
					title : "消息"
				});
			} else {
				$.messager.show({
					msg : data.msg,
					title : "消息"
				});

			}
		});

	}
	function moveOut() {
		/* var checkeds = $('#noAttrDataGrid').datagrid('getChecked');
		 if(checkeds.length == 0){
		     alert("请选择要移入的属性");
		     return;
		   }
		var idStr = "[ ";
		    for(var i = 0; i < checkeds.length; i++){
		      idStr +="{id:'"+ checkeds[i].id + "'}";
		    }
		    idStr+=" ]";
		 */
		var obj = $('#hasAttrDataGrid').datagrid('getSelected');
		if (obj == null) {
			alert("请选择要移入的属性");
			return;
		}
		var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_ref_user_role&groovySubName=delInfo";
		//var url = webPath + 'generalUserContraller/DeleteSysRoleUser.do';
		var dataO = {
			'id' : obj.id,
		};
		$.post(url, dataO, function(data, stats) {
			if (stats == "success") {
				$('#hasAttrDataGrid').datagrid('reload');
				//$('#noAttrDataGrid').datagrid('reload');
				$.messager.show({
					msg : data.msg,
					title : "消息"
				});
			} else {
				$.messager.show({
					msg : data.msg,
					title : "消息"
				});

			}
		});

	}
	
    
	
	
</script>
</head>
<body id="userRole" class="easyui-layout">

	<div id="roleQuery" class="dQueryMod" region="north"
		style="height: 55px">
		<form id="searchForm">
			<table id="searchTable">
				<tr>
					<td>用户标识：</td>
					<td><input type="text" id="userId" name="userId"
						class="dInputText"
						 /></td>
					<td>用户名称：</td>
					<td><input type="text" id="userName" name="userName"
						class="dInputText" /></td>
					<!-- <td>系统角色：</td>
					<td><input type="text" id="sysRoles" name="sysRoles" /></td> -->
					<td>机构标识：</td>
					<td><input class="dInputText" type="text" id="userOrgId"
						name="userOrgId" onClick="clicke('1')" /></td>
					<!-- <td><input  type="text" id="userOrg" onClick="clicke()" disabled="disabled" /></td> -->
					<td><a href="#" class="easyui-linkbutton dRbtnSearch"
						iconCls="icon-search" onclick="ReQuery()">查询</a><a href="#"
						class="easyui-linkbutton dRbtnClean" iconCls="icon-redo"
						onclick="clearForm()">清空</a></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="roleList" region="center">
		<div class="easyui-tabs l_listwid" id="accountTab">
			<table id="userInfoList"></table>
		</div>
	</div>
	<!-- 密码修改 -->
	<div id="changePassword" class="easyui-dialog" modal="true"
		align="center"
		style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="changePwdForm" action="" method="post">
			<table id="changePwdTable"
				style="margin-top: 35px; margin-left: -40px;">
				<tr>
					<td>
					<input	type="hidden" id="id" name="id" value="" />
					<input	type="hidden" id="user_id" name="user_id" value="" /> 
					<input	type="hidden" id="user_name" name="user_name" value="" />
					</td>
				</tr>
				<tr>
					<td align="right">新密码：</td>
					<td><input type="password" id="dbcol_ext_user_password" name="dbcol_ext_user_password" value="" /></td>
				</tr>
				<tr>
					<td align="right">确认密码：</td>
					<td><input type="password" id="confirmPwd" name="confirmPwd"
						value="" /></td>
				</tr>
			</table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70"
					href="javascript:changeSubmit();">确认</a> <a
					class="easyui-linkbutton dPbtnLight70"
					href="javascript:changeCancel();">取消</a>
			</div>
		</form>
	</div>

	<!-- 修改 -->
	<div id="updateOne" class="easyui-dialog" modal="true" align="center"
		style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
		<form id="updateOneForm" novalidate method="post" action="">
			<input type="hidden" id="showForm_temp" value="" />
			<table id="updateUserTable"
				style="margin-top: 10px; margin-left: -40px;">
				<tr>
					<!-- <td>Id：</td> -->
					<td><input type="hidden" id="id" name="id" value="" /></td>
				</tr>
				<tr>
					<td align="right">用户标识：</td>
					<td><input type="text" id="meta_id" name="meta_id" value=""
						
						 /></td>
				</tr>
				<tr>
					<td align="right">用户名称：</td>
					<td><input type="text" id="meta_name" name="meta_name" value="" /></td>
				</tr>
				<tr>
					<td align="right">是否启用：</td>
					<td><select name="dbcol_ext_state" id="dbcol_ext_user_state" class="easyui-combobox"
						style="width: 235px;" panelHeight="70px" editable="false">
							<option value="0">启用</option>
							<option value="1">禁用</option>
					</select></td>
				</tr>
				<tr>
					<td align="right">电话：</td>
					<td><input type="text" id="dbcol_ext_user_mobile" name="dbcol_ext_user_mobile" value="" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"  /></td>
				</tr>
				<tr>
					<td align="right">身份证号：</td>
					<td><input type="text" id="dbcol_ext_user_id_num" name="dbcol_ext_user_id_num" value="" onkeyup="value=value.replace(/[\W]/g,'') "  /></td>
				</tr>
				<tr>
					<td align="right">备注：</td>
					<td><input class="easyui-textbox" id="dbcol_ext_user_remark" name="dbcol_ext_user_remark"
						data-options="multiline:true" style="height: 60px;"></input></td>
				</tr>

			</table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70"
					href="javascript:updateSubmit();">确认</a> <a
					class="easyui-linkbutton dPbtnLight70"
					href="javascript:updatecancel();">取消</a>
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
					<!-- <td>Id：</td> -->
					<td><input type="hidden" id="id" name="id" value="" /></td>
				</tr>
				<tr>
					<td align="right">用户标识：</td>
					<td><input type="text" id="meta_key" name="meta_key" value=""
						
						 /></td>
				</tr>
				<tr>
					<td align="right">用户名称：</td>
					<td><input type="text" id="meta_name" name="meta_name" value="" /></td>
				</tr>
				<tr>
					<td align="right">密码：</td>
					<td><input type="password" id="dbcol_ext_user_passWord" name="dbcol_ext_user_password"
						value="" /></td>
				</tr>
				<tr>
					<td align="right">是否启用：</td>
					<td><select name="dbcol_ext_user_state" id="dbcol_ext_user_state" class="easyui-combobox"
						panelHeight="140px" editable="false" style="width: 235px">
							<option value="0" selected="selected">启用</option>
							<option value="1">禁用</option>
					</select></td>
				</tr>
				<tr>
					<td align="right">电话：</td>
					<td><input type="text" id="dbcol_ext_user_mobile" name="dbcol_ext_user_mobile" value=""  onkeyup="this.value=this.value.replace(/[^0-9]/g,'')" /></td>
				</tr>
				<tr>
					<td align="right">身份证号：</td>
					<td><input type="text" id="dbcol_ext_user_id_num" name="dbcol_ext_user_id_num" value=""   onkeyup="value=value.replace(/[\W]/g,'') "  /></td>
				</tr>
				<tr>
					<td align="right">备注：</td>
					<!-- <td><input type="text" id="remarks" name="remarks" value="" /></td> -->
					<td><input class="easyui-textbox" id="dbcol_ext_user_remark" name="dbcol_ext_user_remark"
						data-options="multiline:true" style="height: 60px;"></input></td>
				</tr>

			</table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:addOne();">确认</a>
				<a class="easyui-linkbutton dPbtnLight70"
					href="javascript:addcancel();">取消</a>
			</div>
		</form>
	</div>

	<div id="checkOrgdialog" class="easyui-dialog" modal="true"
		align="center"
		style="padding: 20px 10px 10px 10px; border: 0px; margin: 0px; width: 300px;"
		closed="true" resizable="true" inline="false">
		<div split="true" title="组织机构">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div id="buttons" style="height: 30px; padding-top: 20px;">
			<a class="easyui-linkbutton dPbtnDark70"
				href="javascript:checkOrg();">确认</a> <a
				class="easyui-linkbutton dPbtnLight70" href="javascript:Orgclose();">取消</a>
		</div>
<!-- remark -->
	</div>


	<!-- 这是为角色添加用户的 -->
	<div id="aur" class="easyui-dialog" class="easyui-dialog" modal="true"
		align="center"
		style="padding: 20px 10px 15px 10px; border: 0px; margin: 0px; width: 600px;"
		closed="true" resizable="true" inline="false">
		<div class="easyui-tab"
			style="width: 210px; height: auto; overflow: hidden; background: #f8f8f8; float: left; margin-left: 8px; border: 1px solid #e2e2e2;"
			title="系统角色">
			系统角色<br>
			<table id="noAttrDataGrid"></table>
		</div>

		<div style="width: 60px; float: left; margin: 20px 0 20px 20px;">
			<a href="javascript:moveIn();" class="easyui-linkbutton dPbtnMove"
				style="margin-bottom: 20px;">移入 》</a> <a href="javascript:moveOut()"
				class="easyui-linkbutton dPbtnMove">《 移出</a>
		</div>

		<div class="easyui-tab"
			style="width: 210px; height: auto; overflow: hidden; background: #f8f8f8; float: left; margin-left: 30px; border: 1px solid #e2e2e2;"
			title="拥有角色">
			拥有角色<br>
			<table id="hasAttrDataGrid"></table>
		</div>
	</div>


</body>
</html>