<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp"%>

<html>
<head>
<title>选择数据</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="js/index.js"></script>
</head>

<body onload="doInit()">
<div id="form" style=";padding:4px" align=center>
<fieldset>
  <legend class="small" align=left>
      <b>快速查询</b>
  </legend>
<form id="form2" name="form2">
<table border="0" width="100%" align="center">
         <tr id="fiTr"><td>主题词：<input type="text" value="" name="keyword" id="keyword"><input type="button" onclick="doQuery()" value="查询"/></td></tr>
     </table>
</form>
</fieldset>
</div>
<br>
<div >
<div id="rightRole">
<div id="hasRole">
<div id="title" class="header" >选择主题词</div>
<div id="rolesDiv">
</div>

</div>
<div id="noRole" align="center" class="item" style="display:none;color:red">未定义主题词</div>
</div>
<div style="position:absolute;top:400px;left:180px;height:30px;width:60px;">
  <input type=button class="SmallButtonW" value="确定" onclick="window.close()"/>
</div>
</div>
</body>
</html>
