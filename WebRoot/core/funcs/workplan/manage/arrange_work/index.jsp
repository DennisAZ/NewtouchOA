<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<title><%=StaticData.SOFTTITLE%></title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<link rel="stylesheet" type="text/css" href="/theme/9/menu_top.css" />
<script language="JavaScript" src="/inc/js/menu_top.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
  var jso = [
          {title:"今日计划", contentUrl:"<%=contextPath%>/core/funcs/workplan/manage/arrange_work/workIndex.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"本周计划", contentUrl:"<%=contextPath%>/core/funcs/workplan/manage/arrange_work/workIndex2.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"本月计划", contentUrl:"<%=contextPath%>/core/funcs/workplan/manage/arrange_work/workIndex3.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"工作计划查询", contentUrl:"<%=contextPath%>/core/funcs/workplan/manage/arrange_work/query.jsp", imgUrl: "<%=imgPath%>/search.gif",useIframe:true}
          ];
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="buildTab(jso, 'smsdiv', 800);">
</body>
</html>
