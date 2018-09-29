<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String  formId = request.getParameter("formId");
String sortId = request.getParameter("sortId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>表单导入</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function tooltip(type , msg , id) {
  if (type == 1 ) {
    alert('导入成功！');
    parent.sort.location.reload();
    location.href = contextPath + '/core/funcs/workflow/flowform/import.jsp?formId='+id+'&sortId=<%=sortId %>';
  } else {
    alert('只能导入TXT或HTM、HTML文件!');
  }
  
}

</script>
</head>

<body  topmargin="5">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><span class="big3"> 导入表单</span><br>
    </td>
  </tr>
</table>

<br>
<br>

<div align="center" class="Big1">
<b>请指定表单TXT文本文件或HTM、HTML文件：</b>
<form method="post" action="<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowImportAct/importForm.act" target="iframe" enctype="multipart/form-data">
  <input type="file" name="htmlFile" id="htmlFile" class="BigInput">
  <input type="hidden" id="formId" name="formId" value="<%=formId %>">
  <input type="checkbox" id="isOa" name="isOa"><label for="isOa">Office anywhere中的表单</label>
  <input type="submit" value="导入" class="BigButton">
  <div align=center>
  
<table class="MessageBox" width="400">
    <tbody>
        <tr>
            <td class="msg info" style="color:red">如果您尝试导入来自其他系统的表单,并且您当前表单存在历史数据,则可能造成无法显示,建议您备份后操作!</td>
        </tr>
    </tbody>
</table></div>
</form>
<br>
<input type="button" id="backButton" value="返回" class="BigButton" onclick="location.href ='<%=contextPath %>/core/funcs/workflow/flowform/edit.jsp?seqId=<%=formId %>&sortId=<%=sortId %>'">
</div>
<iframe id="iframe" name="iframe" style="display:none"></iframe>
</body>
</html>