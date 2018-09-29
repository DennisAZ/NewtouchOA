<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>初始化系统</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="cache-control" content="no=-cache"/>
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	body{
			text-align:center;
		}
		#initLayer{
			border:#7dabe7 2px solid; 
			background-color:#fff;
			width:380px; 
			height:180px; 
			margin-top:20px;
		}
		#initLayer td{
			padding-top:5px;
			padding-bottom:5px;
		}
		#initLayer form{
			padding:0px;
			margin:0px;
		}
		#initTop {
			text-align:left; 
			color:#fff; 
			background:url(images/content/modTop.gif) repeat-x; 
			font-weight:600; 
			font-size:16px; 
			padding:5px;
			padding-bottom:10px;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sys.js"></script>
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript">
		function check(){
			var errStr = "";

			if(isEmpty("cusName")){
				errStr+="- 未填写注册名称！\n";
			}
			if(isEmpty("password")){   
				errStr+="- 未填写授权码！\n"; 
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("Submit","处理中...");
				waitSubmit("cancel1");
				return $("register").submit();
			}				  
		  }
	
  	</script>
  </head>
  
  <body> 
  	<div id="initLayer">
		<div id="initTop">系统初始化</div>
		<form action="userAction.do" method="post" id="register">
			<input type="hidden" name="op" value="iniSystem"/>
			<span class="redWarn">${msg}</span>
			<table class="normal">
                <tr>
					<td style="text-align:right"><span class="red">*</span>注册名称：</td>
					<td><input type="text" name="cusName" id="cusName" class="inputSize2"/></td>
				</tr>
				<tr>
					<td style="text-align:right"><span class="red">*</span>授权码：</td>
					<td><input  type="text"name="password" id="password" class="inputSize2"/></td>
				</tr>
				
				<tr>
					<td colspan="2" style="text-align:center">
					<input type="button" value="确认" class="butSize1" id="Submit" onClick="check()"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input id="cancel1" type="reset" value="重置" class="butSize1"/></td>
				</tr>
			</table>
		</form>
		
    </div> 
	</body>
</html>
