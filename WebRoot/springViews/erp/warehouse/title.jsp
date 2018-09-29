<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>title</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
</head>
<body topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
    <span class="big3">&nbsp;
	        仓库管理
	</span>
    </td>
    <td align="center">
      <input type="button" value="新建仓库" class="BigButtonC" onClick="parent.deptinput.location='input.jsp';" title="仓库">&nbsp;&nbsp;
    </td>
    </tr>
</table>
</body>
</html>