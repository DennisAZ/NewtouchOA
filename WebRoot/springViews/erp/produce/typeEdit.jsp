<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String typeId = request.getParameter("typeId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑生产加工类型</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">

<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var typeId="<%=typeId%>";
function doInit(){
getType();
}
  
 
function getType(){
	var url = "<%=contextPath %>/SpringR/produce/getTypeById?typeId=<%=typeId%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			
			jQuery('#code').val(json.code);
			jQuery('#machine_name').val(json.machine_name);
			jQuery('#remark').val(json.remark);
			
  } 
		
  
	   
	 });
}

 function doSubmitForm() {
     
	 var code = jQuery('#code').val();
	 var machine_name = jQuery('#machine_name').val();
	 var remark=jQuery('#remark').val();
	 
	 var reqRemark = jQuery('#reqRemark').val();
	 var param="code="+encodeURIComponent(code)+
	 	       "&remark="+encodeURIComponent(remark)+
	 	       "&typeId="+encodeURIComponent(typeId)+
	 	       "&machine_name="+encodeURIComponent(machine_name);
     var url="";
     url = "<%=contextPath %>/SpringR/produce/updateType?"+param;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		 if(jsonData == "0"){
		   		alert("更新成功");
		   		window.location.href="typeManage.jsp";
			}else{
					 alert("更新失败"); 
				 }
			 }
	  });
   }
   

</script>
</head><body topmargin="5"  onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	编辑生产加工类型基础信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
      <tr>
      <td nowrap class="TableData">类型编号：</td>
       <td class="TableData">
 		<input type="text" name="code" id="code"  class="BigInput" />
      </td>
     
    </tr>
    <tr>
    	 <td nowrap class="TableData">类型名称：</td>
       <td class="TableData" >
 		<input type="text" name="machine_name" id="machine_name"  class="BigInput"  />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">加工说明：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" class="BigInput" cols="40"></textarea>
      </td>
    </tr>
     <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
       <input type="button" value="编辑" class="BigButton" onclick="return doSubmitForm();">&nbsp;
       <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      </td>
    </tr>
  </table>
</form>
</body>
</html>