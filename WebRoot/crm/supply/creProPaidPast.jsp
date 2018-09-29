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
	<title>创建付款记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	.inputSize2{
			width:120px;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
    
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("pastDate")){
				errStr+="- 未选择付款日期！\n";
			}
			if("${paidPlan}"==""){
				if(isEmpty("payMon")){   
					errStr+="- 未填写付款金额！\n"; 
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
		
		function loadSpo(){
			if(parent.document.getElementById("spoId")!=null){
				$("spoId").value=parent.document.getElementById("spoId").value;
				$("spoUserCode").value=parent.document.getElementById("spoUserCode").value;
				$("spoResp").innerHTML=parent.document.getElementById("spoResp").value;
				$("inName").value=parent.document.getElementById("supName").value;
				$("supId").value=parent.document.getElementById("supId").value;
				var purTitle = parent.document.getElementById("purTil").value;
				if(purTitle.length>60){
					$("purTil").value=purTitle.substring(0,60)+"...";
				}
				else{
					$("purTil").value=purTitle;
				}
			}
		}
		
		function loadPaidPlan(){
			$("pastDate").value="${paidPlan.sppPrmDate}".substring(0,10);
		}
		
    	window.onload=function(){
			if("${paidPlan}"!=""){
				loadPaidPlan();
			}
			loadSpo();
		}
  	</script>
  </head>

<body>
	<div id="containerDiv" class="inputDiv">
		<form id="create" action="salPurAction.do" method="post">
		<input type="hidden" name="op" value="savePaidPast"/>
		<input type="hidden" name="isIfrm" value="1"/>
        	<logic:notEmpty name="paidPlan">
                <input type="hidden" name="planId" value="${paidPlan.sppId}"/>
            </logic:notEmpty>
           <input type="hidden" name="spoId" id="spoId"/>
           <!-- 出账信息 -->
           <input type="hidden" name="purTil" id="purTil"/>
           <input type="hidden" name="inName" id="inName"/>
           <input type="hidden" name="supId" id="supId"/>
		   <input type="hidden" name="spoUserCode" id="spoUserCode"/>
            <!-- 默认设置未开票 -->
            <input type="hidden" name="spoPaidPast.spaIsinv" value="0"/>
			<table class="normal dashTab noBr" style="width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                        <th><span class='red'>*</span>付款日期：</th>
                        <td><input name="pastDate" id="pastDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" ondblClick="clearInput(this)"/></td>
                        <th>付款类别：</th>
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
                                    <option>未添加付款类别</option>
                                </select>
                            </logic:empty>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                    </tr>
                    <tr>
                        <th><span class='red'>*</span>付款金额：</th>
                        <td id="monTd">
                            <logic:empty name="paidPlan">
                                <input name="spoPaidPast.spaPayMon" id="payMon" type="text" class="inputSize2" onblur="checkPrice(this)"/>
                            </logic:empty>
                            <logic:notEmpty name="paidPlan">
                                <input type="text" class="inputSize2" value="<bean:write name='paidPlan' property='sppPayMon' format='0.00'/>" disabled/>
                                <input type="hidden" name="spoPaidPast.spaPayMon" value="<bean:write name='paidPlan' property='sppPayMon' format='0.00'/>"/>
                            </logic:notEmpty>
                        </td>
                        <th>付款方式：</th>
                        <td>
                            <select type="text" name="spoPaidPast.spaPayType" id="payType" class="inputSize2">
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
                        <td><textarea name="spoPaidPast.spaRemark" class="inputSize2" rows="2" onblur="autoShort(this,4000)"></textarea></td>
                        <th>负责账号：</th>
                        <td><span id="spoResp"></span></td>
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
