<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>删除确认</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	body{
			background:#fff;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
    <script type="text/javascript">
		function loadDelObj(){
			var delType="${delType}";
			var innerObj="";
			//载入删除类型
			switch(parseInt(delType)){
				/*case 1:
					innerObj="该教育背景";
					break;*/
				case 2:
					innerObj="该员工";
					break;
				case 3:
					innerObj="该工作";
					break;
				case 4:
					innerObj="该工作";
					break;
				case 5:
					innerObj="该渠道";
					break;
			}
			$("delObj").innerHTML=innerObj;
		}
		
    	function delConfirm(){
			waitSubmit("delete","删除中...");
			waitSubmit("cancel1");
			var delType="${delType}";
			switch(parseInt(delType)){
				case 2:
					self.location.href="empAction.do?op=delSalEmp&seNo=${code}";
					break;
				case 3:
					self.location.href="salTaskAction.do?op=delSalTask&stId=${code}";
					break;
				case 4:
					self.location.href="salTaskAction.do?op=delSalTask&satId=${code}&st=st";
					break;
			}
		}
    	
		createProgressBar();
		window.onload=function(){
			closeProgressBar();
			loadDelObj();
		}
  	</script>
  </head>
  
  <body> 
  	<div id="delConfirm"> 
        <br/>
        <img style="vertical-align:middle;" src="crm/images/content/recycle.gif"/>&nbsp;&nbsp;&nbsp;&nbsp;确定要删除<span id="delObj"></span>吗？
        <br/><br/>
        <button id="delete" class ="butSize1" onClick="delConfirm()">确定</button>		 
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button id="cancel1" class ="butSize1" onClick="cancel()">取消</button>
    </div> 
	</body>
</html>
