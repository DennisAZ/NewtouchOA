<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建图纸类别信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
		<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/dtree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript">
   function doSubmitForm() {
     
	 var drawing_type_name = jQuery('#drawing_type_name').val();
	 var remark = jQuery('#remark').val();
	 
	 if(drawing_type_name == ''){
	 	alert('请填写图纸类别名称！');
	    jQuery("#drawing_type_name").focus();
	    jQuery("#drawing_type_name").select();
	    return false;
	 }
	 var param="drawing_type_name="+encodeURIComponent(drawing_type_name)+"&remark="+encodeURIComponent(remark);
     var url = "<%=contextPath %>/SpringR/produce/addDrawingType?"+param;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		 if(jsonData == "0"){
		   		alert("添加成功");
		   		window.location.href="drawingTypeManage.jsp";
			}else{
					 alert("添加失败"); 
					 return ;
				 }
			 }
	  });
   }
   

</script>
</head>
<body topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	图纸类别基础信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
    <tr>
    	 <td nowrap class="TableData">类别名称：<font color="red">*</font></td>
       <td class="TableData" >
 		<input type="text" name="drawing_type_name" id="drawing_type_name"  class="BigInput"  />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" class="BigInput" cols="40"></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
       <input type="button" value="添加" class="BigButton" onclick="return doSubmitForm();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>