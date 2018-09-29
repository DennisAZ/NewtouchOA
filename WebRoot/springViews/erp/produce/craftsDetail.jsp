<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String craftsId = request.getParameter("craftsId");
	String falg = request.getParameter("falg");
	if("".equals(falg)||falg==null){
		falg="-1";
	}
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>生产工艺明细</title>
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

function doInit(){
getCrafts();
}
  
 
function getCrafts(){
	var url = "<%=contextPath %>/SpringR/produce/getCraftsById?craftsId=<%=craftsId%>";
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");

			jQuery('#pro_cod').val(json.pro_code);
			jQuery('#pro_name').val(json.pro_name);
			jQuery('#crafts_version').val(json.crafts_version);
			if(json.is_using==1){
				document.getElementById("is_using1").checked=true;
			}else{
			    document.getElementById("is_using2").checked=true;
			}
			jQuery('#remark').val(json.remark);
			jQuery('#proName').val(json.pro_name);
			
  } 
		
  
	   
	 });
}



</script>
</head>
<body topmargin="5"  onload="doInit()">

<form action="" method="post" name="form1" id="form1">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">
    <span class="big3">&nbsp;
	生产工艺基础信息
    </span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
 <form action="" method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
      <tr>
      <td nowrap class="TableData">工艺版本号：</td>
       <td class="TableData">
 		<input type="text" name="crafts_version" id="crafts_version"  class="BigInput" readonly="readonly"/>
      </td>
     
    </tr>
    
      <tr>
      <td nowrap class="TableData">产品名称：</td>
       <td class="TableData">
 		<input type="text" name="proName" id="proName"  class="BigInput" readonly="readonly"/>
      </td>
     
    </tr>
    <tr>
    	 <td nowrap class="TableData">是否当前使用：</td>
       <td class="TableData" >
 		是：<input type="radio" disabled="disabled" name="is_using" id="is_using1"  value="1" class="BigInput"  checked onclick="radionChange()"/>&nbsp;&nbsp;&nbsp;
 		否：<input type="radio" disabled="disabled" name="is_using"   id="is_using2" value="0" class="BigInput" onclick="radionChange()" />
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
       <td class="TableData" >
 		<textarea name="remark" id="remark" readonly class="BigInput" cols="40"></textarea>
      </td>
    </tr>
     <tr align="center" class="TableControl">
      <td colspan=6 nowrap>
      <%
      	if("-1".equals(falg)){
      %>
      <input type="button" value="返回" class="BigButton" onclick="javascript:window.history.back(-1);">
      <%
      	}else{
      %>
      <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
      <%
      	}
      %>
      </td>
    </tr>
  </table>
</form>
</body>
</html>