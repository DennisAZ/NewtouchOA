<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/core/inc/header.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>fck sample04</title>
<link href="../sample.css" rel="stylesheet" type="text/css" />
  <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript">

function FCKeditor_OnComplete( editorInstance )
{
  var oCombo = document.getElementById( 'cmbToolbars' ) ;
  //取得fck工具栏的名称
  oCombo.value = editorInstance.ToolbarSet.Name ;
  oCombo.style.visibility = '' ;
}

function ChangeLanguage( languageCode )
{
  document.location.href =  "sample04.jsp?toolbar=" + languageCode ;
}

  </script>
</head>
<body>
<h1>
		FCKeditor - JavaScript - Sample 4</h1>
	<div>
		This sample shows how to change the editor toolbar.
	</div>
  <table cellpadding="0" cellspacing="0" border="0">
    <tr>
      <td>
        Select the toolbar to load:&nbsp;
      </td>
      <td>
        <select id="cmbToolbars" onchange="ChangeLanguage(this.value);" style="visibility: hidden">
          <option value="Default" selected="selected">Default</option>
          <option value="Basic">Basic</option>
          <option value="feedback">feedback</option>
        </select>
      </td>
    </tr>
  </table>
  <br />
    <script type="text/javascript">
// Automatically calculates the editor base path based on the _samples directory.
// This is usefull only for these samples. A real application should use something like this:
// fck所在目录.
var sBasePath = contextPath + "/core/js/cmp/fck/fckeditor/";  
var oFCKeditor = new FCKeditor( 'FCKeditor1' ) ;
oFCKeditor.BasePath	= sBasePath ;
<%
String toolbar = request.getParameter("toolbar");
if (YHUtility.isNullorEmpty(toolbar)) {
  toolbar = "feedback";
}
%>
//设置工具栏，如果不设置则为"feedback"工具栏
oFCKeditor.ToolbarSet = "<%=toolbar %>" ;
oFCKeditor.Value = '<p>This is some <strong>sample text<\/strong>. You are using <a href="http://www.fckeditor.net/">FCKeditor<\/a>.<\/p>' ;
oFCKeditor.Create() ;
    </script>
</body>
</html>