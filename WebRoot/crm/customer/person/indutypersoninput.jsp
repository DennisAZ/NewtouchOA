<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.global.YHSysProps" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
  String deptId = request.getParameter("deptId");
  if (deptId == null) {
    deptId = "";
  }
  String userName = request.getParameter("userName");
  if (userName == null) {
    userName = "";
  }
  String userId = request.getParameter("userId");
  if (userId == null) {
    userId = "";
  }
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript">
var seqId = "<%=seqId%>";
var userIdStr = "<%=userId%>";
var userNameStr = "<%=userName%>";


function setUserManageArea(){
	location = "<%=contextPath%>/crm/customer/manageAreaSet/privtree.jsp?seqId="+ seqId +"&userName="+encodeURI(userNameStr);
}
</script>
</head>
<body>
<form name="userInfoForm" id="userInfoForm" method="post">
<table cellscpacing="1" cellpadding="3" width="100%">
  <tr>
    <td class="Big">
      <img src="<%=imgPath%>/notify_new.gif"></img><span class="big3">&nbsp;设置管辖区域 </span>
    </td>
  </tr>
</table>
<div align="center">
  <input type="button" value="设置管辖区域" class="BigButton" title="设置管辖区域" onclick="javascript:setUserManageArea();">
</div>
<br>
<div id="contentDiv" style="position:absolute;left:0px;top:60px;">
</div>
</form>
</body>
</html>