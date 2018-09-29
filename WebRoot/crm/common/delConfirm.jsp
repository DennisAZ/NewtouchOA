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
    <script type="text/javascript">
		function loadDelObj(){
			var delType="${delType}";
			var innerObj="";
			var delObj = parent.getDelObj('${delType}','${id}');//调用父页面js中自定义getDelObj方法
			$("delObj").innerHTML = delObj[0];
			$("delete").onclick = function(){
				waitSubmit("delete","删除中...");
				waitSubmit("cancel");
				if("${isIfrm}"!=""){//删除成功后是否刷新iframe（1为刷新iframe）
					delObj[1] += "&isIfrm=${isIfrm}";
				}
				self.location.href = delObj[1];
			};
		}

	window.onload=function(){
		loadDelObj();
	}
  </script>
  </head>
  
  <body> 
  	<div id="delConfirm"> 
        <br/>
        <img style="vertical-align:middle;" src="crm/images/content/recycle.gif"/>&nbsp;&nbsp;&nbsp;确定要删除<span id="delObj"></span>吗？<br/><br/>
        <button id="delete" class ="butSize1">确定</button>		 
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button id="cancel" class ="butSize1" onClick="cancel()">取消</button>
    </div> 
	</body>
</html>
