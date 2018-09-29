<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String fid_id = request.getParameter("fid_id");
	String flow = request.getParameter("flow");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>回款记录明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var flow='<%=flow%>';
function doInit(){
	getBankMsg();
	initTime();
	getFIDMsg();
	if(flow == '1'){
		jQuery('#backButton').hide();
		jQuery('#closeButton').show();
		jQuery('#code').attr("readonly","readonly");
	}
}
 function initTime(){
  var beginTimePara = {
      inputId:'paid_date',
      bindToBtn:'pod_dateImg'
  };
  new Calendar(beginTimePara);
}
 function getBankMsg(){
	 var url = "<%=contextPath %>/SpringR/finance/getBankMsg";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					jQuery('#bank_id').append("<option value='"+item.id+"'>"+item.name+"</option>");
				});
			}
	   }
	 });
 }
 
function getFIDMsg(){
	var url = "<%=contextPath %>/SpringR/finance/getFIDMsg?fid_id=<%=fid_id%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");

			jQuery('#fi_code').val(json.code);
			jQuery('#type').val(json.type);
			jQuery('#customer').val(json.customer);
			jQuery('#total').val(json.total);
			jQuery('#already_total').val(json.already_total);
			jQuery('#code').val(json.code);
			jQuery('#bank_id').val(json.bank_id);
			jQuery('#money').val(json.money);
			jQuery('#paid_way').val(json.paid_way);
			jQuery('#paid_date').val(json.paid_date);
			jQuery('#person').val(json.person);
			jQuery('#invoice_id').val(json.invoice_id);
			jQuery('#remark').val(json.remark);
	   }
	 });
}

 function doSubmitForm(formObject) {

	  var code = jQuery('#code').val();
	  var bank_id = jQuery('#bank_id').val();
	  var money = jQuery('#money').val();
	  var paid_way = encodeURIComponent(jQuery('#paid_way').val());
	  var paid_date = jQuery('#paid_date').val();
	  var person = encodeURIComponent(jQuery('#person').val());
	  var invoice_id = encodeURIComponent(jQuery('#invoice_id').val());
	  var remark = encodeURIComponent(jQuery('#remark').val());
	  
	 var para = "fid_id=<%=fid_id%>&code="+code+"&money="+money+"&bank_id="+bank_id
	 		   +"&paid_way="+paid_way+"&paid_date="+paid_date+"&person="+person+"&invoice_id="+invoice_id+"&remark="+remark;

	 var url="<%=contextPath %>/SpringR/finance/updateFID?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("更新成功");
		    window.location.href="fidManage.jsp";
		  }
		  else{
			   alert("更新失败"); 
		  }
	   }
	 });
   }
   
  function checkFloat(id){
	   var megx = /^\d+(\.\d+)?$/;
	   var price = jQuery('#'+id).val();
	   if(!megx.test(price)){
		   alert('请输入正确的数');
		   jQuery('#'+id).val('');
		   return;
	   }
   }

</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	      财务回款记录
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="80%" align="center">
    <tr>
	      <td nowrap class="TableData">应收单编号：</td>
	      <td class="TableData" >
	       <input id="fi_code" name="fi_code" type="text" value="" readonly>
	      </td>
	      <td nowrap class="TableData">应收来源：</td>
	      <td class="TableData">
	      	<select id="type" name="type" disabled="disabled">
					<option value="销售">销售</option>
					<option value="其他">其他</option>
				</select>
	      </td>
	    </tr>
	     <tr>
      <td nowrap class="TableData">应收客户：</td>
      <td class="TableData" colspan='3'>
       <input id="customer" name="customer" type="text" value="" readonly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">应收金额：</td>
      <td class="TableData" >
       <input id="total" name="total" type="text" value="" onblur="checkFloat('total');" readonly>
      </td>
      <td nowrap class="TableData">已收金额：</td>
      <td class="TableData" >
       <input id="already_total" name="already_total" type="text" value="" onblur="checkFloat('already_total');" readonly>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">应收明细编号：</td>
      <td class="TableData" colspan="3">
       <input id="code" name="code" type="text" value="">
      </td>
    </tr>
    
     <tr>
      <td nowrap class="TableData">回款金额：</td>
      <td class="TableData" >
       <input id="money" name="money" type="text" value="" onblur="checkFloat('money');">
      </td>
       <td nowrap class="TableData">回款方式：</td>
      <td class="TableData" >
       <select id="paid_way" name="paid_way">
				<option value="<%=oa.spring.util.StaticData.CHECK%>">支票</option>
				<option value="<%=oa.spring.util.StaticData.CASH%>">现金</option>
				<option value="<%=oa.spring.util.StaticData.POSTAL_REMITTANCE%>">邮政回款</option>
				<option value="<%=oa.spring.util.StaticData.BANK_WIRE_TRANSFER%>">银行电汇</option>
				<option value="<%=oa.spring.util.StaticData.ONLINE_PAYMENT%>">网上支付</option>
				<option value="<%=oa.spring.util.StaticData.ACCEPTANCES%>">承兑汇票</option>
				<option value="<%=oa.spring.util.StaticData.OTHERS%>">其他</option>
			</select>
      </td>
    </tr>
    
     <tr>
     <td nowrap class="TableData">回款操作人：</td>
      <td class="TableData" >
       <input id="person" name="person" type="text" value="" >
      </td>
      <td nowrap class="TableData">回款日期：</td>
      <td class="TableData" >
       <input type="text" id="paid_date" name="paid_date" size="19" maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
     
        <tr>
       <td nowrap class="TableData">现金银行：</td>
      <td class="TableData" >
        <select id="bank_id" name="bank_id">
			</select>
      </td>
	      <td nowrap class="TableData">发票号：</td>
	      <td class="TableData" >
	       <input id="invoice_id" name="invoice_id" type="text" value="" >
	      </td>
    	</tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="remark" name="remark"></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="更新" class="BigButton" onclick="return doSubmitForm(this.form);">
         <input id="backButton" type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
         <input id="closeButton" type="button" value="关闭" class="BigButton" style="display: none" onclick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>