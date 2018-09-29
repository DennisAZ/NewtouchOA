<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.rad.taskmgr.util.YHTaskUtility" %>
<%@ page import="yh.rad.taskmgr.data.YHRadUser" %>
<%@ page import="yh.core.data.YHSysOperator" %>
<%@ page import="java.util.List" %>
<%@ page import="yh.core.servlet.YHServletUtility" %>
<head>
<title>yh</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<style type="text/css">
*{padding:0px;margin:0px;}
body{font-size:12px;}
#header{height:20px;width:100%;}
#header #bannerArea{width:100%;height:55px;background:#fff url(<%=imgPath %>/frame/header_logo_center.jpg) repeat-x;}
#header #bannerArea #logo{float:left;width:560px;height:55px;background:#fff url(<%=imgPath %>/frame/header_logo_left.jpg) no-repeat left top;}
#header #bannerArea #panel{width:300px;height:55px;line-height:50px;float:right;background:url(<%=imgPath %>/img/header_logo_line.jpg) no-repeat left center;padding-left:20px;}
#panel span{margin-right:10px;}
span.userArea{background:url(<%=imgPath %>/frame/user.png) no-repeat left center;font-weight:bold;color:#FFFFFF;padding-left:20px;height:30px;line-height:30px;}
input.commButton{height:33px;width:72px;background:url(<%=imgPath %>/frame/header_login_nodot.jpg) no-repeat center center;border:0px;font-weight:bold;color:#FFFFFF;line-height:33px;vertical-align:middle;cursor:pointer;}

#nav{background-color:#FBFCEC;height:25px;width:100%;border-bottom:1px solid #AFB293;position:relative;}
#nav ul{list-style-type:none;}
#nav #navTrees{width:850px;float:left;}
#nav ul li{height:25px;line-height:25px;list-style-type:none;display:block;float:left;width:auto;padding:0px 4px;margin-left:14px;font-weight:bold;font-size:13px;color:#666666;}
#nav ul li a:link{color:#666666;text-decoration:none;}
#nav ul li a:hover{color:#666666;text-decoration:underline;}
#nav_fetch{width:12px;height:12px;top:5px;right:13px;position:absolute;cursor:pointer;}
</style>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script language="JavaScript">
/**
 * 导航页面
 */
function nev(url, isRefresh) {
  if (!url) {
    return;
  }
  url = contextPath + url;
  if (!isRefresh && $("contentFrame").src == url) {
    return;
  }
  $("contentFrame").src = url;
  doResize();
}
function DisplayNav(){
}
function getHeight() {
  var rtHeight = document.viewport.getDimensions().height - 40;
  return rtHeight;
}
function getWidth() {
  var rtWidth = document.viewport.getDimensions().width;
  return rtWidth;
}
function doResize() {
  $("contentFrame").height = getHeight();
  $("contentFrame").width = getWidth();
}
function displayDesk() {
}
</script>
</head>
<body scroll="no" style="background-color:#FFFFFF" text="#000000" onload="nev('/rad/docs/index.jsp?catagory=component')">
<div id="header">
  <div id="nav">
    <div id="navTrees">
      <table border="0" cellspacing="0" cellpadding="3">
        <tr> 
          <td style="cursor:pointer;" class="small" onclick="nev('/rad/docs/yhbuilder.jsp')"><a href="javascript:emptyFunc();">&nbsp;YH环境塔建&nbsp;</a></td>
          <td style="cursor:pointer;" class="small" onclick="nev('/rad/docs/index.jsp?catagory=component')"><a href="javascript:emptyFunc();">&nbsp;组件库&nbsp;</a></td>
          <td style="cursor:pointer;" class="small" onclick="nev('/rad/docs/index.jsp?catagory=database')"><a href="javascript:emptyFunc();">&nbsp;数据库相关&nbsp;</a></td>
          <td style="cursor:pointer;" class="small" onclick="nev('/rad/docs/index.jsp?catagory=private')"><a href="javascript:emptyFunc();">&nbsp;权限说明 &nbsp;</a></td>
          <td style="cursor:pointer;" class="small" onclick="nev('/rad/docs/index.jsp?catagory=tools')"><a href="javascript:emptyFunc();">&nbsp;工具类&nbsp;</a></td>
          <td style="cursor:pointer;" class="small" onclick="nev('/rad/docs/index.jsp?catagory=module')"><a href="javascript:emptyFunc();">&nbsp;模块开发与说明&nbsp;</a></td>
        </tr>
      </table>
    </div>
  </div>
</div>
<iframe id="contentFrame">
</iframe>
</body>
</html>
