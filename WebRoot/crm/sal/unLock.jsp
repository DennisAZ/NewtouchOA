<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String code=request.getParameter("ordId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>撤销审核操作</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="cache-control" content="no-cache"/>
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#fff;
		}
		.reTxt{
			padding:2px 4px 14px 38px;
			text-align:left;
			background:url(images/content/bigAlert.gif) no-repeat;
		}
		.resConfirm {
			padding:12px; 
			padding-top:12px;
			padding-right:0px;
		}
		.resConfirm form{
			margin:0px;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
  </head>
  
  <body>
  	<div class="resConfirm"> 
      	<form action="orderAction.do" method="post">
          	<input type="hidden" name="op" value="removeSta">
          	<input type="hidden" name="code" value="<%=code%>">
			<input type="hidden" name="type" value="1">
          	<div class="reTxt" style="padding-bottom:5px">
                <div style="padding-bottom:5px">确认撤销订单审核吗？</div>
                撤销审核后可以编辑订单明细，订单状态将转为待审核
          	</div>
          	<button class ="butSize1" type="submit">确认执行</button>		 
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<button class ="butSize1" onClick="cancel()">取消</button>
	  	</form>
    </div> 
	</body>
</html>
