<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>保存客户产品</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <!--<script type="text/javascript" src="js/chooseBrow.js"></script>-->
	<script type="text/javascript" >

    	function check(){
			var errStr = "";
			 /*if(isEmpty("cusName")){   
				errStr+="- 未选择对应客户！\n"; 
			 }*/
			 if(isEmpty("wprId")){   
				errStr+="- 未选择对应产品！\n"; 
			 }
			 if(isEmpty("cpPrice")){
			 	errStr+="- 未选择客户价格！\n";
			 }
			 if($("cpWarnDay").value.indexOf(".")!=-1){
			 	errStr+="- 提前提醒天数必须为整数！\n";
			 }
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("doSave");
				waitSubmit("doCancel");
		 		return $("saveForm").submit();
			 }
		}
		
		/*function chooseCus(){
			parent.addDivBrow(1);
		}*/
		function chooseProd(){
			parent.addDivBrow(20);
		}
		
		function initForm(){
			<c:if test="${empty cusProd}">
			//新建
			disableInput('cpId');
			$("op").value = "saveCusProd";
			$("cusId").value = "${cusInfo.corCode}";
			createCusCb("t_hasTax","hasTaxTd","cusProd.cpHasTax");
			</c:if>
			
			<c:if test="${!empty cusProd}">
			//编辑
			$("op").value = "updCusProd";
			$("cpId").value = "${cusProd.cpId}";
			$("cusId").value = "${cusProd.cusCorCus.corCode}";
			createCusCb("t_hasTax","hasTaxTd","cusProd.cpHasTax","${cusProd.cpHasTax}");
			</c:if>
		}
		
		window.onload=function(){
			initForm();
		}
  	</script>
  </head>
  
  <body>
	<div class="inputDiv">
  	<form id="saveForm" action="customAction.do" method="post">
  		<input type="hidden" id="op" name="op" />
        <input type="hidden" id="cpId" name="cusProd.cpId" />
        <input type="hidden" name="cusId" id="cusId" />
        <input type="hidden" name="isIfrm" value="${isIfrm}"/><!-- 保存后是否刷新iframe -->
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
                <!--<tr>
                    <th class="required">对应客户：<span class='red'>*</span></th>
                    <td colspan="3" class="longTd">
                        <input id="cusName" class="inputSize2SL inputBoxAlign lockBack" type="text" value="<c:out value="${cusInfo.corName}"/>" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                        <button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>
                    </td>
                </tr>-->
                <tr>
                    <th class="required">对应产品：<span class='red'>*</span></th>
                    <td colspan="3" class="longTd">
                    	<input type="hidden" name="wprId" id="wprId" value="${cusProd.wmsProduct.wprId}" />
                        <input id="wprName" class="inputSize2SL inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'wprId')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${cusProd.wmsProduct.wprName}"/>" readonly/>&nbsp;
                        <button class="butSize2 inputBoxAlign" onClick="chooseProd()">选择</button>
                    </td>
                </tr>
            	<tr>
            		<th>产品别名：</th>
                    <td colspan="3">
                    	<input class="inputSize2L" type="text" id="cpOtherName" name="cusProd.cpOtherName" onBlur="autoShort(this,100)" value="${cusProd.cpOtherName}"/>
                    </td>
            	</tr>
            	<tr>
                   <th class="required">客户价格：<span class="red">*</span></th>
                   <td><input name="cusProd.cpPrice" id="cpPrice" type="text" class="inputSize2" onBlur="checkPrice(this)" value="<fmt:formatNumber value='${cusProd.cpPrice}' pattern='#0.00'/>"/></td>
                   <th>是否含税价：</th>
                   <td id="hasTaxTd">&nbsp;</td>
                </tr>
                <tr>
                	 <th>预计月使用量：</th>
                	 <td><input class="inputSize2" type="text" id="cpPreNumber"  name="cusProd.cpPreNumber" onBlur="checkPrice(this)" value="${cusProd.cpPreNumber}"/></td>
                	 <th>提前提醒天数: </th>
                	 <td><input class="inputSize2" type="text" id="cpWarnDay" name="cusProd.cpWarnDay" onBlur="checkPrice(this)" value="${cusProd.cpWarnDay}"/></td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="3"><textarea class="inputSize2L" rows="7" id="remark" name="cusProd.cpRemark" onBlur="autoShort(this,4000)">${cusProd.cpRemark}</textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4"><input id="doSave" class="butSize1" type="button" value="保存" onClick="check()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" /></td>
                </tr>
        </table>
	</form>
    </div>
  </body>
</html>
