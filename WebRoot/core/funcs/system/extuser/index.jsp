<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>系统 接口设置</title>
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
  var tabArray = [{title:"接口用户管理", content:"", contentUrl:"<%=contextPath%>/yh/core/funcs/system/extuser/act/YHExtUserAct/getExtUser.act", imgUrl: imgPath + "/cmp/tab/sys_config.gif", useIframe:true},
                  {title:"添加接口用户", content:"", contentUrl:"<%=contextPath%>/core/funcs/system/extuser/newuse.jsp", imgUrl: imgPath + "/cmp/tab/notify_new.gif", useIframe:true}];
  buildTab(tabArray,'contentDiv',800);
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<div id="contentDiv"></div>
</body>
</html>