<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String craftsId = request.getParameter("craftsId");
	String pro_name = request.getParameter("pro_name");
	String crafts_version = request.getParameter("crafts_version");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增生产工序添加</title>
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
	jQuery('#crafts_version').val("<%=crafts_version%>");
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
function getProcessIndex(){
	var processIndex=jQuery('#process_index').val();
	var url = "<%=contextPath %>/SpringR/produce/getProcessIndex?craftsId=<%=craftsId%>&processIndex="+processIndex;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
					if(json.length!=0){
						alert("此序号已经存在！请重新输入！");
						return false;
					}
					checkIntIndex("process_index");
					
	   }
	 });
	
}
   function doSubmitForm(formObject) {
	 
	 var name = jQuery('#name').val();
	  var productIds = jQuery('#products').val();
	 var machine_type = jQuery('#machine_type').val();
	 var process_index = jQuery('#process_index').val();
	 var bad_rate = jQuery('#bad_rate').val();
	 var process_time = jQuery('#process_time').val();
	 var remark = jQuery('#remark').val();
	 var numbers = getValue(productIds,"Number");
	 if(numbers == '-1'){return false;}
	 var para = "name="+encodeURIComponent(name)+"&process_time="+process_time+"&proId="+productIds+"&machine_type="+encodeURIComponent(machine_type)+"&numbers="+numbers+"&process_index="+process_index+"&remark="+encodeURIComponent(remark)+"&bad_rate="+bad_rate+"&craftsId=<%=craftsId%>";
     var url="<%=contextPath %>/SpringR/produce/addProcess?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="processManage2.jsp?craftsId=<%=craftsId%>&pro_name=<%=pro_name%>&crafts_version=<%=crafts_version%>";
		  }
		  else{
			   alert("添加失败"); 
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
   function checkIntIndex(id){
	   var megx = /^[0-9]*[1-9][0-9]*$/;
	   var number = jQuery('#process_index').val();
	   if(!megx.test(number)){
		   alert('请输入正确的数');
		   jQuery('#process_index').val('');
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
 	}
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增生产工序信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="products" name="products">
<table class="TableBlock" width="80%" align="center">
 <tr style="display:none;">
      <td nowrap class="TableData">所属工艺：</td>
       <td class="TableData">
 		<input type="text" name="crafts_version" id="crafts_version"  class="BigInput" />
      </td>
    </tr>
  <tr>
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
 		<input type="text" name="bad_rate" id="bad_rate"  class="BigInput" />(单位：%)
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
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" ></textarea>
      </td>
    </tr>
    <tr>
       <td class="TableData" colspan="2">
 		<input type="button" value="+工序损耗" class="BigButton" onclick="chooseProduct(['products'],0);">
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
    
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
        <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1)">
      </td>
    </tr>
  </table>
</form>
</body>
</html>