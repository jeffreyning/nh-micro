<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>

<script type="text/javascript" charset="UTF-8">
	var tree;
	$(function() {
		tree = $('#tree-sys').tree({
			url : '<%=path%>/nh-micro-jsp/layout/west_menu.jsp',
			animate : false,
			onClick : function(node){
				if(node.attributes && node.attributes.url && node.attributes.url != ''){
					var href;
					if(/^\//.test(node.attributes.url)){
						href = node.attributes.url.substr(1);
					}else{
						href = node.attributes.url;
					}

					$centerFrame.attr('src',href);


				}
			},
			onLoadSuccess : function(node, data) {
				var t = $(this);
				if (data) {
					$(data).each(function(index, d) {
						if (this.state == 'closed') {
							t.tree('expandAll');
						}
					});
				}
			}
		});
		initBizTree();
	});
	
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
    function zTreeOnClick(event, treeId, treeNode) {
        var treeObj = $.fn.zTree.getZTreeObj("tree-biz");
        var nodes = treeObj.getSelectedNodes();
        var node = nodes[0];
		if(node.dbcol_ext_url && node.dbcol_ext_url != ''){
			var href;
			href = "<%=path %>/"+node.dbcol_ext_url;
			$centerFrame.attr('src',href);

		}
    };	
	function initBizTree() {
		var url = "<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_menu";
	$.ajax({
			url:url,
			type:'post',
			dataType:'json',
			data:"groovySubName=getInfoListAll4UserId",
			success:function(data,status){
				treeData=data;
				$.fn.zTree.init($("#tree-biz"),setting, treeData);  
			}
		});            
    
    
}
</script>
<div class="easyui-panel" fit="true" border="false">
	<div class="easyui-accordion" fit="true" border="false">
		<div title="系统菜单" iconCls="icon-tip">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false">
					<ul id="tree-sys" style="margin-top: 5px;"></ul>
				</div>
			</div>
		</div>		
		<div title="功能菜单" iconCls="icon-tip">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false">
					<ul id="tree-biz" class="ztree" style="margin-top: 5px;"></ul>
				</div>
			</div>
		</div>
	
	</div>
</div>