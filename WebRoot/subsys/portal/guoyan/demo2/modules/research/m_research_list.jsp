<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>无标题文档</title>
<link href="../../style/css/css.css" rel="stylesheet" type="text/css" />
<link href="../../style/css/css-other.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
.box-three-page {
	width: 100%;
	height: auto;
	padding: 0px;
	margin-top:8px;
	background-image: url(../../images-other/news-tit-bg.jpg);
	background-repeat: repeat-x;
	border: 1px solid #d9d9d9;
	float: left;
	text-align: left;
}
.frame-box-three .box-three-page .three-a-down li {
	background-image: url(../../images/ic-a.gif);
	background-repeat: no-repeat;
	line-height: 29px;
	background-position: left center;
	text-align: right;
}
.frame-box-three .box-three-page .three-a-down a {
	font-family: "宋体";
	font-size: 14px;
	margin-left: 15px;
	display: block;
	float: left;
}
.frame-box-three .box-three-page .three-a-down a:hover {
	font-family: "宋体";
	font-size: 14px;
	margin-left: 15px;
	display: block;
	float: left;
	color: #0000CC;
}
.page-aweektxt {
	font-family: "宋体";
	font-size: 15px;
	color: #FFFFFF;
	display: block;
	float: left;
	height: 20px;
	margin-left: 10px;
	letter-spacing: 1px;
	background-image: url(../../images-other/title-bg.jpg);
	background-repeat: no-repeat;
	width: 148px;
	background-position: bottom;
	text-align: center;
	padding-top: 8px;
}
-->
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript">
var pageSize = 5;
var totalRecord  = 0;
var pageInfoS = null;
function doInit(){
  totalRecord = loadData(0,pageSize);
  var cfgs = {
      dataAction: "",
      container: "pageInfo",
      pageSize:pageSize,
      loadData:loadDataAction,
      totalRecord:totalRecord
    };
  pageInfoS = new YHJsPagePilot(cfgs);
  if(totalRecord > 0){
    pageInfoS.show();
  }
}
function loadData(pageIndex,pageSize){
  var param = "pageIndex=" + pageIndex +"&pageSize=" + pageSize+ "&ruleName=research_m";
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridNormal/loadDataPage.act";
  var rtJson = getJsonRs(url,param);
  //alert(rsText);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $('dataContent').innerHTML = "";
    for(var i = 0 ; i < data.pageData.length; i++){
      var news = data.pageData[i];
      var newId = news.newId;
      var subject =  news.subject;
      var newsTime = news.newsTime.substring(0,10);
      var hrefUrl = "m_article.jsp?newsId=" + newId;
      var liHtml = "<li><a href=\"" + hrefUrl + "\"> " + subject + "</a>   " + newsTime + "</li>";
      $('dataContent').insert(liHtml,"bottom");
    }
  }
  return data.totalRecord;
}
function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  totalRecord = loadData(pageNo,pageSize);
}
</script>
</head>

<body onload="doInit()">
<div class="frame-box-three">
     <div class="box-three-page">
	   <div class="three-a-up">
	   <span class="page-aweektxt" id="title">会议资料库</span>
	   </div>
	     <div class="three-a-down">
		    <ul id="dataContent">
			</ul>
		  <div class="next-page" id="pageInfo"></div>
		 </div>
	 </div>
</div>
</body>
</html>
