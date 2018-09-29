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
	<title>创建回款记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	.inputSize2{
			width:120px;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script> 
    
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("pastDate")){
				errStr+="- 未选择回款日期！\n";
			}
			if("${paidPlan}"==""){
				if(isEmpty("payMon")){   
					errStr+="- 未填写回款金额！\n"; 
				}
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
		
		function loadOrdCon(){
			if(parent.document.getElementById("ordCode")!=null){
				$("ordCode").value=parent.document.getElementById("ordCode").value;
				$("ordResp").innerHTML=parent.document.getElementById("ordResp").value;
				$("ordOwner").value=parent.document.getElementById("ordOwner").value;
				$("ordCusName").value=parent.document.getElementById("ordCusName").value;
				var ordTitle = parent.document.getElementById("ordTil").value;
				if(ordTitle.length>60){
					$("ordTil").value=ordTitle.substring(0,60)+"...";
				}
				else{
					$("ordTil").value=ordTitle;
				}
			}
		}
		
		function loadPaidPlan(){
			if("${paidPlan.spdPrmDate}".substring(0,10)>"${TODAY}"){
				$("pastDate").value="${TODAY}";
			}
			else{
				$("pastDate").value="${paidPlan.spdPrmDate}".substring(0,10);
			}
		}
		
    	window.onload=function(){
			if("${paidPlan}"!=""){
				loadPaidPlan();
			}
			else{
				$("pastDate").value='${TODAY}'.substring(0,10);
			}
			loadOrdCon();
		}
  	</script>
  </head>

<body>
	<div id="containerDiv" class="inputDiv">
		<form id="create" action="paidAction.do" method="post">
			<input type="hidden" name="isIfrm" value="1"/>
        	<logic:notEmpty name="paidPlan">
            	<input type="hidden" name="op" value="completePaid"/>
                <input type="hidden" name="paidId" value="${paidPlan.spdId}"/>
            </logic:notEmpty>
            <logic:empty name="paidPlan">
				<input type="hidden" name="op" value="savePaidPast"/>
            </logic:empty>
            <input type="hidden" name="ordCode" id="ordCode"/>
            <input type="hidden" name="ordTil" id="ordTil"/>
            <input type="hidden" name="outName" id="ordCusName"/>
            <!-- 负责账号 -->
            <input type="hidden" name="ordOwner" id="ordOwner"/>
            <!-- 默认设置未开票 -->
            <input type="hidden" name="salPaidPast.spsIsinv" value="0"/>
			<table class="normal dashTab noBr" style="width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                        <th><span class='red'>*</span>回款日期：</th>
                        <td><input name="pastDate" id="pastDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" ondblClick="clearInput(this)"/></td>
                        <th>回款类别：</th>
                        <td>
                            <logic:notEmpty name="paidType">
                            <select name="paidTypeId" id="paidTypeId" class="inputSize2 inputBoxAlign">
                                <option value="">请选择</option>
                                <logic:iterate id="paidType" name="paidType">
                                    <option value="${paidType.typId}">${paidType.typName}</option>
                                </logic:iterate>
                            </select>
                            </logic:notEmpty>
                            <logic:empty name="paidType">
                                <select id="paidTypeId" class="inputSize2 inputBoxAlign" disabled>
                                    <option>未添加</option>
                                </select>
                            </logic:empty>
                            <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                    </tr>
                    <tr>
                        <th><span class='red'>*</span>回款金额：</th>
                        <td id="monTd">
                            <logic:empty name="paidPlan">
                                <input name="salPaidPast.spsPayMon" id="payMon" type="text" class="inputSize2" onBlur="checkPrice(this)"/>
                            </logic:empty>
                            <logic:notEmpty name="paidPlan">
                                <input type="text" class="inputSize2" value="<bean:write name='paidPlan' property='spdPayMon' format='0.00'/>" disabled/>
                                <input type="hidden" name="salPaidPast.spsPayMon" value="<bean:write name='paidPlan' property='spdPayMon' format='0.00'/>"/>
                            </logic:notEmpty>
                        </td>
                        <th>付款方式：</th>
                        <td>
                            <select type="text" name="salPaidPast.spsPayType" id="payType" class="inputSize2">
                                <option value="">请选择</option>
                                <option value="支票">支票</option>
                                <option value="现金">现金</option>
                                <option value="邮政汇款">邮政汇款</option>
                                <option value="银行电汇">银行电汇</option>
                                <option value="网上支付">网上支付</option>
                                <option value="其他">其他</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>备注：</th>
                        <td><textarea name="salPaidPast.spsRemark" class="inputSize2" rows="2" onBlur="autoShort(this,4000)"></textarea></td>
                        <th>负责账号：</th>
                        <td><span id="ordResp"></span></td>
                    </tr>
                    <tr>
                        <td colspan="4" style="border:0px; text-align:center">
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
