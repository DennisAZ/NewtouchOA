<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>国务院发展研究中心科研管理</title>

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
function doInit(){
  totalRecord = loadData(0,pageSize);
  var cfgs = {
      dataAction: "",
      container: "pageInfo",
      pageSize:pageSize,
      loadData:loadDataAction,
      totalRecord:totalRecord,
      params:getParam
    };
  pageInfoS = new YHJsPagePilot(cfgs);
  if(totalRecord > 0){
    pageInfoS.show();
  }
}
//查询修改的地方
function loadData(pageIndex,pageSize,serachParam){
  var param = "pageIndex=" + pageIndex +"&pageSize=" + pageSize+ "&ruleName=kygl-dybg";
  
  var filter_content = "";
  var userParam = "";
  if(serachParam){
    filter_content = serachParam;
    userParam = "filter_content=" + filter_content + "&searchFlag=1";
  }
  var queryParam = mergeQueryString(param, userParam);
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridNormal/loadDataPage.act";
  var rtJson = getJsonRs(url,queryParam);
  //alert(rtJson);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $('dataContent').innerHTML = "";
    //alert(rsText);
    for(var i = 0 ; i < data.pageData.length; i++){
      var news = data.pageData[i];
      var newId = news.newId;
      var subject =  news.subject;
      var newsTime = news.newsTime.substring(0,10);
      var hrefUrl = "kygl-dybg.jsp?Id=" + newId;
      var liHtml = "<li class=po-li><a href=\"" + hrefUrl + "\" class=\"block-left\"> " + subject + "</a>  <p class=\"block-right\">" + newsTime + "</p> </li>";
      $('dataContent').insert(liHtml,"bottom");
    }
  }
  return data.totalRecord;
}

function loadDataAction(obj,serachParam){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  totalRecord = loadData(pageNo,pageSize,serachParam);
  //查询修改的地方
  return totalRecord;
}

/**
 * 查询定义函数
 */
function doSerach(){
  pageInfoS.search();
}

function getParam(){
  return $('serachValue').value;
}
</script>
</head>

<body onload="doInit()">
<div class="frame-box-two">
<div class="s-left">
   <div class="s-menu">
    <div class="menutit-bg">科研管理</div>
    <div class="menu-button1"><a href="kygl-dybg-list.jsp" target="mainframe">调研报告</a></div>
   </div>

</div>
   <div class="s-right">
   <!--第一块从这里开始-->
     <div class="box-two-a">
	   <div class="two-a-up">
	   <img src="../../images-other/icon.jpg" height="31" border="0" class="block-left"/>
	   <span class="popular-title">调研报告</span>
	   </div>
		  <div class="two-a-down-popular">
         <div><input id="serachValue" type="text"></input> <input onclick="doSerach()" value="查询" type="button"></input></div>
		    <ul id="dataContent">
			</ul>
			<div class="next-page" id="pageInfo"></div>
		  </div>
     </div>
   </div>
	
</div>

</body>
</html>