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
<title>新增产品所属大类信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">

<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">

var flag = '<%=flag%>';
function doInit(){
	if(flag == '1'){
		var url = "<%=contextPath %>/SpringR/productStyle/getProductById?id=<%=id%>";
		 jQuery.ajax({
		   type : 'POST',
		   url : url,
		   success : function(jsonData){ 
			
			 var json = JSON.stringify(jsonData);
	   		 var data = eval("(" + json + ")");
	   		 if(data.length > 0){
	   			 jQuery(data).each(function(i,item){  
	   			  jQuery('#name').val(item.name);
	   			  jQuery('#id').val(item.id);
	   			  jQuery('#remark').val(item.remark);
 				});
	   		 }
	   		 
		   }
		 });
	}
}
   function doSubmitForm(formObject) {
<%--	  if($("#unit_name").val() == ""){ --%>
<%--	    alert("计量单位名称不能为空！");--%>
<%--	    $("#unit_name").focus();--%>
<%--	    $("#unit_name").select();--%>
<%--	    return false;--%>
<%--	  }--%>
	 
	 var name = jQuery('#name').val();
	 var id = jQuery('#id').val();
	 var remark = jQuery('#remark').val();
	 var para = "name="+encodeURIComponent(name)+"&id="+encodeURIComponent(id)+"&remark="+encodeURIComponent(remark);
     var url="";
	 if(flag == '1'){
    	 url = "<%=contextPath %>/SpringR/productStyle/productUpdate?"+para;
     }
	 else{
		 url = "<%=contextPath %>/SpringR/productStyle/productSave?"+para;
	 }
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("添加成功");
		    window.location.href="manage.jsp";
		  }else if(jsonData == "2"){
		   	alert("更新成功");
		    window.location.href="manage.jsp";
		  }
		  else{
			   alert("添加失败"); 
		  }
	   }
	 });
   }
</script>
</head>
<body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       新增产品所属大类信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="55%" align="center">
       <input type="hidden" name="id" id="id" size="20" class="BigInput" >
      <tr>
      <td nowrap class="TableData">产品所属大类名字：</td>
       <td class="TableData" colspan="3">
 		<input type="text" name="name" id="name"  class="BigInput" ></textarea>
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