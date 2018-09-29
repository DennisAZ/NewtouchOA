<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ include file="/core/inc/header.jsp" %>
<%
String  flowId = request.getParameter("flowId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入流程</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function tooltip(type , msg) {
  if (type == 1 ) {
    alert('导入成功！');
    history.back();
  } else {
    alert('只能导入xml文件!');
  }
}
function CheckForm()
{
   if(document.form1.attachment.value=="")
   { alert("请选择要导入的xml格式文件！");
     return (false);
   }
   return (true);
}
</script>
</head>

<body  topmargin="5">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/workflow.gif" align="absmiddle"><span class="big3"> 导入流程</span><br>
    </td>
  </tr>
</table>

<br>
<br>

<div align="center" class="Big1">
  <b>请指定用于导入的xml文件：</b>
  <form name="form1" id="form1" method="post" action="<%=contextPath %><%=moduleSrcPath %>/act/YHFlowImportAct/importFlow.act" target="iframe" enctype="multipart/form-data" onsubmit="return CheckForm();">
    <input type="file" name="attachment" id="attachment" class="BigInput" size="30">
    <input type="hidden" name="flowId"  id="flowId" value="<%=flowId %>">
    <input type="submit" value="导入" class="BigButton">
    <br>
     <input type="checkbox" name="userOn" id="userOn"><label for="userOn">导入用户相关信息(若组织结构不同，请勿勾选此项)</label> 
  </form>
  </div>
<br>
<iframe id="iframe" name="iframe" style="display:none"></iframe>
</body>
</html>