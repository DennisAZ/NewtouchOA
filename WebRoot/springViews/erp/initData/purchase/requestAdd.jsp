<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建采购申请单信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
		<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">

function doInit(){
	var url = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE6';
	getAutoCode(url,"reqCode");
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
   function doSubmitForm() {
     //采购订单
	 var reqName = jQuery('#reqName').val();
	 var reqCode = jQuery('#reqCode').val();
	 var reqPerson = jQuery('#reqPerson').val();
	 var reqRemark = jQuery('#reqRemark').val();
	 var productIdss = jQuery('#products').val();
	 var arrSup = getValues(productIdss,"Pro");
	 if(arrSup == null||arrSup==""){return false;}
	 var supId = getValue(productIdss,"supId");
	 if(supId == null||supId==""){return false;}
	 var supName = getValues(productIdss,"supName");
	 if(supName == null||supName==""){return false;}
	 var proId = getValues(productIdss,"proId");
	 if(proId == null||proId==""){return false;}
	 var param="reqName="+encodeURIComponent(reqName)+
	 	       "&reqCode="+encodeURIComponent(reqCode)+
	 	       "&productIds="+encodeURIComponent(proId)+
	 	       "&supName="+encodeURIComponent(supName)+
	 	       "&arrSup="+encodeURIComponent(arrSup)+
	 	       "&reqRemark="+encodeURIComponent(reqRemark);
     var url="";
     url = "<%=contextPath %>/SpringR/purchase/addRequest?"+param;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		 if(jsonData == "0"){
		   		alert("更新成功");
		   		window.location.href="requestManage.jsp";
			}else{
					 alert("更新失败"); 
				 }
			 }
	  });
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
   function getValues(ids,suffix)
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
			  value+=";";
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
 	}
function diogShow(proName){
  	 var url="<%=contextPath%>/springViews/erp/initData/purchase/suppRequest.jsp?proName="+proName;
 
 	var str=openDialogResize(url,  520, 400);
  }
  function deletes(a){
	jQuery('#'+a).remove();
	jQuery('#pros_table'+a).remove();
	jQuery('#trs'+a).remove();
	jQuery('#table_tr_'+a).remove();
	jQuery('#products').val(removeValueFromPro('products',a));
}
  
   function removeValueFromPro(hidId,id)
  {
	  var ids = jQuery('#'+hidId).val();
	  var arr = ids.split(',');
	  var value = "";
	  for(var i=0;i<arr.length;i++){
	  	if(id != arr[i]){
	  		 if(value != ""){
			  value+=",";
		  }
	  		value+=arr[i];
	  	}
	  }
	  return value;
  }
   function removeSameValue(hidId,id,flag)
  {
	  var ids = jQuery('#'+hidId).val();
	  var arr = ids.split(',');
	  var value = "";
	  for(var i=0;i<arr.length;i++){
	  	if(id != arr[i]){
	  		 if(value != ""){
			  value+=",";
		  }
	  		value+=arr[i];
	  	}
	  }
	  if(flag == '1'){
	 	 value = id+','+value;
	  }else{
		  value = value+','+id;
	  }
	  return value;
  }
   function deleteSup(obj,productId,supId,supName){
	   obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
	   jQuery('#'+productId+"supName").val(removeValueFromPro(productId+"supName",supId));
	   jQuery('#'+productId+"supName").val(removeValueFromPro(productId+"supName",supName));
	   jQuery('#'+productId+"Pro").val(removeValueFromPro(productId+"Pro",supId));
	   jQuery('#'+productId+"Pro").val(removeSameValue(productId+"Pro",productId,'1'));
	   jQuery('#'+productId+"supId").val(removeValueFromPro(productId+"supId",supId));
	   jQuery('#'+productId+"proId").val(removeValueFromPro(productId+"proId",supId));
	   jQuery('#'+productId+"proId").val(removeSameValue(productId+"proId",productId,'2'));
   }
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	 采购申请单基础信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="70%" align="center">
      <tr>
      <td nowrap class="TableData">申请单名称：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="reqName" id="reqName"  class="BigInput" />
      </td>
      <td nowrap class="TableData">申请单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="reqCode" id="reqCode"  class="BigInput" />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="reqRemark" id="reqRemark" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
    
    
  </table>
  <hr/>
  <table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	   选择产品
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>

 <input type="hidden" id="products" name="products" >
 <input type="hidden" id="suppId" name="suppId" >
 <input type="hidden" id="oldProducts" name="oldProducts">
<table class="TableBlock" width="100%" align="center">
  
    <tr>
    	 <td class="TableData" colspan="6">
 		<input type="button" value="+选择产品" class="BigButton" onclick="chooseProduct(['products'],0);">
      </td>
    </tr>
      <tr>
      <td colspan="6" align="center">
		       <table id="pro_table" align="center" width="80%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>标准单价</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    
		    </table>
    </td>
     </tr>
     
  </table>
<br>
	<div id="listContainer" style="display:none;width:100;">
</div>
   <table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
       <input type="button" value="添加" class="BigButton" onclick="return doSubmitForm();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>