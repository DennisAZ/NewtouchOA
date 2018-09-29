<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>批量标色</title>
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
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript">
    	function confirmColor(){
			waitSubmit("confirm");
			waitSubmit("cancelButton");
			var subForm=$("sub");//获得提交的form对象
			$("ids").value=getBacthIds();//给隐藏域赋值
			return $("sub").submit();
		}
		
		window.onload=function(){
			createCusCb("t_color","corColorTd", "corColor");
		}
  </script>
  </head>
  
  <body> 
  	<div style="padding:5px 0 0 0;">
        <form id="sub" action="customAction.do" method="post" style="margin:0; padding:0;">
            <input type="hidden" id="op" name="op" value="batchColor" />
            <input type="hidden" id="ids" name="ids" value="" />
            <table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
                <tbody>
                <tr class="noBorderBot">
                	<th>标色：</th>
                    <td id="corColorTd">&nbsp;</td>
                </tr>
                <tr class="submitTr">
                  <td colspan="2">
                    <button id="confirm" class ="butSize1" onClick="confirmColor();">确定</button>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <button id="cancelButton" class ="butSize1" onClick="cancel()">取消</button>
                 </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div> 
	</body>
</html>
