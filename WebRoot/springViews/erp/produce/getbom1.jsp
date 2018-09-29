<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String planId = request.getParameter("planId");
	String type = request.getParameter("type");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>生产退料</title>
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
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript">
var planId="<%=planId%>";
var type="<%=type%>";
function doInit(){
	var url = "<%=contextPath %>/SpringR/produce/getPlanCodeAndInsertBom?planId="+planId+"&type="+type;
	var proids="";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
					if(i==0){
						var str = "<a href='#' style='color:green;' onclick='planDetail(\""+planId+"\")'>"+item.plan_code+"</a>";
 						$('code').innerHTML=str;
 						jQuery('#bom_id').val(item.bom_id);
 						jQuery('#bomCode').val(item.code);
 						jQuery('#remark').val(item.remark);
					}
				
					 var tds = "<tr id='"+i+"'><td align='center' nowrap>"+item.pro_code+"</td>";//产品编号
					  tds += "<td align='center' nowrap>"+item.pro_name+"</td>";//产品名称
					  tds += "<td align='center' nowrap>"+item.pro_type+"</td>";//产品型号
					  tds += "<td align='center' nowrap>"+item.unit_name+"</td>";//计量单位
					  tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"Number' value='"+item.number+"' class='BigInput' size='10' maxlength='10' onblur=\"checkFloat('"+item.pro_id+"','"+item.number+"')\"></td>";//需求数量
					  tds += "<td align='center' nowrap><img  align='absmiddle' style='cursor:pointer' title='删除' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes(this,'"+item.pro_id+"')\"/></td></tr>";
					  jQuery('#pro_table').append(tds);
					  if(proids!=""){
					  	proids+=",";
					  }
					  proids+=item.pro_id;
					  
			    });
			}
	  		jQuery('#products').val(proids);
	   }
	 });
}

   function checkFloat(id,defaultNumber){
	     var megx = /^\d+(\.\d+)?$/;
	   var number = jQuery('#'+id+'Number').val();
	   if(!megx.test(number)){
		   alert('请输入正确的数');
		   jQuery('#'+id+'Number').val(defaultNumber);
		   return;
	   }
   }
   
function planDetail(id){
	window.open('planDetail.jsp?planId='+id+'&flag=1');
}
 	
   function doSubmitForm(formObject) {
	 var bom_id = jQuery('#bom_id').val();
	 var bomCode = jQuery('#bomCode').val();
	 var remark = jQuery('#remark').val();
	 
	 var productIds = jQuery('#products').val();
	 var numbers = getValue(productIds,"Number");
	 if(numbers == '-1'){return false;}
	 
	 var para = "productIds="+productIds+"&numbers="+numbers+"&bomCode="+encodeURIComponent(bomCode)+"&remark="+encodeURIComponent(remark)+"&bom_id="+bom_id+"&planId="+planId+"&type="+type;
     var url="<%=contextPath %>/SpringR/produce/updateBOM?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("提交成功");
		    window.close();
		  }
		  else{
			   alert("提交失败"); 
		  }
	   }
	 });
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
  
    function deletes(obj,id){
 	  obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
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

var nameArray = null;
   function chooseProduct(idArray){
	    nameArray = idArray;
	    var url='bomChooseProduct.jsp';
	 	openDialogResize(url,800, 600);
 	}
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       生产退料
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="bom_id" name="bom_id" value="-1">
 <input type="hidden" id="products" name="products" value="-1">
<table class="TableBlock" width="80%" align="center">
       <tr>
      <td nowrap class="TableData">生产计划编号：</td>
       <td class="TableData">
      	 <span name="code" id="code"></span>
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">生产退料编号：</td>
       <td class="TableData">
 		<input type="text" name="bomCode" id="bomCode"  class="BigInput" />
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
 		<input type="button" value="+添加产品" class="BigButton" onclick="chooseProduct(['products']);">
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData" colspan="2">BOM清单：</td>
    </tr>
      <tr>
      <td colspan="2" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>退料数量</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
        <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>