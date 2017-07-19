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


$(function(){
	$('#infoList').datagrid({
		nowrap:true,
		striped:true,
		pagination : true,
		fitColumns: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"<%=path%>/NhEsbServiceServlet?pageName=<%=pageName%>&cmdName=Groovy&subName=bizflow_intopiece_list&groovySubName=getInfoList4Page&dbcol_ext_bizflow_intopiece_flow_curnode=bizflow_intopiece_qa_approval",
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
						width : 100

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
							var ret='<a href="'+viewpage+'">查看表单详情</a>';							
							var approvalpage="<%=path%>/nh-micro-jsp/bizflow-page/intoPiece/intoPieceInfoMain.jsp?sourceFlag=qaApproval&id="+row.id;
							if(row.dbcol_ext_bizflow_intopiece_flow_curnode=='bizflow_intopiece_qa_approval'){
								ret=ret+'&nbsp;'+'<a href="'+approvalpage+'">进件质检审批</a>';
							}
							return ret;
						}
					}					
				]],
        toolbar : [{
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


</body>
</html>