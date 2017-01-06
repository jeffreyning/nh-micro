<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>
<script type="text/javascript" charset="UTF-8">
	var tree;
	$(function() {
		tree = $('#tree').tree({
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

	});


</script>
<div class="easyui-panel" fit="true" border="false">
	<div class="easyui-accordion" fit="true" border="false">
		<div title="功能菜单" iconCls="icon-tip">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false">
					<ul id="tree" style="margin-top: 5px;"></ul>
				</div>
			</div>
		</div>
	</div>
</div>