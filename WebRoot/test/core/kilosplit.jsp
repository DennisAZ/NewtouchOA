<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/core/inc/header.jsp" %>
<%
String t1 = request.getParameter("t1");
System.out.println(t1);
%>
<head>
<title>SWFUpload Demos - Simple Demo</title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript">
function doInti() {
  bindKiloSplitPrcBatch(["t1", "t2", "t3"], "form1");
  bindAssertDateTimePrcBatch([{id:"t4"}, {id:"t5", type:"dt"}, {id:"t6"}]);
}

</script>
</head>
<body onload="doInti();">
<form action="kilosplit.jsp" name="form1" id="form1">
<input type="text" name="t1" id="t1"></input>
<input type="text" name="t2" id="t2"></input>
<input type="text" name="t3" id="t3"></input>

<input type="text" name="t4" id="t4"></input>
<input type="text" name="t5" id="t5"></input>
<input type="text" name="t6" id="t6"></input>
<input type="submit" value="提交"></input>
</form>
</body>
</html>