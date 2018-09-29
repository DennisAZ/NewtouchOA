<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String reqId=request.getParameter("reqId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看采购申请单信息</title>
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
var reqId="<%=reqId%>"
function doInit(){
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
					  var tds = "<tr><td align='center' nowrap>"+item.proCode+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.proName+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.proType+"</td>";//计量单位
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//单价
					  tds += "<td align='center' nowrap>"+item.proPrice+"</td></tr><tr class='TableHeader' id='oldtrs"+item.pro_id+"'><td></td><td>供货商名称</td><td>电话</td><td>手机</td><td>价格</td><td>操作</td></tr><tr><td colspan='6' align='center'>  <table id='oldpros_table"+item.pro_id+"' align='center' width='80%'></table></td></tr><tr><td colspan='6' align='center'>  <table id='pros_table"+item.pro_id+"' align='center' width='80%'></table></td></tr>";//单价
					  jQuery('#pro_table').append(tds);
						supplier(item.proName,item.reqId);  
		  
				});
			}
	   }
	 });
}
   
  
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
					  var tds = "<tr><td align='center' nowrap>"+item.supName+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.supPhone+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.mobile+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.sp_price+"</td></tr>";//计量单位
					  
					  jQuery('#pros_table'+item.pro_id).append(tds);
		  
				});
			}
	   }
	 });
  }
function returnPage(){
	window.location.href="requestManage.jsp";
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
 <form action="" method="post" name="form1" id="form1">
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
       <td nowrap class="TableData">签约时间：</td>
       <td class="TableData"  colspan="2">
 		      <input type="text" id="beginTime1" name="beginTime1" size="19" maxlength="19" class="BigInput" value="" readonly>
      </td>
      <td nowrap class="TableData" style="display: none">订单状态：</td>
       <td class="TableData"  colspan="2" style="display: none">
 		<input type="text" name="reqStatus" id="reqStatus"  class="BigInput" readonly/>
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
  <table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	  产品信息
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
       <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
     
       <input type="button" value="返回" class="BigButton" onclick="returnPage()">
      </td>
    </tr>
  </table>
<br>
	<div id="listContainer" style="display:none;width:100;">
</div>
   
</form>
</body>
</html>