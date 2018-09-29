<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String purId = request.getParameter("purId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增仓库收货单信息</title>
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
	getCusMsg();
	initTime();
	getAutoCode();
}


function getAutoCode(){
	var url = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE2';
	jQuery.ajax({
		type:'POST',
		url:url,
		success:function(data){
			jQuery('#ppoCode').val(data);
		}
	});
}

 function initTime(){
  var beginTimePara = {
      inputId:'purDate',
     property:{isHaveTime:false},
      bindToBtn:'purDateImg'
  };
  new Calendar(beginTimePara);
}



 var pro_ids="";
 var proNum="";
function getCusMsg(){
	var url = "<%=contextPath %>/SpringR/warehouse/getPurs?purId=<%=purId%>";
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
				if(proNum != ''){
						proNum += ',';
					}
				jQuery('#purCode').val(item.ppo_code);
				jQuery('#purTitle').val(item.ppoTitle);
				jQuery('#supPerson').val(item.sup_name);
				jQuery('#supAddress').val(item.address);
				jQuery('#supPhone').val(item.phone);
				jQuery('#supMobile').val(item.mobile);
				jQuery('#supZipCode').val(item.zip_code);
				jQuery('#supId').val(item.sup_id);
				jQuery('#ppoId').val(item.ppo_id);
			var tds = "<tr><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					tds += "<td align='center' nowrap>"+item.purchase_num+"</td>";//订单数量
					tds += "<td align='center' nowrap>"+item.already_purchase_num+"</td>";//已发数量
					tds += "<td align='center' nowrap><input type='text' id='"+item.proId+"' class='BigInput' size='10' maxlength='10' onblur=\"checkInt('"+item.proId+"')\"></td></tr>";//发货数量
					jQuery('#pro_table').append(tds);
	   				pro_ids += item.proId;
	   				proNum+=item.already_purchase_num;
	  
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

	  var ppoCode = encodeURIComponent(jQuery('#ppoCode').val());
	  var ppoStatus = encodeURIComponent(jQuery('#ppoStatus').val());
	  var supId = jQuery('#supId').val();
	  var purDate = jQuery('#purDate').val();
	  var ppoId = jQuery('#ppoId').val();

	  var ppoRemark = encodeURIComponent(jQuery('#ppoRemark').val());
	  
	 var productSendNum = getValue(pro_ids);
	 if(productSendNum == '0'){return false;}
	 
	 var para = "ppoCode="+ppoCode+"&ppoStatus="+ppoStatus
	 			+"&purId=<%=purId%>&pro_ids="+pro_ids
	 			+"&supId="+supId+"&productSendNum="+productSendNum
	 			+"&purDate="+purDate
	 			+"&alreadyNum="+proNum
	 			+"&ppoId="+ppoId
	 			+"&ppoRemark="+ppoRemark;
	 			
     var url="<%=contextPath %>/SpringR/warehouse/addPur?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="purShowDetial.jsp";
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
	       新增仓库收货单信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="supId" name="supId">
 <input type="hidden" id="ppoId" name="ppoId">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">对应订单编号：</td>
      <td class="TableData" >
       <input id="purCode" name="purCode" type="text" value="" readOnly class="BigInput">
      </td>
      <td nowrap class="TableData">对应订单主题：</td>
      <td class="TableData" >
       <input id="purTitle" name="purTitle" type="text" value="" readOnly class="BigInput">
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">发货单编号：</td>
      <td class="TableData" >
       <input id="ppoCode" name="ppoCode" type="text" value="" class="BigInput"> 
      </td>
      <td nowrap class="TableData" style="display: none">收货单状态：</td>
      <td class="TableData" style="display: none">
			<select id="ppoStatus" name="ppoStatus">
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
      <td nowrap class="TableData">发货方：</td>
      <td class="TableData" >
       <input id="supPerson" name="supPerson" type="text" value="" readOnly class="BigInput">
      </td>
       <td nowrap class="TableData">发货地址：</td>
      <td class="TableData" >
       <input id="supAddress" name="supAddress" type="text" value="" readOnly class="BigInput">
      </td>
    </tr>
    
     <tr>
     <td nowrap class="TableData">联系电话：</td>
      <td class="TableData" >
       <input id="supPhone" name="supPhone" type="text" value="" readOnly class="BigInput">
      </td>
       <td nowrap class="TableData">联系手机：</td>
      <td class="TableData" >
       <input id="supMobile" name="supMobile" type="text" value="" readOnly class="BigInput">
      </td>
     
    </tr>
     
    
    <tr>
     <td nowrap class="TableData">邮编：</td>
	      <td class="TableData" >
	       <input id="supZipCode" name="supZipCode" type="text" value="" readOnly class="BigInput">
	      </td>
      <td nowrap class="TableData">收货时间：</td>
       <td class="TableData"  colspan="2">
        <input type="text" id="purDate" name="purDate" size="19" maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="purDateImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="ppoRemark" name="ppoRemark" class="BigInput"></textarea>
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
		    	<td align='center' nowrap>已收数量</td>
		    	<td align='center' nowrap>收货数量</td>
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