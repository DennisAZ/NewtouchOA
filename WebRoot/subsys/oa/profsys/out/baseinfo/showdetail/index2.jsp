<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
%>
<html>
<head>
<title>出访项目</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
  var seqId = "<%=seqId%>";
  var jso = [
          {title:"基本信息", contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/base2.jsp?seqId=" + seqId, imgUrl: "<%=imgPath%>/show_reader.gif", useIframe:true}
          ,{title:"项目成员", contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/user.jsp?seqId=" + seqId, imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"项目日程", contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/calendar.jsp?seqId=" + seqId, imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"项目会谈纪要", contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/comm.jsp?seqId=" + seqId, imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"项目相关文档", contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/showdetail/file.jsp?seqId=" + seqId, imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ];
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="buildTab(jso, 'smsdiv', 800);">
</body>
</html>
