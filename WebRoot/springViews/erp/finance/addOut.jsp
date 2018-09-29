<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String pp_id = request.getParameter("pp_id");
	String order_id = request.getParameter("order_id");
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

function doInit(){
	var url = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE12';
	getAutoCode(url,"outCode");
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


   function doSubmitForm(formObject) {

	  var outCode = encodeURIComponent(jQuery('#outCode').val());
	  var type = encodeURIComponent(jQuery('#type').val());
	  var total = jQuery('#total').val();
	  var supName = encodeURIComponent(jQuery('#supName').val());
	  var account = jQuery('#account').val();
	  var outAlreadyTotal = jQuery('#outAlreadyTotal').val();
	  var bank = encodeURIComponent(jQuery('#bank').val());
	  var bankName = encodeURIComponent(jQuery('#bankName').val());
	  var remark = encodeURIComponent(jQuery('#remark').val());
	 var para = "outCode="+outCode
	 		   +"&type="+type
	 		   +"&supName="+supName
	 		   +"&account="+account+"&bankName="+bankName+"&bank="+bank+"&total="+total+"&outAlreadyTotal="+outAlreadyTotal
	 		   +"&remark="+remark;

	 var url="<%=contextPath %>/SpringR/finance/addOut?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="moneyOutManage.jsp";
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
   function diogShow(){
  	 var url="<%=contextPath%>/springViews/erp/finance/suppInfo.jsp";
 
 	var str=openDialogResize(url,800,600);
 	
  	   if(str!=null && str!=""){
  	  	var arrStr=str.split(",");
  	   	jQuery('#supName').val(arrStr[3]);
  	  
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
      <td nowrap class="TableData">应付编号：</td>
      <td class="TableData" >
       <input id="outCode" name="outCode" type="text" value="" class="BigInput" >
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
 		<input type="text" name="supName" id="supName"  class="BigInput" readOnly/>
      	<input type="button" name="supp" id="supp"  value="选择供货商" onclick="diogShow()" class="BigButton" /><span style="color:red">[如果没有您选择的供货商，请手动输入！]</span>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">应付金额：</td>
      <td class="TableData" >
       <input id="total" name="total" type="text" value="" class="BigInput" >
      </td>
      <td nowrap class="TableData">已付金额：</td>
      <td class="TableData" >
       <input id="outAlreadyTotal" name="outAlreadyTotal" type="text" value="0" class="BigInput" >
      </td>
    </tr>
     <tr>
       <td nowrap class="TableData">开户银行：</td>
	      <td class="TableData" colspan="3" >
	       <input id="bank" name="bank" type="text" value=""  class="BigInput">
	      </td>
    </tr>
    <tr>
       <td nowrap class="TableData">开户名称：</td>
	      <td class="TableData" >
	       <input id="bankName" name="bankName" type="text" value="" class="BigInput" >
	      </td>
    	   <td nowrap class="TableData">卡号：</td>
	      <td class="TableData" >
	       <input id="account" name="account" type="text" value=""  class="BigInput">
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
      </td>
    </tr>
  </table>
</form>
</body>
</html>