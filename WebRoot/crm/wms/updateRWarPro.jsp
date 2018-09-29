<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>修改入库单产品</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript">       
		function check(){
			var num=$("num");
			var errStr = "";
			if(isEmpty("num")){
				errStr+="- 未填写入库数量！\n"; 
			}
			else if(isNaN(num.value)){
				alert(num)
				errStr+="- 入库数量请填写数字！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("register").submit();
			}						  
		}
  </script> 
  </head>
  <body>
  <logic:notEmpty name="rwinPro">
  <div class="inputDiv">
  	<form action="wmsManageAction.do" method="post" id="register">
  		<input type="hidden" name="wprId" id="wprCode" value="${wprId}">	
  		<input type="hidden" name="wwiId" value="${wwiId}">	
  		<input type="hidden" name="rwiId" value="${rwinPro.rwiId}">
  		<input type="hidden" name="op" value="updateWinPro">
		<table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
        	<tbody>
            	<tr>
                    <th>产品名称：</th>
                    <td><span class="textOverflow" style="width:150px;" title="${rwinPro.wmsProduct.wprName}<logic:notEmpty name='rwinPro' property='wmsProduct.wprModel'>/${rwinPro.wmsProduct.wprModel}</logic:notEmpty>">${rwinPro.wmsProduct.wprName}<logic:notEmpty name='rwinPro' property='wmsProduct.wprModel'>/${rwinPro.wmsProduct.wprModel}</logic:notEmpty></span></td>				
                </tr>
                <tr>
                    <th class="required">入库量：<span class='red'>*</span></th>
                    <td><input name="rwiWinNum" id="num" type="text" value="<bean:write name='rwinPro' property='rwiWinNum' format='0.00'/>" onBlur="checkIsNum(this)" class="inputSize2" ></td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td><textarea name="rwiRemark" class="inputSize2" onBlur="autoShort(this,500)">${rwinPro.rwiRemark}</textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="2">
                    <input type="Button" id="dosave"  class="butSize1" value="保存" onClick="check()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel" value="取消" onClick="cancel()"></td>
                </tr>
            </tbody>
						
	  </table>
	</form>
  </div>
  </logic:notEmpty>
	<logic:empty name="rwinPro">
        <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该产品已被删除</div>
	</logic:empty>
  </body>
</html>
