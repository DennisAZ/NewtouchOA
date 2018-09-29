<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预览页面</title>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/mytable.css"/>
<link  rel="stylesheet"  href="<%=cssPath%>/style.css">
<link type="text/css" href="<%=cssPath%>/cmp/tab.css" rel="stylesheet">
<link href="<%=contextPath%>/core/frame/webos/styles/style1/cmp/css/cmp-all.css" type="text/css" rel="stylesheet"> 
<link href="<%=contextPath%>/core/frame/webos/styles/style1/cmp/css/customer.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.autolayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.columnlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.container.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.panel.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.window.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.cardlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.columnlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.module.js"></script>
<script type="text/javascript" src="js/preview.js"></script>
<style>
.jq-window-header {
  color: #000;
}
.jq-Container-tabheader a {
    font-size: 12px;
  }
  .column {
    height: 100%;
    padding-bottom: 150px;
  }
  
  .yh-imgbox {
    position:relative;
    height: 100px;
    overflow:hidden;
  }
  .yh-imgbox-container {
    position:absolute;
    height: 100px;
    overflow:hidden;
    width:100%;
  }
  
  .yh-imgbox-imgs {
    position:absolute;
  }
  
  .yh-imgbox-texts {
    position:absolute;
    top:0px;
    left:180px;
  }
  
  .yh-imgbox-imgs img {
    height: 100px;
    width: 150px;
    display:block;
  }
  
  .yh-imgbox-texts span {
    cursor:pointer;
    display:block;
    line-height: 20px;
  }
</style>
</head>
<body>
<div id="preview" align="center"></div>
</body>
</html>