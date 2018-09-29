<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/core/inc/header.jsp" %>
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<html>
<head><title>未登录错误</title></head>
<body>
<table align="center" class="MessageBox" width="300">
  <tr>
    <td id="msrgOut" class="msg warning">
     &nbsp;用户未登录，请重新登录!
    </td>
  </tr>
</table>
<table align="center" width="300">
  <tr>
    <td align="center">
      <input type="button" value="重新登录" class="BigButton" onclick="window.open('<%=contextPath %>/login.jsp')" />
    </td>
  </tr>
</table>
</body>
</html>
