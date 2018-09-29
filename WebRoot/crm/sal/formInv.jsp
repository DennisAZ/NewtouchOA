<%@ page language="java" pageEncoding="UTF-8"%>
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
	<title>保存开票记录</title>
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
			if(isEmpty("invCon")){   
				errStr+="- 未填写开票内容！\n"; 
			}
			if(isEmpty("ordId")){   
				errStr+="- 未选择订单！\n"; 
			}
			if(isEmpty("invMon")){   
				errStr+="- 未填写金额！\n"; 
			}
			if(isEmpty("invDate")){   
				errStr+="- 未填写开票日期！\n"; 
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
		
		//初始化表单
		function initForm(){
			<logic:empty name="invInfo">
			//新建
			disableInput('invId');
			$("op").value = "saveInvoice";
			$("sinUser").value="${curSeName}";
			if(parent.document.getElementById("ordCode")!=null){//在详情页面下新建，默认填入订单信息，保存成功后刷新iframe
				$("ordId").value = parent.document.getElementById("ordCode").value;
				$("ordTil").value = parent.document.getElementById("ordTil").value;
				$("isIfrm").value="1";
			}
			</logic:empty>
			
			<logic:notEmpty name="invInfo">
			//编辑
			$("op").value = "updInvoice";
			if("${invInfo.salOrdCon.sodCode}"!=""){
				$("ordTil").value = "${invInfo.salOrdCon.sodTil}";
				$("ordId").value = "${invInfo.salOrdCon.sodCode}";
			}
			$("invMon").value="<bean:write name='invInfo' property='sinMon' format='0.00'/>";
			if('${invInfo.sinIsPaid}'=='0'){
				$("isPaid1").checked="true";
			}
			else{
				$("isPaid2").checked="true";
			}
			if("${invInfo.salInvType}"!=""){
				$("invType").value="${invInfo.salInvType.typId}";
			}
			removeTime("invDate","${invInfo.sinDate}",1);
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
	<form id="create" action="invAction.do" method="post">
			<input type="hidden" id="opMethod" name="op"/>
			<input type="hidden" id="isIfrm" name="isIfrm"/>
            <input type="hidden" id="invId" name="invId" value="${invInfo.sinId}"/>
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<thead>
					<tr>
						<th class="descTitleL">开票内容：<span class='red'>*</span></th>
						<th class="descTitleR" colspan="3"><textarea id="invCon" name="salInvoice.sinCon" class="inputSize2L" rows="1" onBlur="autoShort(this,100)">${invInfo.sinCon}</textarea></th>
					</tr>
				</thead>
            	<tbody>
                    <tr>
                    	<th class="required">对应订单：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd">
                            <input type="text" id="ordTil" class="inputSize2SL inputBoxAlign lockBack" title="此处文本无法编辑，双击可清空文本" ondblClick="clearInput(this,'ordId')" readonly/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="parent.addDivBrow(9)">选择</button>
                            <input type="hidden" id="ordId" name="ordId"/>
                        </td>
                    </tr>
                    <tr>
                        <th>票据类别：</th>
                        <td>
                            <logic:notEmpty name="invType">
                            <select name="invType" id="invType" class="inputSize2 inputBoxAlign">
                                <option value="">请选择</option>
                                <logic:iterate id="invType" name="invType">
                                    <option value="${invType.typId}">${invType.typName}</option>
                                </logic:iterate>
                            </select>
                            </logic:notEmpty>
                            <logic:empty name="invType">
                                <select id="sinType" class="inputSize2 inputBoxAlign" disabled>
                                    <option>未添加</option>
                                </select>
                            </logic:empty>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>    
                        </td>
                        <th>票据编号：</th>
                        <td><input name="salInvoice.sinCode" type="text" class="inputSize2"  onblur="autoShort(this,50)" value="${invInfo.sinCode}"/></td>
                    </tr>
                     <tr>
                        <th class="required">金额：<span class='red'>*</span></th>
                        <td>
                            <input id="invMon" name="salInvoice.sinMon" type="text" class="inputSize2" onBlur="checkPrice(this)"/>
                        </td>
                    	<th class="required">开票日期：<span class='red'>*</span></th>
                        <td>
                            <input id="invDate" name="invDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/>
                        </td>
                    </tr>
                    <tr>
                    	<th>开票人：</th>
                        <td><input name="salInvoice.sinResp" id="sinUser" type="text" class="inputSize2" onBlur="autoShort(this,50)" value="${invInfo.sinResp}"/></td>
                        <th>是否已回款：</th>
                        <td>
                            <input type="radio" name="salInvoice.sinIsPaid" id="isPaid1" value="0" checked/><label for="isPaid1">未回款&nbsp;</label><input type="radio" name="salInvoice.sinIsPaid" id="isPaid2" value="1"/><label for="isPaid2">已回款</label>
                       	</td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="3" name="salInvoice.sinRemark" type="text" onBlur="autoShort(this,200)">${invInfo.sinRemark}</textarea></td>
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
