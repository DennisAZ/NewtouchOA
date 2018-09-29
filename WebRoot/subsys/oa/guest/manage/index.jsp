<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>友好资源管理</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
  var jso = [
          {title:"贵宾信息管理", contentUrl:"<%=contextPath%>/subsys/oa/guest/manage/info.jsp", imgUrl: "<%=imgPath%>/logistics.gif", useIframe:true}
          ,{title:"添加贵宾信息", contentUrl:"<%=contextPath%>/subsys/oa/guest/manage/new/index.jsp", imgUrl: "<%=imgPath%>/logistics.gif", useIframe:true}
          ,{title:"综合信息查询", contentUrl:"<%=contextPath%>/subsys/oa/guest/manage/search/index.jsp", imgUrl: "<%=imgPath%>/logistics.gif", useIframe:true}
           ];
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="buildTab(jso, 'smsdiv', 800);">
</body>
</html>