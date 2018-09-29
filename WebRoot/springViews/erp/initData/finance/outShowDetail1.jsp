<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String pp_id = request.getParameter("pp_id");
	String fId = request.getParameter("fId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增财务出款单信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var fId="<%=fId%>";
function doInit(){
	getCusMsg();
}

function getCusMsg(){
	var url = "<%=contextPath %>/SpringR/finance/getOut?fId="+fId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			jQuery('#supName').val(json.fPayee);
			jQuery('#type').val(json.fType);
			jQuery('#total').val(json.paid_total);
			jQuery('#outAlreadyTotal').val(json.already_paid_total);
			jQuery('#bank').val(json.bank_name);
			jQuery('#open_name').val(json.open_name);
			jQuery('#account').val(json.account);
			jQuery('#fCode').val(json.fCode);
			jQuery('#remark').val(json.fRemark);
	   }
	 });
}



</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增财务出款记录
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="cus_id" name="cus_id">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">订单编号：</td>
      <td class="TableData" >
       <input id="fCode" name="fCode" type="text" value="" class="BigInput" readonly="readonly" >
      </td>
     <td nowrap class="TableData">应付原因：</td>
	      <td class="TableData">
	      	<select id="type" name="type">
					<option value="采购">采购</option>
					<option value="其他">其他</option>
				</select>
	      </td>
      
    </tr>
    <tr>
    <td nowrap class="TableData">收款人：</td>
      <td class="TableData"  colspan="3">
       <input id="supId" name="supId" type="hidden" value="" >
       <input id="supName" name="supName" type="text" value="" class="BigInput" >
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">应付金额：</td>
      <td class="TableData" >
       <input id="total" name="total" type="text" value="" class="BigInput"  readonly>
      </td>
      <td nowrap class="TableData">已付金额：</td>
      <td class="TableData" >
       <input id="outAlreadyTotal" name="outAlreadyTotal" type="text" value="" class="BigInput"  readonly>
      </td>
    </tr>
     <tr>
       <td nowrap class="TableData">开户银行：</td>
	      <td class="TableData" colspan="2" >
	       <input id="bank" name="bank" type="text" value=""  class="BigInput" readonly>
	      </td>
    </tr>
    <tr>
       <td nowrap class="TableData">开户名称：</td>
	      <td class="TableData" >
	       <input id="open_name" name="open_name" type="text" value="" class="BigInput"  readonly>
	      </td>
    	   <td nowrap class="TableData">卡号：</td>
	      <td class="TableData" >
	       <input id="account" name="account" type="text" value=""  class="BigInput" readonly>
	      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="remark" name="remark" class="BigInput"></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
         <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>