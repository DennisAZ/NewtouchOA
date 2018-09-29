<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String code=request.getParameter("code");
String state=request.getParameter("state");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>修改交付状态</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="cache-control" content="no-cache"/>
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#fff;
		}
		.resConfirm {
			padding:10px; 
		}
		.resConfirm form{
			margin:0px;
		}
    </style>
    <script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript">
	function init(){
			if("<%=state%>"=="0"){
				document.getElementById("shipState1").checked="true";
			}
			if("<%=state%>"=="1"){
				document.getElementById("shipState2").checked="true";
			}
			if("<%=state%>"=="2"){
				document.getElementById("shipState3").checked="true";
			}
		}
		window.onload=function(){
			init();
		}
	</script>
  </head>
  
  <body>
  	<div class="resConfirm"> 
      	<form action="salPurAction.do" method="post">
          	<input type="hidden" name="op" value="altSpoSta">
          	<input type="hidden" name="code" value="<%=code%>">
            <div style=" text-align:left;" class="bold blue middle">修改交付状态</div>
			<div style=" padding:12px; padding-top:8px;">
				<input type="radio" id="shipState1"  name="spoIsend" value="0"><label for="shipState1">未交付</label>
				<input type="radio" id="shipState2" name="spoIsend" value="1"><label for="shipState3">部分交付</label>
				<input type="radio" id="shipState3" name="spoIsend" value="2"><label for="shipState2">全部交付</label>
			</div>
          	<button class ="butSize1" type="submit">确认</button>		 
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<button class ="butSize1" onClick="cancel()">取消</button>
	  	</form>
    </div> 
	</body>
</html>
