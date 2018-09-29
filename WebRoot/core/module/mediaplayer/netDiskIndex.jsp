<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%><html>
<head>
<%
  String path = request.getParameter("path") == null ? "" : request.getParameter("path");
  String attachmentName = request.getParameter("fileName") == null ? "" : request.getParameter("fileName");
  String  mediaType = request.getParameter("mediaType") == null ? "2" : request.getParameter("mediaType") ;
  String  video = request.getParameter("video") == null ? "1" : request.getParameter("video") ;
  String param = "fileName=" + attachmentName + "&path=" + path + "&directView=1";
  String attachUrl = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal.act?" + param;
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>媒体播放器</title>
<script type="text/javascript">
var mediaType = "<%=mediaType%>";
var attachmentName1 = "<%=attachmentName%>";
var path1 = "<%=path%>";
var video =  "<%=video%>";
var url =  contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal.act?" + "fileName=" + encodeURIComponent(attachmentName1) + "&path=" + encodeURIComponent(path1) + "&directView=1";
function doInit() {
  if(mediaType == "4"){
    //url = encodeURIComponent(url);
    $('flvplayer').movie = contextPath + "/core/module/mediaplayer/flvplayer.swf?vcastr_file=" + url + "&IsAutoPlay=true";
  }else if(mediaType == "2"){
    if(video == "0"){
      $('mplayer').FileName = url;
    }else{
      $('phx').URL = url;
    }
  }else if(mediaType == "1"){
    $('video1').SRC = url;
  }
  if(mediaType == "4"){
    $('showDown').style.display = "none";
  }else{
    $('showDown').style.display = "";
  }
  if($('downUrl')){
    $('downUrl').href = url;
  }
}
</script>
</head>
<body onload="doInit()">
<table id="showDown" border=0 align="center" class="small" cellspacing="0" cellpadding="3" width="100%" height="100%" style="display:none">
  <tr class="TableContent" height=20>
    <td><b>下载文件：</b><a id="downUrl" href="#"><%=attachmentName %></a></td>
  </tr>
  <tr>
    <td align=center valign=top>
<% if("2".equals(mediaType)){ %>
<% if("0".equals(video)){ %>
<object id="mplayer" width="100%" height="68" classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95"
codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=6,4,5,715"
align="baseline" border="0" standby="Loading Microsoft Windows Media Player components..."
type="application/x-oleobject">
  <param name="FileName" value="<%=attachUrl %>">
  <param name="ShowControls" value="1">
  <param name="ShowPositionControls" value="0">
  <param name="ShowAudioControls" value="1">
  <param name="ShowTracker" value="1">
  <param name="ShowDisplay" value="0">
  <param name="ShowStatusBar" value="1">
  <param name="AutoSize" value="0">
  <param name="ShowGotoBar" value="0">
  <param name="ShowCaptioning" value="0">
  <param name="AutoStart" value="1">
  <param name="PlayCount" value="0">
  <param name="AnimationAtStart" value="0">
  <param name="TransparentAtStart" value="0">
  <param name="AllowScan" value="0">
  <param name="EnableContextMenu" value="1">
  <param name="ClickToPlay" value="0">
  <param name="InvokeURLs" value="1">
  <param name="DefaultFrame" value="datawindow">
</object>
<%} else{
  %>
<object classid="clsid:6BF52A52-394A-11D3-B153-00C04F79FAA6" id="phx" width="100%" height="100%">
<param name="URL" value="<%=attachUrl %>">
<param name="rate" value="1">
<param name="balance" value="0">
<param name="currentPosition" value="0">
<param name="defaultFrame" value>
<param name="playCount" value="1">
<param name="autoStart" value="-1">
<param name="currentMarker" value="0">
<param name="invokeURLs" value="-1">
<param name="baseURL" value>
<param name="volume" value="50">
<param name="mute" value="0">
<param name="uiMode" value="full">
<param name="stretchToFit" value="0">
<param name="windowlessVideo" value="0">
<param name="enabled" value="-1">
<param name="enableContextMenu" value="-1">
<param name="fullScreen" value="0">
<param name="SAMIStyle" value>
<param name="SAMILang" value>
<param name="SAMIFilename" value>
<param name="captioningID" value>
<param name="enableErrorDialogs" value="0">
<param name="_cx" value="6482">
<param name="_cy" value="6350">
</object>
<script>
$('phx').width = 800;
$('phx').height = 500;
//self.resizeTo(800,600);
</script>
<%}%>
<%}else if("4".equals(mediaType)){ 
// attachUrl = URLEncoder.encode(attachUrl);
%>
<embed src="flvplayer2.swf?autostart=true" width="400" height="400" 
type="application/x-shockwave-flash"  
flashvars="file=<%= attachUrl%>"
allowfullscreen = true/>
<% } else if("1".equals(mediaType)){%>
<OBJECT ID=video1 CLASSID="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" HEIGHT=100% WIDTH=100%>
<param name="AUTOSTART" value="1">
<param name="SHUFFLE" value="0">
<param name="PREFETCH" value="0">
<param name="NOLABELS" value="0">
<param name="SRC" value="<%=attachUrl %>">
<param name="CONTROLS" value="ImageWindow,StatusBar,ControlPanel">
<param name="CONSOLE" value="Clip2">
<param name="LOOP" value="0">
<param name="NUMLOOP" value="0">
<param name="CENTER" value="0">
<param name="MAINTAINASPECT" value="0">
<param name="BACKGROUNDCOLOR" value="#000000">
</OBJECT>
<%} else if("3".equals(mediaType)){%>
<script type="text/javascript">
  var attachmentName2 = "<%=attachmentName%>";
  var path2 = "<%=path%>";
  var url2 =  contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal.act?" + "fileName=" + encodeURIComponent(attachmentName2) + "&path=" + encodeURIComponent(path2) + "&directView=1";
  location =  url2;
</script>
<%} %>
</td>
</tr>
</table>
</body>
</html>