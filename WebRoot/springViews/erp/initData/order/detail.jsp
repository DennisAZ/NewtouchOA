<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("orderId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>销售订单信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath %>/SpringR/saleOrder/getOrder?orderId=<%=id%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){ 
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					  jQuery('#orderTitle').val(item.order_title);
					  jQuery('#custName').val(item.cus_name);
					  jQuery('#custContact').val(item.conName);
					  jQuery('#orderStatus').val(item.order_status);
					  jQuery('#beginTime1').val(item.sign_date);
					  jQuery('#remark1').val(item.remark1);
					  jQuery('#contractId').val(item.contract_id);
					  jQuery('#orderCode1').val(item.order_code);
					  jQuery('#opoTitle').val(item.po_title);
					  jQuery('#opoCode').val(item.po_code);
				      jQuery('#products').val();
					  jQuery('#remark2').val(item.remark2);
				      jQuery('#paidName').val(item.paid_title);
					  jQuery('#paidCode').val(item.paid_code);
					  jQuery('#paidStatus').val(item.paid_status);
					  jQuery('#total2').val(item.paTotal);
					  jQuery('#alreadyTotal').val(item.already_total);
					  jQuery('#salePaid').val(item.sale_paid);
					  jQuery('#remark3').val(item.remark3);
					  jQuery('#conWorkPhone').val(item.conMobile);
					  jQuery('#conPhone').val(item.conPhone);
					  jQuery('#conAddress').val(item.conAddress);
					  jQuery('#conEmail').val(item.conEmail);
					  jQuery('#addName').val(item.person);
					  jQuery('#custAddress').val(item.addrAddress);
					  jQuery('#addPhone').val(item.addrPhone);
					  jQuery('#addZipCode').val(item.zip_code);
					  var tds = "<tr><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.ps_name+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					  tds += "<td align='center' nowrap>"+item.sale_price+"</td>";//单价
					  tds += "<td align='center' nowrap>"+item.order_num+"</td>";//订单数量
					  tds += "<td align='center' nowrap>"+item.proTotal+"</td>";//总额
					  tds += "<td align='center' nowrap>"+item.send_date+"</td></tr>";//发货时间
					
					  jQuery('#pro_table').append(tds);
		  
				});
			}
	   }
	 });


}
	
function returnPage(){
	window.location.href="orderManage.jsp";
}	 
	 
   
 
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	   基本资料（订单基本资料，客户资料，合同资料）
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="70%" align="center">
       <input type="hidden" name="id" id="id" size="20" class="BigInput" >
      <tr>
      <td nowrap class="TableData">订单主题：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="orderTitle" id="orderTitle"  class="BigInput" readOnly/>
      </td>
      <td nowrap class="TableData">订单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="orderCode1" id="orderCode1"  class="BigInput" readOnly />
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">对应客户：</td>
       <td class="TableData"  colspan="2">
       <input type="hidden" name="cusId" id="cusId"  />
 		<input type="text" name="custName" id="custName"  class="BigInput" readOnly/>
      </td>
       <td nowrap class="TableData">联系人：</td>
       <td class="TableData"  colspan="2">
         <input type="hidden" name="contactId" id="contactId"  />
 		<input type="text" name="custContact" id="custContact"   class="BigInput" readOnly/>
      </td>
      </tr>
 		<tr>
     
       <td nowrap class="TableData">联系人电话：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="conWorkPhone" id="conWorkPhone"  class="BigInput" readOnly />
      </td>
       <td nowrap class="TableData">联系人手机：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="conPhone" id="conPhone"  class="BigInput" readOnly />
      </td>
    </tr>
    	<tr>
     
       <td nowrap class="TableData">联系人邮件：</td>
       <td class="TableData" colspan="6">
 		<input type="text" name="conEmail" id="conEmail"  class="BigInput" readOnly/>
      </td>
    </tr>
    
    <tr>
       <td nowrap class="TableData">签约时间：</td>
       <td class="TableData"  colspan="2">
 		      <input type="text" id="beginTime1" name="beginTime1" size="19" maxlength="19" class="BigInput" readOnly>
      </td>
      <td nowrap class="TableData" style="display: none;">订单状态：</td>
       <td class="TableData"  colspan="2" style="display: none;">
 		<input type="text" name="orderStatus" id="orderStatus"  class="BigInput" readOnly/>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">合同信息：</td>
       <td class="TableData" colspan="6">
 		<textarea name="contractId" id="contractId" class="BigInput" cols="70" readOnly></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="remark1" id="remark1" class="BigInput" cols="70" readOnly></textarea>
      </td>
    </tr>
    
    
  </table>
  <hr/>
  <table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	      货单基本资料
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>

 <input type="hidden" id="products" name="products">
<table class="TableBlock" width="100%" align="center">
    <tr>
      <td nowrap class="TableData">货单主题：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="opoTitle" id="opoTitle" class="BigInput"  readOnly/>
      </td>
       <td nowrap class="TableData">货单编号：</td>
         <td class="TableData" colspan="2">
 		<input type="text" name="opoCode" id="opoCode" class="BigInput" readOnly />
      </td>
    </tr>
   
    <tr>
       <td nowrap class="TableData">收货人：</td>
       <td class="TableData" colspan="2">
       <input type="hidden" name="addId" id="addId"  />
 		<input type="text" name="addName" id="addName"  class="BigInput" readOnly/>
      </td>
       <td nowrap class="TableData">收货人邮编：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="addZipCode" id="addZipCode"   class="BigInput" readOnly/>
      </td>
    </tr>
     <tr>
       <td nowrap class="TableData">收货人手机：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="addPhone" id="addPhone"   class="BigInput" readOnly/>
      </td>
       <td nowrap class="TableData">收货地址：</td>
         <td class="TableData" colspan="2">
 		<input type="text" name="custAddress" id="custAddress"  class="BigInput"  readOnly/>
      </td>
     
    </tr>
      <tr>
      <td colspan="7" align="center">
		       <table id="pro_table" align="center" width="80%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>单价</td>
		    	<td align='center' nowrap>数量</td>
		    	<td align='center' nowrap>总额</td>
		    	<td align='center' nowrap>交货日期</td>
		    	</tr>
		    </table>
    </td>
     </tr>
      <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="7">
 		<textarea name="remark2" id="remark2" cols="80" readOnly></textarea>
      </td>
    </tr>
  </table>
	<hr/>
	 <table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	    回款单基本资料
    </span>&nbsp;&nbsp;
    </td>
  </tr>
  
</table>
<br>
	<table class="TableBlock" width="70%" align="center">
      <tr>
      <td nowrap class="TableData">回款单主题：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="paidName" id="paidName" class="BigInput" readOnly />
      </td>
      <td nowrap class="TableData">回款单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="paidCode" id="paidCode" class="BigInput" readOnly  />
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">销售支出：</td>
       <td class="TableData" colspan="6">
 		<input type="text" name="salePaid" id="salePaid" class="BigInput" cols="70" readOnly>
      </td>
       <td nowrap class="TableData" style="display: none;">回款单状态：</td>
       <td class="TableData"  colspan="2" style="display: none;">
 		<input type="text" name="paidStatus" id="paidStatus"  class="BigInput"  readOnly/>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">应收金额：</td>
       <td class="TableData"  colspan="2">
 		<input type="text" name="total2" id="total2"  class="BigInput" readOnly/>
      </td>
       <td nowrap class="TableData">已收金额：</td>
       <td class="TableData"  colspan="2">
 		    <input type="text" name="alreadyTotal" id="alreadyTotal"  class="BigInput" readOnly/>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="remark3" id="remark3" class="BigInput" cols="70" readOnly></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
    
       <input type="button" value="返回" class="BigButton" onclick="returnPage()">
     
      </td>
    </tr>
  </table>
</form>
</body>
</html>