<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>手机短信设置</title>
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
  var tabArray = [{title:"短信接收管理 ", content:"", contentUrl:"<%=contextPath%>/core/funcs/system/mobilesms/receivemanage.jsp", imgUrl: imgPath + "/cmp/tab/sys_config.gif", useIframe:true},
                  {title:"短信发送管理", content:"", contentUrl:"<%=contextPath%>/core/funcs/system/mobilesms/sendmanage.jsp", imgUrl: imgPath + "/cmp/tab/sys_config.gif", useIframe:true},
                  {title:"模块权限", content:"", contentUrl:"<%=contextPath%>/core/funcs/system/mobilesms/typepriv.jsp", imgUrl: imgPath + "/cmp/tab/sys_config.gif", useIframe:true},
                  {title:"被提醒权限 ", content:"", contentUrl:"<%=contextPath%>/core/funcs/system/mobilesms/remindpriv.jsp", imgUrl: imgPath + "/cmp/tab/sys_config.gif", useIframe:true},
                  {title:"外发权限 ", content:"", contentUrl:"<%=contextPath%>/core/funcs/system/mobilesms/outpriv.jsp", imgUrl: imgPath + "/cmp/tab/sys_config.gif", useIframe:true},
                  {title:"提醒权限 ", content:"", contentUrl:"<%=contextPath%>/core/funcs/system/mobilesms/sms2remindpriv.jsp", imgUrl: imgPath + "/cmp/tab/sys_config.gif", useIframe:true}];
  buildTab(tabArray,'contentDiv',800);
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<div id="contentDiv"></div>
</body>
</html>