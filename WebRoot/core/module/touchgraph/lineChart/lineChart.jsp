<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript">
function doInit(){
  var height = document.viewport.getHeight() - 20;
  $('lineChartDiv').style.height = height + "px";
}
</script>
</head>
<body onload="doInit()">
<div id="lineChartDiv" align=center style="height:100%">
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
      id="lineChart" width="100%" height="100%"
      codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
      <param name="movie" value="lineChart.swf?contextPath=<%=contextPath %>" />
      <param name="quality" value="high" />
      <param name="bgcolor" value="#869ca7" />
      <param name="allowScriptAccess" value="sameDomain" />
      <embed src="lineChart.swf" quality="high" bgcolor="#869ca7"
        width="100%" height="100%" name="lineChart" align="middle"
        play="true"
        loop="false"
        quality="high"
        allowScriptAccess="sameDomain"
        type="application/x-shockwave-flash"
        pluginspage="http://www.adobe.com/go/getflashplayer">
      </embed>
  </object>
  </div>
</body>
</html>