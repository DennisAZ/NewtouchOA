<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>保存回款记录</title>
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
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("cusId")){
				errStr+="- 未选择客户！\n";
			}
			if(isEmpty("payMon")){   
				errStr+="- 未填写回款金额！\n"; 
			}
			if(isEmpty("seId")){   
				errStr+="- 未选择回款人！\n"; 
			}
			if(isEmpty("pastDate")){
				errStr+="- 未选择回款日期！\n";
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
		
		//选择订单后的回调函数
		function selOrdCallBack(){
			disableInput("cusName,selCusButton");
		}
		function clearOrdInput(obj){
			clearInput(obj,'ordId');
			restoreInput("cusName,selCusButton");
		}
		
		//初始化表单
		function initForm(){
			<logic:empty name="paid">
			//新建
			disableInput('paidId');
			$("op").value = "savePaidPast";
			$("soUserName").value="<c:out value="${CUR_EMP.seName}"/>";
			$("seId").value = "${CUR_EMP.seNo}";
			if(parent.document.getElementById("corCode")!=null){//客户详情下新建
				$("isIfrm").value="1";
				$("cusName").value = parent.document.getElementById("cusName").value;
				$("cusId").value = parent.document.getElementById("corCode").value;
			}
			if(parent.document.getElementById("ordCode")!=null){//在详情页面下新建，默认填入订单信息，保存成功后刷新iframe
				disableInput('cusName,selCusButton');
				$("ordId").value = parent.document.getElementById("ordCode").value;
				$("ordTil").value = parent.document.getElementById("ordTil").value;
				$("cusName").value = parent.document.getElementById("ordCusName").value;
				$("cusId").value = parent.document.getElementById("ordCusCode").value;
				$("isIfrm").value="1";
				$("soUserName").value=parent.document.getElementById("ordSeName").value;
				$("seId").value = parent.document.getElementById("ordSeId").value;
			}
			</logic:empty>
			
			<logic:notEmpty name="paid">
			//编辑
			//disableInput('userCode');
			$("op").value = "modPaidPast";
			$("paidId").value = "${paid.spsId}";
			$("spsContent").value = "<c:out value="${paid.spsContent}"/>";
			$("cusName").value = '<c:out value="${paid.cusCorCus.corName}"/>';
			$("cusId").value = "${paid.cusCorCus.corCode}";
			$("soUserName").value = "<c:out value="${paid.salEmp.seName}"/>";
			$("seId").value = "${paid.salEmp.seNo}";
			if("${paid.salOrdCon.sodCode}"!=""){
				$("ordTil").value = "<c:out value="${paid.salOrdCon.sodTil}"/>";
				$("ordId").value = "${paid.salOrdCon.sodCode}";
				selOrdCallBack();
			}
			$("payMon").value="<bean:write name='paid' property='spsPayMon' format='0.00'/>";
			if('${paid.spsIsinv}'=='0'){
				$("isInv1").checked="true";
			}
			else{
				$("isInv2").checked="true";
			}
			$("payType").value="${paid.spsPayType}";
			removeTime("pastDate","${paid.spsFctDate}",1);
			if(parent.document.getElementById("corCode") !=null){//客户详情下编辑
				$("isIfrm").value="1";
			}
			if(parent.document.getElementById("ordCode")!=null){//在详情页面下，保存成功后刷新iframe
				$("isIfrm").value="1";
			}
			</logic:notEmpty>
		}
		
    	window.onload=function(){
			initForm();
		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
	<form id="create" action="paidAction.do" method="post">
			<input type="hidden" id="opMethod" name="op"/>
			<input type="hidden" id="isIfrm" name="isIfrm" />
            <input type="hidden" id="paidId" name="paidId"/>
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
					 <tr>
                     	<th class="required">对应客户：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd">
                            <input id="cusName" class="inputSize2SL lockBack" type="text" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                            <button id="selCusButton" class="butSize2" onClick="parent.addDivBrow(1)">选择</button>
                            <input type="hidden" name="cusId" id="cusId" />
                        </td>
                    </tr>
                    <tr>
                    	<th>对应订单：</th>
                        <td colspan="3" class="longTd">
                            <input type="text" id="ordTil" class="inputSize2SL inputBoxAlign lockBack" title="此处文本无法编辑，双击可清空文本" ondblClick="clearOrdInput(this)" readonly/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="parent.addDivBrow(9,'${cusInfo.corCode}')">选择</button>
                            <input type="hidden" id="ordId" name="ordId"/>
                        </td>
                    </tr>
                	<tr>
                    	<th class="required">回款金额：<span class='red'>*</span></th>
                        <td><input name="salPaidPast.spsPayMon" id="payMon" type="text" class="inputSize2" onBlur="checkPrice(this)"/></td>
                        <th>付款方式：</th>
                        <td>
                            <select type="text" name="salPaidPast.spsPayType" id="payType" class="inputSize2">
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
                        <th class="required">回款人：<span class='red'>*</span></th>
                        <td>
                        	<input readonly class="inputSize2S lockBack" type="text" id="soUserName" ondblClick="clearInput(this,'seId')" title="此处文本无法编辑，双击可清空文本">&nbsp;
					 		<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
                            <input type="hidden" name="seId" id="seId"/>
                        </td>
                        <th class="required">回款日期：<span class='red'>*</span></th>
                        <td><input name="pastDate" id="pastDate" type="text" class="inputSize2 Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/></td>
                    </tr>
                    <tr>
                    	<th>是否已开票：</th>
                        <td colspan="3"><input type="radio" name="salPaidPast.spsIsinv" id="isInv1" value="0" checked/><label for="isInv1">未开票&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="radio" name="salPaidPast.spsIsinv" id="isInv2" value="1"/><label for="isInv2">已开票</label></td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="3" name="salPaidPast.spsContent" id="spsContent" type="text" onBlur="autoShort(this,200)"></textarea></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                        </td>
                    </tr>
                    <tr>
                        <td class="tipsTd" colspan="4">
                            <div class="tipsLayer">
                                <ul>
                                    <li>选择<span class="impt">对应订单</span>后，<span class="impt">对应客户</span>将不能修改，双击清空<span class="impt">对应订单</span>后可重新选择客户。</li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </tbody>
			</table>
		</form>
    </div>
	</body>
</html>
