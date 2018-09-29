<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  int findName = Integer.parseInt(request.getParameter("c"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加接口用户</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var findName = "<%=findName%>";
function back(){
  location = "<%=contextPath %>/core/funcs/system/censorwords/manage/index.jsp";
}
</script>
</head>
<body topmargin="5">
<table class="MessageBox" align="center" width="300">
  <tr>
  <%
    if(findName != 0){
  %>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">共删除<%=findName %>个词语</div>
    </td>
    <%}else{ %>
  <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">没有您要删除的词语</div>
    </td>
  <%
    }
  %>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="window.history.back();"></center>
</body>
</html>