<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  int dateSpan = YHUtility.getDaySpan("2010-03-31");
  //String timeStr = "<span style=\"color:#FF0000\">3-31还有&lt;" + dateSpan + "&gt;天</span>";
%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Custom Layouts and Containers - Portal Example</title>

    <!-- ** CSS ** -->
    <!-- base library -->
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/core/ext/resources/css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="<%=cssPath %>/extResources/css/ext-yhtheme.css" />

    <!-- overrides to base library -->
    <link rel="stylesheet" type="text/css" href="../ux/css/Portal.css" />

    <!-- page specific -->
    <link rel="stylesheet" type="text/css" href="<%=cssPath %>/style.css" />
    <link rel="stylesheet" type="text/css" href="<%=cssPath %>/mytable.css" />
    
<script type="text/javascript">

var dateSpan = "<%=dateSpan%>";
var contextPath = "<%=contextPath %>";

<%
  //用户禁止查看桌面
  String userName = "";

  YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
  
  if (person != null){
    
    userName = person.getUserId();
    String viewFlag = person.getNotViewTable();
    
    if (viewFlag == null || "".equals(viewFlag.trim())){
      viewFlag = "0";
    }
    
    if ("1".equals(viewFlag.trim())){
      //转向一个空桌面页面
      response.sendRedirect(contextPath + "/core/ext/frame/portal/noneDesktop.jsp");
    }
  }
%>

var userName = "<%=userName%>-";
</script>

<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/prototype/ext-prototype-adapter.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/ext-all.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="../ux/Portal.js"></script>
<script type="text/javascript" src="../ux/PortalColumn.js"></script>
<script type="text/javascript" src="../ux/Portlet.js"></script>
<script type="text/javascript" src="../ux/SliderTip.js"></script>
<script type="text/javascript" src="portal.js"></script>
<script type="text/javascript" src="marquee.js"></script>

</head>
<body></body>
</html>