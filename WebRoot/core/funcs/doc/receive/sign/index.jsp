<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title></title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/cmp/tab.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
function doInit(){
  var ca =  [{title:"未签收", contentUrl:contextPath + "/core/funcs/doc/receive/sign/sign.jsp?type=0",useIframe:true}
  ,{title:"已签收", contentUrl:contextPath + "/core/funcs/doc/receive/sign/sign.jsp?type=1",useIframe:true}];
  buildTab(ca , "content");
}

</script>
</head>

<body onload="doInit();">
<div id="content"></div>
</body>
</html>