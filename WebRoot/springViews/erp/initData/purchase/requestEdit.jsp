<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String reqId=request.getParameter("reqId");
String flow = request.getParameter("flow");
if(flow==""||flow==null){
	flow="8";
}
 %>
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
var reqId="<%=reqId%>";
var flow="<%=flow%>";
function doInit(){
	initTime();
	details();
}
	
   function doSubmitForm() {
     //采购订单
	 var reqName = jQuery('#reqName').val();
	 var reqCode = jQuery('#reqCode').val();
	 var reqPerson = jQuery('#reqPerson').val();
	 var reqRemark = jQuery('#reqRemark').val();
	 var oldReqId = jQuery('#oldReqId').val();
	 var productIdss = jQuery('#products').val();
	 if(productIdss==null||productIdss==""){}else{
	 var arrSup = getValues(productIdss,"Pro");
	 if(arrSup == null||arrSup==""){return false;}
	 var supId = getValue(productIdss,"supId");
	 if(supId == null||supId==""){return false;}
	 var supName = getValues(productIdss,"supName");
	 if(supName == null||supName==""){return false;}
	 var proId = getValues(productIdss,"proId");
	 if(proId == null||proId==""){return false;}}
	 var oldids = jQuery('#oldproIds').val();
	 if(oldids==null||oldids==""){}else{
	 var oldArrSup = getValues(oldids,"oldSupId");
	 if(oldArrSup == null||oldArrSup==""){return false;}
	 var oldSupName = getValues(oldids,"oldSupName");
	 if(oldSupName == null||oldSupName==""){return false;}
	 var oldProId = getValues(oldids,"oldProId");
	 if(oldProId == null||oldProId==""){return false;}
	 }
	 var param="reqName="+encodeURIComponent(reqName)+
	 	       "&reqCode="+encodeURIComponent(reqCode)+
	 	       "&productIds="+encodeURIComponent(proId)+
	 	       "&supName="+encodeURIComponent(supName)+
	 	       "&arrSup="+encodeURIComponent(arrSup)+
	 	       "&oldArrSup="+encodeURIComponent(oldArrSup)+
	 	       "&oldids="+encodeURIComponent(oldids)+
	 	       "&oldSupName="+encodeURIComponent(oldSupName)+
	 	       "&oldProId="+encodeURIComponent(oldProId)+
	 	       "&oldReqId="+encodeURIComponent(oldReqId)+
	 	       "&reqRemark="+encodeURIComponent(reqRemark);
     var url="";
     url = "<%=contextPath %>/SpringR/purchase/updateRequest?"+param;
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
  
   
  function initTime(){
  var beginTimePara = {
      inputId:'beginTime1',
      property:{isHaveTime:false},
      bindToBtn:'beginTimeImg'
  };
  new Calendar(beginTimePara);
}
  var nameArray = null;
   function chooseProduct(idArray,type){
	    nameArray = idArray;
	 	openDialogResize('selectPro.jsp',  520, 400);
	 	var productIds = jQuery('#products').val();
 	}
function diogShow(proName){
  	 var url="<%=contextPath%>/springViews/erp/purchase/suppRequest.jsp?proName="+proName;
 
 	var str=openDialogResize(url,  520, 400);
  }
  function olddeletes(a){
	jQuery('#old'+a).remove();
	jQuery('#oldpros_table'+a).remove();
	jQuery('#oldtrs'+a).remove();
	
}	
function deletes(a){
	jQuery('#'+a).remove();
	jQuery('#pros_table'+a).remove();
	jQuery('#trs'+a).remove();
	
}	
function deletes1(a){
	jQuery('#new'+a).remove();
	jQuery('#'+a+'oldSupId').remove();
	jQuery('#'+a+'oldPro').remove();
	jQuery('#'+a+'oldSupName').remove();
	
}	
var oldproId="";
function details(){
jQuery('#oldReqId').val(reqId)
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
					  jQuery('#beginTime1').val(item.reqDate);
					  jQuery('#reqRemark').val(item.reqRemark);
					  var parm=item.supId+","+encodeURIComponent(item.proName)+",1";
					  var tds = "<tr id='old"+item.pro_id+"'><td align='center' nowrap><input type='hidden' id='"+item.pro_id+"oldSupId'><input type='hidden' id='"+item.pro_id+"oldPro'><input type='hidden' id='"+item.pro_id+"oldSupName'><input type='hidden' id='"+item.pro_id+"oldProId'>"+item.proCode+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.proName+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.proType+"</td>";//计量单位
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//单价
					  tds += "<td align='center' nowrap>"+item.proPrice+"</td>";//产品型号
					  tds += "<td align='center' nowrap><input type='button' value='删除' class='BigButton' onclick=\"olddeletes('"+item.pro_id+"')\"><input type='button' value='查询' class='BigButton' onclick=\"diogShow('"+parm+"')\"></td></tr><tr class='TableHeader' id='oldtrs"+item.pro_id+"'><td></td><td>供货商名称</td><td>电话</td><td>手机</td><td>价格</td><td>操作</td></tr><tr><td colspan='6' align='center'>  <table id='oldpros_table"+item.pro_id+"' align='center' width='80%'></table></td></tr>";
					  jQuery('#pro_table').append(tds);
					  if(oldproId!=""){
					  	oldproId+=",";
					  }
					  oldproId+=item.pro_id
					 oldname="";
					 oldsuppIds="";
					 oldproIds="";
						supplier(item.proName,item.reqId);  
		  
				});
				 jQuery("#oldproIds").val(oldproId);
			}
	   }
	 });
}
   
  var oldids="";
  var oldproIds="";
  var oldsuppIds="";
  var suppIds="";
  var oldname="";
  function supplier(proName,reqId){
  proName=encodeURIComponent(proName);
  var url="<%=contextPath%>/SpringR/purchase/getRequestByProId?proName="+proName+"&reqId="+reqId;
		jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){ 
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					  var tds = "<tr id='new"+item.pro_id+"'><td align='center' nowrap>"+item.supName+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.supPhone+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.mobile+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.sp_price+"</td>";//计量单位
					  tds += "<td align='center' nowrap><input type='hidden' id ='"+item.supId+"SupId' value='"+item.supId+"'><input type='button' value='删除' class='BigButton' onclick=\"deletes1('"+item.pro_id+"')\"></td></tr>";
					
		  			if (suppIds != "") {
				suppIds += ",";
			}
			if (oldids != "") {
				oldids += ",";
			}
			oldids+=item.pro_id;
			if (oldproIds != "") {
				oldproIds += ",";
			}
			if (oldsuppIds != "") {
				oldsuppIds += ",";
			}
			oldproIds+=item.supId+","+item.pro_id;
			oldsuppIds +=item.pro_id+","+item.supId;
			if (oldname != "") {
				oldname += ",";
			}
			       oldname  +=item.supId+","+item.supName;
		        	jQuery('#'+item.pro_id+"oldSupName").val(oldname);
		        	jQuery('#oldids').val(oldids);
					jQuery('#'+item.pro_id+"oldPro").val(oldsuppIds);
					jQuery('#'+item.pro_id+"oldSupId").val(oldsuppIds);
					jQuery('#'+item.pro_id+"oldProId").val(oldproIds);
				    jQuery('#oldpros_table'+item.pro_id).append(tds);
		
				});
					
			}
	   }
	 });
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
 <input type="hidden" id="oldReqId" name="oldReqId" >
 <input type="hidden" id="suppId" name="suppId" >
 <input type="hidden" id="oldproIds" name="oldproId">
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
       <input type="button" value="更新" class="BigButton" onclick="doSubmitForm()">
       <%
       	if(flow.equals("1")){
       %>
        <input id="closeButton" type="button" value="关闭"  class="BigButton" onclick="javascript:window.close();">
      	<%} %>
      </td>
    </tr>
  </table>
</form>
</body>
</html>