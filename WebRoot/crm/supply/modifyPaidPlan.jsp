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
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<title>修改付款计划</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
		function check(){
			var errStr = "";
		    if(isEmpty("sppContent")){   
				errStr+="- 未填写摘要！\n"; 
			 }
			 if(isEmpty("purId")){
					errStr+="- 未选择采购单！\n";
		     }
			if(isEmpty("planDate")){
				errStr+="- 未选择预计付款日期！\n";
			}
			if(isEmpty("payMon")){   
				errStr+="- 未填写付款金额！\n"; 
			}
			 
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("mod").submit();
			}
		}
		
		function init(){
			$("planDate").value="${paid.sppPrmDate}".substring(0,10);
			if(${paid.sppIsp}==0)
				$("isPaid1").checked="true";
			else
				$("isPaid2").checked="true";
			//在详情页面下，保存成功后刷新iframe
			if(parent.document.getElementById("spoId")!=null){
			    $("isIfrm").value="1";
		    } 
		}
		
    	window.onload=function(){
			if('${paid}'!=null&&'${paid}'!=""){
				init();
			}
		}
	</script>
</head>

<body>
	<logic:notEmpty name="paid">
	<div class="inputDiv">
		<form id="mod" action="salPurAction.do" method="post">
			<input type="hidden" name="op" value="modPaidPlan"/>
            <input type="hidden" id="isIfrm" name="isIfrm"/>
            <!-- 原付款计划信息 -->
            <input type="hidden" name="paidId" value="${paid.sppId}"/>
            
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<thead>
                    <tr>
                        <th class="descTitleL">摘要：<span class='red'>*</span></th>
                        <th class="descTitleR" colspan="3"><textarea rows="3"  name="spoPaidPlan.sppContent" id="sppContent" type="text" class="inputSize2L" onBlur="autoShort(this,100)">${paid.sppContent}</textarea></th>
                    </tr>
                </thead>
            	<tbody>
            	    <tr>
                        <th class="required">对应采购单：<span class='red'>*</span></th>
                        <td>
                        <input type="text" id="purTil" class="inputSize2S lockBack" title="此处文本无法编辑，双击可清空文本" ondblClick="clearInput(this,'purId')" value="${paid.salPurOrd.spoTil}" readonly/>&nbsp;<button class="butSize2" onClick="parent.addDivBrow(10)">选择</button>
                        <input type="hidden" id="purId" name="purId" value="${paid.salPurOrd.spoId}"/>
                        </td>
                        <th>负责账号：</th>
                        <td>${paid.limUser.userSeName}&nbsp;</td>
                    </tr>
                     <tr>
                        <th>是否已付款：</th>
                        <td colspan="3" class="longTd"><input type="radio" id="isPaid1" name="spoPaidPlan.sppIsp" class="inputBoxAlign" value="0"/><label for="isPaid1">未付款&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="radio" id="isPaid2" name="spoPaidPlan.sppIsp" class="inputBoxAlign" value="1"/><label for="isPaid2">已付款</label></td>
                    </tr>
                	<tr class="noBorderBot">
                    	<th class="required">付款金额：<span class='red'>*</span></th>
                        <td><input name="spoPaidPlan.sppPayMon" id="payMon" type="text" class="inputSize2" value="<bean:write name='paid' property='sppPayMon' format='0.00'/>"  onblur="checkPrice(this)"/></td>
                        <th class="required">预计付款日期：<span class='red'>*</span></th>
                        <td><input name="planDate" id="planDate" type="text" class="inputSize2 Wdate" style="cursor:hand; "
                                 readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" /></td>
                    </tr>
                </tbody>
			</table>
		</form>
    </div>
	 </logic:notEmpty>
	 <logic:empty name="paid">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该付款计划已被删除</div>
	  </logic:empty>
	</body>
</html>
