<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String seqId = request.getParameter("seqId");
String flag = request.getParameter("flag");
String disable = request.getParameter("disable");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>栏目列表</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
	var flag = "<%=flag %>";
	var disable = "<%=disable %>";
	var tempStr = "";
	if(flag == 1){
	  tempStr = "station";
	}
	else{
	  tempStr = "column";
	}
	var disableStr = "";
	if("1" == disable){
	  disableStr = "&disable=1";
	}
  var jso = [
           {title:"栏目设置", contentUrl:"<%=contextPath%>/cms/"+tempStr+"/modify.jsp?seqId=<%=seqId %>"+disableStr, imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"下级栏目", contentUrl:"<%=contextPath%>/cms/column/manage.jsp?seqId=<%=seqId %>&flag="+flag+disableStr, imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ];
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="buildTab(jso, 'smsdiv', 800);">
</body>
</html>
