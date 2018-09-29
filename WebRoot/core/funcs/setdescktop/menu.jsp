<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>控制面板菜单</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath  %>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath  %>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<style>
a {
  display:block;
  height:20px;
  line-height:20px;
  padding-top:5px;
}

a:link, a:visited, a:hover, a:active {
  display:block;
  height:20px;
  line-height:20px;
  padding-top:5px;
  text-decoration: none;
}

body {
  width: 170px;
  height: 2000px;
}

#left_top {
    background: url("<%=imgPath %>/pageheaders/person_info.jpg") no-repeat scroll left center transparent;
    height: 79px;
    width: 188px;
}

</style>
</head>
<body topmargin="5">
<div class="PageHeader" id="left_top"></div>
<table width="100%" class="BlockTop">
  <tr>
    <td class="left">
    </td>
    <td class="center" align="center">
        界面设置
    </td>
    <td class="right">
    </td>
  </tr>
</table>
<TABLE class="TableNav" width="100%" align="center">
 <TR class="TableData"  height=120>
  <TD>
    <a href="theme" target="c_main"><span>界面设置</span></a>
    <a href="shortcut" target="c_main"><span>菜单快捷组</span></a>
    <a href="setports/customize.jsp" target="c_main"><span>自定义桌面</span></a>
    <!--<a href="winexe" target="c_main"><img border=0 src="<%=imgPath  %>/control_icon.jpg" WIDTH="16" HEIGHT="16" align="absmiddle">&nbsp; Windows快捷组</a><br>-->
    <!-- <a href="url" target="c_main">&nbsp; 个人网址</a><br> -->
    <a href="fav" target="c_main"><span>收藏夹</span></a>
  </TD>
 </tr>

  <TR class="TableContent">
  <TD>个人信息</TD>
  </tr>
 <TR class="TableData" height=70>
  <TD>
    <a href="info" target="c_main"><span>个人资料</span></a>
    <a href="avatar" target="c_main"><span>昵称与头像</span></a>
    <a href="group" target="c_main"><span>自定义用户组</span></a>
    <!-- <a href="concern_user" target="c_main">&nbsp;人员关注</a><br> -->
    <a href="sealpass" target="c_main"><span>印章密码修改</span></a>
  </TD>
 </tr>

 <TR class="TableContent">
  <TD>帐号与安全</TD>
 </tr>
 <TR class="TableData" height=70>
  <TD>
    <a href="mypriv" target="c_main"><span>我的帐户</span></a>
    <a href="pass" target="c_main"><span>修改密码</span></a>
    <a href="log" target="c_main"><span>安全日志</span></a>
  </TD>
 </tr>
</TABLE>
</body>
</html>
