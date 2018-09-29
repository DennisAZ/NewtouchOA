<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String runId = request.getParameter("runId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发文拟稿</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="js/giveNum.js"></script>
<script type="text/javascript">
var runId = "<%=runId %>";
</script>
</head>
<body onload="doInit()">
<form id="form1" name="form1">
<table width="60%" class="TableBlock" align="center">
  <tr>
    <td class="TableContent">文件字：</td>
    <td class="TableData">
    <select id="docWord" name="docWord" onchange="getWordNum(this.value)">
    </select>
    </td>
  </tr>
  <tr>
    <td class="TableContent">年度：</td>
    <td class="TableData">
    <select name="docYear" id="docYear" onchange="getNum(this.value)">
    </select>
    </td>
  </tr>
  <tr>
    <td class="TableContent">编号：</td>
    <td class="TableData">
    <input name="docNum" id="docNum" type="text" onblur="setDocNum(this.value)">
    </td>
  </tr>
   <tr>
    <td class="TableContent">发文编号：</td>
    <td class="TableData">
    <input name="doc" id="doc" type="text" readonly/>
    </td>
  </tr>
  <tr class="TableControl">
    <td colspan=2 align="center">
    <input type="hidden" name="docWordSeqId" id="docWordSeqId" />
    <input type="hidden" name="runId" id="runId" value="<%=runId %>"/>
    <input type="button" class="BigButton" value="分配文号" onclick="sendNum()">
    </td>
  </tr>
</table>
</form>
</body>
</html>