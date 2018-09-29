<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
  String contextPath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>浏览主题图片</title>
</head>
<body style="padding:0px;margin:0px">
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
      id="imageView" width="100%" height="100%"
      codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
      <param name="movie" value="imageView.swf?path=<%=contextPath %>" />
      <param name="quality" value="high" />
      <param name="bgcolor" value="#000000" />
      <param name="allowScriptAccess" value="sameDomain" />
      <embed src="imageView.swf" quality="high" bgcolor="#000000"
        width="100%" height="100%" name="imageView" align="middle"
        play="true"
        loop="false"
        quality="high"
        allowScriptAccess="sameDomain"
        type="application/x-shockwave-flash"
        pluginspage="http://www.adobe.com/go/getflashplayer">
      </embed>
  </object>
</body>
</html>