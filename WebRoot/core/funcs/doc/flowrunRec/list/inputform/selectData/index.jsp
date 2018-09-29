<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String id =  YHUtility.null2Empty(request.getParameter("ctrl"));
String itemStr = YHUtility.null2Empty(request.getParameter("itemStr"));
%>
<html>
<head>
<title>选择数据</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<script type="text/javascript">
var ctrlId = "<%=id %>";
var itemStr = "<%=itemStr %>"
</script>
</head>

<body onload="doInit()">
<div id="form" style="display:none;padding:4px" align=center>
<fieldset >
  <legend class="small">
      <b>快速查询</b>
  </legend>
<form id="form2" name="form2">
<table border="0" width="100%" align="center">
         <tr id="fiTr"></tr>
         <tr id="opTr"></tr>
     </table>
</form>
</fieldset>
</div>
<div id="noForm" style="display:none;color:red"  align=center>提示,没定义查询字段！</div>
<div id="container" style="overflow:auto;padding:4px;height:550px;width:790px"></div>
<div id="msrg" style="display:none"></div>
</body>
</html>
