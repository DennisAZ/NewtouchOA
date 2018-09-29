<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String nodeId = request.getParameter("id");
	String nodeName = request.getParameter("name");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增仓库信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="StyleSheet" href="<%=contextPath %>/springViews/css/dtree.css" type="text/css" />

<style type="text/css">
<!--
#dtreeId{display: none;position:absolute;left: 360px;top:110px;width: 0px solid #c30;width: 200px;height: 180px;background: #ccc;overflow: hidden;}
-->
</style>
<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">

 //提交表单的请求
   function doSubmitForm(formObject) {
	  if($("#area_name").val() == ""){ 
	    alert("组织区域名称不能为空！");
	    $("#area_name").focus();
	    $("#area_name").select();
	    return false;
	  }
	  if($("#parent_name").val() == ""){ 
	    alert("父节点名称不能为空！");
	    $("#parent_name").focus();
	    $("#parent_name").select();
	    return false;
	  }
     var url = "<%=contextPath %>/SpringR/area/addArea";
     var jsonObject;
	 if(formObject != 'undefined' && formObject != null){
	   	jsonObject = getFormElements(formObject);
	 }
	 $.ajax({
	   type : 'POST',
	   url : url,
	   data : jsonObject,
	   contentType : "application/json",
	   success : function(jsonData){   
	   	  
		  var rtJson = eval("(" + jsonData + ")"); 
		  if(rtJson.rtState == "0"){
		  document.getElementById("area_name").value = "";
		  document.getElementById("parent_name").value = "";
	      document.getElementById("parent_id").value = "";
		   // window.location.href = contextPath + "/springViews/jsp/area/submit.jsp";
		  }else{
		    alert(rtJson.rtMsrg); 
		  }
		   parent.deptListTree.location.reload();
	   }
	 });
   }
   
   function showTree(type,setId,setName)
{
	var	url = contextPath + "/SpringR/warehouse/showWHTree/"+new Date().getTime(); 
    var d = new dTree('d');
    
	 jQuery.ajax({
	   type : 'GET',
	   url : url,
	   success : function(json_data){ 
	   var data1 = JSON.stringify(json_data);
	   var data2 = eval("(" + data1 + ")");
	   if(data2.length > 0){
	    	$(data2).each(function(i,item){  
			 var NodeId = item.id;
			 var Parent_id = item.parent_id;
			 var area_name = item.name;
			 d.add(NodeId,Parent_id,area_name,"javascript:setValue('"+ NodeId +"','"+ area_name +"','"+setId+"','"+setName+"')","","","","",true);
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
   function setValue(id,name,setID,setName){
   		jQuery('#'+setID).val(id);
   		jQuery('#'+setName).val(name);
   }
   
   function  display(type){
   	document.getElementById(type).style.display = "block";
   }
   
   function  disappear(type){
   	document.getElementById(type).style.display = "none";
   }
</script>
</head>
<body topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;新增仓库信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
 <input type="hidden" id="tree_code" name="tree_code" value=""/>
<table class="TableBlock" width="55%" align="center">
    <tr>
      <td nowrap class="TableData">仓库名称：<font style='color:red'>*</font></td>
      <td class="TableData" colspan=3>
       <input type="text" name="area_name" id="area_name" size="20" class="BigInput" >
      </td>
    </tr>
    <tr>
        <td nowrap class="TableData">上级仓库：<font style='color:red'>*</font></td>
        <td nowrap class="TableData">
          <input type="text" name="parent_name" id="parent_name" size="20" class="BigInput" >
          <input type="hidden" name="parent_id" id="parent_id" value="-1">
          <input type="button" value="选择区域" onclick="showTree('dtreeId','parent_id','parent_name');" class="BigButton">
          <div id="dtreeId" onmousemove="display(this.id)" onmouseup="disappear(this.id)" class="dtree" style="overflow: auto;"></div>
        </td>
     </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="保存" class="BigButton" onclick="return doSubmitForm(this.form);">
      </td>
    </tr>
  </table>
  <!--  
   <div class="dtree" id="dtreeId"> </div>
   -->
</form>
</body>
</html>