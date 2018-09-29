<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<%
  String userName = request.getParameter("userName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>国务院发展研究中心专栏导航</title>
<link href="../../style/css/css.css" rel="stylesheet" type="text/css" />
<link href="../../style/css/css-other.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
.two-a-down-popular {
	height: auto;
	padding-right: 5px;
	padding-left: 5px;
	font-family: "微软雅黑", "宋体";
	font-size: 15px;
	text-indent: 2em;
	color: #333333;
	line-height: 19px;
	padding-top: 18px;
	padding-bottom: 28px;
	clear: both;
}
.popular-title {
	font-family: "微软雅黑", "宋体";
	font-size: 16px;
	color: #0000CC;
	line-height: 27px;
	vertical-align: middle;
	display: block;
	float: left;
	font-weight: bold;
}
-->
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/portal.js"></script>
<script type="text/javascript">
var pageSize = 5;
var totalRecord  = 0;
var pageInfoS = null;
var userName = "<%=userName%>";
var userId = "";
function doInit(){
  userId = getUserIdByName(userName);
  if(!userId){
    userId = "-1"
  }
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
  var param = "pageIndex=" + pageIndex +"&pageSize=" + pageSize+ "&ruleName=leaderB" + "&filter_userId="+userId;
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
      var hrefUrl = "leader-b-content.jsp?Id=" + newId;
      var liHtml = "<li class=po-li><a href=\"" + hrefUrl + "\" class=\"block-left\"> " + subject + "</a>  <p class=\"block-right\">" + newsTime + "</p> </li>";
      $('dataContent').insert(liHtml,"bottom");
    }
  }
  return data.totalRecord;
}

function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  totalRecord = loadData(pageNo,pageSize,userId);
}
</script>
</head>

<body onload="doInit()">
<div class="frame-box-two">
   <!--第一块从这里开始-->
     <div class="box-two-a">
	   <div class="two-a-up">
	   <img src="../../images-other/icon.jpg" height="31" border="0" class="block-left"/>
	   <span class="popular-title">重要讲话</span>
	   </div>
		  <div class="two-a-down-popular"> 
            <ul id="dataContent">
			</ul>
		  <div class="next-page" id="pageInfo"></div>
 </div>
     </div>
   </div>
</body>
</html>