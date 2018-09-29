<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String po_id = request.getParameter("po_id");
	String order_id = request.getParameter("order_id");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增仓库发货单信息</title>
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
<script type="text/javascript">

function doInit(){
	getProductList();
	getCusMsg();
	initTime();
	getAutoCode();
}

function getAutoCode(){
	var url = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE1';
	jQuery.ajax({
		type:'POST',
		url:url,
		success:function(data){
			jQuery('#pod_code').val(data);
		}
	});
}

 function initTime(){
  var beginTimePara = {
      inputId:'pod_date',
<%--      property:{isHaveTime:true},--%>
      bindToBtn:'pod_dateImg'
  };
  new Calendar(beginTimePara);
}
 
function getCusMsg(){
	var url = "<%=contextPath %>/SpringR/warehouse/getCusMsg?order_id=<%=order_id%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			jQuery('#cus_id').val(json.cus_id);
			jQuery('#order_code').val(json.order_code);
			jQuery('#order_title').val(json.order_title);
			jQuery('#person').val(json.person);
			jQuery('#address').val(json.address);
			jQuery('#phone').val(json.phone);
			jQuery('#zip_code').val(json.zip_code);
	   }
	 });
}

var pro_ids = '';
function getProductList(){
	var url = "<%=contextPath %>/SpringR/warehouse/getProductList?po_id=<%=po_id%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					if(pro_ids != ''){
						pro_ids += ',';
					}
					var tds = "<tr><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					tds += "<td align='center' nowrap>"+item.order_num+"</td>";//订单数量
					tds += "<td align='center' nowrap>"+item.already_send_num+"</td>";//已发数量
					tds += "<td align='center' nowrap><input type='text' id='"+item.id+"' class='BigInput' size='10' maxlength='10' onblur=\"checkInt('"+item.id+"')\"></td></tr>";//发货数量
					jQuery('#pro_table').append(tds);
					pro_ids += item.id;
				});
			}
	   }
	 });
	
}

function getValue(ids)
  {
	  var arr = ids.split(',');
	  var value = "";
	  for(var i=0;i<arr.length;i++){
		  var v = jQuery('#'+arr[i]).val();
		  if(v == "" || v == null){
			  alert('还有项未填写');
			  jQuery('#'+arr[i]).focus();
			  jQuery('#'+arr[i]).select();
			  return '0';
		  }
		  if(value != ""){
			  value+=",";
		  }
		  value += v;
	  }
	  return value;
  }

   function doSubmitForm(formObject) {
<%--	  if($("#unit_name").val() == ""){ --%>
<%--	    alert("计量单位名称不能为空！");--%>
<%--	    $("#unit_name").focus();--%>
<%--	    $("#unit_name").select();--%>
<%--	    return false;--%>
<%--	  }--%>

	  var person = encodeURIComponent(jQuery('#person').val());
	  var address = encodeURIComponent(jQuery('#address').val());
	  var phone = jQuery('#phone').val();
	  var zip_code = jQuery('#zip_code').val();
	  
	  var pod_code = jQuery('#pod_code').val();
	  var pod_status = encodeURIComponent(jQuery('#pod_status').val());
<%--	  var cus_id = jQuery('#cus_id').val();--%>
	  var pod_date = jQuery('#pod_date').val();
	  var pod_sender = encodeURIComponent(jQuery('#pod_sender').val());
	  var pod_send_way = encodeURIComponent(jQuery('#pod_send_way').val());
	  var total = jQuery('#total').val();
	  var remark = encodeURIComponent(jQuery('#remark').val());
	  
	 var productSendNum = getValue(pro_ids);
	 if(productSendNum == '0'){return false;}
	 
	 var para = "pod_code="+pod_code+"&pod_status="+pod_status
	 			+"&order_id=<%=order_id%>&po_id=<%=po_id%>&pro_ids="+pro_ids
	 			+"&person="+person+"&address="+address+"&phone="+phone+"&zip_code="+zip_code+"&productSendNum="+productSendNum
	 			+"&pod_date="+pod_date+"&pod_sender="+pod_sender
	 			+"&pod_send_way="+pod_send_way+"&total="+total+"&remark="+remark;
	 			
     var url="<%=contextPath %>/SpringR/warehouse/addPOD?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="whShowDetial.jsp";
		  }
		  else{
			   alert("添加失败"); 
		  }
	   }
	 });
   }
   
   function checkInt(id){
	   var megx = /^[0-9]*[1-9][0-9]*$/;
	   var number = jQuery('#'+id).val();
	   if(!megx.test(number)){
		   alert('请输入正确的数');
		   jQuery('#'+id).val('');
		   return;
	   }
   }
  function checkFloat(id){
	   var megx = /^\d+(\.\d+)?$/;
	   var price = jQuery('#'+id).val();
	   if(!megx.test(price)){
		   alert('请输入正确的数');
		   jQuery('#'+id).val('');
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
	       新增仓库发货单信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="cus_id" name="cus_id">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">对应订单编号：</td>
      <td class="TableData" >
       <input id="order_code" name="order_code" type="text" value="" readOnly>
      </td>
      <td nowrap class="TableData">对应订单主题：</td>
      <td class="TableData" >
       <input id="order_title" name="order_title" type="text" value="" readOnly>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">发货单编号：</td>
      <td class="TableData" >
       <input id="pod_code" name="pod_code" type="text" value="">
      </td>
      <td nowrap class="TableData" style="display: none">发货单状态：</td>
      <td class="TableData" style="display: none">
			<select id="pod_status" name="pod_status">
				<option value="<%=oa.spring.util.StaticData.NEW_CREATE%>">新建</option>
				<option value="<%=oa.spring.util.StaticData.AUDITING%>">审核中</option>
				<option value="<%=oa.spring.util.StaticData.PASSING%>">审核通过</option>
				<option value="<%=oa.spring.util.StaticData.NO_PASSING%>">审核没通过</option>
				<option value="<%=oa.spring.util.StaticData.RUNNING%>">执行中</option>
				<option value="<%=oa.spring.util.StaticData.OVER%>">已完成</option>
			</select>
      </td>
    </tr>
    
     <tr>
      <td nowrap class="TableData">收货方：</td>
      <td class="TableData" >
       <input id="person" name="person" type="text" value="" >
      </td>
       <td nowrap class="TableData">收货地址：</td>
      <td class="TableData" >
       <input id="address" name="address" type="text" value="" >
      </td>
    </tr>
    
     <tr>
     <td nowrap class="TableData">联系方式：</td>
      <td class="TableData" >
       <input id="phone" name="phone" type="text" value="" >
      </td>
      <td nowrap class="TableData">邮编：</td>
	      <td class="TableData" >
	       <input id="zip_code" name="zip_code" type="text" value="" >
	      </td>
    </tr>
     
    
     <tr>
      <td nowrap class="TableData">物流公司：</td>
      <td class="TableData" >
       <select id="pod_send_way" name="pod_send_way">
       		<option value="">请选择</option>
       		<option value="顺丰">顺丰</option>
       		<option value="申通">申通</option>
       </select>
      </td>
      <td nowrap class="TableData">总运费：</td>
      <td class="TableData" >
       <input id="total" name="total" type="text" value="0" onblur="checkFloat('total')">
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">发货人：</td>
      <td class="TableData" >
       <input id="pod_sender" name="pod_sender" type="text" value="">
      </td>
      <td nowrap class="TableData">发货时间：</td>
       <td class="TableData"  colspan="2">
        <input type="text" id="pod_date" name="pod_date" size="19" maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="remark" name="remark"></textarea>
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData" colspan="4">发货明细：</td>
    </tr>
      <tr>
      <td colspan="4" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>订单数量</td>
		    	<td align='center' nowrap>已发数量</td>
		    	<td align='center' nowrap>发货数量</td>
		    	</tr>
		    </table>
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