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
	<title>修改回款记录</title>
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
			if($("pastDate").value==""||$("pastDate")==null){
				alert("请填写回款日期!");
				return false;
			}
			if("${paidPlan}"==""){
				if($("payMon").value ==""||$("payMon")==null){
					alert("请填写回款金额!");
					return false;
				}
				/*if($("spsCount").value ==""||$("spsCount")==null){
					alert("请填写回款期次!");
					return false;
				}
				else if(!isint($("spsCount").value)){
					alert("回款期次请填写整数!");
					return false;
				}*/
			}
			waitSubmit("dosave","保存中...");
			waitSubmit("doCancel");
			return $("mod").submit();
		}
		
		function init(){
			$("pastDate").value="${paid.spsFctDate}".substring(0,10);
			if($("paidTypeId")!=null){
				if("${paid.salPaidType}"!=null)
					$("paidTypeId").value="${paid.salPaidType.typId}";
				else
					$("paidTypeId").value="";
			}
			$("payType").value="${paid.spsPayType}";
			
			/*if(${paid.spsIsinv}==0)
				$("isInv1").checked="true";
			else
				$("isInv2").checked="true";*/
		}
		
    	window.onload=function(){
			if('${paid}'!=null&&'${paid}'!=''){
				init();
			}
		}
  	</script>
  </head>

<body>
	<div id="containerDiv" class="inputDiv">
    	<logic:notEmpty name="paid">
		<form id="mod" action="paidAction.do" method="post">
			<input type="hidden" name="isIfrm" value="1"/>
			<input type="hidden" name="op" value="modPaidPast"/>
            
            <!-- 原回款计划信息 -->
            <input type="hidden" name="paidId" value="${paid.spsId}"/>
            <input type="hidden" name="salPaidPast.spsIsinv" value="0"/>
			<table class="normal dashTab noBr" style="width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                        <th><span class='red'>*</span>回款日期：</th>
                        <td><input name="pastDate" id="pastDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" ondblClick="clearInput(this)"/></td>
                        <th>回款类别：</th>
                        <td>
                            <logic:notEmpty name="paidType">
                            <select id="paidTypeId" name="paidTypeId" class="inputSize2 inputBoxAlign">
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
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                       
                    </tr>
                    <tr>
                        <th><span class='red'>*</span>回款金额：</th>
                        <td id="monTd"><input name="salPaidPast.spsPayMon" id="payMon" type="text" class="inputSize2" onBlur="checkPrice(this)" value="<bean:write name='paid' property='spsPayMon' format='0.00'/>"/></td>
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
                        <td><textarea name="salPaidPast.spsRemark" class="inputSize2" rows="1" onBlur="autoShort(this,4000)">${paid.spsRemark}</textarea></td>
                        <th>负责账号：</th>
                        <td>${paid.spsResp.userSeName}&nbsp;</td>
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
        </logic:notEmpty>
	   <logic:empty name="paid">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该回款记录已被删除</div>
	  </logic:empty>
    </div>
	</body>
</html>
