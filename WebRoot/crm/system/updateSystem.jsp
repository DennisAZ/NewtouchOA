<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>修改授权信息</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
		body{
			background-color:#fff;
		}
    	td{
			padding:2px;
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
				errStr+="- 未填写企业名称！\n";
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
				waitSubmit("Submit","更新中...");
				waitSubmit("cancel1");
				return $("register").submit();
			}				  
		  }
	
  	</script>
  </head>
  
  <body> 
  	<div style="padding:2px; padding-top:10px;">
		<form action="userAction.do" method="post" id="register" style="padding:0px; margin:0px">
		<input type="hidden" name="op" value="updateSystem">
		 <table class="normal" style="width:260px;">
                <tr>
					<td style="text-align:right"><span class='red'>*</span>注册名称：</td>
					<td style="text-align:left"><input type="text" name="cusName" id="cusName" class="inputSize2"></td>
				</tr>
				<tr>
					<td style="text-align:right"><span class='red'>*</span>授权码：</td>
					<td style="text-align:left"><input  type="text"name="password" id="password" class="inputSize2"></td>
				</tr>								
				<tr>
					<td colspan="2" style="text-align:center; padding-top:10px;">
					<input type="button" value="导入" class="butSize1" id="Submit" onClick="check()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" value="取消" class="butSize1" id="cancel1" onClick="cancel()">
                    </td>
				</tr>
			</table>
		</form>
    </div> 
	</body>
</html>
