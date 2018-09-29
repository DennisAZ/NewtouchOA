<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>科目</title>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-patch.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<script type="text/javascript">
$(function() {
  $("body").pageTabs({
    iframeTabs: [{
      title: "资产",
      src: "list.jsp"
    }, {
      title: "负债",
      src: "list.jsp"
    }, {
      title: "共同",
      src: "list.jsp"
    }, {
      title: "权益",
      src: "list.jsp"
    }, {
      title: "成本",
      src: "list.jsp"
    }, {
      title: "损益",
      src: "list.jsp"
    }]
  });
});
</script>
<style>

.ui-tabs-panel > iframe {
  width: 100%;
  height: 100%;
  border: none;
}
</style>
</head>
<body>
</body>
</html>