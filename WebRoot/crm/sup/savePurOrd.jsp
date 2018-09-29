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
    <title>保存采购单</title>
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
			if(isEmpty("puoCode")){
				errStr+="- 未填写采购单号！\n";
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
		
		function initForm(){
			if("${purOrd}"!=""){
				if($("puoTypeId")!=null){ 
					$("puoTypeId").value = "${purOrd.puoType.typId}";
				}
				if('${purOrd.puoIsEnd}'=='0'){
					$("isEnd1").checked="true";
				}
				else{
					$("isEnd2").checked="true";
				}
				removeTime("puoPurDate","${purOrd.puoPurDate}",1);
			}
		}
		
		//供应商选择
		function chooseSup(){
			parent.addDivBrow(21);
		}
  	</script>
  </head>
  
  <body>
	<div class="inputDiv">
		<form id="saveForm" action="supAction.do" method="post">
			<input type="hidden" name="op" value="savePurOrd"/>
            <input type="hidden" name="puoId" value="${purOrd.puoId}" />
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
					 <tr>
                     	<th class="required">采购单号：<span class='red'>*</span></th>
                        <td><input class="inputSize2" type="text" name="purOrd.puoCode" id="puoCode" value="<c:out value="${purOrd.puoCode}"/>" onBlur="autoShort(this,100)"/></td>
						<th>采购日期：</th>
						<td>
						    <input name="puoPurDate" id="puoPurDate" type="text" class="inputSize2 Wdate" style="cursor:hand"
                                 readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"  value="<c:out value="${purOrd.puoPurDate}"/>" />
						</td>
                    </tr>
                    <tr>
                        <th>供应商：</th>
                        <td colspan="3" class="longTd">
	                    	<input type="hidden" name="supId" id="supId" value="${purOrd.puoSup.supId}" />
	                        <input id="supName" class="inputSize2SL inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'supId')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${purOrd.puoSup.supName}"/>" readonly/>&nbsp;
	                        <button class="butSize2 inputBoxAlign" onClick="chooseSup()">选择</button>
                        </td>
                    </tr>
                    <tr>
                    	<th>采购总金额：</th>
                        <td><input name="purOrd.puoM" id="puoM" type="text" class="inputSize2" onBlur="checkPrice(this)" value="<fmt:formatNumber value='${purOrd.puoM}' pattern='#0.00'/>"/></td>
                        <th>已付款金额：</th>
                   		<td><input name="purOrd.puoPaidM" id="puoPaidM" type="text" class="inputSize2" onBlur="checkPrice(this)" value="<fmt:formatNumber value='${purOrd.puoPaidM}' pattern='#0.00'/>"/></td>
                    </tr>
                    <tr>
                         <th>采购人：</th>
                         <td><input type="hidden" name="empId" id="seNo" value="${limUser.salEmp.seNo}"/>
                         <input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${CUR_EMP.seName}"/>" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                         <button class="butSize2" onClick="parent.addDivBrow(12)">选择</button></td>
                   	     <th>类别：</th>
                       	 <td><c:if test="${!empty puoTypeList}">
                           <select id="puoTypeId" name="puoTypeId" class="inputSize2 inputBoxAlign">
                               <option value=""></option>
                               <c:forEach var="puoTypeObj" items="${puoTypeList}">
                               <option value="${puoTypeObj.typId}"><c:out value="${puoTypeObj.typName}"/></option>
                               </c:forEach>
                           </select>
                           </c:if>
                           <c:if test="${empty puoTypeList}">
                               <select class="inputSize2 inputBoxAlign" disabled="disabled">
                                   <option>未添加</option>
                               </select>
                           </c:if>
                           <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/></td>
                    </tr>
                    <tr>
                    	 <th>交付状态：</th>
                    	 <td colspan="3"><input type="radio" name="purOrd.puoIsEnd" id="isEnd1" value="0" checked/><label for="isEnd1">未交付&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="radio" name="purOrd.puoIsEnd" id="isEnd2" value="1"/><label for="isEnd2">已交付</label></td>
                    </tr>
					<tr>
                        <th>条件与条款：</th>
                        <td colspan="3">
                            <textarea name="purOrd.puoContent" id="purContent" class="inputSize2L" rows="5" onBlur="autoShort(this,4000)"><c:out value="${purOrd.puoContent}"/></textarea>
                        </td>
                    </tr>
                   	<tr class="noBorderBot">
                    	<th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="5" name="purOrd.puoRemark" onBlur="autoShort(this,4000)"><c:out value="${purOrd.puoRemark}"/></textarea></td>
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
    <script type="text/javascript">
    initForm();
    </script>
</html>
