<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/json2.js"></script>
<script type="text/javascript" src="<%=path%>/nh-micro-jsp/js/zTree/js/jquery.ztree.core-3.4.js"></script>
<link rel="stylesheet" type="text/css" href="<%=path%>/nh-micro-jsp/js/zTree/css/zTreeStyle/zTreeStyle.css">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" language="javascript">
var setting = {  
		 check: { /**复选框**/  
		  enable: false,  
		  chkboxType: {"Y":"", "N":""}  
		 },  
		 view: {                                    
		  //dblClickExpand: false,  
		  expandSpeed: 300 //设置树展开的动画速度，IE6下面没效果，  
		 },                            
		 data: {    
		key:{
			name:"dept_name"
				},                                
		  simpleData: {   //简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的                           
		   enable: true,  
		   idKey: "dept_id",  //id和pid，这里不用多说了吧，树的目录级别  
		   pIdKey: "parent_id",  
		   rootPId: "root"   //根节点  
		  }                            
		 },                           
		 callback: {     /**回调函数的设置，随便写了两个**/  
		  beforeClick: beforeClick,                                    
		  onCheck: onCheck                            
		 }  
		};  
		function beforeClick(treeId, treeNode) {  
		 //alert("beforeClick");  
		}  
		function onCheck(e, treeId, treeNode) {  
		 //alert("onCheck");  
		}       
		  
		var citynodes = [      /**自定义的数据源，ztree支持json,数组，xml等格式的**/  
		 {dept_id:"zhongguo", parent_id:"root", dept_name:"中国"},  
		 {dept_id:"beijing", parent_id:"zhongguo", dept_name:"北京"},   
		 {dept_id:"tianjin", parent_id:"zhongguo", dept_name:"天津"}
 
		];  
		  
		$(document).ready(function(){//初始化ztree对象   
			var treeData=citynodes;

			var url="<%=path%>/NhEsbServiceServlet?cmdName=Groovy&subName=nhuser_dept";
			$.ajax({
					url:url,
					type:'post',
					dataType:'json',
					data:"groovySubName=getDept",
					success:function(data,status){
						

						var resultData=data.resultData;
						console.log(resultData);
					var resultDataObj=JSON.parse(resultData);
					var groovyResultObj=resultDataObj.resultObj;
					console.log(groovyResultObj);
				
					treeData=groovyResultObj;
					var zTreeDemo = $.fn.zTree.init($("#tree"),setting, treeData);  
					}
				});
			  
		  
		});  
</script>
</head>
<body>
<div id="tree"  class="ztree">
</div>
</body>
</html>