<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String uuid=UUID.randomUUID().toString();

%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试分库分表查询代理</title>
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
var leftIndexMap="";
var rightIndexMap="";
$(function(){
	$('#infoList').datagrid({
		onLoadSuccess:function(data){
			if(data.status!=null && data.status==1){
				alert("error");
				return;
			}
		
			var total=0;
			if(data.total){
				total=data.total;
			}
			var totalLimitIndex=0;
			if(data.totalLimitIndex){
				totalLimitIndex=data.totalLimitIndex;
			}
			var thisPageNum=0;
			if(data.thisPageNum){
				thisPageNum=data.thisPageNum;
			}
			var totalPageNum=0;
			if(data.totalPageNum){
				totalPageNum=data.totalPageNum;
			}

			$("#total_p").val(totalLimitIndex+"/"+total);
			$("#cur_page_p").val(data.thisPageNum+"/"+totalPageNum);
    
		},
		nowrap:true,
		striped:true,
		pagination : false,
		fitColumns: true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		url:"<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=MicroMergeQueryTemplate&groovySubName=getInfoList4Page",
		columns:[[
					{
						field : 'id',
						title : 'id',
						width : 100
					},
					{
						field : 'col1',
						title : 'col1',
						width : 100
					},
					{
						field : 'col2',
						title : 'col2',
						width : 100
					}					
					
				]],
		toolbar:'#tb',

        rownumbers:false,
        singleSelect:true
		
	});
});

	function nextPageQuery(){
		var data=$('#searchForm').serializeObject();
		data.directFlag=1;
	
		$('#infoList').datagrid('reload',data);		
	}
	
	function prePageQuery(){
		var data=$('#searchForm').serializeObject();
		data.directFlag=2;


		$('#infoList').datagrid('reload',data);		
	}	
	//条件查询
	function ReQuery(){
		var data = $('#searchForm').serializeObject();
		data.directFlag=0;
		var date=new Date();
		var uid=date.getTime();
		data.mergeQueryId=uid;
		$("#mergeQueryId").val(uid);
		$('#infoList').datagrid('reload',data);
	}
	
	//重置查询条件
	function clearForm(){
		$('#searchForm').form('clear');
	}
	
	//刷新
	function refresh(){

		var data=$('#searchForm').serializeObject();
		data.directFlag=0;
		var date=new Date();
		var uid=date.getTime();
		data.mergeQueryId=uid;
		$("#mergeQueryId").val(uid);
		$('#infoList').datagrid('reload',data);
	}


</script>
</head>
<body class="easyui-layout">
<div id="tb">
<a href="#" class="easyui-linkbutton" onclick="refresh()" >首页</a>  
<a href="#" class="easyui-linkbutton" onclick="nextPageQuery()" >下一页</a>  
<a href="#" class="easyui-linkbutton" onclick="prePageQuery()" >上一页 </a> 
当前起始记录条数/总记录条数:<input id="total_p" readonly=true></input>&nbsp;当前页数/总页数:<input id="cur_page_p" readonly=true></input>
</div>

	<div id="infoQuery" class="dQueryMod" region="north"
		style="height: 150px">
		<form id="searchForm">
			<table id="searchTable">
				<tr>
					<td>查询Sql：</td>
					<td><input type="text"  name="originSql" value="select * from <REP_VIEW_NAME> order by col1 desc"></input></td>
				</tr>
				<tr>
					<td>内存排序orderSql：</td>
					<td><input type="text"  name="memOrderStr" value="col1 desc"></input></td>
				</tr>	
				<tr>
					<td>分库标识：</td>
					<td><input type="text"  name="dbNameList" value="default"></input></td>
				</tr>
				<tr>
					<td>分表标识：</td>
					<td><input type="text"  name="viewNameList"  value="test_merge_1,test_merge_2"></input></td>
				</tr>
				<tr>
					<td><input  type="text"  name="mergeQueryId"  id="mergeQueryId"></input></td>
				</tr>				
				<tr>												
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