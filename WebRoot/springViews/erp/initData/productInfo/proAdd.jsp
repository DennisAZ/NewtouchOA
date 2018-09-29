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
<title>产品信息管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<style type="text/css">
#dtreeId{display: none;position:absolute;left: 360px;top:110px;width: 0px solid #c30;width: 200px;height: 180px;background: #ccc;overflow: hidden;}
</style>
<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">
var id="<%=id%>";
var flag = '<%=flag%>';
function doInit(){
	if(flag == '1'){
		var url = "<%=contextPath %>/SpringR/product/getProductById?id=<%=id%>";
		 jQuery.ajax({
		   type : 'POST',
		   url : url,
		   async:false,
		   success : function(jsonData){ 
			
			 var json = JSON.stringify(jsonData);
	   		 var data = eval("(" + json + ")");
	   		 if(data.length > 0){
	   			 jQuery(data).each(function(i,item){  
	   			    jQuery('#proCode').val(item.pro_code);
				    jQuery('#proName').val(item.pro_name);
					jQuery('#proType').val(item.pro_type);
					jQuery('#proPrice').val(item.pro_price);
					jQuery('#unitName').val(item.unit_name);
					jQuery('#styleName').val(item.name);
					jQuery('#parentName').val(item.ptName);
					jQuery('#remark').val(item.remark);
					jQuery('#ptId').val(item.ptId);
 				});
	   		 }
	   		 
		   }
		 });
		
	}
	 var url1 = "<%=contextPath %>/SpringR/productUnit/getList/"+new Date().getTime();
		 jQuery.ajax({
			       type : 'POST',
		  		   url : url1,
	   			   contentType : "application/json",
				   success : function(json_data){ 
				   var data = JSON.stringify(json_data);
				   var dataArray = eval("(" + data + ")");
				   if(dataArray.length > 0){
				   $(dataArray).each(function(i,item){  
				      var selectObj = document.getElementById("unitName");
				    	selectObj.options.add(new Option(item.unitName,item.unitId));
					});
					
				     }
				    }
				});
		 var url2 = "<%=contextPath %>/SpringR/productStyle/getList/"+new Date().getTime();
		 jQuery.ajax({
			       type : 'POST',
		  		   url : url2,
	   			   contentType : "application/json",
				   success : function(json_data){ 
				   var data = JSON.stringify(json_data);
				   var dataArray = eval("(" + data + ")");
				   if(dataArray.length > 0){
				   $(dataArray).each(function(i,item){  
				      var selectObj = document.getElementById("styleName");
				    selectObj.options.add(new Option(item.name,item.id));
					});
					
				     }
				    }
				});
}

   function doSubmitForm(formObject) {
<%--	  if($("#unit_name").val() == ""){ --%>
<%--	    alert("计量单位名称不能为空！");--%>
<%--	    $("#unit_name").focus();--%>
<%--	    $("#unit_name").select();--%>
<%--	    return false;--%>
<%--	  }--%>
	 
	 var proCode = jQuery('#proCode').val();
	 var proName = jQuery('#proName').val();
	 var proType = jQuery('#proType').val();
	 var proPrice = jQuery('#proPrice').val();
	 var unitId = jQuery('#unitName').val();
	 var psId = jQuery('#styleName').val();
	 var ptId = jQuery('#ptId').val();
	 var remark = jQuery('#remark').val();
	 
	 var para = "proName="+encodeURIComponent(proName)+"&remark="+encodeURIComponent(remark)+"&id="+id;
	 +"&proCode="+encodeURIComponent(proCode)+"&proType="+encodeURIComponent(proType)+"&proPrice="+encodeURIComponent(proPrice)
	 +"&unitId="+encodeURIComponent(unitId)+"&psId="+encodeURIComponent(psId)+"&ptId="+encodeURIComponent(ptId);
	 var url="";
	 if(flag == '1'){
    	 url = "<%=contextPath %>/SpringR/product/productUpdate?"+para;
     }
	 else{
		 url = "<%=contextPath %>/SpringR/product/productSave?"+para;
	 }
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="productManage.jsp";
		  }else if(jsonData == "2"){
		   	alert("更新成功");
		    window.location.href="productManage.jsp";
		  }
		  else{
			   alert("添加失败"); 
		  }
	   }
	 });
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
       function  display(type){
   	document.getElementById(type).style.display = "block";
   }
   
   function  disappear(type){
   	document.getElementById(type).style.display = "none";
   }
 function reBlur(){
	 var price=document.getElementById("proPrice").value;
	 var re=/^([0-9]?|[1-9])+\.?[0-9]{0,2}$/;
	 if(!re.test(price)){
		 alert("请输入数字");
		 document.getElementById("proPrice").value="";
		 
	 }
 }
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增产品信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="55%" align="center">
       <input type="hidden" name="ptId" id="ptId" size="20" class="BigInput" >
       <tr>
      <td nowrap class="TableData">产品编号：</td>
       <td class="TableData" colspan="3">
 		<input type="text" name="proCode" id="proCode"  class="BigInput" >
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">产品名字：</td>
       <td class="TableData" colspan="3">
 		<input type="text" name="proName" id="proName"  class="BigInput" >
      </td>
    </tr>
       <tr>
      <td nowrap class="TableData">产品规格：</td>
       <td class="TableData" colspan="3">
 		<input type="text" name="proType" id="proType"  class="BigInput" >
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData">产品单价：</td>
       <td class="TableData" colspan="3">
 		<input type="text" name="proPrice" id="proPrice"  class="BigInput" onblur="reBlur()"  >
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">计量单位：</td>
       <td class="TableData" colspan="3">
      <select name="unitName" id="unitName" style="width:130px;">
        </select>
      </td>
    </tr>
      <tr>
      <td nowrap class="TableData">产品类别：</td>
       <td nowrap class="TableData">
          <input type="text" name="parentName" id="parentName" size="20" class="BigInput" >
          <input type="hidden" name="parentId" id="parentId" class="BigInput" >
          <input type="button" value="选择类别" onclick="showTree('dtreeId','parentId','parentName');" class="BigButton">
          <div id="dtreeId" onmousemove="display(this.id)" onmouseup="disappear(this.id)" class="dtree" style="overflow: auto;"></div>
        </td>
    </tr>
      <tr>
      <td nowrap class="TableData">产品所属大类：</td>
       <td class="TableData" colspan="3">
      <select name="styleName" id="styleName" style="width:130px;">
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" colspan="3">
 		<textarea name="remark" id="remark"   class="BigInput"></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
      <%
      	if(flag.equals("1")){
      %>
       <input type="button" value="更新" class="BigButton" onclick="return doSubmitForm(this.form);">
      <%
      	}else{
      %>
        <input type="button" value="新建" class="BigButton" onclick="return doSubmitForm(this.form);">
      <%
      	}
      %>
      </td>
    </tr>
  </table>
</form>
</body>
</html>