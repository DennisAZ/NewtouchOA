<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
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
  var tabArray = [{title:"发货通知管理", content:"", contentUrl:"<%=contextPath%>/springViews/erp/warehouse/sendManage.jsp", imgUrl: imgPath + "/cmp/tab/email.gif", useIframe:true},
                  {title:"发货明细管理", content:"", contentUrl:"<%=contextPath%>/springViews/erp/warehouse/whShowDetial.jsp", imgUrl: imgPath + "/cmp/tab/mobile_sms.gif", useIframe:true}];
  buildTab(tabArray,'contentDiv',800);
}
</script>
</head>
</html>
<body onload="doInit();" topmargin="3">
<div id="contentDiv"></div>
</body>

