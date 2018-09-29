<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("orderId")==null?"":request.getParameter("orderId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建订单信息</title>
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
	showPpo();
	editDetail();
}

function editDetail(){
	var url = "<%=contextPath %>/SpringR/saleOrder/getOrder?orderId=<%=id%>";
	var po_id = "";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){ 
			var data = JSON.stringify(jsonData);
			var item = eval("(" + data + ")");
		    jQuery('#orderTitle').val(item.order_title);
            jQuery('#orderCode1').val(item.order_code);
			jQuery('#cusId').val(item.cus_id);
			jQuery('#custName').val(item.cus_name);
			jQuery('#contactId').val(item.con_id);
			jQuery('#custContact').val(item.name);
			jQuery('#conWorkPhone').val(item.phone);
			jQuery('#conPhone').val(item.mobile);
			jQuery('#conEmail').val(item.email);
			jQuery('#beginTime1').val(item.sign_date);
			jQuery('#contractId').val(item.contract_id);
			getCodeById(item.contract_id);
			jQuery('#remark1').val(item.saleRemark);
			
			po_id = item.po_id;
			jQuery('#po_id').val(item.po_id);
			jQuery('#opoTitle').val(item.po_title);
			jQuery('#opoCode').val(item.po_code);
			jQuery('#addId').val(item.cad_id);
			jQuery('#addName').val(item.person);
			jQuery('#addZipCode').val(item.zip_code);
			jQuery('#addPhone').val(item.addrPhone);
			jQuery('#custAddress').val(item.address);
			jQuery('#remark2').val(item.poRemark);
			
			jQuery('#paidName').val(item.paid_title);
			jQuery('#paidCode').val(item.paid_code);
            jQuery('#salePaid').val(item.sale_paid);
			jQuery('#total2').val(item.total);
			jQuery('#alreadyTotal').val(item.already_total);
			jQuery('#remark3').val(item.ppRemark);
	   }
	 });

	url = "<%=contextPath %>/SpringR/saleOrder/getPoPro?po_id="+po_id;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){ 
	   		var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				var ids="";
				jQuery(json).each(function(i,item){
					if (ids != "") {
							ids += ",";
						}
						ids += item.pro_id;
						var tds = "<tr od='old"+item.productId+"'><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
						tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
						tds += "<td align='center' nowrap>"+item.pro_type+"</td>";//规格型号
						tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
						tds += "<td align='center' nowrap>"+item.sale_price+"</td>";//单价
						tds += "<td align='center' nowrap>"+item.order_num+"</td>";//数量
						tds += "<td align='center' nowrap>"+item.total+"</td>";//总额
						tds += "<td align='center' nowrap>"+item.send_date+"</td>";//总额
						tds += "</tr>";
						jQuery('#pro_table').append(tds);
				});
			}
	  }
	 });
}
 function showPpo(){
 		jQuery('#one').show();
 		jQuery('#two').show();
 		jQuery('#oneClose').show();
 		jQuery('#oneOpen').hide();
 		jQuery('#twoOpen').hide();
 		jQuery('#twoClose').show();
		jQuery('#oneTitleOpen').hide();
 	jQuery('#oneClose').click(function(){
 		jQuery('#one').hide();
 		jQuery('#oneClose').hide();
 		jQuery('#oneOpen').show();
 	});
 	jQuery('#oneOpen').click(function(){
 		jQuery('#one').show();
 		jQuery('#oneOpen').hide();
 		jQuery('#oneClose').show();
 	});
 	jQuery('#twoClose').click(function(){
 		jQuery('#two').hide();
 		jQuery('#twoClose').hide();
 		jQuery('#twoOpen').show();
 	});
 	jQuery('#twoOpen').click(function(){
 		jQuery('#two').show();
 		jQuery('#twoOpen').hide();
 		jQuery('#twoClose').show();
 	});
 	jQuery('#oneTitleClose').click(function(){
 		jQuery('#oneTitle').hide();
 		jQuery('#oneTitleClose').hide();
 		jQuery('#oneTitleOpen').show();
 	});
 	jQuery('#oneTitleOpen').click(function(){
 		jQuery('#oneTitle').show();
 		jQuery('#oneTitleClose').show();
 		jQuery('#oneTitleOpen').hide();
 	});
 }
 
 	function getCodeById(id){
 	var url = "<%=contextPath %>/SpringR/contract/getContract?conId="+id;
 		jQuery.ajax({
 			type : 'POST',
	  		url : url,
 			success:function(jsonData){
 				var data = JSON.stringify(jsonData);
				var json = eval("(" + data + ")");
 				var str = "<a href='#' onclick='showContract(\""+id+"\")'>"+json.code+"</a>";
 				$('contract').innerHTML=str;
 			}
 		});
 	}
 	function showContract(conId){
 	jQuery('#contractId').val(conId);
 	window.open('editContract.jsp?conId='+conId);
 }
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=contextPath%>/springViews/img/ss.jpg" style="width:18px;height:16px;cursor:pointer" align="absmiddle"  id ="oneTitleClose" >
    <img src="<%=contextPath%>/springViews/img/zk.jpg" style="width:18px;height:16px;cursor:pointer" align="absmiddle"  id ="oneTitleOpen" >
    <span class="big3">
	   基本资料（订单基本资料，客户资料，合同资料）
    </span>
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <div id="oneTitle">
<table class="TableBlock" width="85%" align="center">
      <tr>
      <td nowrap class="TableData">订单主题：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="orderTitle" id="orderTitle"  class="BigInput" />
      </td>
      <td nowrap class="TableData">订单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="orderCode1" id="orderCode1"  class="BigInput" />
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
       <td class="TableData" colspan="5">
 		<input type="text" name="conEmail" id="conEmail"  class="BigInput" readOnly/>
      </td>
    </tr>
    
    <tr>
       <td nowrap class="TableData">签约时间：</td>
       <td class="TableData"  colspan="5">
 		      <input type="text" id="beginTime1" name="beginTime1"  maxlength="19" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">合同信息：</td>
       <td class="TableData" colspan="5">
       <input type="hidden" name="contractId" id="contractId"/>
 		<span name="contract" id="contract"></span><br>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="5">
 		<textarea name="remark1" id="remark1" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
    
    
  </table>
  </div>
  <hr/>
  <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=contextPath%>/springViews/img/ss.jpg" style="width:18px;height:16px;cursor:pointer" align="absmiddle"  id ="oneClose" >
    <img src="<%=contextPath%>/springViews/img/zk.jpg" style="width:18px;height:16px;cursor:pointer"  align="absmiddle"  id ="oneOpen" >
    <span class="big3">
	      货单基本资料
    </span>
    </td>
  </tr>
</table>
<br>

 <div id="one">
<table class="TableBlock" width="85%" align="center">
    <tr>
      <td nowrap class="TableData">货单主题：</td>
       <td class="TableData" colspan="2">
       <input type="hidden" name="po_id" id="po_id" class="BigInput"  />
 		<input type="text" name="opoTitle" id="opoTitle" class="BigInput"  />
      </td>
       <td nowrap class="TableData">货单编号：</td>
         <td class="TableData" colspan="2">
 		<input type="text" name="opoCode" id="opoCode" class="BigInput"  />
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
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="5">
 		<textarea name="remark2" id="remark2" cols="80"></textarea>
      </td>
    </tr>
      <tr>
      <td colspan="6" align="center">
		       <table id="pro_table" align="center" width="100%">
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
  </table>
  </div>
	<hr/>
	 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td>    <img src="<%=contextPath%>/springViews/img/ss.jpg" style="width:18px;height:16px;cursor:pointer" align="absmiddle"  id ="twoClose" >
    <img src="<%=contextPath%>/springViews/img/zk.jpg" style="width:18px;height:16px;cursor:pointer" align="absmiddle"  id ="twoOpen" >
    <span class="big3">
	    回款单基本资料
    </span>&nbsp;&nbsp;
    </td>
  </tr>
  
</table>
<br>
		<div id="two" >
	<table class="TableBlock" width="85%" align="center">

      <tr>
      <td nowrap class="TableData">回款单主题：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="paidName" id="paidName" class="BigInput" />
      </td>
      <td nowrap class="TableData">回款单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="paidCode" id="paidCode" class="BigInput"  />
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">销售支出：</td>
       <td class="TableData" colspan="5">
 		<input type="text" name="salePaid" id="salePaid" class="BigInput"  value="0">
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">应收金额：</td>
       <td class="TableData"  colspan="2">
 		<input type="text" name="total2" id="total2"  class="BigInput"/>
      </td>
       <td nowrap class="TableData">已收金额：</td>
       <td class="TableData"  colspan="2">
 		    <input type="text" name="alreadyTotal" id="alreadyTotal"  class="BigInput" value="0" readonly="readonly"/>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="5">
 		<textarea name="remark3" id="remark3" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
 
    <tr align="center" class="TableControl">
      <td colspan="6" nowrap>
      <input id="closeButton" type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
     </div>
</form>
</body>
</html>