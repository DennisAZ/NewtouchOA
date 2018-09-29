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
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script  type="text/javascript"  src="obejctJs1.js"></script>
<script type="text/javascript">
var purId="<%=purId%>";
var arrMainPro=new Array();
var arrChildPro="";
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
 var productIds="";
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
				var arrPro=new Array();
				jQuery('#purCode').val(item.ppo_code);
				jQuery('#purTitle').val(item.ppoTitle);
				jQuery('#supPerson').val(item.sup_name);
				jQuery('#supAddress').val(item.address);
				jQuery('#supPhone').val(item.phone);
				jQuery('#supMobile').val(item.mobile);
				jQuery('#supZipCode').val(item.zip_code);
				jQuery('#supId').val(item.sup_id);
				jQuery('#ppoId').val(item.ppo_id);
					var  cfgs={
									db_id:"2",
									wh_id:"3",
									number:0
									}
						    var productOut=new ProductOut(cfgs);
						    arrPro.push(productOut);
							var  mainCfgs={
									  pro_id :item.pro_id,//产品id
									  wh_id :'0',//仓库id
								      price :item.pro_price,//单价
								      productOut:arrPro,
								      purchase_num:item.purchase_num,//采购数量;
								      pur_id:purId,//采购单id
								   	  purchase_send_num:item.already_purchase_num,//已经收货数量
								      ppro_id:item.pproId,//产品关联id
								   	  ppo_id:item.ppo_id,//出货单id
									}
						    var pd=new PD(mainCfgs);
							arrMainPro.push(pd);
							var add="add"+item.pro_id;
			var tds = "<tr ><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					tds += "<td align='center' nowrap>"+item.purchase_num+"</td>";//订单数量
					tds += "<td align='center' nowrap>"+item.already_purchase_num+"</td>";//已收数量
					var nums=0;
					nums=parseFloat(item.purchase_num)-parseFloat(item.already_purchase_num);
					tds += "<td align='center' nowrap ><input type='text' id='"+item.pro_id+"' class='BigInput' size='10' readOnly maxlength='10' value='"+productOut.number+"' ></td>";//收货数量
					tds += "<td align='center' nowrap id='"+item.pro_id+"' ><span id='"+add+"'></span></td>";//库存
					tds += "<td align='center' nowrap><img style='cursor:pointer' align='absmiddle' title='选择仓库' src='/yh/core/styles/style1/img/notify_new.gif' onclick=\"diogShow('"+item.pro_id+"',"+nums+","+i+")\"></td></tr>";//入库管理
					jQuery('#pro_table').append(tds);
	  				if(productIds!=""){
	  					productIds+=",";
	  				}
	  				productIds+=item.pro_id;
			 });
	 	 }
	  	}
	  });
}
 function selectBatch(proId){
 	var dbNum=jQuery('#'+proId).val();
 	if(dbNum=="0"||dbNum==0){
 		alert("请先选择入库数量！");
 		return false;
 	}
 } 
function deletes(proId,j){
var num=jQuery('#'+proId).val();
for(var i=0;i<arrMainPro.length;i++){
if(proId==arrMainPro[i].pro_id){
var nums= parseFloat(arrMainPro[i].productOut[j].number);
	arrMainPro[i].productOut.remove(j);
	num=parseFloat(num)-parseFloat(nums);
		}
	}
		jQuery('#'+proId).val(num);
	var redd=proId+j
	jQuery('#'+redd).remove();

	
}
  var str="";
																																											
function updateObject(){
	for(var i=0;i<arrMainPro.length;i++){
		for(var j=1;j<arrMainPro[i].productOut.length;j++){
		if (str!= "") {
				str += ";";
		}
		str+=arrMainPro[i].pro_id+","+arrMainPro[i].price+","+arrMainPro[i].productOut[j].db_id+","+arrMainPro[i].productOut[j].wh_id+","+arrMainPro[i].productOut[j].number+","+arrMainPro[i].purchase_num+","+arrMainPro[i].pur_id+","+arrMainPro[i].purchase_send_num+","+arrMainPro[i].ppro_id+","+arrMainPro[i].ppo_id+","+arrMainPro[i].productOut[j].batch+","+arrMainPro[i].productOut[j].invalid_time;;
		}
	}
	return str;
}


    function doSubmitForm(formObject) {
    var ppoCode = encodeURIComponent(jQuery('#ppoCode').val());
	  var ppoStatus = encodeURIComponent(jQuery('#ppoStatus').val());
	  var supId = jQuery('#supId').val();
	  var purDate = jQuery('#purDate').val();
     var detailStr = updateObject(); //收货明细
    
    if(purDate == null || purDate == ''){
	  	alert('请选择收货时间');
	  	jQuery('#purDate').focus();
		jQuery('#purDate').select();
		return false;
	  }
    if(detailStr == null || detailStr == ''){
    	alert('请填写收货明细信息');
		return false;
    }
	  var ppoId = jQuery('#ppoId').val();
	  var ppoRemark = encodeURIComponent(jQuery('#ppoRemark').val());
	 var para = "ppoCode="+ppoCode+"&ppoStatus="+ppoStatus
	 			+"&arrChildPro="+str
	 			+"&supId="+supId
	 			+"&ppoId="+ppoId
	 			+"&purId="+purId
	 			+"&purDate="+purDate
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
   function checkFloatNum(id){
	   var megx = /^\d+(\.\d+)?$/;
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
   function diogShow(proId,purchase_num,i){
  	var url="<%=contextPath%>/springViews/erp/warehouse/whIn.jsp?proId="+proId+"&k="+i+"&orderNum="+purchase_num;
 	var str=openDialogResize(url,  1200, 400);
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
 <input type="hidden" id="dbIds" name="dbIds">
 <input type="hidden" id="ppoId" name="ppoId">
 <input type="hidden" id="arrChildPro" name="arrChildPro">
 <input type="hidden" id="arrChildPro1" name="arrChildPro1">
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
      <td nowrap class="TableData">收货单编号：</td>
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
      <td nowrap class="TableData">收货时间：<font color="red">*</font></td>
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
      <td nowrap class="TableData" colspan="4">收货明细：</td>
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
		    	<td align='center' nowrap>库存[仓库名称/入库数量]</td>
		    	<td align='center' nowrap>操作</td>
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