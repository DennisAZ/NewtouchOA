<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/AssistInput.css" type="text/css"/>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/AssistInput1.0.js" ></script>
<script type="text/javascript">
function doInit() {
  var url = contextPath + "/yh/rad/docs/assistinput/YHAssistInputAct/getData.act?w=";
  var par = {bindToId:"input",requestUrl:url,showLength:5,func:showMore};
  new AssistInuput(par);
}
function showMore() {
  alert("显示更多！");
}
</script>
</head>

<body onload="doInit()">
请输入：<input value="" id="input" name="input"/>
</body>
</html>