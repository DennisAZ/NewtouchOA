<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>保存付款记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
    <script type="text/javascript" src="crm/js/chooseBrow.js"></script>
    <script type="text/javascript" src="crm/sup.js"></script>
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("supName")){
				errStr+="- 未选择供应商！\n";
			}
			if(isEmpty("payMon")){   
				errStr+="- 未填写付款金额！\n"; 
			}
			if(isEmpty("seId")){   
				errStr+="- 未选择付款人！\n"; 
			}
			if(isEmpty("pastDate")){
				errStr+="- 未选择付款日期！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("create").submit();
			}
		}
		

		//供应商选择
		function chooseSup(){
			parent.addDivBrow(21);
		}
		//采购单选择
		function choosePurOrd(){
			parent.addDivBrow(23);
		}
		
		
		//初始化表单
		function initForm(){
			<logic:notEmpty name="supPaidPast">
				removeTime("pastDate","${supPaidPast.sppFctDate}",1);
				if("${supPaidPast.sppIsinv}" == "0"){
					$("isInv1").checked=true;
				}else{
					$("isInv2").checked=true;
				}
				$("payType").value = "${supPaidPast.sppPayType}";
				$("puoCode").value = "<c:out value="${supPaidPast.purOrd.puoCode}"/>";
				$("puoId").value = "${supPaidPast.purOrd.puoId}";
				$("seId").value = "${supPaidPast.salEmp.seNo}";
				$("soUserName").value = "<c:out value="${supPaidPast.salEmp.seName}"/>";
			</logic:notEmpty>
			<logic:empty name="supPaidPast">
				$("supId").value = parent.document.getElementById("supId").value;
				$("supName").value = parent.document.getElementById("supName").value;
				$("seId").value = "${CUR_EMP.seNo}";
				$("soUserName").value="<c:out value="${CUR_EMP.seName}"/>";
			</logic:empty>
		}
		
    	window.onload=function(){
			initForm();
		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
	<form id="create" action="supAction.do" method="post">
			<input type="hidden" name="op" value="saveSupPaidPast"/>
			<input type="hidden" name="isIfrm" value="${isIfrm}"/>
            <input type="hidden" name="supId" value="${supId}"/>
            <input type="hidden" name="sppId" value="${supPaidPast.sppId}"/>
   			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
					 <tr>
                        <th class="required">对应供应商：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd">
	                    	<input type="hidden" name="supId" id="supId" value="${supPaidPast.supplier.supId}" />
	                        <input id="supName" class="inputSize2SL inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'supId')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${supPaidPast.supplier.supName}"/>" readonly/>&nbsp;
	                        <button class="butSize2 inputBoxAlign" onClick="chooseSup()">选择</button>
                        </td>
                    </tr>
                    <tr>
                    	<th>对应采购单：</th>
                        <td colspan="3" class="longTd">
	                    	<input type="hidden" name="puoId" id="puoId" value="${supPaidPast.purOrd.puoId}" />
	                        <input id="puoCode" class="inputSize2SL inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'supId')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${supPaidPast.purOrd.puoCode}"/>" readonly/>&nbsp;
	                        <button class="butSize2 inputBoxAlign" onClick="choosePurOrd()">选择</button>
                        </td>
                    </tr>
                	<tr>
                    	<th class="required">付款金额：<span class='red'>*</span></th>
                        <td><input name="supPaidPast.sppPayMon" id="payMon" type="text" class="inputSize2" onBlur="checkPrice(this)" value="<fmt:formatNumber value='${supPaidPast.sppPayMon}' pattern='#0.00'/>"/></td>
                        <th>付款方式：</th>
                        <td>
                            <select type="text" name="supPaidPast.sppPayType" id="payType" class="inputSize2">
                                <option value="">请选择</option>
                                <option value="支票">支票</option>
                                <option value="现金">现金</option>
                                <option value="邮政汇款">邮政汇款</option>
                                <option value="银行电汇">银行电汇</option>
                                <option value="网上支付">网上支付</option>
                                <option value="承兑汇票">承兑汇票</option>
                                <option value="其他">其他</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th class="required">付款人：<span class='red'>*</span></th>
                         <td>
                        	<input readonly class="inputSize2S lockBack" type="text" id="soUserName" ondblClick="clearInput(this,'seId')" title="此处文本无法编辑，双击可清空文本">&nbsp;
					 		<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
                            <input type="hidden" name="seId" id="seId"/>
                        </td>
                        <th class="required">付款日期：<span class='red'>*</span></th>
                        <td><input name="pastDate" id="pastDate" type="text" class="inputSize2 Wdate" value="<c:out value="${supPaidPast.sppFctDate}"/>" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/></td>
                    </tr>
                    <tr>
                    	<th>是否已开票：</th>
                        <td colspan="3"><input type="radio" name="supPaidPast.sppIsinv" id="isInv1" value="0" checked/><label for="isInv1">未开票&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="radio" name="supPaidPast.sppIsinv" id="isInv2" value="1"/><label for="isInv2">已开票</label></td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="3" name="supPaidPast.sppContent" id="spsContent" type="text" onBlur="autoShort(this,200)" >${supPaidPast.sppContent}</textarea></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
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
