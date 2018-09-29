<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String purchaseId = request.getParameter("purchaseId");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建采购订单信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>

<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">

function doInit(){
	var url = "<%=contextPath %>/SpringR/purchase/getPurchaseById?purchaseId=<%=purchaseId%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){ 
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					  jQuery('#purTitle').val(item.purTitle);
					  jQuery('#purCode').val(item.purCode);
					  jQuery('#userDesc').val(item.purPerson);
					  jQuery('#beginTime1').val(item.purSignDate);
					  jQuery('#purContractId').val(item.purContractId);
					  jQuery('#purStatus').val(item.purStatus);
					  jQuery('#purRemark').val(item.purRemark);
					 //供货商信息
					  jQuery('#supRemark').val(item.supRemark);
					  jQuery('#supContactMan').val(item.supContactName);
					  jQuery('#supName').val(item.supName);
					  jQuery('#supCode').val(item.supCode);
					  jQuery('#supPhone').val(item.supPhone);
					  jQuery('#supMobile').val(item.supMobile);
					  jQuery('#supFex').val(item.supFex);
					  jQuery('#supEmail').val(item.supEmail);
					  jQuery('#supWebsite').val(item.supWebsite);
					  jQuery('#supQq').val(item.supQq);
					  jQuery('#supAddress').val(item.supAddress);
					  jQuery('#supZipCode').val(item.supZipCode);
					  jQuery('#supProducts').val(item.supProducts);
					  jQuery('#supBank').val(item.supBank);
					  jQuery('#supBankName').val(item.supBankName);
					  jQuery('#supBankAccount').val(item.supBankAccount);
					 //货单
					  jQuery('#opoRemark').val(item.purOutRemark);
					  jQuery('#opoTitle').val(item.ppo_title);
					  jQuery('#opoCode').val(item.ppo_code); 
				
					 //出款单
					  jQuery('#paidTitle').val(item.paidTitle); 	   
					  jQuery('#paidCode').val(item.paidCode); 	   
					  jQuery('#paidStatus').val(item.paidStatus); 	   
					  jQuery('#paidTotal').val(item.paidTotal); 	   
					  jQuery('#alreadyTotal').val(item.paidAlreadyTotal); 	   
					  jQuery('#paidRemark').val(item.paidRemark); 	   
	 
	 
					  var tds = "<tr><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.pro_type+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					  tds += "<td align='center' nowrap>"+item.proPrice+"</td>";//单价
					  tds += "<td align='center' nowrap>"+item.proNum+"</td>";//订单数量
					  tds += "<td align='center' nowrap>"+item.proTotal+"</td>";//总额
					  tds += "<td align='center' nowrap>"+item.proDate+"</td></tr>";//发货时间
					
					  jQuery('#pro_table').append(tds);
		  
				});
			}
	   }
	 });


}
	
function returnPage(){
	window.location.href="purchaseManage.jsp";
}	 
	 
 
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	   基本资料（订单基本资料，供货商资料，合同资料）
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="70%" align="center">
      <tr>
      <td nowrap class="TableData">订单主题：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="purTitle" id="purTitle"  class="BigInput" readOnly />
      </td>
      <td nowrap class="TableData">订单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="purCode" id="purCode"  class="BigInput" readOnly />
      </td>
    </tr>
     <tr>
     <td nowrap class="TableData">采购员：</td>
       <td class="TableData" colspan="6">
    	 <input type="hidden" name="user" id="user" value="" />
  <input type="text" name="userDesc" id="userDesc" style="vertical-align: top;" size="10" class="SmallStatic" size="10" value="" READONLY>
  <!--selectUser() 这个函数是多选  -->
   </td>
    </tr>
      <tr>
     
      <td nowrap class="TableData">对应供货商：</td>
       <td class="TableData"  colspan="2">
       <input type="hidden" name="supRemark" id="supRemark"  />
       <input type="hidden" name="suppId" id="suppId"  />
       <input type="hidden" name="supCode" id="supCode"  />
 		<input type="text" name="supName" id="supName"  class="BigInput" readOnly/>
      </td>
        	 <td nowrap class="TableData">联系人：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supContactMan" id="supContactMan"  class="BigInput" readOnly />
      </td>
      </tr>
 		<tr>
     
       <td nowrap class="TableData">电话：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supPhone" id="supPhone"  class="BigInput" readOnly />
      </td>
       <td nowrap class="TableData">手机：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supMobile" id="supMobile"  class="BigInput" readOnly />
      </td>
    </tr>
    <tr>
       <td nowrap class="TableData">传真：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supFex" id="supFex"  class="BigInput" readOnly />
      </td>
     
       <td nowrap class="TableData">电子邮件：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supEmail" id="supEmail"  class="BigInput" readOnly />
      </td>
    </tr>
    <tr>
    <td nowrap class="TableData">QQ：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supQq" id="supQq"  class="BigInput" readOnly />
      </td>
    	 <td nowrap class="TableData">邮编：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supZipCode" id="supZipCode"  class="BigInput" readOnly />
      </td>
     
    </tr>
    <tr>
       <td nowrap class="TableData">地址：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supAddress" id="supAddress"  class="BigInput" readOnly />
      </td>
       <td nowrap class="TableData">网址：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supWebsite" id="supWebsite"  class="BigInput" readOnly />
      </td>
    </tr>
     <tr>
       <td nowrap class="TableData">供货产品：</td>
       <td class="TableData" colspan="6">
 		<input type="text" name="supProducts" id="supProducts"  class="BigInput" readOnly />
 		
      </td>
    </tr>
     <tr>
     
       <td nowrap class="TableData">开户行：</td>
       <td class="TableData" colspan="6">
 		<input type="text" name="supBank" id="supBank"  class="BigInput" readOnly />
      </td>
    </tr>
     <tr>
     
       <td nowrap class="TableData">开户名称：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supBankName" id="supBankName"  class="BigInput" readOnly />
      </td>
       <td nowrap class="TableData">开户账号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="supBankAccount" id="supBankAccount"  class="BigInput" readOnly />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" style="display: none">订单状态：</td>
       <td class="TableData"  colspan="2" style="display: none">
 		<input type="text" name="purStatus" id="purStatus"  class="BigInput" readOnly/>
      </td>
       <td nowrap class="TableData">签约时间：</td>
       <td class="TableData"  colspan="2">
 		      <input type="text" id="beginTime1" name="beginTime1" size="19" maxlength="19" class="BigInput" readOnly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">合同信息：</td>
       <td class="TableData" colspan="6">
 		<textarea name="purContractId" id="purContractId" class="BigInput" cols="70" readOnly></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="purRemark" id="purRemark" class="BigInput" cols="70" readOnly></textarea>
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
 <input type="hidden" id="oldProducts" name="oldProducts">
<table class="TableBlock" width="100%" align="center">
    <tr>
      <td nowrap class="TableData">货单主题：</td>
       <td class="TableData" colspan="2">
       <input type="hidden" name="proId" id="proId" class="BigInput"  />
 		<input type="text" name="opoTitle" id="opoTitle" class="BigInput" readOnly />
      </td>
       <td nowrap class="TableData">货单编号：</td>
         <td class="TableData" colspan="2">
 		<input type="text" name="opoCode" id="opoCode" class="BigInput"  readOnly/>
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
 		<textarea name="opoRemark" id="opoRemark" cols="80" readOnly></textarea>
      </td>
    </tr>
  </table>
	<hr/>
	 <table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	    出款单基本资料
    </span>&nbsp;&nbsp;
    </td>
  </tr>
  
</table>
<br>
	<table class="TableBlock" width="70%" align="center">
      <tr>
      <td nowrap class="TableData">出款单主题：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="paidTitle" id="paidTitle" class="BigInput" readOnly />
      </td>
      <td nowrap class="TableData">出款单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="paidCode" id="paidCode" class="BigInput"  readOnly/>
      </td>
    </tr>
      <tr style="display: none">
       <td nowrap class="TableData">出款单状态：</td>
       <td class="TableData"  colspan="6">
 		<input type="text" name="paidStatus" id="paidStatus"  class="BigInput" readOnly/>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">应付金额：</td>
       <td class="TableData"  colspan="2">
 		<input type="text" name="paidTotal" id="paidTotal"  class="BigInput" readOnly/>
      </td>
       <td nowrap class="TableData">已付金额：</td>
       <td class="TableData"  colspan="2">
 		    <input type="text" name="alreadyTotal" id="alreadyTotal"  class="BigInput" readOnly/>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name=""paidRemark"" id="paidRemark" class="BigInput" cols="70" readOnly></textarea>
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