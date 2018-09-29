<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>更新仓库信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">jQuery.noConflict();</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var batch='';
function doInit(){
getWareHouse();
	var url = "<%=contextPath %>/SpringR/db/getDBMsgById?id=<%=id%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
					jQuery('#warehouse').val(json.wh_id);
					jQuery('#proId').val(json.proId);
					jQuery('#pro_code').val(json.pro_code);
					jQuery('#pro_name').val(json.pro_name);
					jQuery('#pro_type').val(json.pro_type);
					jQuery('#unit_name').val(json.unit_name);
					jQuery('#price').val(json.price);
					jQuery('#number').val(json.num);
					jQuery('#batch').val(json.batch);
					jQuery('#invalid_time').val(json.invalid_time);
					batch = json.batch;
					var imgTime = "InvalidImg";
					var timePara = {
	    				inputId:"invalid_time",
	   					property:{isHaveTime:false},
	    				bindToBtn:imgTime
  					};
 					new Calendar(timePara);
	   }
	 });
	 	
}

function getWareHouse(){
	var url = "<%=contextPath %>/SpringR/db/getWareHouse/"+new Date().getTime();
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					jQuery('#warehouse').append("<option value='"+item.id+"'>"+item.name+"</option>");
				});
			}
	   }
	 });
	
}
 function checkFloat(id,suffix){
	   var megx = /^\d+(\.\d+)?$/;
	   var v = jQuery('#'+id+suffix).val();
	   if(!megx.test(v)){
		   alert('请输入正确的数');
		   jQuery('#'+id+suffix).val('');
		   return;
	   }
   }
   
   function checkBatch(){
	   var megx = /^[u0391-uffe5]+$/;
	   var v = jQuery('#batch').val();
	   //如果值没有改变就不进行比对
	   if(v == batch){
	   	return;
	   }
	   if(v !="" && !megx.test(v)){
		   alert('请勿输入中文');
		   jQuery('#batch').val(batch);
		   return;
	   }
	  
	  //判断输入的批次编号相对于当前库存的产品是否唯一
	  var whId = jQuery('#warehouse').val();
	  var proId = jQuery('#proId').val();
	  var url = "<%=contextPath %>/SpringR/db/checkBatckOnly?whId="+whId+"&proId="+proId+"&batch="+v;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
	   			if(jsonData == '0'){
	   				alert('该批次编号已存在！');
		   			jQuery('#batch').val(batch);
		   			return;
	   			}
	   }
	 });
   }

   function doSubmitForm(formObject) {

	 var price = jQuery('#price').val();
	 var number = jQuery('#number').val();
	 var batch = jQuery('#batch').val();
	 var invalid_time = jQuery('#invalid_time').val();
	 
	 var para = "id=<%=id%>&batch="+batch+"&price="+price+"&number="+number+"&invalid_time="+invalid_time;
     var url="<%=contextPath %>/SpringR/db/updateDB?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("更新成功");
		    window.close();
		  }
		  else{
			   alert("更新失败"); 
		  }
	   }
	 });
   }
   
  
       function checkFloat(suffix){
	   var megx = /^\d+(\.\d+)?$/;
	   var price = jQuery('#'+suffix).val();
	   if(!megx.test(price)){
		   alert('请输入正确的数');
		   jQuery('#'+suffix).val('');
		   return;
	   }
   }
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       更新仓库信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="proId" name="proId">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">选择仓库：</td>
      <td class="TableData" >
       <select id="warehouse" name="warehouse" disabled=true>
       </select>
      </td>
    </tr>
    </tr>
    <tr>
    <td nowrap class="TableData">产品编号：</td>
       <td class="TableData" >
 		<input  id="pro_code" name="pro_code" type="text" value="" readOnly>
      </td>
    </tr>
     <tr>
    <td nowrap class="TableData">产品名称：</td>
       <td class="TableData" >
 		<input  id="pro_name" name="pro_name" type="text" value="" readOnly>
      </td>
    </tr>
     <tr>
    <td nowrap class="TableData">规格型号：</td>
       <td class="TableData" >
 		<input id="pro_type" name="pro_type" type="text" value="" readOnly>
      </td>
    </tr>
     <tr>
    <td nowrap class="TableData">计量单位：</td>
       <td class="TableData" >
 		<input id="unit_name" name="unit_name" type="text" value="" readOnly>
      </td>
    </tr>
     <tr>
    <td nowrap class="TableData">入库单价：</td>
       <td class="TableData" >
 		<input id="price" name="price" type="text" value="" onblur="checkFloat('price');">
      </td>
    </tr>
     <tr>
    <td nowrap class="TableData">数量：</td>
       <td class="TableData" >
 		<input id="number" name="number" type="text" value="" onblur="checkFloat('number');">
      </td>
    </tr>
     <tr>
    <td nowrap class="TableData">批次编号：</td>
       <td class="TableData" >
 		<input id="batch" name="batch" type="text" value="" onblur="checkBatch();">
      </td>
    </tr>
     
      <tr>
      <td nowrap class="TableData">失效日期：</td>
       <td class="TableData" >
 		<input type='text' id='invalid_time' size='10' maxlength='20' class='BigInput' value=''><img src='<%=imgPath%>/calendar.gif' id='InvalidImg' align='absMiddle' border='0' style='cursor:pointer' > 
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
        <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>