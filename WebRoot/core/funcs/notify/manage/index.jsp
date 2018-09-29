<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>公告通知管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
/**
 * 页面加载初始化

 */
function doInit() {
  var tabArray = [{title:"公告管理", content:"", contentUrl:"<%=contextPath%>/core/funcs/notify/manage/notifyList.jsp", imgUrl: "<%=imgPath%>/notify.gif", useIframe:true},
                  {title:"新建公告", content:"", contentUrl:"<%=contextPath%>/core/funcs/notify/manage/notifyAdd.jsp", imgUrl:  "<%=imgPath%>/notify_new.gif", useIframe:true},
                  {title:"公告查询", content:"", contentUrl:"<%=contextPath%>/core/funcs/notify/manage/notifyQuery.jsp", imgUrl: "<%=imgPath%>/search.gif", useIframe:true}
                 ];
  buildTab(tabArray,'contentDiv',800);
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<div id="contentDiv"></div>
</body>
</html>