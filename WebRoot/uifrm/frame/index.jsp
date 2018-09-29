<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>T6中小企业管理平台</title>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../js/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="../js/ui/yh/yh.all.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.borderlayout.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<script type="text/javascript" src="js/index.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/index.css"/>
</head>
<body>
  <div class="ui-layout-north" id="northContainer">
    <div class="banner_bg">
    </div>
  </div>
  <div class="ui-layout-west" id="westContainer">
    <div class="left-menu"><div></div></div>
  </div>
  <div class="ui-layout-center" id="mainContainer">
    <iframe id="desktop" name="desktop" src="portal/index.jsp" frameborder="0">
    </iframe>
    <iframe id="workspace" name="workspace" src="about:blank" frameborder="0">
    </iframe>
  </div>
  <div class="ui-layout-south" id="southContainer">
    <div class="status-content"></div>
  </div>
</body>
</html>