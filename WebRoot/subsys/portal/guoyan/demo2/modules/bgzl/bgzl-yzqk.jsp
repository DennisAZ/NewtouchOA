<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<%
 String newsId = request.getParameter("Id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>无标题文档</title>
<link href="../../style/css/css.css" rel="stylesheet" type="text/css" />
<link href="../../style/css/css-other.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/leaderact/js/leaderact.js"></script>

<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
.article {
	height: auto;
	padding-bottom: 20px;
	border: 1px solid #d9d9d9;
	background-image: url(../../images-other/lead-header-bg.jpg);
	background-repeat: repeat-x;
}
.two-a-down-article {
	font-family: "宋体";
	font-size: 14px;
	line-height: 21px;
	text-indent: 2em;
	padding-top: 30px;
	padding-right: 18px;
	padding-bottom: 18px;
	padding-left: 20px;
	min-height: 500px;
	height: auto;
}
.article-title {
	font-family: "宋体";
	font-size: 22px;
	color: #0000FF;
	text-align: center;
	line-height: 30px;
}
.other-font {
	height: 30px;
	padding-top: 10px;
	text-align: center;
}
.day-name-l {
	font-family: "宋体";
	font-size: 13px;
	color: #999999;
}
.day-name-r {
	font-family: "宋体";
	font-size: 13px;
	color: #666666;
	float: right;
	display: block;
	margin-right: 30px;
}
-->
</style>
<script type="text/javascript">
function doInit(){
  var newsId = "<%=newsId%>";
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridNormal/loadOneData.act";
  var param = "Id=" + newsId + "&ruleName=bgzl-yzqk";
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $('subject').innerHTML = data.subject;
    $('content').innerHTML = data.content;
    $('publish').innerHTML = "发布人：" + data.publish;
    $('newsTime').innerHTML = "发布日期：" + data.newsTime.substring(0,10);
  }
}
</script>
</head>

<body onload="doInit()">
<div class="frame-box-two">
<div class="s-left">
   <div class="s-menu">
      <div class="menutit-bg">办公专栏</div>
	  <div class="menu-button1"><a href="../../bgzl.jsp" target="mainframe">中心通知</a></div>
	  <div class="menu-button1"><a href="bgzl-yzqk-list.jsp" target="mainframe">一周情况</a></div>
	  <div class="menu-button1"><a href="bgzl-gwg-list.jsp" target="mainframe">公文库</a></div>
   </div>

</div>
<div class="s-right">
   <!--第一块从这里开始-->
     <div class="article">
	   <div class="two-a-up">
	   <img src="../../images-other/icon.jpg" width="60" height="39" border="0"/></div>
	      <div class="article-title" id="subject"></div>
		  <div class="other-font">
		  <span class="day-name-l" id="newsTime"></span>
		  </div>
		  <div class="two-a-down-article" id="content"></div>
<span class="day-name-r" id="publish"></span>
	 </div>
	 		  
</div>
	
</div>
</body>
</html>