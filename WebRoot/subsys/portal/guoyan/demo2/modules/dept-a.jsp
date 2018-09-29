<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>国务院发展研究中心部门专栏</title>
<link href="css.css" rel="stylesheet" type="text/css" />
<link href="style/css/css-other.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/portal.js"></script>
<script type="text/javascript">
var pageSize = 5;
var pageSize2 = 5;
var totalRecord  = 0;
var totalRecord2  = 0;
var pageInfoS = null;
var pageInfoS2 = null;
function doInit(){
  totalRecord = loadData(0,pageSize);
  totalRecord2 = loadData2(0,pageSize2);
  var cfgs = {
      dataAction: "",
      container: "pageInfo",
      pageSize:pageSize,
      loadData:loadDataAction,
      totalRecord:totalRecord
    };
  var cfgs2 = {
      dataAction: "",
      container: "pageInfo2",
      pageSize:pageSize,
      loadData:loadDataAction2,
      totalRecord:totalRecord2
    };
  pageInfoS = new YHJsPagePilot(cfgs);
  if(totalRecord > 0){
    pageInfoS.show();
  }
  pageInfoS2 = new YHJsPagePilot2(cfgs2);
  if(totalRecord2 > 0){
    pageInfoS2.show();
  }
}

function loadData(pageIndex,pageSize){
  var param = "pageIndex=" + pageIndex +"&pageSize=" + pageSize+ "&ruleName=deptNotify" + "&filter_dept=1";
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
      var hrefUrl = "modules/dept/deptNotify.jsp?Id=" + newId;
      var liHtml = "<li class=po-li><a href=\"" + hrefUrl + "\" class=\"block-left\"> " + subject + "</a>  <p class=\"block-right\">" + newsTime + "</p> </li>";
      $('dataContent').insert(liHtml,"bottom");
    }
  }
  return data.totalRecord;
}
function loadData2(pageIndex,pageSize){
  var param = "pageIndex=" + pageIndex + "&pageSize=" + pageSize+"&ruleName=deptActive" + "&filter_dept=1";
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridNormal/loadDataPage.act";
  var rtJson = getJsonRs(url,param);
  //alert(rsText);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $('dataContent2').innerHTML = "";
    for(var i = 0 ; i < data.pageData.length; i++){
      var news = data.pageData[i];
      var newId = news.newId;
      var subject =  news.subject;
      var newsTime = news.newsTime.substring(0,10);
      var hrefUrl = "modules/dept/deptNotify.jsp?Id=" + newId;
      var liHtml = "<li class=po-li><a href=\"" + hrefUrl + "\" class=\"block-left\"> " + subject + "</a>  <p class=\"block-right\">" + newsTime + "</p> </li>";
      $('dataContent2').insert(liHtml,"bottom");
    }
  }
  return data.totalRecord;
}
function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  totalRecord = loadData(pageNo,pageSize);
}
function loadDataAction2(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  totalRecord2 = loadData2(pageNo,pageSize);
}
</script>
</head>

<body onload="doInit()">
<div class="frame-box-two">
<div class="s-left">
   <div class="s-menu">
      <div class="menutit-bg">部门专栏</div>
	  <div class="menu-button1"><a href="dept-b.jsp" target="mainframe">部门职责</a></div>
	  <div class="menu-button1"><a href="dept-c.jsp" target="mainframe">机构设置</a></div>
	  <div class="menu-button1"><a href="modules/dept/deptNotifyList.jsp" target="mainframe">部门通知</a></div>
	  <div class="menu-button1"><a href="modules/dept/deptActiveList.jsp" target="mainframe">工作动态</a></div>
   </div>
   <div><img src="images-other/img-a.jpg" /></div>
</div>
   <div class="s-right">
   <!--第一块从这里开始-->
     <div class="box-two-a">
	   <div class="two-a-up"><img src="images-other/dept-header-tit.jpg" width="131" height="42" border="0"/></div>
		  <div class="two-a-down-popular">
		    <ul id="dataContent">
			</ul>
		  <div class="next-page" id="pageInfo"></div>
		  
		  </div>
	 </div>
	 <div class="h10px"></div>
	 <!--第二块从这里开始-->
	 <div class="box-two-a">
	      <div class="two-a-up">
		  <img src="images-other/work-header-tit.jpg" width="131" height="42" border="0"/></div>
		  <div class="two-a-down-popular"> <ul id="dataContent2">
			</ul>
      <div class="next-page" id="pageInfo2"></div>
      </div>
	 </div>
	 <div class="h20px"></div>

  </div>
	
</div>
</body>
</html>