<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>fck sample01</title>
<link href="../sample.css" rel="stylesheet" type="text/css" />
  <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
</head>
<body>
<h1>
    FCKeditor - JavaScript - Sample 1
  </h1>
    <script type="text/javascript">
// Automatically calculates the editor base path based on the _samples directory.
// This is usefull only for these samples. A real application should use something like this:
// fck所在目录.
var sBasePath = contextPath + "/core/js/cmp/fck/fckeditor/";
//实例化一个fck
var oFCKeditor = new FCKeditor( 'FCKeditor1' ) ;
oFCKeditor.BasePath = sBasePath ;
//设置fck高度
oFCKeditor.Height = 300 ;
//设置fck默认值
oFCKeditor.Value  = '<p>This is some <strong>sample text<\/strong>. You are using <a href="http://www.fckeditor.net/">FCKeditor<\/a>.<\/p>' ;
oFCKeditor.Create() ;
    </script>
</body>
</html>