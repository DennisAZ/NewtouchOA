<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String notifyId = request.getParameter("notifyId");
	String flag = request.getParameter("flag");
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
var notifyId="<%=notifyId%>";
function doInit(){
	getNotifyById();
	getNotifyPro();
	initTime();
	
}
function initTime(){
var beginTimePara = {
      inputId:'beginTime',
      property:{isHaveTime:false},
      bindToBtn:'beginTimeImg'
  };
  new Calendar(beginTimePara);
var createTimePara = {
      inputId:'createTime',
      property:{isHaveTime:false},
      bindToBtn:'createTimeImg'
  };
  new Calendar(createTimePara);

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
	// var createTime = jQuery('#createTime').val();
	 var remark = jQuery('#remark').val();
	  var productIds = jQuery('#products').val();
	  if(productIds=="-1"){
	   productIds = jQuery('#products1').val();
	   }
	 var numbers = getValue(productIds,"Number");
	  var trueDate = getValue(productIds,"beginTime");
	 if(numbers == '-1'){return false;}
	 var para = "proId="+productIds+"&beginTime="+encodeURIComponent(beginTime)+"&trueDate="+encodeURIComponent(trueDate)+"&code="+encodeURIComponent(code)+"&type="+encodeURIComponent(type)+"&numbers="+numbers+"&remark="+encodeURIComponent(remark)+"&notifyId="+notifyId;
     var url="<%=contextPath %>/SpringR/produce/updateNotify?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("更新成功");
		    if('<%=flag%>' == 1){
		    	window.close();
		    }
		    else{
		   	 window.location.href="notifyManage.jsp";
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
	   var price = jQuery('#'+id+'Price').val();
	   if(!megx.test(price)){
		   alert('请输入正确的数');
		   jQuery('#'+id+'Price').val('');
		   return;
	   }
   }
   function checkInt(id){
	   var megx = /^[0-9]*[1-9][0-9]*$/;
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
   function selectId1(id){
 		     var productIds = jQuery('#products1').val();
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
 		     jQuery('#products1').val(values);
 	}
 function deletes1(obj,id){
 	  obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
 	  //从拼接的id串中去除指定id
 	  jQuery('#'+id).remove();
 	  selectId1(id);
 }
 function deletes(obj,id){
 	  obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
 	  //从拼接的id串中去除指定id
 	  jQuery('#'+id).remove();
 	  selectId(id);
 }
  var nameArray = null;
   function chooseProduct(idArray,type){
	    nameArray = idArray;
	    var url="nframe.jsp?shortName=-1";
	 	openDialogResize(url,800, 600);
	 	var productIds=jQuery('#products').val();
	 	if(productIds!="-1"){
	 	oldProId="";
	 	for(var i=0;i<k;i++){
	 		jQuery('#'+i).remove();
	 	}
	    jQuery('#products1').remove();
	 	}
	 	times(productIds,"beginTime");
 	}
function getNotifyById(){
	var url = "<%=contextPath %>/SpringR/produce/getNotifyById?notifyId="+notifyId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			jQuery('#code').val(json.CODE);
			jQuery('#type').val(json.TYPE);
			jQuery('#beginTime').val(json.produce_time);
			jQuery('#createTime').val(json.create_time);
			jQuery('#remark').val(json.remark);
	   }
	 });
}
var proids="";
var k=0;
function getNotifyPro(){
		var url = "<%=contextPath %>/SpringR/produce/getNotifyProById?notifyId="+notifyId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					  var tds = "<tr id='"+i+"'><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.ptName+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					  tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"Number' value='"+item.number+"' class='BigInput' size='10' maxlength='10' onblur=\"checkInt('"+item.pro_id+"')\"></td>";//数量
					  tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"beginTime' name='"+item.pro_id+"beginTime' value='"+item.end_time+"' size='19' maxlength='19' class='BigInput' value=''><img src='<%=imgPath%>/calendar.gif' id='"+item.pro_id+"beginTimeImg' align='absMiddle' border='0' style='cursor:pointer' > </td>";
					  tds += "<td align='center' nowrap><a href='#' style='color:red' onclick=\"showCrafts('"+item.craftsId+"','"+item.pro_name+"')\">"+item.crafts_version+"</a></td>";//工艺版本
					  tds += "<td align='center' nowrap><a href='#' style='color:red' onclick=\"showDrawings('"+item.drawingId+"')\">"+item.drawing_version+"</a></td>";//图纸版本
					  tds += "<td align='center' nowrap><img  align='absmiddle' style='cursor:pointer' title='删除' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes1(this,'"+item.pro_id+"')\"/></td></tr>";
					  jQuery('#pro_table').append(tds);
					  if(proids!=""){
					  	proids+=",";
					  }
					  proids+=item.pro_id;
					    k++;
				});
			}
	   }
	 });
	 jQuery('#products1').val(proids);
	 times(proids,"beginTime");
}

 function showCrafts(craftsId,pro_name){
	    var url="processManage3.jsp?craftsId="+craftsId+'&pro_name='+encodeURIComponent(pro_name);
	 	openDialogResize(url,800, 500);
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
	       新增生产通知单基础信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="products" name="products" value="-1">
 <input type="hidden" id="products1" name="products1">
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
 		<select id="type">
 			<option value="销售">销售</option>
 			<option value="其他">其他</option>
 		</select>
      </td>
     
    </tr>
      <tr>
      <td nowrap class="TableData">生产时间：</td>
        <td class="TableData" >
   		<input type="text" id="beginTime" name="beginTime" size="19" maxlength="19" 

class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" 

style="cursor:pointer" >
      </td>
     
    </tr>
     <tr style="display:none;">
      <td nowrap class="TableData">创建时间：</td>
       <td class="TableData" >
   		<input type="text" id="createTime" name="createTime" size="19" 

maxlength="19" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" id="createTimeImg" align="absMiddle" border="0" 

style="cursor:pointer" >
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
      <td colspan=4 nowrap>
              <input type="button" value="更新" class="BigButton" onclick="return doSubmitForm(this.form);">
     	  <%
      	if("1".equals(flag)){
      %>
        <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
        <%
        }else{
        %>
        <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1)">
        <%
        	}
        %>
      </td>
    </tr>
  </table>
</form>
</body>
</html>