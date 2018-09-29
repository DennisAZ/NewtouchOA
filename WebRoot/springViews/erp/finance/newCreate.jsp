<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String pp_id = request.getParameter("pp_id");
	String order_id = request.getParameter("order_id");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增财务回款单信息</title>
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

function doInit(){
	getCusMsg();
	initTime();
	getBankMsg();
	var url = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE11';
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
	var url = "<%=contextPath %>/SpringR/finance/getCusMsg?order_id=<%=order_id%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			jQuery('#order_title').val(json.order_title);
			jQuery('#cus_name').val(json.cus_name);
			jQuery('#cus_id').val(json.id);
			jQuery('#pp_total').val(json.total);
			jQuery('#pp_already_total').val(json.already_total);
	   }
	 });
}



   function doSubmitForm(formObject) {

	  var ppd_code = jQuery('#ppd_code').val();
	  var ppd_status = jQuery('#ppd_status').val();
	  var cus_id = jQuery('#cus_id').val();
	  var cus_name = encodeURIComponent(jQuery('#cus_name').val());
	  var bank_id = jQuery('#bank_id').val();
	  var total = jQuery('#total').val();
	  var paid_way = encodeURIComponent(jQuery('#paid_way').val());
	  var paid_date = jQuery('#paid_date').val();
	  var person = encodeURIComponent(jQuery('#person').val());
	  var invoice_id = encodeURIComponent(jQuery('#invoice_id').val());
	  var remark = encodeURIComponent(jQuery('#remark').val());
	  
	 var para = "order_id=<%=order_id%>&pp_id=<%=pp_id%>&ppd_code="+ppd_code+"&bank_id="+bank_id
	 		   +"&ppd_status="+ppd_status+"&cus_id="+cus_id+"&cus_name="+cus_name+"&total="+total
	 		   +"&paid_way="+paid_way+"&paid_date="+paid_date+"&person="+person+"&invoice_id="+invoice_id+"&remark="+remark;

	 var url="<%=contextPath %>/SpringR/finance/addPPD?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="ppdShowDetial.jsp";
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
	       新增财务回款记录
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="cus_id" name="cus_id">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">对应订单：</td>
      <td class="TableData" >
       <input id="order_title" name="order_title" type="text" value="" readOnly>
      </td>
      <td nowrap class="TableData">对应客户：</td>
      <td class="TableData" >
       <input id="cus_id" name="cus_id" type="hidden" value="" >
       <input id="cus_name" name="cus_name" type="text" value="" readOnly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">应收金额：</td>
      <td class="TableData" >
       <input id="pp_total" name="pp_total" type="text" value="" readOnly>
      </td>
      <td nowrap class="TableData">已收金额：</td>
      <td class="TableData" >
       <input id="pp_already_total" name="pp_already_total" type="text" value="" readOnly>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">回款记录编号：</td>
      <td class="TableData" >
       <input id="ppd_code" name="ppd_code" type="text" value="">
      </td>
      <td nowrap class="TableData" style="display: none">回款记录状态：</td>
      <td class="TableData" style="display: none">
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
      <td nowrap class="TableData">回款金额：</td>
      <td class="TableData" >
       <input id="total" name="total" type="text" value="" onblur="checkFloat('total');">
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
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>