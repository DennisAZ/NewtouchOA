<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.webinfo.file.YHWebInfoFileOperate" %>
<%@ page import="java.net.URLEncoder" %>
<%
String baseFilePath = request.getParameter("filePath");
baseFilePath = YHUtility.transferCode(baseFilePath, "ISO-8859-1", "UTF-8");
if(baseFilePath == null || "".equals(baseFilePath)){
  out.println("你没有指定路径,如不指定有些功能将不能用");
  baseFilePath = "subsys/fis/basecode/requirements";
}
String attachmentName = request.getParameter("attachmentName");
attachmentName = YHUtility.transferCode(attachmentName, "ISO-8859-1", "UTF-8");
if(attachmentName == null || "".equals(attachmentName)){
  out.println("你没有指定附件,如不指定有些功能将不能用");
}
String attachmentPath = baseFilePath + YHWebInfoFileOperate.ATTACHMENTPATH + 
	"/" + URLEncoder.encode(attachmentName, "UTF-8");
%>
<HTML>
<HEAD>
<TITLE>ddd.docx 在线编辑</TITLE>
<meta http-equiv="content-type" content="text/html;charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">

<script> 
var TANGER_OCX_OBJ;
function TANGER_OCX_EnableFileNewMenu(boolvalue)
{
  TANGER_OCX_OBJ.EnableFileCommand(0) = boolvalue;
}
//允许或禁止文件－>打开菜单
function TANGER_OCX_EnableFileOpenMenu(boolvalue)
{
  TANGER_OCX_OBJ.EnableFileCommand(1) = boolvalue;
}
//允许或禁止文件－>保存菜单
function TANGER_OCX_EnableFileSaveMenu(boolvalue)
{
  TANGER_OCX_OBJ.EnableFileCommand(3) = boolvalue;
}
//允许或禁止文件－>另存为菜单

function TANGER_OCX_EnableFileSaveAsMenu(boolvalue)
{
  TANGER_OCX_OBJ.EnableFileCommand(4) = boolvalue;
}
//允许或禁止文件－>打印菜单
function TANGER_OCX_EnableFilePrintMenu(boolvalue)
{
  TANGER_OCX_OBJ.EnableFileCommand(5) = boolvalue;
}
//允许或禁止文件－>打印预览菜单
function TANGER_OCX_EnableFilePrintPreviewMenu(boolvalue)
{
  TANGER_OCX_OBJ.EnableFileCommand(6) = boolvalue;
}
function setInfo(){
  var useUTF8 = (document.charset == "utf-8");
  TANGER_OCX_OBJ.IsUseUTF8Data = useUTF8;
  TANGER_OCX_OBJ.FileNew = false;
  TANGER_OCX_OBJ.FileClose = false;
  TANGER_OCX_EnableFileNewMenu(false);
  TANGER_OCX_EnableFileOpenMenu(false);
  TANGER_OCX_EnableFileSaveMenu(false);
  TANGER_OCX_EnableFileSaveAsMenu(false);
  TANGER_OCX_OBJ.Menubar  = true;
    TANGER_OCX_OBJ.IsShowToolMenu = true;  //关闭或打开工具菜单
    TANGER_OCX_OBJ.Toolbars  = true;
  
}
function myload()
{
  TANGER_OCX_OBJ = document.getElementById('TANGER_OCX');
  setInfo();
 
  TANGER_OCX_OBJ.BeginOpenFromURL("/yh/getFile?uploadFileNameServer=<%=attachmentPath %>");
}
function save(){
  if(confirm('是否保存！'))
  TANGER_OCX_OBJ.SaveToURL('/yh/yh/core/funcs/webinfo/act/YHWebInfoAct/doUpdateDoc.act','docFile','baseFilePath=<%=baseFilePath%>','<%=attachmentName%>',0);
}
</script>
</HEAD>
 
<BODY class="bodycolor" leftmargin="0" topmargin="5" onLoad="myload();this.focus();" onunload="save();">
<input class="SmallButtonW" type="button" onclick="save()" value="保存">
<div>
<object id="TANGER_OCX" classid="<%=StaticData.Classid%>"
codebase="<%=contextPath %>/core/cntrls/<%=StaticData.Codebase%>" width="1024" height="800">
 
<param name="IsNoCopy" value="0">
<param name="FileSave" value="1">
<param name="FileSaveAs" value="1">
<param name="BorderStyle" value="1">
<param name="BorderColor" value="14402205">
<param name="TitlebarColor" value="14402205">
<param name="TitlebarTextColor" value="0">
<param name="Caption" value="Office文档在线编辑">
<param name="IsShowToolMenu" value="-1">
<param name="IsHiddenOpenURL" value="0">
<param name="IsUseUTF8URL" value="-1">
<param name="MakerCaption" value="<%=StaticData.MakerCaption%>">
<param name="MakerKey" value="<%=StaticData.MakerKey%>">
<param name="ProductCaption" value="<%=StaticData.ProductCaption%>">
<param name="ProductKey" value="<%=StaticData.ProductKey%>">
 
<SPAN STYLE="color:red"><br>不能装载文档控件，请设置好IE安全级别为中或中低，不支持非IE内核的浏览器。</SPAN>
</object>

</div>
</body>
</html>