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

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

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
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					jQuery('#warehouse').val(item.warehouse.id);
					jQuery('#pro_code').val(item.product.proCode);
					jQuery('#pro_name').val(item.product.proName);
					jQuery('#pro_type').val(item.product.proType);
					jQuery('#unit_name').val(item.product.pUnit.unitName);
					jQuery('#price').val(item.price);
					jQuery('#number').val(item.number);
					jQuery('#total').val(item.total);
					jQuery('#remark').val(item.remark);
				});
			}
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


   function doSubmitForm(formObject) {
<%--	  if($("#unit_name").val() == ""){ --%>
<%--	    alert("计量单位名称不能为空！");--%>
<%--	    $("#unit_name").focus();--%>
<%--	    $("#unit_name").select();--%>
<%--	    return false;--%>
<%--	  }--%>

	 accMul();
	 var warehouseId = jQuery('#warehouse').val();
	 var price = jQuery('#price').val();
	 var number = jQuery('#number').val();
	 var total = jQuery('#total').val();
	 var remark = jQuery('#remark').val();
	 
	 var para = "id=<%=id%>&wh_id="+warehouseId+"&price="+price+"&number="+number+"&total="+total+"&remark="+remark;
     var url="<%=contextPath %>/SpringR/db/updateDB?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("更新成功");
		    window.location.href="dbManage.jsp";
		  }
		  else{
			   alert("更新失败"); 
		  }
	   }
	 });
   }
   
  
     function accMul(){
	var price = jQuery('#price').val();
	var number = jQuery('#number').val();
	var m=0;s1=price.toString();
	try{m+=s1.split('.')[1].length}catch(e){}
	var total = Number(s1.replace('.',''))*Number(number)/Math.pow(10,m);
	jQuery('#total').val(total);
}
       function checkFloat(){
	   var megx = /^\d+(\.\d+)?$/;
	   var price = jQuery('#price').val();
	   if(!megx.test(price)){
		   alert('请输入正确的数');
		   jQuery('#price').val('');
		   return;
	   }
   }
   function checkInt(){
	   var megx = /^[0-9]*[1-9][0-9]*$/;
	   var number = jQuery('#number').val();
	   if(!megx.test(number)){
		   alert('请输入正确的数');
		   jQuery('#number').val('');
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
 <input type="hidden" id="products" name="products">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">选择仓库：</td>
      <td class="TableData" >
       <select id="warehouse" name="warehouse">
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
    <td nowrap class="TableData">单价：</td>
       <td class="TableData" >
 		<input id="price" name="price" type="text" value="" onblur="checkFloat();">
      </td>
    </tr>
     <tr>
    <td nowrap class="TableData">数量：</td>
       <td class="TableData" >
 		<input id="number" name="number" type="text" value="" onblur="checkInt();">
      </td>
    </tr>
     <tr>
    <td nowrap class="TableData">总额：</td>
       <td class="TableData" >
 		<input id="total" name="total" type="text" value="" title="点击自动计算" onclick="accMul();">
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" ></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>