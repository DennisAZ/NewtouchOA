<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String planId = request.getParameter("planId");
	String flag = request.getParameter("flag");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑生产计划单</title>
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
function doInit(){
	getPlan();
	getPlanPro();
	initTime();
}
function initTime(){
var createTimePara = {
      inputId:'createTime',
      property:{isHaveTime:false},
      bindToBtn:'createTimeImg'
  };
  new Calendar(createTimePara);

}
   function doSubmitForm(formObject) {
	 
	 var planCode = jQuery('#planCode').val();
	 var remark = jQuery('#remark').val();
	 var productIds = jQuery('#products').val();
	 var numbers = getValue(productIds,"Number");
	 if(numbers == '-1'){return false;}
	 var craftsIds = getValue(productIds,"Crafts");
	 if(craftsIds == '-1'){return false;}
	 var drawingIds = getValue(productIds,"Drawing");
	 if(drawingIds == '-1'){return false;}
	 var para = "planId="+planId+"&drawingIds="+drawingIds+"&craftsIds="+craftsIds+"&proId="+productIds+"&planCode="+encodeURIComponent(planCode)+"&numbers="+numbers+"&remark="+encodeURIComponent(remark);
     var url="<%=contextPath %>/SpringR/produce/updatePlan?"+para;
	 
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
		   	 window.location.href="planManage.jsp";
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
 function deletes(obj,id){
 	  obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
 	  selectId(id);
 }
function getPlan(){
	var url = "<%=contextPath %>/SpringR/produce/getPlan?planId="+planId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			jQuery('#planCode').val(json.code);
			jQuery('#code').val(json.pnCode);
			var str = "<a href='#' style='color:green;' onclick='notifyDetail(\""+json.pn_id+"\")'>"+json.pnCode+"</a>";
 			$('code').innerHTML=str;
			jQuery('#createTime').val(json.create_time);
			jQuery('#remark').val(json.remark);
	   }
	 });
}
function notifyDetail(notifyId){
	window.open('notifyDetail.jsp?notifyId='+notifyId+'&flag=1');
}

var proids="";
var k=0;
function getPlanPro(){
		var url = "<%=contextPath %>/SpringR/produce/getPlanPro?planId="+planId;
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
					  tds += "<td align='center' nowrap>"+item.number+"</td>";//数量
					  tds += "<td align='center' nowrap>"+item.end_time+"</td>";
					  tds += "<td align='center' nowrap><input type='hidden' id='"+item.pro_id+"Crafts' value='"+item.craftsId+"'><a href='#' style='color:red' onclick=\"showCrafts('"+item.craftsId+"','"+item.pro_name+"')\">"+item.crafts_version+"</a></td>";//工艺版本
					  tds += "<td align='center' nowrap><input type='hidden' id='"+item.pro_id+"Drawing' value='"+item.drawingId+"'><a href='#' style='color:red' onclick=\"showDrawings('"+item.drawingId+"')\">"+item.drawing_version+"</a></td>";//图纸版本
					  tds += "<td align='center' nowrap>"+item.already_number+"</td>";
					  tds += "<td align='center' nowrap><input type='text' id='"+item.pro_id+"Number' value='"+item.pNumber+"' class='BigInput' size='10' maxlength='10' onblur=\"checkInt('"+item.pro_id+"','"+item.number+"','"+item.already_number+"')\"></td>";
					  tds += "<td align='center' nowrap><img  align='absmiddle' style='cursor:pointer' title='删除' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes(this,'"+item.pro_id+"')\"/></td></tr>";
					  jQuery('#pro_table').append(tds);
					  if(proids!=""){
					  	proids+=",";
					  }
					  proids+=item.pro_id
					  k++;
				});
			}
	   }
	 });
	 jQuery('#products').val(proids);
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
	       编辑生产计划
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="products" name="products" value="-1">
<table class="TableBlock" width="80%" align="center">
       <tr>
      <td nowrap class="TableData">通知单编号：</td>
       <td class="TableData">
 		<span name="code" id="code"></span>
      </td>
     
    </tr>
      <tr>
      <td nowrap class="TableData">计划编号：</td>
       <td class="TableData">
 		<input type="text" name="planCode" id="planCode"  class="BigInput" />
      </td>
     
    </tr>
     <tr style="display:none">
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
      <td nowrap class="TableData" colspan="2">计划生产产品：</td>
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
		    	<td align='center' nowrap>累计已生产数量</td>
		    	<td align='center' nowrap>本次生产数量</td>
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