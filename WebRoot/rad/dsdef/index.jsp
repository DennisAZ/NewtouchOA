<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>数据字典维护</title>
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
  var tabArray = [{title:"列表", content:"", contentUrl:"<%=contextPath%>/rad/dsdef/jsp/dsdeflist.jsp", imgUrl: imgPath + "/cmp/tab/asset.gif", useIframe:true},
                  {title:"增加", content:"", contentUrl:"<%=contextPath%>/rad/dsdef/jsp/dsdefinput.jsp", imgUrl: imgPath + "/cmp/tab/1news.gif", useIframe:true},
                  {title:"新列表", content:"", contentUrl:"<%=contextPath%>/rad/dsdef/jsp/dsdeflist2.jsp", imgUrl: imgPath + "/cmp/tab/asset.gif", useIframe:true},
                  {title:"新增加", content:"", contentUrl:"<%=contextPath%>/rad/dsdef/jsp/dsdefinput2.jsp", imgUrl: imgPath + "/cmp/tab/1news.gif", useIframe:true}];
  buildTab(tabArray,'contentDiv',800);
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<div id="contentDiv"></div>
</body>
</html>