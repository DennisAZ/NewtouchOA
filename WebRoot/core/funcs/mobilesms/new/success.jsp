<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>Insert  title  here</title>
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/javascript">
function doInit(){
  if('${requestScope.MESSAGE}'){
    $('noMobileTable').show();
    $('noMobile').innerHTML = '${requestScope.MESSAGE}';
  }
  if('${requestScope.MOD}'){
    $('MODTable').show();
    $('MODMSG').innerHTML = '${requestScope.MOD}';
  }
}
</script>
</head> 
<body class="bodycolor" topmargin="5" onload="doInit()"> 
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small"> 
  <tr> 
    <td class="Big"><img src="<%=imgPath%>/mobile_sms.gif" WIDTH="19" HEIGHT="17"><span class="big3"> 手机短信发送情况</span> 
    </td> 
  </tr> 
</table> 
 
<br> 
<table class="MessageBox" align="center" width="210" id="noMobileTable" style="display:none">
  <tr>
    <td class="msg info">
      <h4 class="title">以下人员未设置手机号</h4>
      <div class="content" id="noMobile" style="font-size:12pt"></div>
    </td>
  </tr>
  <tr>
    <td class="msg info">
      <div class="content" id="MODMSG" style="font-size:12pt"></div>
    </td>
  </tr>
    <tr> 
    <td class="msg info" style="background-image:none;"> 
      <div class="content" style="font-size:12pt">短信已提交至短信服务器，正在后台进行发送，您可以继续进行其它工作</div> 
    </td> 
  </tr> 
</table>
<br> 
<div align="center"> 
  <input type="button" name="button1" value="继续发手机短信" class="BigButtonC" onClick="history.back()">&nbsp;
  <input type="button" name="button1" value="查看发送状态" class="BigButtonC" onClick="location='<%=contextPath %>/core/funcs/mobilesms/sendManage/list.jsp'"> 
</div> 
<input type="hidden" id="TO_ID" name="TO_ID" value=""> 
<input type="hidden" id="TO_ID1" name="TO_ID1" value=""> 
<input type="hidden" id="TO_NAME" name="TO_NAME" value=""> 
<input type="hidden" id="CONTENT" name="CONTENT" value=""> 
</body> 
</html> 