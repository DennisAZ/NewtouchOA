<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String purId = request.getParameter("purId");
	String pDeId = request.getParameter("pDeId");
	String flow = request.getParameter("flow");
	if(flow==null||flow.equals("")){
		flow="8";
	}
	if(purId==null||purId.equals("")){
		purId="8";
	}
	if(pDeId==null||pDeId.equals("")){
		pDeId="8";
	}
	String flag = request.getParameter("flag")==null?"":request.getParameter("flag");
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
var pDeId="<%=pDeId%>";
var purId="<%=purId%>";

var arrMainPro=new Array();
var arrChildPro="";
function doInit(){
	getPud();
	getPurPro();
	initTime();
}

 function initTime(){
  var beginTimePara = {
      inputId:'purDate',
     property:{isHaveTime:false},
      bindToBtn:'purDateImg'
  };
  new Calendar(beginTimePara);
}


function getPud(){
		var url = "<%=contextPath %>/SpringR/warehouse/getPod?pdId=<%=pDeId%>";
		jQuery.ajax({
			type:'POST',
			url:url,
			async:false,
			success:function(jsonData){
				var data=JSON.stringify(jsonData);
				var json=eval("("+data+")");
				jQuery('#purCode').val(json.ppo_code);
				jQuery('#purTitle').val(json.ppo_title);
				jQuery('#supPerson').val(json.sup_name);
				jQuery('#supAddress').val(json.address);
				jQuery('#supPhone').val(json.phone);
				jQuery('#supMobile').val(json.mobile);
				jQuery('#supZipCode').val(json.zip_code);
				jQuery('#supId').val(json.supId);
				jQuery('#ppoId').val(json.ppo_id);
				jQuery('#ppoCode').val(json.ppidCode);
				jQuery('#purDate').val(json.ppidDate);
				jQuery('#ppoRemark').val(json.ppidRemark);
			}
		})
}

var arrWhName="";
 var pro_ids="";
 var productIds="";
 var proNum="";
function getPurPro(){
var productOut="";
var orderNum=0;
var k=0;
		var url = "<%=contextPath %>/SpringR/warehouse/getPodPro?pdId=<%=pDeId%>";
		jQuery.ajax({
			type:'POST',
			url:url,
			async:false,
			success:function(jsonData){
				var data=JSON.stringify(jsonData);
				var json=eval("("+data+")");
				if(json.length>0){
					jQuery(json).each(function(i,item){
						var arrPro=new Array();
						var batch="";
						var url1 = "<%=contextPath %>/SpringR/warehouse/getPudNumbers?pdId=<%=pDeId%>&proId="+item.pro_id;
					jQuery.ajax({
	 					  type : 'POST',
	   					  url : url1,
	  					  async:false,
	  					  success : function(jsonData1){   
						  var data1 = JSON.stringify(jsonData1);
						  var json1 = eval("(" + data1 + ")");
						  if(json1.length > 0){
						  jQuery(json1).each(function(j,item1){
					
								var  cfgs={
									db_id:item1.dbLogId,
									wh_id:item1.whId,
									number:item1.number,
									batch:item1.batch,
									invalid_time:item1.invalid_time
									}
						    var productOut=new ProductOut(cfgs);
						    arrPro.push(productOut);
						    	var parm=item.pro_id+j;
							batch=item1.batch;	
							var radd=item1.pro_id+j;
							arrWhName+="<a id='"+radd+"'  title='[批次号："+item1.batch+"]--[有效时间："+item1.invalid_time+"]'>"+item1.whName+"["+item1.number+"]<img  align='absmiddle' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes('"+item.pro_id+"',"+j+")\"></a><span id='"+parm+"'></span>";
							k++;
							orderNum+=parseInt(item1.number);
							
								});
							}		
	  				 	}
					 });
									var  mainCfgs={
									  pro_id :item.pro_id,//产品id
									  wh_id :'0',//仓库id
								      price :item.pur_price,//单价
								      productOut:arrPro,
								      purchase_num:item.purchase_num,//采购数量;
								      pur_id:purId,//采购单id
								   	  purchase_send_num:item.already_purchase_num,//已经收货数量
								      ppro_id:item.pproId,//产品关联id
								   	  ppo_id:item.ppo_id,//出货单id
									}
						    var pd=new PD(mainCfgs);
							arrMainPro.push(pd);
									   var del=pd.wh_id+"_"+pd.pro_id
									   var add="add"+item.pro_id;
									var tds = "<tr ><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
									tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
									tds += "<td align='center' nowrap>"+item.purchase_num+"</td>";//订单数量
									tds += "<td align='center' nowrap>"+item.already_purchase_num+"</td>";//已发数量
									tds += "<td align='center' nowrap><input type='text' value='"+orderNum+"' id ='"+item.pro_id+"' class='BigInput' size='10' readOnly maxlength='10'></td>";//已发数量
									tds += "<td align='center' nowrap id='"+item.pro_id+"'><span id='"+add+"'></span>"+arrWhName+"</td>";//仓库
									var nums=parseInt(item.purchase_num)-parseInt(item.already_purchase_num)-orderNum;
									tds += "<td align='center' nowrap><img style='cursor:pointer'   align='absmiddle' src='/yh/core/styles/style1/img/notify_new.gif' onclick=\"diogShow('"+item.pro_id+"',"+nums+","+orderNum+","+i+")\"></td></tr>";//入库管理
									k++;
									
									jQuery('#pro_table').append(tds);
										arrWhName="";
							orderNum=0;
							if(productIds!=""){
	  					productIds+=",";
	  				}
	  				productIds+=item.pro_id;
					})
				}
			}
		})
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
function updateObject(){
	for(var i=0;i<arrMainPro.length;i++){
		for(var j=0;j<arrMainPro[i].productOut.length;j++){
		if (arrChildPro!= "") {
				arrChildPro += ";";
		}
		arrChildPro+=arrMainPro[i].pro_id+","+arrMainPro[i].price+","+arrMainPro[i].productOut[j].db_id+","+arrMainPro[i].productOut[j].wh_id+","+arrMainPro[i].productOut[j].number+","+arrMainPro[i].purchase_num+","+arrMainPro[i].pur_id+","+arrMainPro[i].purchase_send_num+","+arrMainPro[i].ppro_id+","+arrMainPro[i].ppo_id+","+arrMainPro[i].productOut[j].batch+","+arrMainPro[i].productOut[j].invalid_time;
		}
	}
}

    function doSubmitForm(formObject) {
     updateObject();
	  var ppoCode = encodeURIComponent(jQuery('#ppoCode').val());
	  var ppoStatus = encodeURIComponent(jQuery('#ppoStatus').val());
	  var supId = jQuery('#supId').val();
	  var purDate = jQuery('#purDate').val();
	  var ppoId = jQuery('#ppoId').val();
	  var ppoRemark = encodeURIComponent(jQuery('#ppoRemark').val());
      var para = "ppoCode="+ppoCode+"&ppoStatus="+ppoStatus
	 			+"&purId=<%=purId%>"
	 			+"&pDeId=<%=pDeId%>"
	 			+"&supId="+supId
	 			+"&purDate="+purDate
	 			+"&ppoId="+ppoId
	 			+"&arrChildPro="+encodeURIComponent(arrChildPro)
	 			+"&ppoRemark="+ppoRemark;
	 			
     var url="<%=contextPath %>/SpringR/warehouse/updatePur?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("更新成功");
		    window.location.href="purShowDetial.jsp";
		  }
		  else{
			   alert("更新失败"); 
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

function returnPage(){
	window.location.href="purShowDetial.jsp"
}
 function diogShow(proId,nums,purchase_num,i){
  	var url="<%=contextPath%>/springViews/erp/warehouse/whIn.jsp?proId="+proId+"&k="+i+"&orderNum="+nums;
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
      <td nowrap class="TableData">发货单编号：</td>
      <td class="TableData" >
       <input id="ppoCode" name="ppoCode" type="text" value=""  class="BigInput"> 
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
        <input type="text" id="purDate" name="purDate" size="19"  maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="purDateImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="ppoRemark" name="ppoRemark"  class="BigInput"></textarea>
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
		    	<td align='center' nowrap>库存[仓库名称/入库数量]</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
       <%if(!"0".equals(flag)){ %>
       <input type="button" value="更新" class="BigButton" onclick="return doSubmitForm(this.form);">
      <% }%>
      <%
       	if(flow.equals("1")){
       %>
        <input id="closeButton" type="button" value="关闭"  class="BigButton" onclick="javascript:window.close();">
      	<%} else{%>
        <input id="backButton" type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      	<%} %>
      </td>
    </tr>
  </table>
</form>
</body>
</html>