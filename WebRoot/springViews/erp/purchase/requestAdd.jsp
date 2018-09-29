<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建采购申请单信息</title>
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

function doInit(){
	var url = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE6';
	getAutoCode(url,"reqCode");
	
}

function getAutoCode(url,id){
	jQuery.ajax({
		type:'POST',
		url:url,
		success:function(data){
			jQuery('#'+id).val(data);
		}
	});
}
   function doSubmitForm() {
	 var reqName = encodeURIComponent(jQuery('#reqName').val());
	 if(reqName == ""){
	 	alert("申请单名称不能为空！");
	 	return false;
	 }
	 var reqCode = encodeURIComponent(jQuery('#reqCode').val());
	  if(reqCode == ""){
	 	alert("申请单编号不能为空！");
	 	return false;
	 }
	 var reqRemark = encodeURIComponent(jQuery('#reqRemark').val());
	 
    var str = "";
	 for(var i=0;i<dataArr.length;i++){
	   var arr = dataArr[i];
	   for(var j=0;j<arr.length;j++){
	 	if(str != "")
	 	{
			str +=";"; 		
	 	}
	 	str = str + arr[j];	
	   }
	}
	if(str == ""){
		alert("请填写完整相应产品信息！");
		return;
	}
	
	 var param="reqName="+reqName+
	 	       "&reqCode="+reqCode+
	 	       "&str="+encodeURIComponent(str)+
	 	       "&reqRemark="+reqRemark;
     var url = "<%=contextPath %>/SpringR/purchase/addRequest";
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   data: param,
	   success : function(jsonData){   
		 if(jsonData == "0"){
		   		alert("添加成功");
		   		window.location.href="requestManage.jsp";
			}else{
					 alert("添加失败"); 
				 }
			 }
	  });
   }
   
  
  var nameArray = null;
   function chooseProduct(idArray,type){
	    nameArray = idArray;
	 	openDialogResize('rframe.jsp',  800, 600);
 	}
 	
var dataArr=new Array();
function diogShow(proId){
  	 var url="<%=contextPath%>/springViews/erp/purchase/suppRequest.jsp?proId="+proId;
 	 var arr=openDialogResize(url,  800, 600);
	 if(arr != null && arr!=undefined && arr.length >0){
		 dataArr.push(arr);
	 }
}
function deletePro(obj,id){
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
 	if(dataArr.length > 0){
 		//删除数组中该产品的选择的供应商
 		for(var i=0;i<dataArr.length;i++){
 			var arr = dataArr[i];
 			if(arr.length > 0){
 				if(arr[0].split(",")[0] == id){
 					dataArr.splice(i,1);
 				}
 			}
 		}
 	}
}

function deleteSup(proId,subId){
	var newArr1 = new Array();
	for(var i=0;i<dataArr.length;i++)
	{
		var arr = dataArr[i];
		var newArr2 = new Array();
		for(var j=0;j<arr.length;j++)
		{
			var a = arr[j].split(",");
			var pro_id = a[0];
			var sub_id = a[1];
	 		if(pro_id == proId && sub_id == subId)
 			{
 				jQuery('#'+subId).remove();
			}else{
				newArr2.push(pro_id+","+sub_id);
			}
		}
		newArr1.push(newArr2);
	}
	dataArr = newArr1;
}
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	 采购申请单基础信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <input type="hidden" id="products" name="products" >
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="90%" align="center">
      <tr>
      <td nowrap class="TableData">申请单名称：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="reqName" id="reqName"  class="BigInput" />
      </td>
      <td nowrap class="TableData">申请单编号：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="reqCode" id="reqCode"  class="BigInput" />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="6">
 		<textarea name="reqRemark" id="reqRemark" class="BigInput" cols="70"></textarea>
      </td>
    </tr>
    <tr>
    	 <td class="TableData" colspan="7">
 		<input type="button" value="+选择产品" class="BigButton" onclick="chooseProduct(['products'],0);">
      </td>
    </tr>
      <tr>
      <td colspan="7" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>供货商[名称/价格]</td>
		    	<td align='center' nowrap>操作</td>
				</tr>
				</table>
		</td>
	  </tr>
	<tr align="center" class="TableControl">
      <td colspan="7" nowrap>
       <input type="button" value="添加" class="BigButton" onclick="return doSubmitForm();">
      </td>
    </tr>
</table>
</form>
</body>
</html>