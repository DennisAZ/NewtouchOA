<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>工作流</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/sso.js"></script>
<script type="text/javascript">
function handleBack(data) {
  alert(data.retState);
}
function test() {
  rsSSO("http://192.168.0.123/yh/test/core/sso/info.jsp");
}
</script>
</head>
<body>
<input type="button" onclick="test();" value="取数"></input>
</body>
</html>