<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ include file="/core/inc/header.jsp" %>
<%
 String msg = request.getParameter("msg") == null ? "" : request.getParameter("msg");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<title>发送提示</title>
</head>

<body>
<TABLE class=MessageBox width=380 align=center>
<TBODY>
<TR>
<TD class="msg info">
<H4 class=title>提示</H4>
<DIV class=content 
style="FONT-SIZE: 12pt"><% if("".equals(msg)){%>内部邮件保存成功！<%}else{%>内部邮件保存失败，失败原因：<br><%=msg%><%}%></DIV></TD></TR></TBODY></TABLE><BR>
<DIV align=center>
<SCRIPT>
   if(parent.mail_menu)
      parent.mail_menu.location.reload();
</SCRIPT>
<INPUT class=SmallButtonW onclick="goBack();" type=button value=返回> 
</DIV>
</body>
</html>