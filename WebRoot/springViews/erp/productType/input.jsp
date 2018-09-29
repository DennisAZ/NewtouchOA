<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String id = request.getParameter("id");
	String flag = "0";
	if(null != id && !"".equals(id)){
		flag = "1";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>更新产品类别信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<style type="text/css">
#dtreeId{display: none;position:absolute;right: 200px;top:70px;width: 0px solid #c30;width: 200px;height: 180px;background: #ccc;overflow: hidden;}
</style>
<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>

<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">

var flag = '<%=flag%>';
function doInit(){
	if(flag == '1'){
		var url = "<%=contextPath %>/SpringR/productType/getProductById?id=<%=id%>";
		 jQuery.ajax({
		   type : 'POST',
		   url : url,
		   success : function(jsonData){ 
			
			 var json = JSON.stringify(jsonData);
	   		 var data = eval("(" + json + ")");
	   		 if(data.length > 0){
	   			 jQuery(data).each(function(i,item){  
	   			  jQuery('#name').val(item.name);
	   			  jQuery('#parentName').val(item.parentName);
	   			  jQuery('#id').val(item.id);
	   			   jQuery('#parentId').val(item.parentId);
	   			  jQuery('#remark').val(item.remark);
 				});
	   		 }
	   		 
		   }
		 });
	}
}
	function showTree(type,setId,setName){
		var	url = contextPath + "/SpringR/productType/showWHTree/"+new Date().getTime(); 
     d = new dTree('d');
	 jQuery.ajax({
	   type : 'GET',
	   url : url,
	   success : function(json_data){ 
	   var data1 = JSON.stringify(json_data);
	   var data2 = eval("(" + data1 + ")");
	   if(data2.length > 0){
	    	$(data2).each(function(i,item){  
			 var NodeId = item.id;
			 var Parent_id = item.parentId;
			 var area_name = item.name;
			 var parentName=item.parentName
			 d.add(NodeId,Parent_id,area_name,"javascript:setValue('"+ NodeId +"','"+ Parent_id +"','"+ area_name +"')","","","","",true);
 			});
	   }else{
	   		alert("当前树还没有形成，当前添加的默认为根节点，请先添加树根节点名称！");
	   		return ;
	   }
 		document.getElementById(type).innerHTML = d;
 		display(type); //调用显示这棵树
	   }
	 });
		
	}
	   function setValue(id,parentId,name){
		   jQuery('#id').val(id);
   		jQuery('#parentId').val(parentId);
   		jQuery('#parentName').val(name);
   }
   
    function doSubmitForm1(formObject) {
	 var name = jQuery('#name').val();
	 var id = jQuery('#id').val();
	 var parentId = jQuery('#parentId').val();
	 var remark = jQuery('#remark').val();
	  var parentName = jQuery('#parentName').val();
	  if(name==""||name==null){
		  alert("请先添加节点名称！");
		  return false;
	  }else{
	 var para = "name="+encodeURIComponent(name)+"&id="+encodeURIComponent(id)+"&parentId="+encodeURIComponent(parentId)+"&remark="+encodeURIComponent(remark)+"&parentName="+encodeURIComponent(parentName);
     var url="";
		 url = "<%=contextPath %>/SpringR/productType/productSave?"+para;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.parent.location.reload();
		 } else{
			   alert("添加失败"); 
		  }
	   }
	 });
	 }
   }
    
   
      function  display(type){
   	document.getElementById(type).style.display = "block";
   }
   
   function  disappear(type){
   	document.getElementById(type).style.display = "none";
   }
  
   
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增产品类别信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="55%" align="center">
       <input type="hidden" name="id" id="id" size="20" class="BigInput" >
    <tr>
        <td nowrap class="TableData">上级类别：<font style='color:red'>*</font></td>
        <td nowrap class="TableData">
          <input type="text" name="parentName" id="parentName" size="20" class="BigInput" disabled="disabled">
          <input type="hidden" name="parentId" id="parentId" class="BigInput" >
          <input type="button" value="选择类别" onclick="showTree('dtreeId','parentId','parentName');" class="BigButton">
          <div id="dtreeId" onmousemove="display(this.id)" onmouseup="disappear(this.id)" class="dtree" style="overflow: auto;"></div>
        </td>
     </tr>
      <tr>
      <td nowrap class="TableData">产品类别名字：</td>
       <td class="TableData" colspan="3">
 		<input type="text" name="name" id="name"  class="BigInput"/>
      </td>
    </tr>
     
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="3">
 		<textarea name="remark" id="remark" class="BigInput" ></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
    
       <input type="button" value="新建" class="BigButton" onclick="return doSubmitForm1(this.form);">
     
      </td>
    </tr>
  </table>
</form>
</body>
</html>