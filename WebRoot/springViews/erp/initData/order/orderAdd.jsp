<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("orderId");
	String flag = request.getParameter("flag");
	String flow = request.getParameter("flow");
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

var flag = '<%=flag%>';
var flow = '<%=flow%>';
function doInit(){
	initTime();
	if(flag=="0"){
	editDetail();
	}
	
	if(flow == "1"){
		jQuery('#orderTitle').attr("readonly","readonly");
		jQuery('#orderCode1').attr("readonly","readonly");
		jQuery('#closeButton').show();
	}
	var url1 = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE3';
	var url2 = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE4';
	var url3 = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE5';
	getAutoCode(url1,"orderCode1");
	getAutoCode(url2,"opoCode");
	getAutoCode(url3,"paidCode");
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
	 var orderTitle = jQuery('#orderTitle').val();
	 var cusId = jQuery('#cusId').val();
	 var custName = jQuery('#custName').val();
	 var custContact = jQuery('#custContact').val();
	 var orderStatus = jQuery('#orderStatus').val();
	 var signDate = jQuery('#beginTime1').val();
	 var remark1 = jQuery('#remark1').val();
	 var contractId = jQuery('#contractId').val();
	 var orderCode = jQuery('#orderCode1').val();
	 var opoTitle=jQuery('#opoTitle').val();
	 var opoCode=jQuery('#opoCode').val();
     var productIds = jQuery('#products').val();
     var oldProductId = jQuery('#oldProducts').val();
     var oldPids = jQuery('#oldPids').val();
     if(oldProductId==null||oldProductId==""){}else{
      var oldSendDate = getValue(oldProductId,"beginTime");
	 if(oldSendDate == null){return false;}
	 var oldPrices = getValue(oldProductId,"Price");
	 if(oldPrices == '0'){return false;}
	 var oldNumbers = getValue(oldProductId,"Number");
	 if(oldNumbers == '0'){return false;}
	 var oldTotals = getValue(oldProductId,"Total");
	 if(oldTotals == '0'){return false;}
	 }
	 if(productIds==""||productIds==null){}else{
	 var sendDate = getValue(productIds,"beginTime");
	 if(sendDate == null){return false;}
	 var prices = getValue(productIds,"Price");
	 if(prices == '0'){return false;}
	 var numbers = getValue(productIds,"Number");
	 if(numbers == '0'){return false;}
	 var totals = getValue(productIds,"Total");
	 if(totals == '0'){return false;}
	 }
	 var remark2 = jQuery('#remark2').val();
     var paidName = jQuery('#paidName').val();
	 var paidCode = jQuery('#paidCode').val();
	 var paidStatus = jQuery('#paidStatus').val();
	 var total2 = jQuery('#total2').val();
	 var alreadyTotal = jQuery('#alreadyTotal').val();
	 var salePaid = jQuery('#salePaid').val();
	 var remark3 = jQuery('#remark3').val();
	 var conWorkPhone = jQuery('#conWorkPhone').val();
	 var conPhone = jQuery('#conPhone').val();
	 var conAddress = jQuery('#conAddress').val();
	 var conEmail = jQuery('#conEmail').val();
	 var addName = jQuery('#addName').val();
	 var addWorkPhone = jQuery('#addWorkPhone').val();
	 var custAddress = jQuery('#custAddress').val();
	 var addPhone = jQuery('#addPhone').val();
	 var addId = jQuery('#addId').val();
	 var contactId = jQuery('#contactId').val();
	 var orderId = jQuery('#id').val();
	 var proId = jQuery('#proId').val();
	 var oldConId = jQuery('#oldConId').val();
	 var oldCusId = jQuery('#oldCusId').val();
	 var oldAddrId = jQuery('#oldAddrId').val();
	 
	 
	 
	 var param="orderTitle="+encodeURIComponent(orderTitle)+
				 "&cusId="+encodeURIComponent(cusId)+
				 "&proId="+encodeURIComponent(proId)+
				 "&addId="+encodeURIComponent(addId)+
				 "&contactId="+encodeURIComponent(contactId)+
				 "&oldConId="+encodeURIComponent(oldConId)+
				 "&oldCusId="+encodeURIComponent(oldCusId)+
				 "&oldAddrId="+encodeURIComponent(oldAddrId)+
				 "&conWorkPhone="+encodeURIComponent(conWorkPhone)+
				 "&conPhone="+encodeURIComponent(conPhone)+
				 "&conAddress="+encodeURIComponent(conAddress)+
				 "&conEmail="+encodeURIComponent(conEmail)+
				 "&addWorkPhone="+encodeURIComponent(addWorkPhone)+
				 "&addName="+encodeURIComponent(addName)+
				 "&custAddress="+encodeURIComponent(custAddress)+
				 "&addPhone="+encodeURIComponent(addPhone)+
				 "&custName="+encodeURIComponent(custName)+
				 "&custContact="+encodeURIComponent(custContact)+
				 "&orderStatus="+encodeURIComponent(orderStatus)+
				 "&signDate="+encodeURIComponent(signDate)+
				 "&remark1="+encodeURIComponent(remark1)+
				 "&contractId="+encodeURIComponent(contractId)+
				 "&orderCode="+encodeURIComponent(orderCode)+
				 "&opoTitle="+encodeURIComponent(opoTitle)+
				 "&opoCode="+encodeURIComponent(opoCode)+
				 "&productIds="+encodeURIComponent(productIds)+
				 "&sendDate="+encodeURIComponent(sendDate)+
				 "&prices="+encodeURIComponent(prices)+
				 "&numbers="+encodeURIComponent(numbers)+
				 "&totals="+encodeURIComponent(totals)+
				 "&oldProductId="+encodeURIComponent(oldProductId)+
				 "&oldSendDate="+encodeURIComponent(oldSendDate)+
				 "&oldPrices="+encodeURIComponent(oldPrices)+
				 "&oldNumbers="+encodeURIComponent(oldNumbers)+
				 "&oldTotals="+encodeURIComponent(oldTotals)+
				 "&remark2="+encodeURIComponent(remark2)+
				 "&paidName="+encodeURIComponent(paidName)+
				 "&paidCode="+encodeURIComponent(paidCode)+
				 "&paidStatus="+encodeURIComponent(paidStatus)+
				 "&total2="+encodeURIComponent(total2)+
				 "&alreadyTotal="+encodeURIComponent(alreadyTotal)+
				 "&salePaid="+encodeURIComponent(salePaid)+
				 "&orderId="+encodeURIComponent(orderId)+
				 "&oldPids="+encodeURIComponent(oldPids)+
				 "&remark3="+encodeURIComponent(remark3);
	 
     var url="";
     if(flag=="0"){
      url = "<%=contextPath %>/SpringR/saleOrder/saleOrderUpdate?"+param;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		 if(jsonData == "0"){
		   		alert("更新成功");
		   		window.location.href="orderManage.jsp";
			}else{
					 alert("更新失败"); 
				 }
			 }
	  });
     }else{
     url = "<%=contextPath %>/SpringR/saleOrder/saleOrderSave?"+param;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		 if(jsonData == "0"){
		   		alert("添加成功");
		   		window.location.href="orderManage.jsp";
			}else{
					 alert("添加失败"); 
				 }
			 }
	  });
	  }
   }
  
 
  function diogShow(){
  		var corCust=new Array();
  	var url="<%=contextPath%>/springViews/erp/initData/order/custList.jsp";
 
 	var cor=openDialogResize(url,  520, 400);
 	if(cor==""||cor==null){
 		document.getElementById("custName").value="";
	document.getElementById("cusId").value="";
 	}else{
 	corCust=cor.split(",");
	document.getElementById("custName").value=corCust[0];
	document.getElementById("cusId").value=corCust[1];
	}
  }
  function diogShow1(){
  		var arrContent=new Array();
  		var cusId=document.getElementById("cusId").value;
  			if(cusId==""||cusId==null){
  			alert("请先选择客户信息")
  			return false;
  			
  		}
  	var url="<%=contextPath%>/springViews/erp/initData/order/custContact.jsp?cusId="+cusId;
 	 var str="";
 	 str=openDialogResize(url, 520, 400);
 	 if(str==""||str==null){
 		 document.getElementById("custContact").value="";
	document.getElementById("contactId").value="";
	 var conWorkPhone = jQuery('#conWorkPhone').val("");
	 var conPhone = jQuery('#conPhone').val("");
	 var conAddress = jQuery('#conAddress').val("");
	 var conEmail = jQuery('#conEmail').val("");
 	 }else{
 	 arrContent=str.split(",");
 	
	document.getElementById("custContact").value=arrContent[0];
	document.getElementById("contactId").value=arrContent[1];
	 var conWorkPhone = jQuery('#conWorkPhone').val(arrContent[3]);
	 var conPhone = jQuery('#conPhone').val(arrContent[2]);
	 var conAddress = jQuery('#conAddress').val(arrContent[4]);
	 var conEmail = jQuery('#conEmail').val(arrContent[5]);
	}
  }
   function diogShow2(){
  		var arrAddress=new Array();
  		var cusId=document.getElementById("cusId").value;
  			if(cusId==""||cusId==null){
  			alert("请先选择客户信息")
  			return false;
  			
  		}
  	var url="<%=contextPath%>/springViews/erp/initData/order/custAddress.jsp?cusId="+cusId;
 	 var str="";
 	 str=openDialogResize(url, 520, 400);
 	 	if(str==""||str==null){
 		document.getElementById("addName").value="";
		document.getElementById("addId").value="";
		document.getElementById("addPhone").value="";
		document.getElementById("custAddress").value="";
 	}else{
 	arrAddress=str.split(",");
 	
	document.getElementById("addName").value=arrAddress[1];
	document.getElementById("addId").value=arrAddress[0];
	document.getElementById("addPhone").value=arrAddress[3];
	document.getElementById("custAddress").value=arrAddress[2];
	document.getElementById("addZipCode").value=arrAddress[4];
	}
	
  }
  function initTime(){
  var beginTimePara = {
      inputId:'beginTime1',
      property:{isHaveTime:false},
      bindToBtn:'beginTimeImg'
  };
  new Calendar(beginTimePara);
}
  
  
 function accMul(id){
	var price = jQuery('#'+id+'Price').val();
	var number = jQuery('#'+id+'Number').val();
	var m=0;s1=price.toString();
	try{m+=s1.split('.')[1].length}catch(e){}
	var total = Number(s1.replace('.',''))*Number(number)/Math.pow(10,m);
	jQuery('#'+id+'Total').val(total);
}
   function checkFloat(id){
	   var megx = /^\d+(\.\d+)?$/;
	   var price = jQuery('#'+id+'Price').val();
	   if(!megx.test(price)){
		   alert('请输入正确的数');
		   jQuery('#'+id+'Price').val('');
		   return;
	   }
   }
   function checkInt(id){
	   var megx = /^[0-9]*[1-9][0-9]*$/;
	   var number = jQuery('#'+id+'Number').val();
	   if(!megx.test(number)){
		   alert('请输入正确的数');
		   jQuery('#'+id+'Number').val('');
		   return;
	   }
   }
  function getValue(ids,suffix)
  {
	  var arr = ids.split(',');
	  var value = "";
	  for(var i=0;i<arr.length;i++){
		  var v = jQuery('#'+arr[i]+suffix).val();
		  if(v == "" || v == null){
			  alert('还有项未填写');
			  jQuery('#'+arr[i]+suffix).focus();
			  jQuery('#'+arr[i]+suffix).select();
			  return '0';
		  }
		  if(value != ""){
			  value+=",";
		  }
		  value += v;
	  }
	  return value;
  }
  
  
  var nameArray = null;
   function chooseProduct(idArray,type){
	    nameArray = idArray;

	 	openDialogResize('frame.jsp',  520, 400);
	 		 var productIds = jQuery('#products').val();
	 	times(productIds,"beginTime");
 	}
 	function selectId(id){
 		     var productIds = jQuery('#products').val();
 		     var arrProduct=productIds.split(",");
 		     var values="";
 		    for(var i=0;i<arrProduct.length;i++){
 		    	 if(arrProduct[i]!=id){
 		    	  if(values != ""){
			  		values+=",";
				  }
		  			values += arrProduct[i];
 		    	 }
 		    }
 		     jQuery('#products').val(values);
 	}
 function deletes(obj,id){
 	  obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
 	  jQuery('#'+id).remove();
 	  selectId(id);
 }
 	function times(productIds,suffix){
 		var arr = productIds.split(',');
 		for(var i=0;i<arr.length;i++){
		var dates= arr[i]+suffix;
		var timePara=arr[i]+"time";
		var imgTime=arr[i]+"beginTimeImg";
		var timePara = {
	    inputId:dates,
	    property:{isHaveTime:false},
	    bindToBtn:imgTime
  };
 new Calendar(timePara);
	  }
 		
 	}
 		function time2(productIds){
 		
		var dates= productIds+"beginTime";
		var timePara=productIds+"time";
		var imgTime=productIds+"beginTimeImg";
		var timePara = {
	    inputId:dates,
	    property:{isHaveTime:false},
	    bindToBtn:imgTime
  };
 new Calendar(timePara);
 		
 	}	 
 		var ids="";
 		var pids="";
function editDetail(){
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
					
						if (ids != "") {
							ids += ",";
						}
						ids += item.productId;
						if (pids != "") {
							pids += ",";
						}
						pids += item.cpId;
						
					  jQuery('#oldProducts').val(ids);
					  jQuery('#oldPids').val(pids);
					  jQuery('#id').val(item.order_id);
					  jQuery('#proId').val(item.proId);
					  jQuery('#oldConId').val(item.conId);
					  jQuery('#oldAddrId').val(item.addrId);
					  jQuery('#oldCusId').val(item.cusId);
						var tds = "<tr od='old"+item.productId+"'><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
						tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
						tds += "<td align='center' nowrap>"+item.ps_name+"</td>";//规格型号
						tds += "<td align='center' nowrap>"+item.pt_name+"</td>";//计量单位
						tds += "<td align='center' nowrap><input type='text' id='"+item.productId+"Price'  class='BigInput' size='10' maxlength='10' value='"+item.sale_price+"' onblur=\"checkFloat('"+item.productId+"')\"></td>";//单价
						tds += "<td align='center' nowrap><input type='text' id='"+item.productId+"Number' class='BigInput' size='10' maxlength='10' value='"+item.order_num+"' onblur=\"checkInt('"+item.productId+"')\"></td>";//数量
						tds += "<td align='center' nowrap><input type='text' id='"+item.productId+"Total' class='BigInput' size='10' maxlength='10'  value='"+item.proTotal+"' onclick=\"accMul('"+item.productId+"')\" title='点击自动计算' readOnly></td>";//总额
						tds += "<td align='center' nowrap><input type='text' id='"+item.productId+"beginTime' name='"+item.productId+"beginTime' size='19' maxlength='19' class='BigInput' value='"+item.send_date+"'><img src='<%=imgPath%>/calendar.gif' id='"+item.productId+"beginTimeImg' align='absMiddle' border='0' style='cursor:pointer' > </td>";//总额
						tds += "<td align='center' nowrap><input type='button' value='删除' class='BigButton' onclick=\"oldDeletes(this,'"+item.productId+"')\"></td></tr>";
						jQuery('#pro_table').append(tds);
						time2(item.productId);
				});
			}
	   }
	 });

	
}
	function oldSelectId(id){
 		     var productIds = jQuery('#oldProducts').val();
 		     var arrProduct=productIds.split(",");
 		     var values="";
 		    for(var i=0;i<arrProduct.length;i++){
 		    	 if(arrProduct[i]!=id){
 		    	  if(values != ""){
			  		values+=",";
				  }
		  			values += arrProduct[i];
 		    	 }
 		    }
 		     jQuery('#oldProducts').val(values);
 	}
 function oldDeletes(obj,id){
 	  obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
 	  jQuery('#old'+id).remove();
 	  oldSelectId(id);
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
       <input type="hidden" name="oldConId" id="oldConId" size="20" class="BigInput" >
       <input type="hidden" name="oldCusId" id="oldCusId" size="20" class="BigInput" >
       <input type="hidden" name="oldAddrId" id="oldAddrId" size="20" class="BigInput" >
       <input type="hidden" name="oldPids" id="oldPids" size="20" class="BigInput" >
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
 		<input type="button" name="cust" id="cust"  value="选择客户" onclick="diogShow()" class="BigButton" />
      </td>
       <td nowrap class="TableData">联系人：</td>
       <td class="TableData"  colspan="2">
         <input type="hidden" name="contactId" id="contactId"  />
 		<input type="text" name="custContact" id="custContact"   class="BigInput" readOnly/>
 		<input type="button" name="contact" id="contact"  value="选择联系人" onclick="diogShow1()" class="BigButton"/>
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
 		      <input type="text" id="beginTime1" name="beginTime1" size="19" maxlength="19" class="BigInput" readonly="readonly" value="">
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData" style="display: none;">订单状态：</td>
       <td class="TableData"  colspan="2" style="display: none;">
 		<input type="text" name="orderStatus" id="orderStatus"  class="BigInput"/>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">合同信息：</td>
       <td class="TableData" colspan="6">
 		<textarea name="contractId" id="contractId" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="remark1" id="remark1" class="BigInput" cols="70"></textarea>
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
      	<input type="button" name="address" id="address"  value="选择收货人" onclick="diogShow2()" class="BigButton"/>
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
    	 <td class="TableData" colspan="6">
 		<input type="button" value="+添加产品" class="BigButton" onclick="chooseProduct(['products'],0);">
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
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
    </td>
     </tr>
      <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="7">
 		<textarea name="remark2" id="remark2" cols="80"></textarea>
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
 		<input type="text" name="paidName" id="paidName" class="BigInput" />
      </td>
      <td nowrap class="TableData">回款单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="paidCode" id="paidCode" class="BigInput"  />
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">销售支出：</td>
       <td class="TableData" colspan="6">
 		<input type="text" name="salePaid" id="salePaid" class="BigInput" value="0">
      </td>
       <td nowrap class="TableData" style="display: none;">回款单状态：</td>
       <td class="TableData"  colspan="2" style="display: none;">
 		<input type="text" name="paidStatus" id="paidStatus"  class="BigInput" />
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">应收金额：</td>
       <td class="TableData"  colspan="2">
 		<input type="text" name="total2" id="total2"  class="BigInput"/>
      </td>
       <td nowrap class="TableData">已收金额：</td>
       <td class="TableData"  colspan="2">
 		    <input type="text" name="alreadyTotal" id="alreadyTotal"  class="BigInput" value="0"/>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="remark3" id="remark3" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
     
    <%
    	if(flag.equals("1")){
     %>
       <input type="button" value="添加" class="BigButton" onclick="return doSubmitForm(this.form);">
     <%}else{ %>
     	<input type="button" value="修改" class="BigButton" onclick="return doSubmitForm(this.form);">
     <%} %>
     <input id="closeButton" type="button" value="关闭" style="display: none" class="BigButton" onclick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>