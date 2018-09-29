<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String wmsCode=request.getParameter("wmsCode");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>数据整理</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#fff;
			padding-top:8px;
		}
		
		div{
			margin:0px; 
			padding:5px;
			padding-left:15px;
			text-align:left;
		}
		
		form{
			margin:0px;
			padding:5px;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript">
		function submitForm(){
			waitSubmit("dosave","正在执行...");
			waitSubmit("cancel2");
			return $("form").submit();
		}
    	
    </script>
  </head>
  
  <body>
   	<div>
		<img src="images/content/blueCircle.gif"/>&nbsp;本操作会将自动删除数量为0库存数据
   	</div>
    <div>
    	<img src="images/content/blueCircle.gif"/>&nbsp;本操作只会对本仓库执行清理
    </div>
    <form action="wwoAction.do" method="post" id="form">
		<input type="hidden" name="op" value="wmsOption">
		<input type="hidden" name="wmsCode" value="<%=wmsCode%>">
		<button class ="butSize1" id="dosave" onClick="submitForm()">执行</button>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<button id="cancel2" class ="butSize1"  onClick="cancel()">取消</button>
    </form>
  </body>
</html>
