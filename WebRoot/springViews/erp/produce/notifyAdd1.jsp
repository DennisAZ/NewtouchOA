<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String craftsId = request.getParameter("craftsId");
	String pro_name = request.getParameter("pro_name");
	String orderCode = request.getParameter("orderCode");
	String orderId = request.getParameter("order_id");
		if("".equals(orderId)||orderId==null){
			orderId="-1";
		}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增生产通知单</title>
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
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript">

function doInit(){
	var beginTimePara = {
      inputId:'beginTime',
      property:{isHaveTime:false},
      bindToBtn:'beginTimeImg'
  };
  new Calendar(beginTimePara);
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
 		
   function doSubmitForm(formObject) {
	 
	 var code = jQuery('#code').val();
	  var type = jQuery('#type').val();
	 var beginTime = jQuery('#beginTime').val();
	 var remark = jQuery('#remark').val();
	 var productIds = jQuery('#products').val();
	 var numbers = getValue(productIds,"Number");
	  var trueDate = getValue(productIds,"beginTime");
	 if(numbers == '-1'){return false;}
	 var para = "proId="+productIds+"&beginTime="+encodeURIComponent(beginTime)+"&trueDate="+encodeURIComponent(trueDate)+"&code="+encodeURIComponent(code)+"&type="+encodeURIComponent(type)+"&numbers="+numbers+"&remark="+encodeURIComponent(remark)+"&type_id=<%=orderId%>";
     var url="<%=contextPath %>/SpringR/produce/addNotify?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="notifyManage.jsp";
		  }
		  else{
			   alert("添加失败"); 
		  }
	   }
	 });
   }
   
   function checkFloat(id){
	    var megx = /^\d+(\.\d+)?$/;
	   var number = jQuery('#'+id+'Number').val();
	   if(!megx.test(number)){
		   alert('请输入正确的数');
		   jQuery('#'+id+'Number').val('');
		   return;
	   }
   }
  function getValue(ids,suffix)
  {
  	if(ids == ""){
  		//表示目前没有选择产品
  		alert("请先选择产品");
  		return '-1';
  	}
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
  var nameArray = null;
   function chooseProduct(idArray,type){
	    nameArray = idArray;
	    var url="nframe.jsp?shortName=-1";
	 	openDialogResize(url,800, 600);
	 	var productIds=jQuery('#products').val();
	 	times(productIds,"beginTime");
 	}
 	 function showCrafts(craftsId){
	    var url="craftsDetail.jsp?craftsId="+craftsId+"&falg=2";
	 	openDialogResize(url,500, 300);
 	}
 	 function showDrawings(drawingId){
	    var url="drawingShow.jsp?drawingId="+drawingId;
	 	openDialogResize(url,800, 500);
 	}
 	
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增生产通知单
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="products" name="products">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">通知单编号：</td>
       <td class="TableData">
 		<input type="text" name="code" id="code"  class="BigInput" />
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">通知单来源：</td>
       <td class="TableData">
 		<select id="type" disabled=true>
 			<option value="销售">销售</option>
 			<option value="其他">其他</option>
 		</select>
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData">订单编号：</td>
      	<td>
 			<span id="PCode"><a href='<%=contextPath%>/springViews/erp/order/detail.jsp?orderId=<%=orderId%>' target='view_window'><%=orderCode%></a></span>
 			<input type="hidden" name="type_id" id="type_id" value="<%=orderId%>" class="BigInput" />
 			</td>
 	  </tr>
      <tr>
      <td nowrap class="TableData">生产时间：</td>
        <td class="TableData" >
   		<input type="text" id="beginTime" name="beginTime" size="19" maxlength="19" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" class="BigInput" cols="40"></textarea>
      </td>
    </tr>
    <tr>
       <td class="TableData" colspan="2">
 		<input type="button" value="+添加产品" class="BigButton" onclick="chooseProduct(['products'],0);">
      </td>
    </tr>
      <tr>
      <td colspan="2" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>计划生产数量</td>
		    	<td align='center' nowrap>交货时间</td>
		    	<td align='center' nowrap>工艺版本号</td>
		    	<td align='center' nowrap>图纸版本号</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
        <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1)">
      </td>
    </tr>
  </table>
</form>
</body>
</html>