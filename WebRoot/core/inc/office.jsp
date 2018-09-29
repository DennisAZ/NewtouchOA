<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String fileNameOrgn = request.getParameter(YHActionKeys.UPLOAD_FILE_NAME_ORGN);
  String fileNameServer = request.getParameter(YHActionKeys.UPLOAD_FILE_NAME_SERVER);
  String isReadOnlyStr = request.getParameter("isReadOnly");
  boolean isReadOnly = true;
  int opNum = 5;
  if (isReadOnlyStr != null && isReadOnlyStr.equalsIgnoreCase("false")) {
    isReadOnly = false;
    opNum = 4;
  }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>文档浏览</title>
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" src="/core/js/tangerocx.js"></SCRIPT>
<script>
function myload()
{

  TANGER_OCX_SetInfo();
  TANGER_OCX_OBJ.Menubar = false;
  TANGER_OCX_OBJ.Toolbars = false;
}

function MY_SetMarkModify(flag)
{
  if(flag)
  {
     mflag1.className="TableHeader2";
     mflag2.className="TableHeader1";
  }
  else
  {
     mflag1.className="TableHeader1";
     mflag2.className="TableHeader2";
  }
  TANGER_OCX_SetMarkModify(flag);
}

function MY_ShowRevisions(flag)
{
  if(flag)
  {
     sflag1.className="TableHeader2";
     sflag2.className="TableHeader1";
  }
  else
  {
     sflag1.className="TableHeader1";
     sflag2.className="TableHeader2";
  }
  TANGER_OCX_ShowRevisions(flag);
}
</script>
</head>  
<body onLoad="javascript:myload()" onunload="javascript:close_doc()">
<form name="form1" id="form1" method="post" action="upload.jsp?ID=1" enctype="multipart/form-data">
<table width="100%" height="100%" align="center">
  <tr>
    <td valign="top">
                                                         
      <object border="1" id="TANGER_OCX" classid="<%=StaticData.Classid%>"
        codebase="<%=contextPath %>/core/cntrls/<%=StaticData.Codebase%>" align="center" width="100%" height="100%">
      
      <param name="IsNoCopy" value="1">
      <param name="BorderStyle" value="1">
      <param name="BorderColor" value="14402205">
      <param name="TitlebarColor" value="14402205">
      <param name="TitlebarTextColor" value="0">
      <param name="Caption" value="Office文档在线编辑">
      <param name="IsShowToolMenu" value="0">
      <param name="IsHiddenOpenURL" value="0">
      <param name="MakerCaption" value="<%=StaticData.MakerCaption%>">
      <param name="MakerKey" value="<%=StaticData.MakerKey%>">
      <param name="ProductCaption" value="<%=StaticData.ProductCaption%>">
      <param name="ProductKey" value="<%=StaticData.ProductKey%>">
      
      <SPAN STYLE="color:red"><br>不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>
      </object>
    </td>
  </tr>
<%
  if (!isReadOnly) {
%>
  <tr>
    <td align="center">
      <input onclick="TANGER_OCX_SaveDoc(0)" type="button" value="保存">
    </td>
  </tr>
<%
  }
%>
</table>

<script language="JScript" for=TANGER_OCX event="OnDocumentClosed()">
TANGER_OCX_OnDocumentClosed()
</script>

<script language="JScript">
var TANGER_OCX_str;
var TANGER_OCX_obj;

function close_doc()
{
   document.all("TANGER_OCX").setAttribute("IsNoCopy",false);
}
</script>

<script language="JScript" for=TANGER_OCX event="OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj)">
  TANGER_OCX_OnDocumentOpened(TANGER_OCX_str,TANGER_OCX_obj);
</script>

<span id="TANGER_OCX_op" style="display:none">4</span>
<span id="TANGER_OCX_filename" style="display:none"><%=fileNameOrgn %></span>
<span id="TANGER_OCX_attachName" style="display:none"><%=fileNameOrgn %></span>
<span id="TANGER_OCX_attachURL" style="display:none"><%=contextPath %>/getFile?uploadFileNameOrgn=<%=YHUtility.encodeURL(fileNameOrgn) %>&uploadFileNameServer=<%=YHUtility.encodeURL(fileNameServer) %></span>
<span id="TANGER_OCX_user" style="display:none"></span>

<input style="display:none" type="file" name="ATTACHMENT">
<input type="hidden" name="ATTACHMENT_NAME" value="<%=fileNameOrgn %>">
</form>
</body>
</html>