<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>保存试卷</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/util.js"></script>
<script type="text/javascript">

function doInit() {
  WarningMsrg('所选试题数量超出范围！', 'msrg','error');
}

</script>
</head>
<body onload="doInit()" topmargin="5">

<div id="msrg">
</div>
<br>
<div align="center">
 <input type="button" value="返回" class="BigButton" onClick="location='paperDetails.jsp?paperSeqId=${param.paperSeqId}'">
</div>

</body>
</html>