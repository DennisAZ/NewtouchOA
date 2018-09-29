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
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background:#fff;
		}
    </style>
    
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    
    <script type="text/javascript">
		function loadDelObj(){
		    var confirmType="${confirmType}";
			var innerObj="";
			var confirmObj = parent.getConObj('${confirmType}','${id}');//调用父页面js中自定义confirmObj方法
			$("confirmObj").innerHTML = confirmObj[0];
			$("confirm").onclick = function(){
				waitSubmit("confirm");
				waitSubmit("cancel");
				if("${isIfrm}"!=""){//删除成功后是否刷新iframe（1为刷新iframe）
					confirmObj[1] += "&isIfrm=${isIfrm}";
				}
				self.location.href = confirmObj[1];
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
        <span id="confirmObj"></span><br/><br/>
        <button id="confirm" class ="butSize1">确定</button>		 
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button id="cancel" class ="butSize1" onClick="cancel()">取消</button>
    </div> 
	</body>
</html>
