<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>功能列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
		body{
			background-color:#fff;
		}
    	
		.deepBlueBg,.grayBack {
			padding:4px;
		}
		.menuLis{
			padding:5px;
			color:#003366;
		}
		.menuText{
			color:#999999;
			text-align:left;
			padding:2px;
		}
    </style>
	<script language="javascript">
		
		//载入选中样式
		function loadStyle(){
			var typeId=parent.document.getElementById("typeId").value;
			document.getElementById("type"+typeId).className="deepBlueBg";
			
			var lis = document.getElementsByTagName("li");
			for(var j=0;j<lis.length;j++){
				if(lis[j].className!="deepBlueBg"){
					lis[j].className="menuLis";
					lis[j].onmouseover=function(){
							this.className="grayBack";
					};
					lis[j].onmouseout=function(){
							this.className="menuLis";
					}; 
				}
				lis[j].style.cursor="pointer";
			}
		}
		function menuForward(type)
		{
			userCode=parent.document.getElementById("userCode").value;
			parent.location.href="../userAction.do?op=getFunOperate&userCode="+userCode+"&funType="+type;
		}
		//生成左侧功能模块菜单
		function createMenu(id){
			//var funList = new Array(["客户管理","cus"],["销售管理","sal"],["库存管理","wms"],["项目管理","proj"],["协同办公","oa"],["人事管理","hr"],["采购管理","pur"],["财务管理","acc"],["统计分析","sta"],["系统设置","sys"]);
			var funList = new Array(["客户管理","cus"],["销售管理","sal"],["采购管理","pur"],["协同办公","oa"],["人员管理","hr"],["统计报表","sta"],["系统设置","sys"]);
			for(var i=0;i<funList.length;i++){
				document.getElementById(id).innerHTML+="<li id='type"+ funList[i][1] +"' onclick='menuForward(\""+ funList[i][1] +"\")'>"+ funList[i][0] +"</li>";
			}
		}
		window.onload=function(){
			createMenu("funListMenu");
			loadStyle();
		}
	</script>
</head>

<body>
<div style="padding:5px; border:#ccc 1px solid; background-color:#f0eeee" onMouseOver="this.style.backgroundColor='#f6f6f6'" onMouseOut="this.style.backgroundColor='#f0eeee'">
	<div class="menuText">功能列表</div>
    <ul id="funListMenu" class="listtxt" style="padding:0px; margin:0px"></ul>
</div>
</body>
</html>

