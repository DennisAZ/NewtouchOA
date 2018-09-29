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
	<title>修改付款记录</title>
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
				alert("请填写付款日期!");
				return false;
			}
			
			if($("payMon").value ==""||$("payMon")==null){
				alert("请填写付款金额!");
				return false;
			}
			waitSubmit("dosave","保存中...");
			waitSubmit("doCancel");
			return $("mod").submit();
		}
		
		function init(){
			$("pastDate").value="${paid.spaFctDate}".substring(0,10);
			if($("paidTypeId")!=null){
				if("${paid.typeList}"!=null)
					$("paidTypeId").value="${paid.typeList.typId}";
				else
					$("paidTypeId").value="";
			}
			$("payType").value="${paid.spaPayType}";
			
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
		<form id="mod" action="salPurAction.do" method="post">
			<input type="hidden" name="op" value="modPaidPast"/>
            <input type="hidden" name="isIfrm" value="1"/>
            <!-- 原付款记录信息 -->
            <input type="hidden" name="paidId" value="${paid.spaId}"/>
            <input type="hidden" name="spoPaidPast.spsIsinv" value="${paid.spaIsinv}"/>
			<table class="normal dashTab noBr" style=" width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                        <th><span class='red'>*</span>付款日期：</th>
                        <td><input name="pastDate" id="pastDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" ondblClick="clearInput(this)"/></td>
                        <th>付款类别：</th>
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
                                    <option>未添加付款类别</option>
                                </select>
                            </logic:empty>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                       
                    </tr>
                    <tr>
                        <th><span class='red'>*</span>付款金额：</th>
                        <td id="monTd"><input name="spoPaidPast.spaPayMon" id="payMon" type="text" class="inputSize2" onblur="checkPrice(this)" value="<bean:write name='paid' property='spaPayMon' format='0.00'/>"/></td>
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
                        <td><textarea name="spoPaidPast.spaRemark" class="inputSize2" rows="1" onblur="autoShort(this,4000)">${paid.spaRemark}</textarea></td>
                        <th>负责账号：</th>
                        <td>${paid.limUser.userSeName}&nbsp;
                        </td>
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
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该付款记录已被删除</div>
	  </logic:empty>
    </div>
	</body>
</html>
