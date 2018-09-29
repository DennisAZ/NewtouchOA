<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="yh.core.util.ReloadLicenseUtil"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统信息</title>
<script>
function submitForm() {
  if (document.getElementById('file').value) {
	  document.getElementById('bbsForm').submit();
  }
  else {
    alert('请上传注册文件');
  }
}

</script>
</head>

<body>

<form action="uploadLicense.jsp" enctype="multipart/form-data" id="bbsForm" name="bbsForm" method="post">
<br>
<% if(ReloadLicenseUtil.sysLinsencePageErrorFlag){%>
    license错误或过期，请上传正确的license文件，或请联系开发商解决！
    	
 <% } %>
 <br/>
 <!--  
<table class="TableBlock" width="80%" align="center" cellpadding="5">

  <tr class="TableLine2" id="uploadFile">
    <td nowrap width="150"><b>&nbsp;上传注册文件：</b></td>
    <td nowrap>
      <input type="file" id="file" name="file">
      &nbsp;<input type="button" onclick="submitForm()" class="BigButton" value="注册确认">
     
    </td>
  </tr>
</table>
-->
 </form>
</body>
</html>