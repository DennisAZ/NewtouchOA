<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if (treeId == null) {
	treeId = "";
  }
  String deptParent = request.getParameter("deptParent");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
</script>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/images/notify_new.gif" align="absmiddle"><span class="big3"> 新建公共用户组</span><br>
    </td>
  </tr>
</table>
<div align="center">
<input type="button" value="新建公共用户组" class="BigButtonC" onClick="location='new.php';" title="创建新的公共用户组">
</div>
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/images/notify_open.gif" align="absmiddle"><span class="big3"> 管理公共用户组</span>
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" align="center">
  <tr class="TableHeader">
    <td nowrap align="center" width="200">用户组名称</td>
    <td nowrap align="center" width="60">排序号</td>
    <td nowrap align="center" width="120">操作</td>
  </tr>
  <tr class="TableData">
    <td nowrap align="center"></td>
    <td nowrap align="center"></td>
    <td nowrap align="center">
       <a href="edit.jsp?GROUP_ID=4"> 编辑</a>
       <a href="javascript:delete_group(4);"> 删除</a>
       <a href="setuser.jsp?GROUP_ID=4"> 设置用户</a>
    </td>
  </tr>
</table>
<br>
<div align="center">
  <input type="button" value="返回" class="BigButton" title="返回" name="button2" OnClick="location='../dept_new.php'">
</div>
</body>
</html>