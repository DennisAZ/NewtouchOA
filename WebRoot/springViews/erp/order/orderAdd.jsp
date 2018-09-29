<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("orderId")==null?"":request.getParameter("orderId");
	String flow = request.getParameter("flow")==null?"":request.getParameter("flow");
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

var flow = '<%=flow%>';
function doInit(){
	showPpo();
	initTime();
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
function orderValidate(){
		 var cusId = jQuery('#cusId').val();
		 var contractId = jQuery('#contractId').val();
		 var productIds = jQuery('#products').val();
		 var contactId = jQuery('#contactId').val();
		 var addId = jQuery('#addId').val();
		 if(cusId==""||cusId==null){
		 	alert("请先选择客户！！");
		 	return false;
		 }else if(contactId==""||contactId==null){
		 	alert("请先选择联系人信息！！");
		 	return false;
		 }else if(contractId==""||contractId==null){
		 	alert("请先添加合同信息！！");
		 	return false;
		 }else if(addId==""||addId==null){
		 	alert("请先选择收货人信息！！");
		 	return false;
		 }else if(productIds==""||productIds==null){
		 	alert("请先选择产品信息！！");
		 	return false;
		 }else{
		 doSubmitForm();
		 }
}		

   function doSubmitForm() {
	 var orderCode = jQuery('#orderCode1').val();//订单编号
	 var orderTitle = jQuery('#orderTitle').val();//订单主题
	 var signDate = jQuery('#beginTime1').val();//签约时间
	 var contractId = jQuery('#contractId').val();//合同id
	 var remark1 = jQuery('#remark1').val();//销售备注
	 
	 var cusId = jQuery('#cusId').val();//客户id
	 var contactId = jQuery('#contactId').val();//联系人id
	 var addId = jQuery('#addId').val();
	 var opoCode=jQuery('#opoCode').val();
	 var opoTitle=jQuery('#opoTitle').val();
	 var remark2 = jQuery('#remark2').val();
     var productIds = jQuery('#products').val();
	 if(productIds==""||productIds==null)
	 {}
	 else{
	 	var prices = getValue(productIds,"Price");
		if(prices == '-1'){return false;}
		var sendDate = getValue(productIds,"beginTime");
	 	if(sendDate == null){return false;}
	 	var numbers = getValue(productIds,"Number");
	 	if(numbers == '-1'){return false;}
	 	var totals = getValue(productIds,"Total");
	 	if(totals == '-1'){return false;}
	 }
	 
	 var paidCode = jQuery('#paidCode').val();
     var paidName = jQuery('#paidName').val();
	 var total2 = jQuery('#total2').val();
	 var remark3 = jQuery('#remark3').val();
	 var sale_paid = jQuery('#salePaid').val();
	 var already_total = jQuery('#alreadyTotal').val();
	 var custName = jQuery('#custName').val();
	 
	 var param="orderCode="+encodeURIComponent(orderCode)+
				 "&orderTitle="+encodeURIComponent(orderTitle)+
				 "&signDate="+encodeURIComponent(signDate)+
				 "&contractId="+encodeURIComponent(contractId)+
				 "&remark1="+encodeURIComponent(remark1)+
				 "&cusId="+encodeURIComponent(cusId)+
				 "&contactId="+encodeURIComponent(contactId)+
				 "&addId="+encodeURIComponent(addId)+
				 "&opoCode="+encodeURIComponent(opoCode)+
				 "&opoTitle="+encodeURIComponent(opoTitle)+
				 "&remark2="+encodeURIComponent(remark2)+
				 "&productIds="+encodeURIComponent(productIds)+
				 "&prices="+encodeURIComponent(prices)+
				 "&sendDate="+encodeURIComponent(sendDate)+
				 "&numbers="+encodeURIComponent(numbers)+
				 "&totals="+encodeURIComponent(totals)+
				 "&paidCode="+encodeURIComponent(paidCode)+
				 "&paidName="+encodeURIComponent(paidName)+
				 "&total2="+encodeURIComponent(total2)+
				 "&remark3="+encodeURIComponent(remark3)+
				 "&sale_paid="+encodeURIComponent(sale_paid)+
				 "&already_total="+encodeURIComponent(already_total)+
				 "&custName="+encodeURIComponent(custName);
	 
     var url = "<%=contextPath %>/SpringR/saleOrder/saleOrderSave?"+param;
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
  
 
  function diogShow(){
  		var corCust=new Array();
  	var url="<%=contextPath%>/springViews/erp/order/custFrame.jsp";
 
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
  	var url="<%=contextPath%>/springViews/erp/order/custContact.jsp?cusId="+cusId;
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
  	var url="<%=contextPath%>/springViews/erp/order/custAddress.jsp?cusId="+cusId;
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
	
	if(total != '' && total != null){
		toReciveTotal(total);
	}
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
	var megx = /^\d+(\.\d+)?$/;
	   var number = jQuery('#'+id+'Number').val();
	   if(!megx.test(number)){
		   alert('请输入正确的数');
		   jQuery('#'+id+'Number').val('');
		   return;
	   }
	   totals(id);
	   
   }
    function totals(id){
    var price=jQuery('#'+id+'Price').val();
	var num=jQuery('#'+id+'Number').val();
   	var total=Math.round(parseFloat(price)*parseFloat(num)*Math.pow(10,3))/Math.pow(10,3);
  	jQuery('#'+id+'Total').val(total);
  	if(total != '' && total != null){
		toReciveTotal(total);
	}
   	}
      function toReciveTotal(total){ //因为销售而应收到的款项总额
   		var suffix = 'Total';
   		var productIds = jQuery('#products').val();
   		 var arr = productIds.split(',');
	     var value = 0.0;
		  for(var i=0;i<arr.length;i++){
			  var val = jQuery('#'+arr[i]+suffix).val();
			  if(val != "" && val != null){
				  value = (parseFloat(value) + parseFloat(val));
			  }
		  }
	  jQuery('#total2').val(value);
	  return ;
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
			  return '-1';
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

	 	openDialogResize('frame.jsp',  600, 400);
	 	var productIds = jQuery('#products').val();
	 	times(productIds,"beginTime");
 	}
 function deletes(obj,id){
 	  obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
 	  jQuery('#'+id).remove();
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
		var imgTime=productIds+"beginTimeImg";
		var timePara = {
	    inputId:dates,
	    property:{isHaveTime:false},
	    bindToBtn:imgTime
  		};
 		new Calendar(timePara);
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
 		
 var arr;
 function chooseContract(array){
	 arr = array;
	 var url="chooseContract.jsp";
 	openDialogResize(url,  520, 400);
 }
 
 function showContract(conId){
 	jQuery('#contractId').val(conId);
 	window.open('editContract.jsp?conId='+conId);
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
        <a href="javascript:void(0)" class="orgAdd" onClick="chooseContract(['contract','contractId']);" >合同选择</a>
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

 <input type="hidden" id="products" name="products">
 <div id="one">
<table class="TableBlock" width="85%" align="center">
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
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="5">
 		<textarea name="remark2" id="remark2" cols="80"></textarea>
      </td>
    </tr>
    <tr>
    	 <td class="TableData" colspan="6">
 		<input type="button" value="+添加产品" class="BigButton" onclick="chooseProduct(['products'],0);">
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
		    	<td align='center' nowrap>操作</td>
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
       <input type="button" value="添加" class="BigButton" onclick="return orderValidate();">
      </td>
    </tr>
  </table>
     </div>
</form>
</body>
</html>