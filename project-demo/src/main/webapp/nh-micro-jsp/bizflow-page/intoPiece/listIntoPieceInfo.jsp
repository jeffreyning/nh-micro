<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String pageName="listDictionaryInfo";	
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>表单列表信息管理</title>
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

function add(){
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=viewTaskDataGo"+
	"&operFlag=create&toPage=/nh-micro-jsp/bizflow-page/intoPiece/intoPieceInfo.jsp";		
	window.location.href=url;
}


function startFlow(id){
	var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=startFlow&id="+id;		
	$.post(url,function(data,stats){
				if(stats=="success" ){
					$.messager.show({
						msg : data.msg,
						title : "消息"
					});
					$('#infoList').datagrid('reload');
				}
			}
		);
}


$(function(){
	$('#infoList').datagrid({
		nowrap:true,
		striped:true,
		pagination : true,
		fitColumns: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"<%=path%>/NhEsbServiceServlet?pageName=<%=pageName%>&cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=getInfoList4Page",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 100

					}
					,
					{
						field : 'meta_key',
						title : '单号',
						width : 100,
						sortable:true
					}					
					,
					{
						field : 'remark',
						title : '表单标题',
						width : 100,
						sortable:true
					}
					,
					{
						field : 'dbcol_ext_customer_name',
						title : '客户名称',
						width : 30,
						sortable:true
					}						
					,
					{
						field : 'dbcol_ext_customer_card_id',
						title : '客户身份证',
						width : 30,
						sortable:true
					}
					,
					{
						field : 'dbcol_ext_application_credit_amount',
						title : '申请额度',
						width : 50,
						sortable:true
					}	
					,
					{
						field : 'dbcol_ext_approval_credit_amount',
						title : '批贷额度',
						width : 50,
						sortable:true
					}
					,
					{
						field : 'dbcol_ext_bizflow_intopiece_flow_curnode',
						title : '审批节点',
						width : 50,
						sortable:true
					}
					,
					{
						field : 'dbcol_ext_bizflow_intopiece_flow_status',
						title : '审批结论',
						width : 50,
						sortable:true
					}
					,
					{
						field : 'dbcol_ext_bizflow_intopiece_flow_finish',
						title : '审批是否结束',
						width : 50,
						sortable:true
					}					
					,
					{
						field : 'create_time',
						title : '表单创建时间',
						width : 50,
						sortable:true
					}
					,
					{
						field : 'oper',
						title : '操作',
						width : 150,
						formatter: function(value, row, index){
							var viewpage="<%=path%>/nh-micro-jsp/bizflow-page/intoPiece/intoPieceInfoMain.jsp?sourceFlag=viewForm&id="+row.id;
							
							var updatepage="<%=path%>/nh-micro-jsp/bizflow-page/intoPiece/intoPieceInfoMain.jsp?sourceFlag=updateForm&id="+row.id;
													
							var ret='<a href="'+viewpage+'">查看表单详情</a>';
							if(row.dbcol_ext_bizflow_intopiece_flow_finish==null || row.dbcol_ext_bizflow_intopiece_flow_finish==""){
								ret=ret+'&nbsp;'+'<a href="'+updatepage+'">修改表单信息</a>';
								ret=ret+'&nbsp;'+'<a href="javascript:void(0)" onclick="startFlow(\''+row.id+'\')">启动审批</a>';
							}
							return ret;

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
					<td>单号：</td>
					<td><input type="text" id="meta_key" name="meta_key" /></td>
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