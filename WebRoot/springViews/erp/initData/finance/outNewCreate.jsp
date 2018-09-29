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
	initTime();
	getBankMsg();
	var url = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE13';
	getAutoCode(url,"ppd_code");
}
function getAutoCode(url,id){
	jQuery.ajax({
		type:'POST',
		url:url,
		success:function(data){
			jQuery('#'+id).val(data);
		}
	});
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
			jQuery('#total').val(json.paid_total);
			jQuery('#outAlreadyTotal').val(json.already_paid_total);
			jQuery('#bank').val(json.bank_name);
			jQuery('#open_name').val(json.open_name);
			jQuery('#account').val(json.account);
			jQuery('#fCode').val(json.fCode);
	   }
	 });
}



   function doSubmitForm(formObject) {

	  var fCode = jQuery('#fCode').val();
	  var total = jQuery('#total').val();
	  var supName = encodeURIComponent(jQuery('#supName').val());
	  var bank_id = jQuery('#bank_id').val();
	  var account = jQuery('#account').val();
	  var outAlreadyTotal = jQuery('#outAlreadyTotal').val();
	  var bank = encodeURIComponent(jQuery('#bank').val());
	  var bankName = encodeURIComponent(jQuery('#bankName').val());
	  var ppd_code = encodeURIComponent(jQuery('#ppd_code').val());
	  var paid_date = jQuery('#paid_date').val();
	  var ppd_status = jQuery('#ppd_status').val();
	  var outTotal = jQuery('#outTotal').val();
	  var paid_way = encodeURIComponent(jQuery('#paid_way').val());
	  var person = encodeURIComponent(jQuery('#person').val());
	  var invoice_id = encodeURIComponent(jQuery('#invoice_id').val());
	  var remark = encodeURIComponent(jQuery('#remark').val());
	  
	 var para = "fCode="+fCode+"&fId="+fId+"&ppd_code="+ppd_code+"&bank_id="+bank_id
	 		   +"&ppd_status="+ppd_status+"&outTotal="+outTotal+"&supName="+supName
	 		   +"&account="+account+"&bankName="+bankName+"&bank="+bank+"&total="+total+"&outAlreadyTotal="+outAlreadyTotal
	 		   +"&paid_way="+paid_way+"&paid_date="+paid_date+"&person="+person+"&invoice_id="+invoice_id+"&remark="+remark;

	 var url="<%=contextPath %>/SpringR/finance/addOutDetail?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="outShowDetail.jsp";
		  }
		  else{
			   alert("添加失败"); 
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
       <input id="fCode" name="fCode" type="text" value="" class="BigInput" >
      </td>
      <td nowrap class="TableData">收款人：</td>
      <td class="TableData" >
       <input id="supId" name="supId" type="hidden" value="" >
       <input id="supName" name="supName" type="text" value="" class="BigInput" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">应付金额：</td>
      <td class="TableData" >
       <input id="total" name="total" type="text" value="" class="BigInput" >
      </td>
      <td nowrap class="TableData">已付金额：</td>
      <td class="TableData" >
       <input id="outAlreadyTotal" name="outAlreadyTotal" type="text" value="" class="BigInput" >
      </td>
    </tr>
     <tr>
       <td nowrap class="TableData">开户银行：</td>
	      <td class="TableData" colspan="2" >
	       <input id="bank" name="bank" type="text" value=""  class="BigInput">
	      </td>
    </tr>
    <tr>
       <td nowrap class="TableData">开户名称：</td>
	      <td class="TableData" >
	       <input id="open_name" name="open_name" type="text" value="" class="BigInput" >
	      </td>
    	   <td nowrap class="TableData">卡号：</td>
	      <td class="TableData" >
	       <input id="account" name="account" type="text" value=""  class="BigInput">
	      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">出款记录编号：</td>
      <td class="TableData" >
       <input id="ppd_code" name="ppd_code" type="text" value="" class="BigInput">
      </td>
      <td nowrap class="TableData" style="display: none">出款记录状态：</td>
      <td class="TableData" style="display: none" class="BigInput">
      	<select id="ppd_status" name="ppd_status">
				<option value="<%=oa.spring.util.StaticData.NEW_CREATE%>">新建</option>
				<option value="<%=oa.spring.util.StaticData.AUDITING%>">审核中</option>
				<option value="<%=oa.spring.util.StaticData.PASSING%>">审核通过</option>
				<option value="<%=oa.spring.util.StaticData.NO_PASSING%>">审核没通过</option>
				<option value="<%=oa.spring.util.StaticData.RUNNING%>">执行中</option>
				<option value="<%=oa.spring.util.StaticData.OVER%>">已完成</option>
			</select>
      </td>
    </tr>
    
     <tr>
      <td nowrap class="TableData">出款金额：</td>
      <td class="TableData" >
       <input id="outTotal" name="outTotal" type="text" value="" onblur="checkFloat('total');" class="BigInput">
      </td>
       <td nowrap class="TableData">出款方式：</td>
      <td class="TableData" >
       <select id="paid_way" name="paid_way" class="BigInput">
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
     <td nowrap class="TableData">出款操作人：</td>
      <td class="TableData" >
       <input id="person" name="person" type="text" value=""  class="BigInput">
      </td>
      <td nowrap class="TableData">出款日期：</td>
      <td class="TableData" >
       <input type="text" id="paid_date" name="paid_date" size="19" maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
     
       <tr>
       <td nowrap class="TableData">现金银行：</td>
      <td class="TableData" >
        <select id="bank_id" name="bank_id" class="BigInput">
			</select>
      </td>
	      <td nowrap class="TableData">发票号：</td>
	      <td class="TableData" >
	       <input id="invoice_id" name="invoice_id" type="text" value=""  class="BigInput">
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
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
         <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>