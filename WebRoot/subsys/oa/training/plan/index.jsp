<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>培训计划列表</title>
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
  var tabArray = [{title:"培训计划管理", content:"", contentUrl:"<%=contextPath%>/subsys/oa/training/plan/planManage.jsp", imgUrl: imgPath + "/cmp/tab/email.gif", useIframe:true},
                  {title:"新建培训计划", content:"", contentUrl:"<%=contextPath%>/subsys/oa/training/plan/newTrainingPlan.jsp", imgUrl: imgPath + "/cmp/tab/mobile_sms.gif", useIframe:true},
                  {title:"培训计划查询", content:"", contentUrl:"<%=contextPath%>/subsys/oa/training/plan/queryTrainingPlan.jsp", imgUrl: imgPath + "/cmp/tab/mobile_sms.gif", useIframe:true}
  								];
  buildTab(tabArray,'contentDiv',800);
}
</script>
</head>
</html>
<body onload="doInit();" topmargin="3">
<div id="contentDiv"></div>
</body>

