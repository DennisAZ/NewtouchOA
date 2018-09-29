<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>物流信息设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<style type="text/css">
#dtreeId{display: none;position:absolute;right:300px;top:110px;width: 0px solid #c30;width: 200px;height: 180px;background: #ccc;overflow: hidden;}
</style>
<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">


   function doSubmitForm(formObject) {
	 
	 var logistics = jQuery('#logistics').val();
	 
	 var url="";
    	 url = "<%=contextPath %>/SpringR/system/addLogistics?logistics="+encodeURIComponent(logistics);
	 
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		   	alert("添加成功");
		   	window.location.href="logisticsManage.jsp";
		  }
		  else{
			   alert("添加失败"); 
		  }
	   }
	 });
   }
</script>
</head>
<body topmargin="5"  >
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	       物流信息设置
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="55%" align="center">
       <input type="hidden" name="ptId" id="ptId" size="20" class="BigInput" >
       <tr>
      <td nowrap class="TableData">物流公司名称：</td>
       <td class="TableData" colspan="3">
 		<input type="text" name="logistics" id="logistics"  class="BigInput" >
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="新建" class="BigButton" onclick="return doSubmitForm(this.form);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>