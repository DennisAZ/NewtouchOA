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
    
    <title>修改产品盘点</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/chooseStro.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript">
		function showIndex(){
			var rmcType = '${rwmsChange.rmcType}'; 
	    	$("rt").value=rmcType;
		}
		
		function check(){
		 	var num=$("relNum").value;
			var errStr = "";
			if(isEmpty("relNum")){
				errStr+="- 未填写实际盘点数量！\n"; 
			}
			else if(isNaN(num)){
				errStr+="- 实际盘点数量请填写数字！\n";
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
		
		window.onload=function(){
			if("${rwmsChange}"!=null&&"${rwmsChange}"!=""){
				showIndex();
			}
		}	 
	</script>
  </head>
  <body>
  <logic:notEmpty name="rwmsChange">
  <div class="inputDiv">
  		<form action="wwoAction.do" method="post" id="register">
		<input type="hidden" name="op" value="updateRwmc">
  		<input type="hidden" name="rwcId" value="${rwcId}">
		<input type="hidden" name="wprCode" id="wprCode" value="${wprCode}">
		<input name="rwcDifferent" id="rspNum" type="hidden" value="<bean:write name='rwmsChange' property='rwcDifferent' format='0.00'/>">
		<input type="hidden" name="rmcWmsCount" id="num" value="<bean:write name='rwmsChange' property='rmcWmsCount' format='0.00'/>">
		<table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
        	<tbody>
            	<tr>
                    <th>盘点仓库：</th>
                    <td><span class="textOverflow" style="width:150px" title="${wmsCheck.wmsStro.wmsName}">${wmsCheck.wmsStro.wmsName}</span></td>		
                </tr>
                <tr>
                    <th>盘点产品：</th>
                    <td><span class="textOverflow" style="width:150px" title="${rwmsChange.wmsProduct.wprName}<logic:notEmpty name='rwmsChange' property='wmsProduct.wprModel'>/${rwmsChange.wmsProduct.wprModel}</logic:notEmpty>">${rwmsChange.wmsProduct.wprName}<logic:notEmpty name='rwmsChange' property='wmsProduct.wprModel'>/${rwmsChange.wmsProduct.wprModel}</logic:notEmpty></span></td>		
                </tr>
                <tr>
                    <th>账面数量：</th>
                    <td><bean:write name="rwmsChange" property="rmcWmsCount" format="###,##0.00"/>&nbsp;</td>
              	</tr>
              	<tr>
                    <th class="required">实际盘点数量：<span class='red'>*</span></th>
                    <td><input class="inputSize2" name="rmcRealNum" id="relNum" onBlur="checkIsNum(this)" type="text" value="<bean:write name='rwmsChange' property='rmcRealNum' format='0.00'/>"/></td>
              	</tr>
            	<tr>
                    <th>差异数量：</th>
                    <td><span id="dif"><bean:write name="rwmsChange" property="rwcDifferent" format="###,##0.00"/></span>&nbsp;</td>
              	</tr>
                <tr>
                    <th>模式：</th>
                    <td>
                    <select name="rmcType" id="rt" class="inputSize2">
                        <option value="">&nbsp;</option>
                        <option value="0">不计成本</option>
                        <option value="1">计算成本</option>
                    </select>
                    </td>
              	</tr>
              	<tr class="noBorderBot">
                    <th>备注：</th>
                    <td><textarea name="wmcRemark" class="inputSize2" onBlur="autoShort(this,500)">${rwmsChange.rmcRemark}</textarea></td>
              	</tr>
                <tr class="submitTr">
                    <td colspan="2">
                    <input type="button" id="dosave" class="butSize1" value="保存" onClick="check()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel" value="取消" onClick="cancel()"></td>
                </tr>
            </tbody>
						
	  </table>
	</form>
  </div>
  
      <script type="text/javascript">       
		$("relNum").onkeyup=function(){
			if($("num")!=null&&$("num").value!=""){
				$("rspNum").value=parseFloat($("relNum").value)-parseFloat($("num").value);
				$("dif").innerText=parseFloat($("relNum").value)-parseFloat($("num").value);
			}
		}
  </script> 
  </logic:notEmpty>
	<logic:empty name="rwmsChange">
        <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该产品已被删除</div>
	</logic:empty>
  </body>
</html>
