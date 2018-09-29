<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String fi_id = request.getParameter("fi_id");
	String flow = request.getParameter("flow");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>更新财务应收单</title>
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
var flow='<%=flow%>';
function doInit(){
	initTime();
	getFinanceInMsg();
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
 function getFinanceInMsg(){
	var url = "<%=contextPath %>/SpringR/finance/getFinanceInsMsg?fi_id=<%=fi_id%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			jQuery('#code').val(json.code);
			jQuery('#type').val(json.type);
			jQuery('#customer').val(json.customer);
			jQuery('#total').val(json.total);
			jQuery('#already_total').val(json.already_total);
			jQuery('#remark').val(json.remark);
	   }
	 });
}
 
   function doSubmitForm(formObject) {

	  var code = jQuery('#code').val();
	  var type = encodeURIComponent(jQuery('#type').val());
	  var customer = encodeURIComponent(jQuery('#customer').val());
	  var total = jQuery('#total').val();
	  var already_total = jQuery('#already_total').val();
	  var remark = encodeURIComponent(jQuery('#remark').val());
	  
	 var para = "fi_id=<%=fi_id%>&code="+code+"&type="+type+"&customer="+customer+"&total="+total+"&already_total="+already_total+"&remark="+remark;

	 var url="<%=contextPath %>/SpringR/finance/updateFinanceIn?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("更新成功");
		    window.location.href="financeInShowDetial.jsp";
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
	       更新财务应收单
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="cus_id" name="cus_id">
<table class="TableBlock" width="80%" align="center">
	<tr>
	      <td nowrap class="TableData">应收单编号：</td>
	      <td class="TableData" >
	       <input id="code" name="code" type="text" value="">
	      </td>
	      <td nowrap class="TableData">应收来源：</td>
	      <td class="TableData">
	      	<select id="type" name="type">
					<option value="销售">销售</option>
					<option value="其他">其他</option>
				</select>
	      </td>
	    </tr>
	     <tr>
      <td nowrap class="TableData">应收客户：</td>
      <td class="TableData" colspan='3'>
       <input id="customer" name="customer" type="text" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">应收金额：</td>
      <td class="TableData" >
       <input id="total" name="total" type="text" value="" onblur="checkFloat('total');">
      </td>
      <td nowrap class="TableData">已收金额：</td>
      <td class="TableData" >
       <input id="already_total" name="already_total" type="text" value="" onblur="checkFloat('already_total');">
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