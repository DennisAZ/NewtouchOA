<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>同步加载多标签</title>
<link rel="stylesheet" href = "../css/change.css">
<script type="text/javascript" src="main2.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function doInit() {
  var rtText = getTextRs("/yh/raw/ljf/YHCodeTrnsAct/trnsCode.act?page=test;rad;client;tab;code;go2.jsp");
  $("codeDisp").innerHTML = rtText;
}
</script>
</head>

<body onload="doInit();">
<div style="font-size:16px;color:#333333">同步加载多标签</div>
<div id="codeDisp">
</div>
</body>
</html>