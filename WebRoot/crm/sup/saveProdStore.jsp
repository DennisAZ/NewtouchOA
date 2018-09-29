<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>保存库存</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/sup/sup.js"></script>
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("pstName")){
				errStr+="- 未填写库存名称！\n";
			}
			if(isEmpty("wprId")){
				errStr+="- 未选择对应商品！\n";
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
		
		
		//商品选择
		function chooseProd(){
			parent.addDivBrow(20,1);
		}
		
		window.onload = function(){
			if("${prodStore}"!=""){
				$("psTypId").value = "${prodStore.storeType.typId}";
			}
		}
  	</script>
  </head>
  
  <body>
	<div class="inputDiv">
		<form id="saveForm" action="supAction.do" method="post">
			<input type="hidden" name="op" value="saveProdStore"/>
            <input type="hidden" name="pstId" value="${prodStore.pstId}" />
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
                    <tr>
                    	<th class="required">库存名称：<span class='red'>*</span></th>
                    	<td><input name="prodStore.pstName" id="pstName" type="text" class="inputSize2"  onBlur="autoShort(this,100)" value="<c:out value="${prodStore.pstName}" />"/></td>
	                    <th class="required">对应商品：<span class='red'>*</span></th>
	                    <td>
	                    	<input type="hidden" name="wprId" id="wprId" value="${prodStore.wmsProduct.wprId}" />
	                        <input id="wprName" class="inputSize2S inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'wprId')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${prodStore.wmsProduct.wprName}"/>" readonly/>&nbsp;
	                        <button class="butSize2 inputBoxAlign" onClick="chooseProd()">选择</button>
	                    </td>
                    </tr>
                    <tr class="noBorderBot">
                    	<th>仓库：</th>
                        <td>
                        	<c:if test="${!empty typList}">
                        		<select name="psTypId" id="psTypId" class="inputSize2 inputBoxAlign">
                        			<option value=""></option>
                        			<c:forEach var="type" items="${typList}">
                        				<option value="${type.typId}"><c:out value="${type.typName}"/></option>
                        			</c:forEach>
                        		</select>
                        	</c:if>
                        	<c:if test="${empty typList}">
                               <select class="inputSize2 inputBoxAlign" disabled="disabled">
                                   <option>未添加</option>
                               </select>
                           </c:if>
                           <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                        <th>库存数量：</th>
                   		<td><input name="prodStore.pstCount" id="pstCount" type="text" class="inputSize2" onBlur="checkPrice(this)" value="<c:out value="${prodStore.pstCount}" />"/></td>
                    </tr>
                    <tr class="submitTr">
                    <td colspan="4">
                    <input id="doSave" class="butSize1" type="button" value="保存" onClick="check()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                    </td>
                </tr>
                </tbody>
			</table>
		</form>
    </div>
	</body>
</html>
