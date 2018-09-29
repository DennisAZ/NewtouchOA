<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>视频会议设置</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
function check(){
  var jso = [
             {title:"管理员设置", contentUrl:"<%=contextPath%>/subsys/oa/netmeeting/video/set/manager.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
            ,{title:"参数设置", contentUrl:"<%=contextPath%>/subsys/oa/netmeeting/video/set/set.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
            ];
  buildTab(jso, 'smsdiv', 800);
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="check();">
</body>
</html>
