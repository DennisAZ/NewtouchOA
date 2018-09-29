<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	
	String exam_id=request.getParameter("exam_id");
	String proId=request.getParameter("proId");
	String pro_code=request.getParameter("pro_code");
 %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>仓库选择</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script  type="text/javascript"  src="map.js"></script>
<script> 

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
   
   var parentWindowObj = window.dialogArguments;
   function doSubmitForm(){
   		var warehouses = jQuery('#warehouses').val();
   		var arrWH=warehouses.split(",");
   		for(var i=0;i<arrWH.length;i++){
   		  if(arrWH[i] != ""){
   			var warehouseName = jQuery("#"+arrWH[i]+"Name").html();
   			var price = jQuery("#"+arrWH[i]+"Price").val();
   			var number = jQuery("#"+arrWH[i]+"Number").val();
   			if(number == '0')
   			{
   				alert("入库数量不能等于0！");				
				return false;
   			}
   			var batch = jQuery("#"+arrWH[i]+"Batch").val();
   			if(batch==""||batch==null){
				alert("批次号没有填写！");				
				return false;
			} 
   			var invalid = jQuery("#"+arrWH[i]+"Invalid").val();
   			if(invalid==""||invalid==null){
				alert("失效时间没有填写！");				
				return false;
			} 
			
			//判断当前库存是不是已经添加，如果是，则累加
			var dbNumMap = parentWindowObj["dbNumMap"];
			//dbId+","+whId+","+proId+","+whName+","+batch;
			var key = arrWH[i]+",<%=proId%>,<%=exam_id%>,"+price+","+batch+","+invalid;
			if(dbNumMap.containsKey(key)){
				var n = dbNumMap.get(key);
				n = parseFloat(n)+parseFloat(number);
				dbNumMap.removeByKey(key);
				dbNumMap.put(key,n);
				parentWindowObj["dbNumMap"] = dbNumMap;//重新赋值
				var obj = parentWindowObj.document.getElementById(key);
				obj.parentNode.removeChild(obj);
				var titleStr=warehouseName+"|"+price+"|"+n+"|"+batch+"|"+invalid;
			    tds="<a id='"+key+"' title='"+titleStr+"'>"+warehouseName+"["+n+"]<img  style='cursor:pointer' align='absmiddle' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes('"+key+"','<%=proId%>','"+n+"')\" title='删除'></a>";
			}else{
				dbNumMap.put(key,number);//没有则新增
				var titleStr=warehouseName+"|"+price+"|"+number+"|"+batch+"|"+invalid;
			    tds="<a id='"+key+"' title='"+titleStr+"'>"+warehouseName+"["+number+"]<img  style='cursor:pointer' align='absmiddle' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes('"+key+"','<%=proId%>','"+number+"')\" title='删除'></a>";
			}
			parentWindowObj.jQuery('#add_<%=proId%>').append(tds);
			
			//更新产品的需求数量
			var proNumMap = parentWindowObj["proNumMap"];
			if(proNumMap.containsKey("<%=proId%>")){
				var proNumber = proNumMap.get("<%=proId%>");
				var n =parseFloat(proNumber)+parseFloat(number);
				proNumMap.removeByKey("<%=proId%>");
				proNumMap.put("<%=proId%>",n);
			}
			else{
				proNumMap.put("<%=proId%>",number);
			}
			parentWindowObj["proNumMap"] = proNumMap;
   		  }
   		}
   		window.close();
   }
  
 function deletes(obj,id){
 	  obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
 	  jQuery('#'+id).remove();
 	   var warehouses = jQuery('#warehouses').val();
 		     var arrWH=warehouses.split(",");
 		     var values="";
 		    for(var i=0;i<arrWH.length;i++){
 		    	 if(arrWH[i]!=id){
 		    	  if(values != ""){
			  		values+=",";
				  }
		  			values += arrWH[i];
 		    	 }
 		    }
 		     jQuery('#warehouses').val(values);
 }
  var nameArray = null;
   function chooseWareHouse(idArray){
	    nameArray = idArray;
	    var url="chooseWareHouse.jsp";
	 	openDialogResize(url,800, 600);
	 	//注册时间控件
	 	var warehouses = jQuery('#warehouses').val();
	 	times(warehouses,"Invalid");
 	}
 	
 	function times(warehouses,suffix){
 		var arr = warehouses.split(',');
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
<body topmargin="5" >
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       产品入库选择
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="warehouses" name="warehouses">
<table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData" width="30">产品编号：</td>
      <td class="TableData" >
       <input type="text" value="<%=pro_code%>">
      </td>
     
    </tr>
    <tr>
       <td class="TableData" colspan="2">
 		<input type="button" value="仓库选择" class="BigButton" onclick="chooseWareHouse(['warehouses']);">
      </td>
    </tr>
      <tr>
      <td colspan="2" align="center">
		       <table id="pro_table" align="center" width="100%">
		    	<tr class="TableHeader">
		    	<td align='center' nowrap>仓库名称</td>
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
      <td colspan="2" nowrap>
        <input type="button" value="提交" class="BigButton" onclick="return doSubmitForm();">
        <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>