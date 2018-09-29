<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>印章管理</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
/**
 * 页面加载初始化
 */
function doInit() {
  var tabArray = [{title:"印章制作", content:"", contentUrl:"<%=contextPath%>/subsys/jtgwjh/sealmanage/makeseal.jsp", imgUrl: imgPath + "/cmp/tab/green_arrow.gif", useIframe:true},
                  {title:"批量印章制作", content:"", contentUrl:"<%=contextPath%>/subsys/jtgwjh/sealmanage/multiplemakeseal.jsp", imgUrl: imgPath + "/cmp/tab/green_arrow.gif", useIframe:true},
                  
                  {title:"印章权限管理", content:"", contentUrl:"<%=contextPath%>/subsys/jtgwjh/sealmanage/manage.jsp", imgUrl: imgPath + "/cmp/tab/green_arrow.gif", useIframe:true},
                  {title:"印章日志", content:"", contentUrl:"<%=contextPath%>/subsys/jtgwjh/sealmanage/log.jsp", imgUrl: imgPath + "/cmp/tab/green_arrow.gif", useIframe:true},
 
    
                  {title:"同步", content:"", contentUrl:"<%=contextPath%>/subsys/jtgwjh/sealmanage/synSeal.jsp", imgUrl: imgPath + "/cmp/tab/green_arrow.gif", useIframe:true}];
  
  buildTab(tabArray,'contentDiv',800);
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<div id="contentDiv"></div>
</body>
</html>