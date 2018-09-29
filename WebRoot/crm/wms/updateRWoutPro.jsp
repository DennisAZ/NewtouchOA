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
    
    <title>修改出库单产品</title>
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
			var num="<bean:write name='rstroPro' property='rspProNum' format='0.00'/>";
			var num2=$("rspNum").value;
			
			var errStr = "";
			if(isEmpty("rspNum")){
				errStr+="- 未填写出库数量！\n"; 
			}
			else if(isNaN(num2)){
				errStr+="- 出库数量请填写数字！\n";
			}
			if(parseFloat(num2)>parseFloat(num)){
				errStr+="- 出库数量大于库存，无法出库！\n";	
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("register").submit();
			}				  
		}
  </script> 
  </head>
  <body>
  <logic:notEmpty name="rwoutPro">
  <div class="inputDiv">
  		<form action="wwoAction.do" method="post" id="register">
		<input type="hidden" name="op" value="updateWoutPro">
  		<input type="hidden" name="wprCode" id="wprCode" value="${wprId}">	
  		<input type="hidden" name="wwoId" value="${wwoId}">	
  		<input type="hidden" name="rwoId" value="${rwoutPro.rwoId}">
  		
		<table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
        	<tbody>
            	<tr>
                    <th>调出仓库：</th>
                    <td><span class="textOverflow" style="width:150px" title="${wmsWarOut.wmsStro.wmsName}">${wmsWarOut.wmsStro.wmsName}</span></td>
                </tr>
                <tr>
                    <th>出库产品：</th>
                    <td><span class="textOverflow" style="width:150px" title="${rwoutPro.wmsProduct.wprName}<logic:notEmpty name='rwoutPro' property='wmsProduct.wprModel'>/${rwoutPro.wmsProduct.wprModel}</logic:notEmpty>">${rwoutPro.wmsProduct.wprName}<logic:notEmpty name='rwoutPro' property='wmsProduct.wprModel'>/${rwoutPro.wmsProduct.wprModel}</logic:notEmpty></span></td>			
                </tr>
                <tr>
                     <th>库存量：</th>
                     <td><bean:write name="rstroPro" property="rspProNum" format="###,##0.00"/>&nbsp;</td>
                </tr>
                <tr>
                     <th class="required">出库量：<span class='red'>*</span></th>
                     <td><input name="rwoWoutNum" id="rspNum" type="text" value="<bean:write name='rwoutPro' property='rwoWoutNum' format='0.00'/>"  onblur="checkIsNum(this)" class="inputSize2"/></td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td><textarea name="rwoRemark" class="inputSize2" onBlur="autoShort(this,500)">${rwoutPro.rwoRemark}</textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="2">
                    <input type="Button" id="dosave" class="butSize1" value="保存" onClick="check()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel" value="取消" onClick="cancel()"></td>
                </tr>	
            </tbody>
					
	  </table>
	</form>
  </div>
  </logic:notEmpty>
	<logic:empty name="rwoutPro">
        <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该产品已被删除</div>
	</logic:empty>
  </body>
</html>
