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
var seqId = '<%=seqId%>';
  var jso = [
          {title:"基本信息", contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/news/base.jsp", imgUrl: "<%=imgPath%>/show_reader.gif", useIframe:true}
          ,{title:"项目成员",disabledFun:showDiv,contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/news/user.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"项目日程",disabledFun:showDiv,contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/news/calendar.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"项目会谈纪要",disabledFun:showDiv,contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/news/comm.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ,{title:"项目相关文档",disabledFun:showDiv,contentUrl:"<%=contextPath%>/subsys/oa/profsys/out/baseinfo/news/file.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
          ];
  var tabs = null;
  function doInit() {
    tabs = buildTab(jso,'smsdiv',800);
    if(seqId ==''){
      closeTabs();
    }
  }
  function closeTabs() {
    tabs.setDisable(true , 1);
    tabs.setDisable(true , 2);
    tabs.setDisable(true , 3);
    tabs.setDisable(true , 4);
  }
  function showDiv() {
    alert("请先保存项目基本信息！");
  }
  function showTabs(){
    tabs.setDisable(false , 1);
    tabs.setDisable(false , 2);
    tabs.setDisable(false , 3);
    tabs.setDisable(false , 4);
  }
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit();">
</body>
</html>
