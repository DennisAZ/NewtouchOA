<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发送文件</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function onSubmit(){
  document.form1.submit();
}
</script>
</head>
<body >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3">数据交换平台--YH下发送文件</span><br>
    </td>
  </tr>
</table>
<br>
<form id="form1" name="form1" action="<%=contextPath%>/yh/core/esb/server/demo/act/ESBDemoAct/uploadFile.act" method="post" enctype="multipart/form-data">
<table class="TableBlock" width="90%" align="center">
  <tr>
	<td align="left" width="30%" class="TableContent" nowrap>发送文件路径：</td>
    <td align="center" class="TableData" width="70%" nowrap>
      <input id="filePath" name="filePath" type="file" style="width: 100%;">
    </td>
  </tr>
  <tr>
	<td align="left" width="30%" class="TableContent" nowrap>接收文件人：</td>
    <td align="center" class="TableData" width="70%" nowrap>
      <input id="toId" name="toId" type="text" style="width: 98%;">
    </td>
  </tr>  
  <tr>
	<td align="left" width="30%" class="TableContent" nowrap colspan="2">
      YH三个点的演示，此处选择文件路径必须为YH服务器路径。
    </td>
  </tr>
  <tr>
  	<td colspan="2" align="center">
  	  <input type="button" value="提交" onclick="onSubmit()">
  	</td>
  </tr>
</table>
</form>
</body>
</html>