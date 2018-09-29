<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>保存投票</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/Javascript">
var seqId = parent.seqId;
function showItem() {
  var url = "<%=contextPath%>/subsys/oa/vote/manage/item/index.jsp?seqId=" + seqId;
  window.location.href = url;
}
function showVote() {
  var url = "<%=contextPath%>/subsys/oa/vote/manage/vote.jsp?seqId=" + seqId;
  window.location.href = url;
}
</script>
</head>
<body class="bodycolor" topmargin="5px">
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>投票保存成功！</div>
    </td>
  </tr>
</table>
<br>
<div align="center">
<input type="button" value="添加投票项目" class="BigButtonC" onClick="javascript:showItem();">
&nbsp;&nbsp;<input type="button" value="添加子投票" class="BigButton" onClick="javascript:showVote();">
&nbsp;&nbsp;<input type="button" value="返回" class="BigButton" onClick="javascript:history.back()"></div>
</body>
</html>