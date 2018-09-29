<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>菜单备份/恢复</title>
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";

function doInit(){
  
}

function commit(){
  
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>

    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3"> 菜单备份</span><br>
    </td>
  </tr>
</table>
<div align="center" class="Big1">
<input type="button"  value="菜单导出/备份" class="BigButtonC" onClick="window.open('export.php')">
</div>
<br>
<br>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>

    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3"> 菜单恢复</span><br>
    </td>
  </tr>
</table>
<div align="center" class="Big1">
<b>请指定用于菜单恢复的SQL文件：</b>
<form name="form1" method="post" action="sql.php" enctype="multipart/form-data">
  <input type="file" name="sql_file" class="BigInput" size="30">
  <input type="button" value="恢复" class="BigButton" onclick="return checkForm();">

</form>
</div>

</body>
</html>