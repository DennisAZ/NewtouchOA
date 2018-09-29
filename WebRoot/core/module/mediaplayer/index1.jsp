<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.net.URLDecoder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
  String attachmentName = request.getParameter("attachmentName") == null ? "" : request.getParameter("attachmentName");
  attachmentName = URLDecoder.decode(attachmentName,"utf-8");
  String attachmentId = request.getParameter("attachmentId") == null ? "" : request.getParameter("attachmentId");
  String moudle = request.getParameter("module")== null ? "" : request.getParameter("module");
  String  mediaType = request.getParameter("mediaType") == null ? "2" : request.getParameter("mediaType") ;
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
  function doInit(){
     if(mediaType=="3"){
        TANGER_OCX_OBJ = document.all("TANGER_OCX");
		TANGER_OCX_OBJ.AddDocTypePlugin(".pdf","PDF.NtkoDocument","4.0.0.2",contextPath+"/core/module/mediaplayer/ntkooledocall.cab",51,true);
		
		var attachmentName2 = "<%=attachmentName%>";
		var attachmentId2 = "<%=attachmentId%>";
		var moudle2 = "<%=moudle%>";
		var param2 = "attachmentName=" + encodeURIComponent(attachmentName2) + "&attachmentId=" + attachmentId2 + "&module=" + moudle2 + "&directView=1";
		var url = contextPath+"/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?" + param2+"&date="+new Date();
		TANGER_OCX_OBJ.BeginOpenFromURL(url); //打开PDF从URL
     }
   }
</script>
</head>
<body onload="doInit();">
		<% if("3".equals(mediaType)){%>
<object id="TANGER_OCX" classid="<%=StaticData.Classid%>"
codebase="<%=contextPath %>/core/cntrls/<%=StaticData.Codebase%>" width="1024" height="800">
<param name="Toolbars" value="-1">
<param name="BorderStyle" value="1">
<param name="Titlebar" value="0">
<param name="TitlebarColor" value="42768">
<param name="TitlebarTextColor" value="0">
<param name="Menubar" value="-1">
<param name="MenubarColor" value="14402205">
<param name="BorderColor"  value="14402205">
<PARAM NAME="MenuButtonColor" VALUE="16180947">
<param name="MenuBarStyle" value="3">
<param name="MenuButtonStyle" value="7">
<param name="IsNoCopy" value="0">
<param name="IsStrictNoCopy" value="0">
<param name="MaxUploadSize" value="2000000">
<param name="FileSave" value="-1">
<param name="FileSaveAs" value="-1">
<param name="FilePrint" value="-1">
<param name="IsSaveDocExtention" value="0">
<param name="SignCursorType" value="1">
<param name="IsResetToolbarsOnOpen" value="-1"> 
<param name="MakerCaption" value="<%=StaticData.MakerCaption%>">
<param name="MakerKey" value="<%=StaticData.MakerKey%>">
<param name="ProductCaption" value="<%=StaticData.ProductCaption%>">
<param name="ProductKey" value="<%=StaticData.ProductKey%>">
<SPAN STYLE="color:red"><br>不能装载文档控件，请设置好IE安全级别为中或中低，不支持非IE内核的浏览器。</SPAN>
</object>
<%} %>
</body>
</html>