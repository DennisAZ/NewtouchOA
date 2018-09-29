<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增采购退货单</title>
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
		<script  type="text/javascript"  src="obejctJs1.js"></script>
<script type="text/javascript">
var arrMainPro=new Array();
function doInit(){
	var url = '<%=contextPath%>/SpringR/system/getAutoCode?code_type=CODE10';
	getAutoCode(url,"code");
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
  var str="";
																																											
function updateObject(){
	for(var i=0;i<arrMainPro.length;i++){
		for(var j=0;j<arrMainPro[i].productOut.length;j++){
		if (str!= "") {
				str += ";";
		}
		str+=arrMainPro[i].pro_id+","+arrMainPro[i].cache_pro_id+","+arrMainPro[i].price+","+arrMainPro[i].productOut[j].db_id+","+arrMainPro[i].productOut[j].wh_id+","+arrMainPro[i].productOut[j].number+","+arrMainPro[i].productOut[j].sumNum;
		}
	}
}

 
   function doSubmitForm(formObject) {
	  updateObject();
	  var code = jQuery('#code').val();
	  var type = jQuery('#type').val();
	  var reason = jQuery('#reason').val();
	  var remark = jQuery('#remark').val();
	  
	 var para = "code="+code+"&type="+encodeURIComponent(type)+"&remark="+encodeURIComponent(remark)+"&reason="+encodeURIComponent(reason)+"&arrChildStr="+ encodeURIComponent(str);
	 var url="<%=contextPath %>/SpringR/warehouse/saleReturnAdd?"+para;
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="returnManage.jsp";
		  }
		  else{
			   alert("添加失败"); 
		  }
	   }
	 });
   }


      function chooseProduct(idArray,type){
	    nameArray = idArray;

	 	openDialogResize('selectProduct.jsp',  520, 400);
 	}
 	function diogShow(proId,i){
  	 var url="<%=contextPath%>/springViews/erp/warehouse/returnIn.jsp?proId="+proId+"&k="+i;
 	 var str=openDialogResize(url,  520, 400);
  }
     function deletes(proId,i){
     	arrMainPro.remove(i);
     	jQuery('#'+proId).remove();
     	
     }
     function deleteDb(proId,parm,j){
    	 for(var i=0;i<arrMainPro.length;i++){
		 if(proId==arrMainPro[i].pro_id){
			arrMainPro[i].productOut.remove(j);
			
		 }
	  }
	       jQuery('#'+parm).remove();
     }
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增采购退货单
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="customerId" name="customerId">
<table class="TableBlock" width="80%" align="center">
	<tr>
	      <td nowrap class="TableData">退货单编号：</td>
	      <td class="TableData" >
	       <input id="code" name="code" type="text" value="">
	      </td>
	      
	    </tr>
	    <tr>
	    	<td nowrap class="TableData">退货来源：</td>
	      <td class="TableData">
	      	<select id="type" name="type">
					<option value="采购">采购</option>
					<option value="其他">其他</option>
				</select>
	      </td>
	    </tr>
    <tr>
      <td nowrap class="TableData">退货原因：</td>
      <td class="TableData" >
       <textarea id="reason" name="reason" ></textarea>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan="3">
		<textarea id="remark" name="remark"></textarea>
      </td>
    </tr>
     <tr>
    	 <td class="TableData" colspan="6">
 		<input type="button" value="+添加产品" class="BigButton" onclick="chooseProduct(['products'],0);">
      </td>
    </tr>
      <tr>
      <td colspan="6" align="center">
		       <table id="pro_table" align="center" width="80%" >
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>产品编号</td>
		    	<td align='center' nowrap>产品名称</td>
		    	<td align='center' nowrap>规格型号</td>
		    	<td align='center' nowrap>计量单位</td>
		    	<td align='center' nowrap>数量</td>
		    	<td align='center' nowrap>库存（仓库名称/出库数量）</td>
		    	<td align='center' nowrap>操作</td>
		    	</tr>
		    </table>
    </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm(this.form);">
        <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>