<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.data.YHSysOperator" %> 
 <%
 String baseFilePath = request.getParameter("filePath");
 YHSysOperator opt = (YHSysOperator)session.getAttribute("user");
 if(baseFilePath == null || "".equals(baseFilePath)){
   out.println("你没有指定路径,如不指定有些功能将不能用");
   baseFilePath = "subsys/fis/basecode/requirements";
 }
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公共信息</title>
<style type="text/css">
<!--
.titleDivStyle {
    font-size: 14px;
    font-weight: bold;
    float: left;
    width: 150px;
}
.dateDivStyle {
    font-size: 12px;
    float: right;
    width: 200px;
}
.webInfoHeadDivStyle {
    height: 20px;
}
.userDivStyle {
    font-size: 12px;
    float: right;
    width: 100px;
}
.webInfoFootDivStyle {
    height: 30px;
    border-bottom-width: 1px;
    border-bottom-style: solid;
    border-bottom-color: #999;
}
.contentDivStyle {
    border-top-width: 1px;
    border-bottom-width: 1px;
    border-top-style: dotted;
    border-bottom-style: dotted;
    border-top-color: #3FF;
    border-bottom-color: #3FF;
}
.keyWordDivStyle {
    font-size: 12px;
    float: left;
    width: 100px;
}
.webInfoDivStyle {
    width: 800px;
    padding-top: 3px;
}
.editDivStyle {
    float: right;
    width: 50px;
}
.deleteDivStyle {
     
    float: right;
    width: 50px;
}
.attachmentDivStyle{
	float: left;
	text-align:left;
	width: 200px
}
.readDivStyle{
	float: right;
    width: 70px;
}
-->
</style>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/webinfo.js"></script>
</head>
<body onload="doInit()">
<div><input type="button" value="增加" class="SmallButtonW" id="button" onclick="addWebInof()"/></div>
<div id="fckEditor" style="display:none">
<form id="webInfoForm">
<input id="fileName" name="fileName" type="hidden"/>
<div style='color:blue;size:12px' id='msg'>带*项为必填</div>
<table>
  <tr>
    <td> 标题:</td>
    <td class="big4">
      <input id="webInfoTitle" name="webInfoTitle" class="SmallInput" type="text"/>*
    </td>
    <td> 关键字:</td>
    <td class="big4">
      <input id="webInfoKeyWord"  class="SmallInput" name="webInfoKeyWord" type="text" />
    </td>
    
  </tr>
  <tr>
    <td>发布人:</td>
    <td  class="big4">
      <input id="webInfoUser" name="webInfoUser" value="<%=opt.getName() %>"  class="SmallInput" type="text"/>*
    </td>
    <td> 发布日期：</td>
    <td  class="big4">
      <input id="webInfoDate" readonly  class="SmallStatic" name="webInfoDate" type="text" />*
    </td>
  </tr>
  <tr>
    <td colspan="4">
      <a href="javascript:showAttachmentForm()">添加附件</a>
      <input type=hidden value='0' name='fileCount' id='fileCount'/>
    </td></tr>
  <tr>
    <td id="attachmentName">
    </td>
    <td id="attachmentPath" colspan="3">
    </td>
  </tr>
</table>
</form>
<iframe name="returnPage" style="display:none"></iframe>
<div id="uploadDiv">

</div>
<form name="attachmentForm" id="attachmentForm" action="<%=contextPath %>/yh/core/funcs/webinfo/act/YHWebInfoAct/doUploadFile.act" target="returnPage" method="post" style="display:none" enctype="multipart/form-data">
<INPUT class="SmallInput" TYPE="file" NAME="attachFile" id="attachFile"/>
<input type="hidden" value="<%=baseFilePath %>" id="baseFilePath" name="baseFilePath"/>
<input type="submit" class = 'SmallButtonW ' value="upload" onclick="return checkAttachForm()"><input type="button" class = 'SmallButtonW ' onclick="$('attachmentForm').hide()" value="close">
</form>
<script type="text/javascript">
var optName = "<%=opt.getName()%>";
var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
var oFCKeditor = new FCKeditor( 'FCKeditor1' ) ;
oFCKeditor.BasePath    = sBasePath ;
oFCKeditor.Height       = 500 ;
var sSkinPath = sBasePath + 'editor/skins/silver/';
oFCKeditor.Config['SkinPath'] = sSkinPath ;
oFCKeditor.Config['PreloadImages'] =
                sSkinPath + 'images/toolbar.start.gif' + ';' +
                sSkinPath + 'images/toolbar.end.gif' + ';' +
                sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
                sSkinPath + 'images/toolbar.buttonarrow.gif' ;
//oFCKeditor.Config['FullPage'] = true ;
oFCKeditor.ToolbarSet    = 'Default' ;
oFCKeditor.Value        = '' ;
oFCKeditor.Create();

</script>
</div>
<div id="pageBar">
<div id="searchDiv">关键字：<input type="text" class="SmallInput" onkeyup="getWebInfoList(0,$F('listLength'))" onpaste="getWebInfoList(0,$F('listLength'))"  id="searchStr" name="searchStr" value=""/></div>
共<span id="pageCount"></span>页,
<span id="webInfoCount"></span>条,当前第<select class="SmallSelect" id="stepTo" name="stepTo" onchange="pageChange(this)">
</select>页
<a href="#" id="firstPage" onclick="getWebInfoList(0,$F('listLength'))"><<</a>
<a href="#" id="previousPage"><</a>
||
<a href="#" id="nextPage">></a>
<a href="#" id="lastPage">>></a>
步长：<select class="SmallSelect"  id="listLength" name="listLength" onchange="getWebInfoList(0,$F('listLength'))">
    <option value="10">10</option>
    <option value="15">15</option>
    <option value="20">20</option>
</select>
</div>
<div id="container">
</div>
</body>
</html>