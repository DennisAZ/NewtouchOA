<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<title>系统安全设置</title>
</head>
<body topmargin="5">
<div align="center">
<br>
<table class="MessageBox" align="center" width="240">
  <tr>

    <td class="msg info">
      <div class="content" style="font-size:12pt">设置完成！</div>
    </td>
  </tr>
</table>
<Input type="button" name="button" class="BigButton" value="返回" onclick="location='<%=contextPath%>/core/funcs/menu/menupara.jsp'">
</div>
</body>

</html>