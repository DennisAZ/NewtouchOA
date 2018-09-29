<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String reqId=request.getParameter("reqId");
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
var reqId="<%=reqId%>";
function doInit(){
	initTime();
	requestDetail();
	
	var url1 = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE7';
	var url2 = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE8';
	var url3 = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE9';
	getAutoCode(url1,"purCode");
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

var ids = "";
function requestDetail(){
		var url="<%=contextPath%>/SpringR/purchase/getRequestById?reqId="+reqId;
		jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){ 
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					  jQuery('#reqName').val(item.reqName);
					  jQuery('#reqCode').val(item.reqCode);
					  jQuery('#reqStatus').val(item.reqStatus);
					  jQuery('#orderStatus').val(item.order_status);
					  jQuery('#beginTime12').val(item.reqDate);
					  jQuery('#reqRemark').val(item.reqRemark);
					  var tds = "<tr><td align='center' nowrap>"+item.proCode+"</td>";//产品编号
					 tds += "<td align='center' nowrap>"+item.proName+"</td>";//产品名称
					 tds += "<td align='center' nowrap>"+item.proType+"</td>";//规格型号
					 tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					 tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"Price'  class='BigInput' size='10' maxlength='10' onblur=\"checkFloat('"+item.pro_id+"')\"></td>";//单价
					 tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"Number' class='BigInput' size='10' maxlength='10' onblur=\"checkInt('"+item.pro_id+"')\"></td>";//数量
					 tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"Total' class='BigInput' size='10' maxlength='10' onclick=\"accMul('"+item.pro_id+"')\" title='点击自动计算' readOnly></td>";//总额
					 tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"beginTime' name='"+item.pro_id+"beginTime' size='19' maxlength='19' class='BigInput' value=''><img src='<%=imgPath%>/calendar.gif' id='"+item.pro_id+"beginTimeImg' align='absMiddle' border='0' style='cursor:pointer' > </td></tr>";
					 jQuery('#pro_table').append(tds);
					 time2(item.pro_id);
					if (ids != "") {
						ids += ",";
					}
					ids += item.pro_id;
				  	
				});
				jQuery('#products').val(ids);
			}
	   }
	 });
	}
   function doSubmitForm(formObject) {
     //采购订单
	 var purTitle = jQuery('#purTitle').val();
	 var purCode = jQuery('#purCode').val();
	 var userDesc = jQuery('#userDesc').val();
	 var purSignDate = jQuery('#beginTime1').val();
	 var purContractId = jQuery('#purContractId').val();
	 var purStatus = jQuery('#purStatus').val();
	 var purRemark = jQuery('#purRemark').val();
	 //供货商信息
	 var supRemark = jQuery('#supRemark').val();
	 var supContactMan = jQuery('#supContactMan').val();
	 var supName = jQuery('#supName').val();
	 var supCode = jQuery('#supCode').val();
	 var supPhone = jQuery('#supPhone').val();
	 var supMobile = jQuery('#supMobile').val();
	 var supFex = jQuery('#supFex').val();
	 var supEmail = jQuery('#supEmail').val();
	 var supWebsite = jQuery('#supWebsite').val();
	 var supQq = jQuery('#supQq').val();
	 var supAddress = jQuery('#supAddress').val();
	 var supZipCode = jQuery('#supZipCode').val();
	 var supProducts = jQuery('#supProducts').val();
	 var supBank = jQuery('#supBank').val();
	 var supBankName = jQuery('#supBankName').val();
	 var supBankAccount = jQuery('#supBankAccount').val();
	 //货单
	 var productIds = jQuery('#products').val();
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
	 var opoRemark = jQuery('#opoRemark').val();
	 var opoTitle=jQuery('#opoTitle').val();
	 var opoCode=jQuery('#opoCode').val(); 

	 //出款单
	 var paidTitle=jQuery('#paidTitle').val(); 	   
	 var paidCode=jQuery('#paidCode').val(); 	   
	 var paidStatus=jQuery('#paidStatus').val(); 	   
	 var paidTotal=jQuery('#paidTotal').val(); 	   
	 var alreadyTotal=jQuery('#alreadyTotal').val(); 	   
	 var paidRemark=jQuery('#paidRemark').val(); 	   
	 var userId=jQuery('#user').val(); 	   
	 
	 
	 
	 
	 var param="purTitle="+encodeURIComponent(purTitle)+
	 	       "&purCode="+encodeURIComponent(purCode)+
	 	       "&userDesc="+encodeURIComponent(userDesc)+
	 	       "&purRemark="+encodeURIComponent(purRemark)+
	 	       "&purContractId="+encodeURIComponent(purContractId)+
	 	       "&purSignDate="+encodeURIComponent(purSignDate)+
	 	       "&purStatus="+encodeURIComponent(purStatus)+
	 	       "&supRemark="+encodeURIComponent(supRemark)+
	 	       "&supCode="+encodeURIComponent(supCode)+
	 	       "&supContactMan="+encodeURIComponent(supContactMan)+
	 	       "&supPhone="+encodeURIComponent(supPhone)+
	 	       "&supMobile="+encodeURIComponent(supMobile)+
	 	       "&userId="+encodeURIComponent(userId)+
	 	       "&supFex="+encodeURIComponent(supFex)+
	 	       "&supEmail="+encodeURIComponent(supEmail)+
	 	       "&supWebsite="+encodeURIComponent(supWebsite)+
	 	       "&supQq="+encodeURIComponent(supQq)+
	 	       "&supAddress="+encodeURIComponent(supAddress)+
	 	       "&supName="+encodeURIComponent(supName)+
	 	       "&supZipCode="+encodeURIComponent(supZipCode)+
	 	       "&supProducts="+encodeURIComponent(supProducts)+
	 	       "&supBank="+encodeURIComponent(supBank)+
	 	       "&supBankName="+encodeURIComponent(supBankName)+
	 	       "&supBankAccount="+encodeURIComponent(supBankAccount)+
	 	       "&opoTitle="+encodeURIComponent(opoTitle)+
	 	       "&opoCode="+encodeURIComponent(opoCode)+
	 	       "&opoRemark="+encodeURIComponent(opoRemark)+
	 	       "&productIds="+encodeURIComponent(productIds)+
	 	       "&sendDate="+encodeURIComponent(sendDate)+
	 	       "&prices="+encodeURIComponent(prices)+
	 	       "&numbers="+encodeURIComponent(numbers)+
	 	       "&totals="+encodeURIComponent(totals)+
	 	       "&paidTitle="+encodeURIComponent(paidTitle)+
	 	       "&paidCode="+encodeURIComponent(paidCode)+
	 	       "&paidStatus="+encodeURIComponent(paidStatus)+
	 	       "&paidTotal="+encodeURIComponent(paidTotal)+
	 	       "&alreadyTotal="+encodeURIComponent(alreadyTotal)+
	 	       "&paidRemark="+encodeURIComponent(paidRemark);
     var url="";
     url = "<%=contextPath %>/SpringR/purchase/addPurchase?"+param;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		 if(jsonData == "0"){
		   		alert("更新成功");
		   		window.location.href="purchaseManage.jsp";
			}else{
					 alert("更新失败"); 
				 }
			 }
	  });
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
  function diogShow(){
  	 var url="<%=contextPath%>/springViews/erp/initData/purchase/suppInfo.jsp?reqId="+reqId;
 
 	var str=openDialogResize(url,  520, 400);
  	   if(str!=null||str!=""){
  	  	var arrStr=str.split(",");
  	   	jQuery('#supRemark').val(arrStr[1]);
  	   	jQuery('#suppId').val(arrStr[0]);
  	   	jQuery('#supContactMan').val(arrStr[3]);
  	   	jQuery('#supName').val(arrStr[4]);
  	   	jQuery('#supPhone').val(arrStr[5]);
  	   	jQuery('#supMobile').val(arrStr[6]);
  	   	jQuery('#supFex').val(arrStr[7]);
  	   	jQuery('#supEmail').val(arrStr[8]);
  	   	jQuery('#supWebsite').val(arrStr[9]);
  	   	jQuery('#supQq').val(arrStr[10]);
  	   	jQuery('#supAddress').val(arrStr[11]);
  	   	jQuery('#supZipCode').val(arrStr[12]);
  	   	jQuery('#supProducts').val(arrStr[13]);
  	   	jQuery('#supBank').val(arrStr[14]);
  	   	jQuery('#supBankName').val(arrStr[15]);
  	   	jQuery('#supBankAccount').val(arrStr[1]);
  	   	jQuery('#supCode').val(arrStr[2]);
  	   }
  }
  var nameArray = null;
   function chooseProduct(idArray,type){
	    nameArray = idArray;

	 	openDialogResize('selectProduct.jsp',  520, 400);
	 	var productIds = jQuery('#products').val();
	 	times(productIds,"beginTime");
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
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	 查看采购申请单基础信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="for1" id="form1">
<table class="TableBlock" width="70%" align="center">
      <tr>
      <td nowrap class="TableData">申请单名称：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="reqName" id="reqName"  class="BigInput" readonly="readonly"/>
      </td>
      <td nowrap class="TableData">申请单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="reqCode" id="reqCode"  class="BigInput" readonly />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">订单状态：</td>
       <td class="TableData"  colspan="2">
 		<input type="text" name="reqStatus" id="reqStatus"  class="BigInput" readonly/>
      </td>
       <td nowrap class="TableData">签约时间：</td>
       <td class="TableData"  colspan="2">
 		      <input type="text" id="beginTime12" name="beginTime12" size="19" maxlength="19" class="BigInput" value="" readonly>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="reqRemark" id="reqRemark" class="BigInput" cols="70" readonly></textarea>
      </td>
    </tr>
    
    
  </table>
  <hr/>
<br>
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
       <input type="hidden" name="id" id="id" size="20" class="BigInput" >
       <input type="hidden" name="oldConId" id="oldConId" size="20" class="BigInput" >
       <input type="hidden" name="oldCusId" id="oldCusId" size="20" class="BigInput" >
       <input type="hidden" name="oldAddrId" id="oldAddrId" size="20" class="BigInput" >
      <tr>
      <td nowrap class="TableData">订单主题：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="purTitle" id="purTitle"  class="BigInput" />
      </td>
      <td nowrap class="TableData">订单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="purCode" id="purCode"  class="BigInput" />
      </td>
    </tr>
    <tr>
     <td nowrap class="TableData">采购员：</td>
       <td class="TableData" colspan="6">
    	 <input type="hidden" name="user" id="user" value="" />
  <input type="text" name="userDesc" id="userDesc" style="vertical-align: top;" size="10" class="SmallStatic" size="10" value="" READONLY>
  <!--selectUser() 这个函数是多选  -->
  <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['user', 'userDesc']);">选择</a>
  <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
   </td>
    </tr>
      <tr>
     
      <td nowrap class="TableData">对应供货商：</td>
       <td class="TableData"  colspan="2">
       <input type="hidden" name="supRemark" id="supRemark"  />
       <input type="hidden" name="suppId" id="suppId"  />
       <input type="hidden" name="supCode" id="supCode"  />
 		<input type="text" name="supName" id="supName"  class="BigInput" readOnly/>
 		<input type="button" name="supp" id="supp"  value="选择供货商" onclick="diogShow()" class="BigButton" />
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
       <td nowrap class="TableData">签约时间：</td>
       <td class="TableData"  colspan="2">
 		      <input type="text" id="beginTime1" name="beginTime1" size="19" maxlength="19" class="BigInput" value="" readonly="readonly">
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData" style="display: none">订单状态：</td>
       <td class="TableData"  colspan="2" style="display: none">
 		<input type="text" name="purStatus" id="purStatus"  class="BigInput"/>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">合同信息：</td>
       <td class="TableData" colspan="6">
 		<textarea name="purContractId" id="purContractId" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="purRemark" id="purRemark" class="BigInput" cols="70"></textarea>
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

 <input type="hidden" id="oldProducts" name="oldProducts">
 <input type="hidden" id="products" name="products">
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
 		<textarea name="opoRemark" id="opoRemark" cols="80"></textarea>
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
 		<input type="text" name="paidTitle" id="paidTitle" class="BigInput" />
      </td>
      <td nowrap class="TableData">出款单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="paidCode" id="paidCode" class="BigInput"  />
      </td>
    </tr>
      <tr style="display: none">
       <td nowrap class="TableData">出款单状态：</td>
       <td class="TableData"  colspan="6">
 		<input type="text" name="paidStatus" id="paidStatus"  class="BigInput" />
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">应付金额：</td>
       <td class="TableData"  colspan="2">
 		<input type="text" name="paidTotal" id="paidTotal"  class="BigInput"/>
      </td>
       <td nowrap class="TableData">已付金额：</td>
       <td class="TableData"  colspan="2">
 		    <input type="text" name="alreadyTotal" id="alreadyTotal"  class="BigInput" value="0"/>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name=""paidRemark"" id="paidRemark" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
     
       <input type="button" value="添加" class="BigButton" onclick="return doSubmitForm(this.form);">
       <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>