<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <HEAD>
  <TITLE> 组织机构</TITLE>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/zTree/css/zTreeStyle/zTreeStyle.css">
  <SCRIPT type="text/javascript">  

		var setting = {
			check : {
				/**复选框**/
				enable : false,
				chkboxType : {
					"Y" : "",
					"N" : ""
				}
			},
			view : {
				expandSpeed : 300
			//设置树展开的动画速度，IE6下面没效果，  
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
				onClick: zTreeOnClick
			}
		};

			//第一次加载树
			$(function() {
				initTree();
			});

			function initTree() {
				var url = "<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_dept";
			$.ajax({
					url:url,
					type:'post',
					dataType:'json',
					data:"groovySubName=getInfoListAll",
					success:function(data,status){
						//var resultData=data.resultData;
						//var resultDataObj=JSON.parse(resultData);
						//var groovyResultObj=resultDataObj.resultObj;
				
						treeData=data;
						$.fn.zTree.init($("#tree"),setting, treeData);  
					}
				});            
            
            
        }
        
    	function refresh() {
       		initTree();//重新加载树
    	}      
 
        function zTreeOnClick(event, treeId, treeNode) {
            var treeObj = $.fn.zTree.getZTreeObj("tree");
            var nodes = treeObj.getSelectedNodes();
            var seleObj = nodes[0];
            setUser(seleObj);
        };

        
        function Adduser(){
        	var parent_id=$("#updatelistReponsitory #meta_key").val();
        	$("#addlistReponsitory #dbcol_ext_parent_id").val(parent_id);
        	$("#addlistReponsitory").dialog('open').dialog('setTitle', '添加机构');
        } 
        function setUser(seleObj){
    		if (seleObj != null&&seleObj !="") {
    			$("#updatelistReponsitoryForm #id").val(seleObj.id);
    			$("#updatelistReponsitoryForm #meta_key").val(seleObj.meta_key);
    			$("#updatelistReponsitoryForm #meta_name").val(seleObj.meta_name);
    			$("#updatelistReponsitoryForm #dbcol_ext_parent_id").val(seleObj.dbcol_ext_parent_id);
    			$("#updatelistReponsitoryForm #dbcol_ext_dept_type").val(seleObj.dbcol_ext_dept_type);
    			$("#updatelistReponsitoryForm #remark").val(seleObj.remark);

    		}	
        } 
        function Deluser(dept_id){
    		$.messager.confirm("信息提示", "确认要删除‘" + dept_id + "’吗？",
 					function(confirm) {
 						if (confirm) {
 							var url = "<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_dept&groovySubName=delInfo";
 							$.post(url, {"id" : dept_id}, function(data, status) {
 								if (status == "success") {
 									$.messager.show({
 										msg : "删除成功",
 										title : "消息"
 									});
 									refresh();
 								}
 							});
 						}
 					});
    		refresh();        	
        } 
        
    	function cancelGro(flag) {
    		$("#"+flag+"Form").form("clear");
    		$("#"+flag).dialog("close");
    	}


    	function addlistReponsitory() {
    		$("#addlistReponsitoryForm #addparentId").val("");
    		var url = "<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_dept&groovySubName=createInfo";
    		var dataO = $("#addlistReponsitoryForm").serialize();
    		if($("#addlistReponsitoryForm").form('validate')){
    			$.post(url, dataO, function(data, status) {
    				$.messager.show({
    					msg : "添加成功",
    					title : "消息"
    				});
    				refresh();
    				cancelGro("addlistReponsitory");
    				
    			});
    			
    		}else{
    			$.messager.alert('信息提示', '校验失败，修改后提交', 'info');
    		} 
    	}    
    	
    	function upodatelistReponsitory(){

    			var url = "<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_dept&groovySubName=updateInfo";
	   			var dataO = $("#updatelistReponsitoryForm").serialize();
	   			if($("#updatelistReponsitoryForm").form('validate')){
	   				$.post(url,dataO,function(data,status){
	   					 $.messager.show({
	   						msg : "修改成功",
	   						title : "消息"
	   					});
	   					refresh();
	   					cancelGro("updatelistReponsitory");
	   					
	   				});
	   				
	   			}else{
	   				$.messager.alert('信息提示', '校验失败，修改后提交', 'info');
	   			}

    	}
    	
 </SCRIPT>  
 </HEAD>
<BODY class="easyui-layout">
<div region="west"  split="true" title="组织机构" style="width:240px;">
   <ul id="tree" class="ztree"></ul>
</div>
     <div id="dialogDemo" class="easyui-dialog" style="width:300px; height:200px;" title="添加" iconCls="icon-edit" closed="true" resizable="true" inline="false">
      Content
   </div>
<div region="center"  title="机构信息" split="true">  
    <div id="toolbar" class="dRbtnsToolbar">
        <a href="#" class="easyui-linkbutton dRbtnAdd" iconcls="icon-addOne" onclick="Adduser()">添加</a><a href="#" class="easyui-linkbutton dRbtnUpdate" 
        iconcls="icon-edit" onclick="upodatelistReponsitory()" >修改</a><a href="#" class="easyui-linkbutton dRbtnDelete" 
        iconcls="icon-remove" onclick="Deluser($('#updatelistReponsitory #id').val())" >删除</a>
    </div>
    
  		<div id="updatelistReponsitory" align="left" buttons="#buttonsAdd" class="dRumPack">
			
			<!-- <input type="hidden" id="updatelistReponsitory_temp" value="" /> -->
			<form id="updatelistReponsitoryForm" novalidate action=""  method="post">
				<table>
					<tr>
						<!-- <td>Id：</td> -->
						<td><input type="hidden" id="id" name="id" value="" /></td>
					</tr>				
					<tr>
						<td align="right">机构编码：</td>
						<td><input type="text" id="meta_key" name="meta_key"  onkeyup="value=value.replace(/[\W]/g,'') "  
							cols="30" class="" validtype="name" required missingMessage="不能为空" ></input></td>
					</tr>
					<tr>
						<td align="right">机构名称：</td>
						<td><input type="text" id="meta_name" name="meta_name" 
							cols="30" class="" required missingMessage="不能为空"></input></td>
					</tr>
					<tr>
						<td align="right">机构类型：</td>
						<td>
						 <select id="dbcol_ext_dept_type" name="dbcol_ext_dept_type">
						 			<option value="" selected="">--请选择--</option>
                                    <option value="1">1级部门</option>
                                    <option value="2">2级部门</option>   
                                    <option value="3">3级部门</option>
                                    <option value="4">4级部门</option> 
                         </select>
                         </td>
					</tr>
					<tr>
						<td align="right">机构描述：</td>
						<td><input class="" id="remark" name="remark" data-options="multiline:true" style="height:60px;"></input></td>
					</tr>


					<tr>
						<td align="right"></td>
						<td>
							<input type="hidden" id="dbcol_ext_parent_id" name="dbcol_ext_parent_id" />
						</td>
					</tr>


				</table>
			</form>
		</div>    

	</div>
		<!-- 添加部门 -->
 		<div id="addlistReponsitory" class="easyui-dialog" modal="true" align="center" 
			style="padding:20px 10px 10px 10px; width:540px;" closed="true" resizable="true" inline="false">
			<input type="hidden" id="listReponsitory_temp" value="" />
			<form id="addlistReponsitoryForm" novalidate action="" method="post" class="lyr_addlistReponsitoryForm">
				<table>
					<tr>
						<td align="right">机构编码：</td>
						<td><input type="text" id="meta_key" name="meta_key"  onkeyup="value=value.replace(/[\W]/g,'') "  
							cols="30" class="" validtype="name" required missingMessage="不能为空" ></input></td>
					</tr>
					<tr>
						<td align="right">机构名称：</td>
						<td><input type="text" id="meta_name" name="meta_name" 
							cols="30" class="" required missingMessage="不能为空"></input></td>
					</tr>
					<tr>
						<td align="right">机构类型：</td>
						<td>
						 <select id="dbcol_ext_dept_type" name="dbcol_ext_dept_type">
						 			<option value="" selected="">--请选择--</option>
                                    <option value="1">1级部门</option>
                                    <option value="2">2级部门</option>   
                                    <option value="3">3级部门</option>
                                    <option value="4">4级部门</option> 
                         </select>
                         </td>
					</tr>
					<tr>
						<td align="right">机构描述：</td>
						<td><input class="" id="remark" name="remark" data-options="multiline:true" style="height:60px;"></input></td>
					</tr>


					<tr>
						<td align="right"></td>
						<td>
							<input type="hidden" id="dbcol_ext_parent_id" name="dbcol_ext_parent_id" />
						</td>
					</tr>


				</table>
			</form>
			<div id="buttonsAdd" style="padding:10px 0 0 70px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:addlistReponsitory();">确认</a>
				<a class="easyui-linkbutton dPbtnLight70" href="javascript:cancelGro('addlistReponsitory');">取消</a>
			</div>
		</div> 	
		
</BODY>
</html>