<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String order_id = request.getParameter("order_id");
	String po_id = request.getParameter("po_id");
	String pod_id = request.getParameter("pod_id");
	String flow = request.getParameter("flow");//工作流中访问的标识
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>仓库发货单信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script  type="text/javascript"  src="map.js"></script>
<script type="text/javascript">
var flow='<%=flow%>';
var pod_id='<%=pod_id%>';

function doInit(){
	getPODMsg();
	getProductList();
	initTime();
	if(flow == '1'){
		jQuery('#closeButton').show();
		jQuery('#pod_code').attr("readonly","readonly");
	}
}

 function initTime(){
  var beginTimePara = {
      inputId:'pod_date',
      property:{isHaveTime:false},
      bindToBtn:'pod_dateImg'
  };
  new Calendar(beginTimePara);
 }
 
function getPODMsg(){
	var url = "<%=contextPath %>/SpringR/warehouse/getPODMsg?pod_id=<%=pod_id%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var item = eval("(" + data + ")");
			jQuery('#order_code').val(item.order_code);
			jQuery('#order_title').val(item.order_title);
			jQuery('#pod_code').val(item.pod_code);
			jQuery('#person').val(item.person);
			jQuery('#address').val(item.address);
			jQuery('#phone').val(item.phone);
			jQuery('#zip_code').val(item.zip_code);
			jQuery('#pod_send_way').val(item.logisId);
			jQuery('#total').val(item.total);
			jQuery('#userDesc').val(item.pod_sender);
			jQuery('#user').val(item.pod_sender_id);
			jQuery('#pod_date').val(item.pod_date);
			jQuery('#remark').val(item.remark);
				getLogistics(item.logisId);	
	   }
	 });
}
  function getLogistics(pod_send_way){

	var url = "<%=contextPath %>/SpringR/system/getSaleLogistics/"+new Date().getTime();
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					if(pod_send_way==item.id){
					jQuery('#pod_send_way').append("<option  value='"+item.id+"' selected>"+item.name+"</option>");
					}else{
					jQuery('#pod_send_way').append("<option  value='"+item.id+"'  >"+item.name+"</option>");
					}
					
				});
			}
	   }
	 });
	
}
var dbNumMap = new Map();//以dbId+","+whId+","+proId+","+batch组成的联合key，value是number，该产品该仓库下的值
function getProductList(){

	var url = "<%=contextPath %>/SpringR/warehouse/getProducts?pod_id=<%=pod_id%>";
	
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
				
					var tds = "<tr><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					tds += "<td align='center' nowrap>"+item.pro_type+"</td>";//产品规格
					tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//产品单位
					tds += "<td align='center' nowrap>"+item.order_num+"</td>";//总数量
					tds += "<td align='center' nowrap>"+item.already_send_num+"</td>";//已发数量
					tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"Number' class='BigInput' size='10' readOnly maxlength='10' value='"+item.pod_num+"' ></td>";//发货数量
			 		var add="add_"+item.pro_id;	
			 		var arrWhName="<span id='"+add+"'>";
					var url1 = "<%=contextPath %>/SpringR/warehouse/getNumbers?pod_id=<%=pod_id%>&proId="+item.pro_id;
					jQuery.ajax({
	 					  type : 'POST',
	   					  url : url1,
	  					  async:false,
	  					  success : function(jsonData1){   
						  var data1 = JSON.stringify(jsonData1);
						  var json1 = eval("(" + data1 + ")");
						  if(json1.length > 0){
						  jQuery(json1).each(function(j,item1){
							var key = item1.dbId+","+item1.wh_id+","+item.pro_id+","+item1.batch;
							arrWhName+="<a id='"+key+"'>"+item1.whName+"("+item1.batch+")["+item1.number+"]<img  align='absmiddle' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes('"+key+"','"+item.pro_id+"',"+item1.number+")\"></a>";
							dbNumMap.put(key,item1.number);
							});
							arrWhName+="</span>"
						  }		
	  				 	}
					 });
					tds += "<td align='center' nowrap>"+arrWhName+"</td>";
					tds += "<td align='center' nowrap ><img align='absmiddle' style='cursor:pointer'  src='/yh/core/styles/style1/img/notify_new.gif' onclick=\"diogShow('"+item.pro_id+"',"+item.order_num+","+item.already_send_num+")\"></td></tr>";//出库管理					
					jQuery('#pro_table').append(tds);
				});
			}		
	   }
	 });
}
 function diogShow(proId,orderNum,already_send_num){
		var nums=parseFloat(orderNum)-parseFloat(already_send_num)-parseFloat(jQuery('#'+proId+"Number").val());
  		var url="<%=contextPath%>/springViews/erp/warehouse/whOutUpdate.jsp?proId="+proId+"&orderNum="+nums;
 		openDialogResize(url, 1000, 800);
  }
function deletes(id,proId,number){
		var obj = document.getElementById(id);
		obj.parentNode.removeChild(obj);
		jQuery('#'+id).remove();
		jQuery('#'+proId+'Number').val(parseFloat(jQuery('#'+proId+'Number').val())-parseFloat(number));
		dbNumMap.removeByKey(id);//移除该记录
}

  function whValidate(){
	var userName=jQuery('#userDesc').val();
	var pod_date=jQuery('#pod_date').val();
	if(userName==null||userName==""){
		alert("请选择发货人！！");
		return false;
	}else if(pod_date==""||pod_date==null){
		alert("请选择发货时间！！");
		return false;
	}else{
		doSubmitForm();
	}
}

   function doSubmitForm() {
	  var str = "";
	  var keyArr = dbNumMap.keys();
	 for(var i=0;i<keyArr.length;i++){
	 	if(str != "")
	 	{
			str +=";"; 		
	 	}
	 	//dbId+","+whId+","+proId+","+batch+","+number
	 	var value = keyArr[i]+","+dbNumMap.get(keyArr[i]);
	 	str = str + value;	
	   }
	if(str == ""){
		alert("请选择出库信息！");
		return;
	}
	  var person = encodeURIComponent(jQuery('#person').val());//收货方
	  var address = encodeURIComponent(jQuery('#address').val());//收货地址
	  var phone = jQuery('#phone').val();//联系方式
	  var zip_code = jQuery('#zip_code').val();//邮编
	  var userId = jQuery('#user').val();//发货人id
	  var pod_code = encodeURIComponent(jQuery('#pod_code').val());//发货单编号
	  var pod_date = jQuery('#pod_date').val();//发货时间
	  var pod_sender = encodeURIComponent(jQuery('#userDesc').val());//发货人
	  var pod_send_way = encodeURIComponent(jQuery('#pod_send_way').val());//发货方式
	  var total = jQuery('#total').val();//总运费
	  var remark = encodeURIComponent(jQuery('#remark').val());
	  
	 
	 var para = "pod_code="+pod_code
	 			+"&pod_id=<%=pod_id%>&po_id=<%=po_id%>"
	 			+"&person="+person+"&address="+address+"&phone="+phone+"&zip_code="+zip_code
	 			+"&pod_date="+pod_date+"&pod_sender="+pod_sender+"&str="+encodeURIComponent(str)
	 			+"&pod_send_way="+pod_send_way+"&total="+total+"&remark="+remark+"&userId="+userId;
	 			
     var url="<%=contextPath %>/SpringR/warehouse/updatePOD";
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   data: para,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("更新成功");
		    if(flow == '1'){
		    	window.close();
		    }
		    else{
		    window.location.href="whShowDetial.jsp";
		    }
		  }
		  else{
			   alert("更新失败"); 
		  }
	   }
	 });
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
<input id="arrChildPro" name="arrChildPro" type="hidden">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       仓库发货单信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
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
       </select>
      </td>
      <td nowrap class="TableData">总运费：</td>
      <td class="TableData" >
       <input id="total" name="total" type="text" value="" onblur="checkFloat('total')">
      </td>
    </tr>
    
    <tr>
        <td nowrap class="TableData">发货人：</td>
       <td class="TableData" >
    	 <input type="hidden" name="user" id="user" value="" />
  <input type="text" name="userDesc" id="userDesc" style="vertical-align: top;" size="10" class="SmallStatic" size="10" value="" READONLY>
  <!--selectUser() 这个函数是多选  -->
  <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['user', 'userDesc']);">选择</a>
  <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
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
		    	<td align='center' nowrap>产品规格</td>
		    	<td align='center' nowrap>单位</td>
		    	<td align='center' nowrap>订单数量</td>
		    	<td align='center' nowrap>已发数量</td>
		    	<td align='center' nowrap>发货数量</td>
		    	<td align='center' nowrap>仓库名称(批次)[出库数量]</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="更新" class="BigButton" onclick="return whValidate();">
        <input id="closeButton" type="button" value="关闭" style="display: none" class="BigButton" onclick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>