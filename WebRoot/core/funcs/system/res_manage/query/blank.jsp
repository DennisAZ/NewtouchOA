<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统资源情况查询</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
</head>

<body class="bodycolor" topmargin="5">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/diary.gif" HEIGHT="20"><span class="big3"> 系统资源情况查询</span>
    </td>
  </tr>
</table>
<br>
 <div id="noData" align=center >
   <table class="MessageBox" width="250">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info">请选择用户
            </td>
        </tr>
    </tbody>
</table>
</div>

</body>
</html>
