
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title id="title"></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
/**
 * 读取cookie
 */
function getCookie(){
  var arr = document.cookie.match(new RegExp("(^| )calendarQueryType=([^;]*)(;|$)"));
  if (arr != null){
    return unescape(arr[2]);
  }
  else{
    return null;
  }
}
function doOnload(){
  var calendarQueryType = getCookie();
  if(calendarQueryType=="day"){
    window.location.href = "<%=contextPath %>/core/funcs/calendar/info/day.jsp";
  }else if(calendarQueryType=="week"){
    window.location.href = "<%=contextPath %>/core/funcs/calendar/info/week.jsp";
  }else if(calendarQueryType=="month"){
    window.location.href = "<%=contextPath %>/core/funcs/calendar/info/month.jsp";
  }else{
    window.location.href = "<%=contextPath %>/core/funcs/calendar/info/day.jsp";
  }
}
</script>
<body topmargin="5"  onload="doOnload();">
</body>
</html>
