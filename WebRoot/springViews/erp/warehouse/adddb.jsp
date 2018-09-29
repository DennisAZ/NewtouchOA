<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增仓库信息</title>
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

function doInit(){
	getWareHouse();
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
					jQuery('#warehouse').append("<option  value='"+item.id+"' >"+item.name+"</option>");
					getWhType();
				});
			}
	   }
	 });
	
}
function getWhType(){
 var whId = jQuery('#warehouse').val();
	var url = "<%=contextPath %>/SpringR/db/getWhType?whId="+whId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
					jQuery('#styleName').val(json.name);
				}
		
	 });
	
}

   function doSubmitForm() {
	 var productIds = jQuery('#products').val();
	 
	 var prices = getValue(productIds,"Price");
	 if(prices == '-1'){return false;}
	 var numbers = getValue(productIds,"Number");
	 if(numbers == '-1'){return false;}
	 var batchs = getValue(productIds,"Batch");
	 if(batchs == '-1'){return false;}
	 var invalid_times = getValue(productIds,"Invalid");
	 if(invalid_times == '-1'){return false;}
	 
	 var warehouseId = jQuery('#warehouse').val();
	 
	 var para = "productIds="+productIds+"&prices="+prices+"&numbers="+numbers+"&batchs="+batchs+"&invalid_times="+invalid_times+"&warehouseId="+warehouseId;
     var url="<%=contextPath %>/SpringR/db/addDB?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="dbManage.jsp";
		  }
		  else{
			   alert("添加失败"); 
		  }
	   }
	 });
   }
   
   function checkFloat(id,suffix){
	   var megx = /^\d+(\.\d+)?$/;
	   var v = jQuery('#'+id+suffix).val();
	   if(v !="" && !megx.test(v)){
		   alert('请输入正确的数');
		   jQuery('#'+id+suffix).val('');
		   return;
	   }
   }
   function checkBatch(id){
	   var megx = /^[u0391-uffe5]+$/;
	   var v = jQuery('#'+id+'Batch').val();
	   if(v !="" && !megx.test(v)){
		   alert('请勿输入中文');
		   jQuery('#'+id+'Batch').val('');
		   return;
	   }
	  
	  //判断输入的批次编号相对于当前库存的产品是否唯一
	  var whId = jQuery('#warehouse').val();
	  var url = "<%=contextPath %>/SpringR/db/checkBatckOnly?whId="+whId+"&proId="+id+"&batch="+v;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
	   			if(jsonData == '0'){
	   				alert('该批次编号已存在！');
		   			jQuery('#'+id+'Batch').val('');
		   			return;
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
			  return '-1';
		  }
		  if(value != ""){
			  value+=",";
		  }
		  value += v;
	  }
	  return value;
  }
  
  function selectId(id){
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
 function deletes(obj,id){
 	  obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
 	  jQuery('#'+id).remove();
 	  selectId(id);
 }
  var nameArray = null;
   function chooseProduct(idArray,type){
	    nameArray = idArray;
	    var url="frame.jsp?shortName=-1";
	 	openDialogResize(url,800, 600);
	 	//注册时间控件
	 	var productIds = jQuery('#products').val();
	 	times(productIds,"Invalid");
 	}
 	
 	function times(productIds,suffix){
 		var arr = productIds.split(',');
 		for(var i=0;i<arr.length;i++){
			var btnId= arr[i]+suffix;
			var imgTime=arr[i]+suffix+"Img";
			var timePara = {
	    		inputId:btnId,
	   			property:{isHaveTime:false},
	    		bindToBtn:imgTime
  			};
 			new Calendar(timePara);
	  }
 	}
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增仓库信息
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
       <select id="warehouse" name="warehouse" onchange="getWhType()">
       </select>
      </td>
     
    </tr>
    <tr>
     <td nowrap class="TableData">仓库类别：</td>
    	 <td class="TableData"><input type="button" id="styleName" style="color: red;font-size: 14 larger;width:80px;height: 30px;" name="styleName" disabled="disabled"/></td>
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
		    	<td align='center' nowrap>入库单价</td>
		    	<td align='center' nowrap>数量</td>
		    	<td align='center' nowrap>批次编号</td>
		    	<td align='center' nowrap>失效日期</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>