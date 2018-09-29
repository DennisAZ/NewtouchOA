<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
    String userId=request.getParameter("userId");
    String windows=request.getParameter("windows");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/userinfo/js/userinfoUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/userinfo/js/userinfo.js"></script>
<title>Insert title here</title>
</head>
<body onLoad="getDiaryShare('bodyContent',<%=userId%>)">
<div id="body_top">
<table border="0" width="100%" cellspacing="0" cellpadding="3" margin-top="3" class="small">
  <tr>
    <td class="Big">&nbsp;<img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle"><span class="big3">共享日志</span>
    </td>
  </tr>
</table>
</div>
<div   id="bodyContent"></div>
<br><br>
<div  align="center">
     <input type="button" value="返回" class="BigButton" onClick="history.back();">

</div>
</body>
</html>