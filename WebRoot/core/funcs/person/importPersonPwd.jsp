<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入用户密码</title>
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script Language="JavaScript">
function importPerson() {
  $('form1').submit();
}
function update1() {
  var url = contextPath + "/yh/core/funcs/person/act/YHPersonAct/add.act";
  getJsonRs(url);
}
</script>
</head>
<body topmargin="5" onload="">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big"><img src="<%=imgPath%>/sys_config.gif" align="absmiddle"><span class="big3">&nbsp;导入用户</span><br>
      </td>
    </tr>
  </table>
  <br>
  <br>
  <input type="button" value="更新" onclick="update1()">
  <div align="center" class="Big1">
  <b>请指定用于导入的CSV文件：</b>
  <form id="form1" name="form1" method="post" action="<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/importPersonPwd.act" enctype="multipart/form-data">
    <input type="file" name="csvFile" id="csvFile" class="BigInput" size="30">
    <input type="button" value="导入" class="BigButton" onclick="importPerson();">
  </form>
  <%=(String)request.getAttribute("data") %>
</body>
</body>
</html>