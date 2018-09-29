<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>车辆管理</title>
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
           {title:"待批申请", contentUrl:"<%=contextPath%>/subsys/oa/vehicle/query/query.jsp?status=0", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"已准申请", contentUrl:"<%=contextPath%>/subsys/oa/vehicle/query/query.jsp?status=1", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"使用中车辆", contentUrl:"<%=contextPath%>/subsys/oa/vehicle/query/query.jsp?status=2", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"未准申请", contentUrl:"<%=contextPath%>/subsys/oa/vehicle/query/query.jsp?status=3", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"未使用车辆", contentUrl:"<%=contextPath%>/subsys/oa/vehicle/query/notUsing.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"全部记录查询", contentUrl:"<%=contextPath%>/subsys/oa/vehicle/query/queryAll.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ];
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="buildTab(jso, 'smsdiv', 800);">
</body>
</html>
