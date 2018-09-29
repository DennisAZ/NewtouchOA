<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String processId = request.getParameter("processId");
	String craftsId = request.getParameter("craftsId");
	String pro_name = request.getParameter("pro_name");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑生产工序</title>
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
	getType();
	getPro();
}

function getWareHouse(){
	var url = "<%=contextPath %>/SpringR/produce/getMachineType/"+new Date().getTime();
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					jQuery('#machine_type').append("<option  value='"+item.id+"' >"+item.machine_name+"</option>");
					
				});
			}
	   }
	 });
	
}
var craftsId="";
function getType(){
	var url = "<%=contextPath %>/SpringR/produce/getProcessByIds?id=<%=processId%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");

			jQuery('#name').val(json.name);
			jQuery('#machine_type').val(json.machine_name);
			jQuery('#process_index').val(json.process_index);
			jQuery('#bad_rate').val(json.bad_rate);
			jQuery('#process_time').val(json.process_time);
			jQuery('#remark').val(json.remark);
			craftsId=json.crafts_id;
  		} 
	 });
}
var oldProId="";
var k=0;
function getPro(){
	var url = "<%=contextPath %>/SpringR/produce/getProcessPro?processId=<%=processId%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
				if(json.length > 0){
								jQuery(json).each(function(i,item){

			var tds = "<tr id='"+i+"' ><td align='center' nowrap>"+item.pro_code+"</td>";
									tds += "<td align='center' nowrap>"+item.pro_name+"</td>";
									tds += "<td align='center' nowrap>"+item.pt_name+"</td>";
									tds += "<td align='center' nowrap>"+item.unit_name+"</td>";
									tds += "<td align='center' nowrap><input type='text'  value='"+item.number+"' id='"+item.pro_id+"Number'  class='BigInput' size='10' readOnly maxlength='10'></td>";
									tds += "<td align='center' nowrap><input type='button' value='删除' class='BigButton' onclick=\"deletes(this,'"+item.pro_id+"')\"></td></tr>";
									jQuery('#pro_table').append(tds);
									if(oldProId!=""){
										oldProId+=",";
									}
									oldProId+=item.pro_id
									k++;
									
							});
									
					}
			
  		} 
	 });
jQuery('#products1').val(oldProId);

}

function getProcessIndex(){
	var processIndex=jQuery('#process_index').val();
	var url = "<%=contextPath %>/SpringR/produce/getProcessIndex?craftsId="+craftsId+"&processIndex="+processIndex;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
					if(jsonData!=null && jsonData!=""){
						alert("此序号已经存在！请重新输入！");
						return false;
					}
					
	   }
	 });
	
}
   function doSubmitForm(formObject) {
	 
	 var name = jQuery('#name').val();
	  var productIds = jQuery('#products').val();
	  if(productIds=="-1"){
	
	   productIds = jQuery('#products1').val();
	   }
	 var machine_type = jQuery('#machine_type').val();
	 var process_index = jQuery('#process_index').val();
	 var bad_rate = jQuery('#bad_rate').val();
	 var process_time = jQuery('#process_time').val();
	 var remark = jQuery('#remark').val();
	 var numbers = getValue(productIds,"Number");
	 if(numbers == '-1'){return false;}
	 var para = "name="+encodeURIComponent(name)+"&process_time="+process_time+"&proId="+productIds+"&machine_type="+encodeURIComponent(machine_type)+"&numbers="+numbers+"&process_index="+process_index+"&remark="+encodeURIComponent(remark)+"&bad_rate="+bad_rate+"&processId=<%=processId%>&craftsId="+craftsId;
     var url="<%=contextPath %>/SpringR/produce/updateProcess?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("更新成功");
		   window.location.href="processManage2.jsp?craftsId=<%=craftsId%>&pro_name=<%=pro_name%>";
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
	    var proId=jQuery('#products').val();
	    if(proId!=""||proId!=null){
	 	oldProId="";
	 	for(var i=0;i<k;i++){
	 		jQuery('#'+i).remove();
	 	}
	    jQuery('#products1').remove();
	 	}
	 	
 	}
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	编辑生产工序
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="products" name="products" value="-1">
 <input type="hidden" id="products1" name="products1">
<table class="TableBlock" width="80%" align="center">
  
      <td nowrap class="TableData">工序名称：</td>
       <td class="TableData">
 		<input type="text" name="name" id="name"  class="BigInput" />
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData">加工类型：</td>
      <td class="TableData" >
       <select id="machine_type" name="machine_type" >
       </select>
      </td>
     
    </tr>
    <tr>
      </td>
    </tr>
    <tr>
    	 <td nowrap class="TableData">加工顺序：</td>
       <td class="TableData" >
 		<input type="text" name="process_index"   id="process_index"  

class="BigInput"  onblur="getProcessIndex();"/>
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData">不良率：</td>
       <td class="TableData">
 		<input type="text" name="bad_rate" id="bad_rate"  class="BigInput" />（单位：%）
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData">工时：</td>
       <td class="TableData">
 		<input type="text" name="process_time" id="process_time"  class="BigInput" 

/>
      </td>
    </tr>
    <tr>
       <td class="TableData" colspan="2">
 		<input type="button" value="+工序损耗"  class="BigButton" onclick="chooseProduct(['products'],0);">
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
		    	<td align='center' nowrap>数量</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
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
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>