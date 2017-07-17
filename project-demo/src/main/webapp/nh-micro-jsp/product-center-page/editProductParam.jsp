<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String pageId=request.getParameter("pageId");
String dept_id="inputList";
String dept_path="root";
String data_table_name="nh_micro_product_center_list";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/common.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/zTree/css/zTreeStyle/zTreeStyle.css">
  <SCRIPT type="text/javascript">  
function add_selectEdit(data){
		$('#selectEditTable').datagrid('loadData',data);
		$("#input_select_extedit").dialog('open').dialog('setTitle', '信息添加');
}	

function addOne_selectEdit(){

	var sels = $("#paramTable").datagrid("getSelected");
    if(sels==""||sels==null){
	    return;
    }
    editData=$('#selectEditTable').datagrid('getRows');
    sels.select_options=JSON.stringify(editData);

}
function addCancel_selectEdit() {

	$("#input_select_extedit").dialog('close');
}

  function onEdit(){
		var sels = $("#paramTable").datagrid("getSelected");
	    if(sels==""||sels==null){
		    return;
	    }
	    add_selectEdit(JSON.parse(sels.select_options));
	    
  }
/*   $.extend($.fn.datagrid.defaults.editors, {
		buttonEdit : {
	    init: function(container, options)
	    {
			var editorContainer = $('<div/>');

			var button = "<a href='javascript:void(0)' onclick='onEdit()'>高级属性编辑</a>";

			editorContainer.append(button);
			editorContainer.appendTo(container);
			return button;
	    },
	    getValue: function(target)
	    {
	    	console.log("getvalue");
	    	console.log("target="+target);	    	
	        //return $(target).text();
	    },
	    setValue: function(target, value)
	    {
	    	console.log("setvalue");
	    	console.log("target="+target);
	    	console.log("value="+value);
	       // $(target).text(value);
	    },
	    resize: function(target, width)
	    {
	        var span = $(target);
	        if ($.boxModel == true){
	            span.width(width - (span.outerWidth() - span.width()) - 10);
	        } else {
	            span.width(width - 10);
	        }
	    }
	    }
	});  */   
  
  
function checkDefaultData(data){
	
	var size=data.length;
	for(var i=0;i<size;i++){

		var tempId=data[i].input_id;
		if(tempId!=null && tempId!=""){
			if(tempId.indexOf("dbcol_ext_")<0){
				data[i].input_id="dbcol_ext_"+tempId;
			}
		}
		data[i].input_name=data[i].input_id;
	}
	
}

		function saveParamConfig(){
			var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=micro_page_struct&groovySubName=updateDeptDataList&data_table_name=<%=data_table_name%>&page_id=<%=pageId%>&dept_id=<%=dept_id%>&dept_path=<%=dept_path%>";
			var data=new Object();
			var data_value=$("#paramTable").datagrid('getRows');
			var paramData=new Object();
			paramData=data_value;
			checkDefaultData(paramData);
			data.dept_data=JSON.stringify(paramData);
			$.ajax({
				url:url,
				type:'post',
				data:data,
				success:function(data,status){
					
				}
				
			});
			
		}

		function getParentParam(){
			var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=micro_page_struct&groovySubName=getDeptDataList&data_table_name=<%=data_table_name%>&page_id=<%=pageId%>&dept_id=<%=dept_id%>&dept_path=<%=dept_path%>";
			$.ajax({
				url:url,
				type:'post',
				dataType:'json',
				success:function(data,status){
					$('#paramTable').datagrid('loadData',data);
				}
				
			});
			
			
		}		
		
		function endAllEdit(gridname){
			var rows = $('#'+gridname).datagrid('getRows');
			for (var i = 0; i < rows.length; i++) {
		    $('#'+gridname).datagrid('endEdit', i);
			}
		}		
		//刷新
		function refresh(gridname){
			var rows = $('#'+gridname).datagrid('getRows');
			 $('#'+gridname).datagrid('loadData',rows);
		}

		/* 增加 */
		function add(gridname){
			var index=0;
			var sels = $("#"+gridname).datagrid("getSelected");
		    if(sels==""||sels==null){
			    
		    }else{
		    	index=$("#"+gridname).datagrid('getRowIndex',sels);
		    }	

			$("#"+gridname).datagrid('insertRow',{//如果处于未被点击状态，在第一行开启编辑  
		        index: index,     
		        row: {}  
		    }); 

			$("#"+gridname).datagrid('beginEdit',index);//没有这行，即使开启了也不编辑	
		}	



		/* 修改 */
		function update(gridname){  
			var sels = $("#"+gridname).datagrid("getSelected");
		    if(sels==""||sels==null){
			    alert("请选择行");
		    }else{
		    	var index=$("#"+gridname).datagrid('getRowIndex',sels);
		    	$("#"+gridname).datagrid('beginEdit',index);	
		    }
		}	



		/*删除*/
		function remove(gridname){
			var sels = $("#"+gridname).datagrid("getSelected");
			if(sels == ''|| sels==null){
				alert('请选择行');
			}else{
				var result = confirm("确定要删除吗？");
				if(result == true){
					var index=$("#"+gridname).datagrid('getRowIndex',sels);  
					console.log(index);
					$("#"+gridname).datagrid('deleteRow',index);  
				}
			}
		}
		
		
			//第一次加载树
			$(function() {
				//页面替换参数grid
				$('#paramTable').datagrid({
					nowrap:true,
					striped:true,
					pagination : false,
					fitColumns: true,
					columns:[[
								
								
								{
									field : 'product_yue_lilv',
									title : '产品月利率',
									width : 50,
									editor : {//是否可编辑  
			                            type : 'validatebox',  
			                            options : {//必须校验  
			                                
			                            }  
			                        }  

								}
								,
								{
									field : 'product_start_qishu',
									title : '阶段开始期数',
									width : 50,
									editor : {//是否可编辑  
			                            type : 'validatebox',  
			                            options : {//必须校验  
			                                
			                            }  
			                        }
								}
								,
								{
									field : 'product_end_qishu',
									title : '阶段截止期数',
									width : 50,
									editor : {//是否可编辑  
			                            type : 'validatebox',  
			                            options : {//必须校验  
			                                
			                            }  
			                        }
								}								
/* 								,
								{
									field : 'product_lixi_alg_id',
									title : '阶段利息公式ID',
									width : 100,
									editor : {//是否可编辑  
			                            type : 'validatebox',  
			                            options : {//必须校验  
			                                
			                            }  
			                        }
								}	 */							
							]],
			        toolbar : [ {
						id : "add",
						text : "添加",
						iconCls:"icon-addOne",
						handler : function() {
							add('paramTable');
						}
					},{
						id : "update",
						text : "修改",
						iconCls : "icon-edit",
						handler : function() {
							update('paramTable');
						}
					},{
						id : "delete",
						text : "删除",
						iconCls : "icon-edit",
						handler : function() {
							remove('paramTable');
						}

					},{
						id : "end",
						text : "结束编辑",
						iconCls : "icon-reload",
						handler : function() {
							endAllEdit('paramTable');
						}
					
					},
					'-', {
			            text: '上移', iconCls: 'icon-up', handler: function () {
			                MoveUp('paramTable');
			            }
			        }, '-', {
			            text: '下移', iconCls: 'icon-down', handler: function () {
			                MoveDown('paramTable');
			            }
			        }					
					
					
					],
			        rownumbers:false,
			        singleSelect:true
					
				});
				getParentParam();
				
				
				
				//页面替换参数grid
				$('#selectEditTable').datagrid({
					nowrap:true,
					striped:true,
					pagination : false,
					fitColumns: true,
					columns:[[
								{
									field : 'name',
									title : '名称',
									width : 50,
									editor : {//是否可编辑  
			                            type : 'validatebox',  
			                            options : {//必须校验  
			                            }  
			                        }  
								},
								{
									field : 'value',
									title : '值',
									width : 50,
									editor : {//是否可编辑  
			                            type : 'validatebox',  
			                            options : {//必须校验  
			                            }  
			                        }  
								}

							]],
			        toolbar : [ {
						id : "add",
						text : "添加",
						iconCls:"icon-addOne",
						handler : function() {
							add('selectEditTable');
						}
					},{
						id : "update",
						text : "修改",
						iconCls : "icon-edit",
						handler : function() {
							update('selectEditTable');
						}
					},{
						id : "delete",
						text : "删除",
						iconCls : "icon-edit",
						handler : function() {
							remove('selectEditTable');
						}

					},{
						id : "end",
						text : "结束编辑",
						iconCls : "icon-reload",
						handler : function() {
							endAllEdit('selectEditTable');
						}
					
					},
					'-', {
			            text: '上移', iconCls: 'icon-up', handler: function () {
			                MoveUp('selectEditTable');
			            }
			        }, '-', {
			            text: '下移', iconCls: 'icon-down', handler: function () {
			                MoveDown('selectEditTable');
			            }
			        }					
					
					
					],
			        rownumbers:false,
			        singleSelect:true
					
				});				
				
			});		
				


			function MoveUp(gridname) {
			    var row = $("#"+gridname).datagrid('getSelected');
			    var index = $("#"+gridname).datagrid('getRowIndex', row);
			    mysort(index, 'up', gridname);
			     
			}

			function MoveDown(gridname) {
			    var row = $("#"+gridname).datagrid('getSelected');
			    var index = $("#"+gridname).datagrid('getRowIndex', row);
			    mysort(index, 'down', gridname);
			     
			}
			 
			 
			function mysort(index, type, gridname) {
			    if ("up" == type) {
			        if (index != 0) {
			            var toup = $('#' + gridname).datagrid('getData').rows[index];
			            var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
			            $('#' + gridname).datagrid('getData').rows[index] = todown;
			            $('#' + gridname).datagrid('getData').rows[index - 1] = toup;
			            $('#' + gridname).datagrid('refreshRow', index);
			            $('#' + gridname).datagrid('refreshRow', index - 1);
			            $('#' + gridname).datagrid('selectRow', index - 1);
			        }
			    } else if ("down" == type) {
			        var rows = $('#' + gridname).datagrid('getRows').length;
			        if (index != rows - 1) {
			            var todown = $('#' + gridname).datagrid('getData').rows[index];
			            var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
			            $('#' + gridname).datagrid('getData').rows[index + 1] = todown;
			            $('#' + gridname).datagrid('getData').rows[index] = toup;
			            $('#' + gridname).datagrid('refreshRow', index);
			            $('#' + gridname).datagrid('refreshRow', index + 1);
			            $('#' + gridname).datagrid('selectRow', index + 1);
			        }
			    }
			 
			}
    	
 </SCRIPT>  
 </HEAD>
<BODY class="easyui-layout">

<div region="center"  title="List类型节点数据填充" split="true">  
    <div id="toolbar" class="dRbtnsToolbar">
<a href="#" class="easyui-linkbutton dRbtnUpdate" iconcls="icon-edit" onclick="saveParamConfig()" >保存</a>
    </div>
    <table id="paramTable">
    </table>
  		
	</div>
	<div id="input_select_extedit" class="easyui-dialog" modal="true" align="center"
		style="padding: 10px; border: 0px; margin: 0px; width: 540px;"
		closed="true" resizable="true" inline="false">
			<table id="selectEditTable"></table>
			<div id="buttons"
				style="margin-top: 20px; margin-left: 40px; padding-bottom: 10px;">
				<a class="easyui-linkbutton dPbtnDark70" href="javascript:addOne_selectEdit();">确认</a>
				<a class="easyui-linkbutton dPbtnLight70"
					href="javascript:addCancel_selectEdit();">取消</a>
			</div>

	</div>		
</BODY>
</html>